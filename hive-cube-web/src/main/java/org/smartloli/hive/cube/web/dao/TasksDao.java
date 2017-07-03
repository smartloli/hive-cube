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
package org.smartloli.hive.cube.web.dao;

import java.util.List;
import java.util.Map;

import org.smartloli.hive.cube.common.pojo.SchedulerTask;
import org.smartloli.hive.cube.common.pojo.Task;

/**
 * Task dao interface.
 * 
 * @author smartloli.
 *
 *         Created by Jun 6, 2017
 */
public interface TasksDao {

	public int batchModifyTaskStatus(List<Task> tasks);

	public int convertOrCancleAutoTask(Map<String, Object> params);

	public int count(Map<String, Object> params);

	public int deleteTaskById(int id);

	public Task findTaskById(int id);

	public String getAppIdByTaskId(int id);

	public List<Task> getPrivateTasks(Map<String, Object> params);

	public List<Task> getTasks(Map<String, Object> params);

	public String getTaskLogById(int id);

	public List<Task> loadRunningTask();
	
	public List<SchedulerTask> loadAutoTask();

	public int modifyTaskContentByParams(Map<String, Object> params);
	
	public int removeProcessById(int id);

	public int stopTaskById(int id);

}
