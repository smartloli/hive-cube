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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.smartloli.hive.cube.common.client.CommonClientConfigs;
import org.smartloli.hive.cube.common.pojo.D3;
import org.smartloli.hive.cube.common.pojo.Task;
import org.smartloli.hive.cube.common.util.CalendarUtils;
import org.smartloli.hive.cube.common.util.StrUtils;
import org.smartloli.hive.cube.web.dao.DashboardDao;
import org.smartloli.hive.cube.web.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Dashboard data generator.
 * 
 * @author smartloli.
 *
 *         Created by Aug 12, 2016.
 * 
 */
@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private DashboardDao dashboardDao;

	@Override
	public String getDashboard() {
		JSONObject object = new JSONObject();
		object.put("panel", dashboardDao.panel());
		object.put("chart", chart());
		object.put("lastest", datas());
		return object.toJSONString();
	}

	private JSONArray datas() {
		JSONArray array = new JSONArray();
		List<Task> tasks = dashboardDao.lastest10();
		for (Task task : tasks) {
			JSONObject object = new JSONObject();
			object.put("id", task.getId());
			object.put("name", task.getName());
			object.put("owner", task.getOwner());
			if (task.getStatus().equals(CommonClientConfigs.TaskStatus.EXECUTING)) {
				object.put("status", "<a class='btn btn-primary btn-xs'>" + task.getStatus() + "...</a>");
			} else if (task.getStatus().contains(CommonClientConfigs.TaskStatus.QUEUE)) {
				object.put("status", "<a class='btn btn-warning btn-xs'>" + task.getStatus() + "</a>");
			} else if (task.getStatus().equals(CommonClientConfigs.TaskStatus.EXECUTION_END)) {
				object.put("status", "<a class='btn btn-success btn-xs'>" + task.getStatus() + "</a>");
			} else if (task.getStatus().equals(CommonClientConfigs.TaskStatus.EXECUTION_ERROR)) {
				object.put("status", "<a class='btn btn-danger btn-xs'>" + task.getStatus() + "</a>");
			} else if (task.getStatus().equals(CommonClientConfigs.TaskStatus.EXECUTION_STOP)) {
				object.put("status", "<a class='btn btn-info btn-xs'>" + task.getStatus() + "</a>");
			}
			object.put("size", StrUtils.stringify(task.getFileSize()));
			object.put("stime", task.getStartTime());
			object.put("etime", task.getEndTime());
			array.add(object);
		}
		return array;
	}

	private JSONArray chart() {
		List<D3> charts = dashboardDao.chart();
		JSONArray array = new JSONArray();
		Map<String, JSONObject> map = new HashMap<>();
		for (int i = 0; i < 24; i++) {
			JSONObject object = new JSONObject();
			String tm = "";
			if (i < 10) {
				tm = CalendarUtils.getCustomDate("yyyy-MM-dd") + " 0" + i + ":00";
			} else {
				tm = CalendarUtils.getCustomDate("yyyy-MM-dd") + " " + i + ":00";
			}
			object.put(CommonClientConfigs.Task.PERIOD, tm);
			object.put(CommonClientConfigs.Task.NEW_TASK_NUMBER, 0);
			map.put(tm, object);
		}

		for (D3 chart : charts) {
			JSONObject object = map.get(chart.getHour());
			object.put(CommonClientConfigs.Task.NEW_TASK_NUMBER, chart.getSize());
		}

		for (Entry<String, JSONObject> entry : map.entrySet()) {
			array.add(entry.getValue());
		}
		return array;
	}

}
