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
package org.smartloli.hive.cube.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.smartloli.hive.cube.common.client.CommonClientConfigs;
import org.smartloli.hive.cube.common.pojo.Scheduler;
import org.smartloli.hive.cube.common.util.QuartzManager;
import org.smartloli.hive.cube.web.dao.SchedulerDao;
import org.smartloli.hive.cube.web.dao.TasksDao;
import org.smartloli.hive.cube.web.service.SchedulerService;
import org.smartloli.hive.cube.web.task.ScheduleTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * Scheduler service interface.
 * 
 * @author smartloli.
 *
 *         Created by Jun 12, 2017
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {

	@Autowired
	private SchedulerDao schedulerDao;

	@Autowired
	private TasksDao taskDao;

	@Override
	public int updateScheduler(Scheduler scheduler) {
		int scheduleCode = schedulerDao.updateScheduler(scheduler);
		Map<String, Object> map = new HashMap<>();
		map.put("id", scheduler.getTaskId());
		map.put("auto", scheduler.getAuto());
		int taskCode = taskDao.convertOrCancleAutoTask(map);
		if (scheduleCode > 0 && taskCode > 0) {
			if (scheduler.getType().equals(CommonClientConfigs.Scheduler.ADD) || scheduler.getType().equals(CommonClientConfigs.Scheduler.START)) {
				QuartzManager.addJob(String.valueOf(scheduler.getTaskId()), ScheduleTask.class, scheduler.getCronExpression());
			} else if (scheduler.getType().equals(CommonClientConfigs.Scheduler.MODIFY)) {
				QuartzManager.modifyJobTime(String.valueOf(scheduler.getTaskId()), scheduler.getCronExpression());
			} else if (scheduler.getType().equals(CommonClientConfigs.Scheduler.STOP)) {
				if (taskDao.stopTaskById(scheduler.getTaskId()) > 0) {
					QuartzManager.removeJob(String.valueOf(scheduler.getTaskId()));
				}
			} else if (scheduler.getType().equals(CommonClientConfigs.Scheduler.DELETE)) {
				QuartzManager.removeJob(String.valueOf(scheduler.getTaskId()));
				taskDao.deleteTaskById(scheduler.getTaskId());
				schedulerDao.deleteSchedulerById(scheduler.getTaskId());
			} else if (scheduler.getType().equals(CommonClientConfigs.Scheduler.CANCLE)) {
				QuartzManager.removeJob(String.valueOf(scheduler.getTaskId()));
				schedulerDao.deleteSchedulerById(scheduler.getTaskId());
			}

			return 1;
		}
		return 0;
	}

	@Override
	public String getSchedulerByTaskId(int id) {
		Scheduler scheduler = schedulerDao.getSchedulerByTaskId(id);
		JSONObject object = new JSONObject();
		object.put("cron", scheduler.getCronExpression());
		return object.toJSONString();
	}

}
