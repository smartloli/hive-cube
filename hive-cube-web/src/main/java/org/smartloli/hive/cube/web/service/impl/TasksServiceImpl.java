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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.client.CommonClientConfigs;
import org.smartloli.hive.cube.common.pojo.Queue;
import org.smartloli.hive.cube.common.pojo.SchedulerTask;
import org.smartloli.hive.cube.common.pojo.Task;
import org.smartloli.hive.cube.common.util.CalendarUtils;
import org.smartloli.hive.cube.common.util.YarnManager;
import org.smartloli.hive.cube.web.dao.TasksDao;
import org.smartloli.hive.cube.web.service.TasksService;
import org.smartloli.hive.cube.web.task.TaskFactory;
import org.smartloli.hive.cube.web.task.TaskQueue;
import org.smartloli.hive.cube.web.task.ThreadExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Task execution classes.
 * 
 * @author smartloli.
 *
 *         Created by Jun 6, 2017
 */
@Service
public class TasksServiceImpl implements TasksService {

	private Logger LOG = LoggerFactory.getLogger(TasksServiceImpl.class);

	@Autowired
	private TasksDao tasksDao;

	@Override
	public List<Task> getTasks(Map<String, Object> params) {
		List<Task> tasks = tasksDao.getTasks(params);
		for (Task task : tasks) {
			task.setAppId(tasksDao.getAppIdByTaskId(task.getId()));
		}
		try {
			YarnManager.getProcessByAppIds(tasks);
		} catch (Exception e) {
			LOG.error("Get task process has error,msg is " + e.getMessage());
		}
		return tasks;
	}

	@Override
	public int count(Map<String, Object> params) {
		return tasksDao.count(params);
	}

	@Override
	public List<Task> loadRunningTask() {
		return tasksDao.loadRunningTask();
	}

	@Override
	public int batchModifyTaskStatus(List<Task> tasks) {
		return tasksDao.batchModifyTaskStatus(tasks);
	}

	@Override
	public String findTaskById(int id) {
		Task task = tasksDao.findTaskById(id);
		JSONObject target = new JSONObject();
		target.put("name", task.getName());
		target.put("email", task.getEmail());
		target.put("log", task.getLog());
		target.put("content", JSON.parseObject(task.getContent()).getString("context"));
		target.put("column", JSON.parseObject(task.getContent()).getString("column"));
		return target.toJSONString();
	}

	@Override
	public int modifyTaskContentByParams(Map<String, Object> params) {
		return tasksDao.modifyTaskContentByParams(params);
	}

	@Override
	public boolean executorTaskById(int id) {
		Task task = tasksDao.findTaskById(id);
		if (task.getStatus().equals(CommonClientConfigs.TaskStatus.EXECUTING)) {
			LOG.info("[" + task.getId() + "] task executing,no duplication of execution.");
			return true;
		} else {
			task.setStartTime(CalendarUtils.getDate());
			task.setStatus(CommonClientConfigs.TaskStatus.EXECUTING);
			task.setLog("");
			task.setDownload("");
			task.setEndTime("");
			task.setFileSize(0L);
			int code = tasksDao.batchModifyTaskStatus(Arrays.asList(task));
			if (code > 0) {
				Queue queue = new Queue();
				queue.setRank(0);
				queue.setVip(CommonClientConfigs.Task.TASK_VIP);
				queue.setStatus(CommonClientConfigs.TaskStatus.EXECUTING);
				queue.setTask(task);
				if (TaskQueue.remove(queue)) {
					LOG.info("Task enter vip,and delete from queues.");
				}
				ThreadExecutor.addTask(queue.getVip(), queue);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Task> getPrivateTasks(Map<String, Object> params) {
		List<Task> tasks = tasksDao.getPrivateTasks(params);
		for (Task task : tasks) {
			task.setAppId(tasksDao.getAppIdByTaskId(task.getId()));
		}
		try {
			YarnManager.getProcessByAppIds(tasks);
		} catch (Exception e) {
			LOG.error("Get private[auto private] task process has error,msg is " + e.getMessage());
		}
		return tasks;
	}

	@Override
	public boolean executorPrivateTaskById(int id) {
		Task task = tasksDao.findTaskById(id);
		if (task.getStatus().equals(CommonClientConfigs.TaskStatus.EXECUTING) || task.getStatus().contains(CommonClientConfigs.TaskStatus.QUEUE)) {
			LOG.info("[" + task.getId() + "] task executing or queue,no duplication of execution.");
			return true;
		} else {
			task.setStartTime(CalendarUtils.getDate());
			task.setStatus(CommonClientConfigs.TaskStatus.EXECUTING);
			task.setLog("");
			task.setDownload("");
			task.setEndTime("");
			task.setFileSize(0L);
			int code = tasksDao.batchModifyTaskStatus(Arrays.asList(task));
			if (code > 0) {
				Queue queue = new Queue();
				queue.setVip(CommonClientConfigs.Task.TASK_COMMON);
				if (TaskQueue.isFull()) {
					queue.setRank(TaskQueue.getPrivateRank());
					queue.setTask(task);
					queue.setStatus(CommonClientConfigs.TaskStatus.QUEUE + "(" + queue.getRank() + ")");
				} else {
					queue.setRank(0);
					queue.setTask(task);
					queue.setStatus(CommonClientConfigs.TaskStatus.EXECUTING);
				}
				TaskQueue.getQueues().add(queue);
				if (TaskFactory.modify() != null && TaskFactory.modify().size() > 0) {
					tasksDao.batchModifyTaskStatus(TaskFactory.modify());
				}
				if (queue.getRank() == 0) {
					ThreadExecutor.addTask(queue.getVip(), queue);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int deleteTaskById(int id) {
		tasksDao.removeProcessById(id);
		return tasksDao.deleteTaskById(id);
	}

	@Override
	public String getAppIdByTaskId(int id) {
		return tasksDao.getAppIdByTaskId(id);
	}

	@Override
	public String getTaskLogById(int id) {
		JSONObject object = new JSONObject();
		object.put("log", tasksDao.getTaskLogById(id));
		return object.toJSONString();
	}

	@Override
	public int killTaskById(int id) {
		String appId = tasksDao.getAppIdByTaskId(id);
		if (appId == "" || appId == null) {
			return 0;
		} else {
			try {
				if (!YarnManager.killApplication(appId)) {
					Task task = tasksDao.findTaskById(id);
					task.setEndTime(CalendarUtils.getDate());
					task.setStatus(CommonClientConfigs.TaskStatus.EXECUTION_STOP);
					tasksDao.batchModifyTaskStatus(Arrays.asList(task));
				}
			} catch (Exception e) {
				e.printStackTrace();
				return 1;
			}
		}
		return 2;
	}

	@Override
	public List<SchedulerTask> loadAutoTask() {
		return tasksDao.loadAutoTask();
	}

}
