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
package org.smartloli.hive.cube.web.task;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.client.CommonClientConfigs;
import org.smartloli.hive.cube.common.pojo.Queue;
import org.smartloli.hive.cube.common.pojo.Task;
import org.smartloli.hive.cube.common.util.CalendarUtils;
import org.smartloli.hive.cube.web.service.impl.EngineServiceImpl;
import org.smartloli.hive.cube.web.service.impl.TasksServiceImpl;
import org.smartloli.hive.cube.web.controller.StartupListener;
import org.smartloli.hive.cube.web.util.Reporter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Queue task execution plan.
 * 
 * @author smartloli.
 *
 *         Created by Jun 5, 2017
 */
public class RunnablePriority implements Runnable, Comparable<RunnablePriority> {

	private Logger LOG = LoggerFactory.getLogger(RunnablePriority.class);

	private int priority;
	private Queue queue;

	public int getPriority() {
		return priority;
	}

	public RunnablePriority(int priority) {
		this.priority = priority;
	}

	public RunnablePriority(int priority, Queue queue) {
		this.priority = priority;
		this.queue = queue;
	}

	@Override
	public int compareTo(RunnablePriority o) {
		if (this.getPriority() < o.priority) {
			return 1;
		}
		if (this.getPriority() > o.priority) {
			return -1;
		}
		return 0;
	}

	@Override
	public synchronized void run() {
		try {
			// Executor task by hive engine.
			Thread.sleep(CommonClientConfigs.Task.WAIT);
			Task task = queue.getTask();

			JSONObject object = JSON.parseObject(task.getContent());
			String column = object.getString("column");
			JSONArray contexts = object.getJSONArray("context");
			executor(contexts, task, column);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void common(Task task) {
		TasksServiceImpl taskService = StartupListener.getBean("tasksServiceImpl", TasksServiceImpl.class);
		int code = taskService.batchModifyTaskStatus(Arrays.asList(task));
		if (code <= 0) {
			LOG.info("Modify task status has error,code[" + code + "]");
		}
		// Finished executor task and remove current task from queues.
		if (TaskQueue.remove(queue)) {
			LOG.info("Remove has sucess,number of tasks available[" + TaskQueue.getQueues().size() + "]");
			Reporter.sender(task);
		}
	}

	private void executor(JSONArray contexts, Task task, String column) {
		EngineServiceImpl engineService = StartupListener.getBean("engineServiceImpl", EngineServiceImpl.class);
		JSONObject context = (JSONObject) contexts.get(0);
		if ("hive".equals(context.getString("type"))) {
			String result = engineService.executeQuery(task.getId(), context.getString("operate"), column);
			JSONObject object = JSON.parseObject(result);
			if (object.getString("error").length() == 0) {
				JSONObject tmp = object.getJSONObject("reback");
				task.setDownload(tmp.getString("download"));
				task.setStatus(CommonClientConfigs.TaskStatus.EXECUTION_END);
				task.setEndTime(CalendarUtils.today());
				task.setFileSize(tmp.getLongValue("size"));
			} else {
				task.setLog(object.getString("error"));
				task.setStatus(CommonClientConfigs.TaskStatus.EXECUTION_ERROR);
				task.setEndTime(CalendarUtils.today());
			}
		}
		common(task);
	}

}
