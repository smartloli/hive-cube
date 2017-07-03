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

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.Scheduler;
import org.smartloli.hive.cube.web.controller.StartupListener;
import org.smartloli.hive.cube.web.service.impl.TasksServiceImpl;

/**
 * Schedule task job.
 * 
 * @author smartloli.
 *
 *         Created by Jun 12, 2017
 */
public class ScheduleTask implements Job {

	private Logger LOG = LoggerFactory.getLogger(ScheduleTask.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		int taskId = Integer.parseInt(context.getJobDetail().getJobDataMap().get(Scheduler.NAME).toString());
		TasksServiceImpl taskService = StartupListener.getBean("tasksServiceImpl", TasksServiceImpl.class);
		boolean status = taskService.executorPrivateTaskById(taskId);
		LOG.info("Schedule task[" + taskId + "] has finished,status[" + status + "]");
	}

}
