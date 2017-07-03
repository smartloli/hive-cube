/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartloli.hive.cube.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.client.CommonClientConfigs;
import org.smartloli.hive.cube.common.pojo.Queue;
import org.smartloli.hive.cube.common.pojo.SchedulerTask;
import org.smartloli.hive.cube.common.pojo.Task;
import org.smartloli.hive.cube.common.util.QuartzManager;
import org.smartloli.hive.cube.web.service.TasksService;
import org.smartloli.hive.cube.web.task.ScheduleTask;
import org.smartloli.hive.cube.web.task.TaskFactory;
import org.smartloli.hive.cube.web.task.TaskQueue;
import org.smartloli.hive.cube.web.task.ThreadExecutor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

/**
 * Initialize boot listener.
 * 
 * @author smartloli.
 *
 *         Created by May 22, 2017
 */
@Component
public class StartupListener implements ApplicationContextAware {

	private Logger LOG = LoggerFactory.getLogger(StartupListener.class);

	private static ApplicationContext applicationContext;

	@Autowired
	private TasksService taskServce;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (TaskQueue.getCounter() == 0) {
			StartupListener.applicationContext = applicationContext;

			List<Task> tasks = taskServce.loadRunningTask();
			List<SchedulerTask> schedulerTasks = taskServce.loadAutoTask();

			// Load auto tasks from tasks and schedule table
			for (SchedulerTask scheduleTask : schedulerTasks) {
				QuartzManager.addJob(String.valueOf(scheduleTask.getId()), ScheduleTask.class, scheduleTask.getCronExpression());
			}

			// Load tasks from tasks table.
			for (int i = 0; i < tasks.size(); i++) {
				Queue queue = new Queue();
				queue.setVip(CommonClientConfigs.Task.TASK_COMMON);
				if (TaskQueue.isFull()) {
					queue.setRank(TaskQueue.getRank(i));
					queue.setTask(tasks.get(i));
					queue.setStatus(CommonClientConfigs.TaskStatus.QUEUE + "(" + queue.getRank() + ")");
				} else {
					queue.setRank(0);
					queue.setTask(tasks.get(i));
					queue.setStatus(CommonClientConfigs.TaskStatus.EXECUTING);
				}
				TaskQueue.getQueues().add(queue);
			}

			// Change tasks table status.
			if (TaskFactory.modify() != null && TaskFactory.modify().size() > 0) {
				int code = taskServce.batchModifyTaskStatus(TaskFactory.modify());
				if (code > 0) {
					// Add task to queues.
					for (Queue queue : TaskQueue.getQueues()) {
						if (queue.getRank() == 0) {
							ThreadExecutor.addTask(queue.getVip(), queue);
						}
					}
					// Monitor queue executor status.
					MonitorTask monitor = new MonitorTask();
					monitor.start();
				} else {
					LOG.error("Taskfactory modify task status has error,code[" + code + "]");
				}
			}
			TaskQueue.setCounter();
		}
	}

	public static Object getBean(String beanName) {
		if (applicationContext == null) {
			applicationContext = ContextLoader.getCurrentWebApplicationContext();
		}
		return applicationContext.getBean(beanName);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		return clazz.cast(getBean(beanName));
	}

	/** Listen task thread. */
	class MonitorTask extends Thread {
		public void run() {
			while (true) {
				try {
					if (TaskQueue.hasFree()) {
						TaskQueue.updateQueues();
						if (TaskFactory.modify() != null && TaskFactory.modify().size() > 0) {
							taskServce.batchModifyTaskStatus(TaskFactory.modify());
						}
					}
					sleep(CommonClientConfigs.Task.LISTEN);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
