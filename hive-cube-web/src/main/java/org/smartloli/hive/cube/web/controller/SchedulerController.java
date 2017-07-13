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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.smartloli.hive.cube.common.client.CommonClientConfigs;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.TaskStatus;
import org.smartloli.hive.cube.common.pojo.Scheduler;
import org.smartloli.hive.cube.common.pojo.Signiner;
import org.smartloli.hive.cube.common.pojo.Task;
import org.smartloli.hive.cube.common.util.StrUtils;
import org.smartloli.hive.cube.web.service.SchedulerService;
import org.smartloli.hive.cube.web.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Scheduler task and viewer data.
 * 
 * @author smartloli.
 *
 *         Created by Sep 6, 2016.
 */
@Controller
@RequestMapping("/tasks")
public class SchedulerController {

	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private TasksService taskService;

	/** Auto private task viewer. */
	@RequestMapping(value = "/auto/private", method = RequestMethod.GET)
	public ModelAndView autoPrivateView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/tasks/auto_private");
		return mav;
	}

	/** Auto public task viewer. */
	@RequestMapping(value = "/auto/public", method = RequestMethod.GET)
	public ModelAndView autoPublicView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/tasks/auto_public");
		return mav;
	}

	/** Get auto quartz content by task id. */
	@RequestMapping(value = "/auto/quartz/content/{id}/ajax", method = RequestMethod.GET)
	public void getScheduleTaskByIdAjax(@PathVariable("id") int id, HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = schedulerService.getSchedulerByTaskId(id).getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Get private tasks */
	@RequestMapping(value = "/auto/private/table/ajax", method = RequestMethod.GET)
	public void getPrivateTasksAjax(HttpServletResponse response, HttpServletRequest request) {
		String aoData = request.getParameter("aoData");
		JSONArray params = JSON.parseArray(aoData);
		int sEcho = 0, iDisplayStart = 0, iDisplayLength = 0;
		String search = "";
		for (Object object : params) {
			JSONObject param = (JSONObject) object;
			if ("sEcho".equals(param.getString("name"))) {
				sEcho = param.getIntValue("value");
			} else if ("iDisplayStart".equals(param.getString("name"))) {
				iDisplayStart = param.getIntValue("value");
			} else if ("iDisplayLength".equals(param.getString("name"))) {
				iDisplayLength = param.getIntValue("value");
			} else if ("sSearch".equals(param.getString("name"))) {
				search = param.getString("value");
			}
		}
		Signiner signin = (Signiner) SecurityUtils.getSubject().getSession().getAttribute(CommonClientConfigs.Login.SESSION_USER);

		Map<String, Object> map = new HashMap<>();
		map.put("search", "%" + search + "%");
		map.put("start", iDisplayStart);
		map.put("size", iDisplayLength);
		map.put("auto", CommonClientConfigs.TaskType.AUTO);
		map.put("owner", signin.getRealname());

		List<Task> tasks = taskService.getPrivateTasks(map);
		JSONArray aaDatas = new JSONArray();
		for (Task task : tasks) {
			JSONObject object = new JSONObject();
			String download = task.getDownload();
			int id = task.getId();
			object.put("id", id);
			object.put("name", task.getName());
			object.put("owner", task.getOwner());
			if (task.getStatus().equals(TaskStatus.EXECUTING)) {
				object.put("status", "<a class='btn btn-primary btn-xs'>" + task.getStatus() + "...</a>");
			} else if (task.getStatus().contains(TaskStatus.QUEUE)) {
				object.put("status", "<a class='btn btn-warning btn-xs'>" + task.getStatus() + "</a>");
			} else if (task.getStatus().equals(TaskStatus.EXECUTION_END)) {
				object.put("status", "<a class='btn btn-success btn-xs'>" + task.getStatus() + "</a>");
			} else if (task.getStatus().equals(TaskStatus.EXECUTION_ERROR)) {
				object.put("status", "<a class='btn btn-danger btn-xs'>" + task.getStatus() + "</a>");
			} else if (task.getStatus().equals(TaskStatus.EXECUTION_STOP)) {
				object.put("status", "<a class='btn btn-info btn-xs'>" + task.getStatus() + "</a>");
			} else if (task.getStatus().equals(TaskStatus.EXECUTION_START)) {
				object.put("status", "<a class='btn btn-info btn-xs'>" + task.getStatus() + "</a>");
			}
			
			if (task.getLog() != "" && task.getLog() != null) {
				object.put("log", "<a name='operate_task_log' href='#" + id + "'>Detail</a>");
			} else {
				object.put("log", task.getLog());
			}
			if (download != null && download != "") {
				object.put("result", "<a href='/assert/hc/export/" + task.getDownload() + "'>Download</a>");
			} else {
				object.put("result", "");
			}
			
			try {
				object.put("process", "<a name='operate_process_modal' href='#" + id + "'>" + task.getProcess() + "</a>");
			} catch (Exception e) {
				object.put("process", "0.00%");
				e.printStackTrace();
			}
			object.put("filesize", StrUtils.stringify(task.getFileSize()));
			object.put("stime", task.getStartTime());
			object.put("etime", task.getEndTime());
			if (task.getStatus().equals(CommonClientConfigs.TaskStatus.EXECUTION_STOP)) {
				object.put("operate",
						"<div class='btn-group'><button class='btn btn-primary btn-xs dropdown-toggle' type='button' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>Action <span class='caret'></span></button><ul class='dropdown-menu dropdown-menu-right'><li><a name='operater_modal_setting' tag='modify' href='#"
								+ id + "'>Crontab</a></li><li><a name='operater_modal_setting' tag='start' href='#" + id + "'>Start</a></li><li><a name='operater_modal' href='#"
								+ id + "'>Edit</a><li><a name='operater_modal_setting' tag='delete' href='#" + id + "'>Delete</a></li></ul></div>");
			} else {
				object.put("operate",
						"<div class='btn-group'><button class='btn btn-primary btn-xs dropdown-toggle' type='button' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>Action <span class='caret'></span></button><ul class='dropdown-menu dropdown-menu-right'><li><a name='operater_modal_setting' tag='modify' href='#"
								+ id + "'>Crontab</a></li><li><a name='operater_modal_setting' tag='stop' href='#" + id + "'>Stop</a></li><li><a name='operater_modal' href='#" + id
								+ "'>Edit</a></li><li><a name='operater_modal_setting' tag='delete' href='#" + id + "'>Delete</a></li></ul></div>");
			}

			aaDatas.add(object);
		}

		Map<String, Object> sender = new HashMap<>();
		sender.put("is_auto", CommonClientConfigs.TaskType.AUTO);
		sender.put("owner", signin.getRealname());
		int count = taskService.count(sender);

		JSONObject target = new JSONObject();
		target.put("sEcho", sEcho);
		target.put("iTotalRecords", count);
		target.put("iTotalDisplayRecords", count);
		target.put("aaData", aaDatas);
		try {
			byte[] output = target.toJSONString().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Get public task. */
	@RequestMapping(value = "/auto/public/table/ajax", method = RequestMethod.GET)
	public void getPublicTasksAjax(HttpServletResponse response, HttpServletRequest request) {
		String aoData = request.getParameter("aoData");
		JSONArray params = JSON.parseArray(aoData);
		int sEcho = 0, iDisplayStart = 0, iDisplayLength = 0;
		String search = "";
		for (Object object : params) {
			JSONObject param = (JSONObject) object;
			if ("sEcho".equals(param.getString("name"))) {
				sEcho = param.getIntValue("value");
			} else if ("iDisplayStart".equals(param.getString("name"))) {
				iDisplayStart = param.getIntValue("value");
			} else if ("iDisplayLength".equals(param.getString("name"))) {
				iDisplayLength = param.getIntValue("value");
			} else if ("sSearch".equals(param.getString("name"))) {
				search = param.getString("value");
			}
		}

		Map<String, Object> map = new HashMap<>();
		map.put("search", "%" + search + "%");
		map.put("start", iDisplayStart);
		map.put("size", iDisplayLength);
		map.put("auto", CommonClientConfigs.TaskType.AUTO);

		List<Task> tasks = taskService.getTasks(map);
		JSONArray aaDatas = new JSONArray();
		for (Task task : tasks) {
			JSONObject object = new JSONObject();
			String download = task.getDownload();
			int id = task.getId();
			object.put("id", id);
			object.put("name", task.getName());
			object.put("owner", task.getOwner());
			if (task.getStatus().equals(TaskStatus.EXECUTING)) {
				object.put("status", "<a class='btn btn-primary btn-xs'>" + task.getStatus() + "...</a>");
			} else if (task.getStatus().contains(TaskStatus.QUEUE)) {
				object.put("status", "<a class='btn btn-warning btn-xs'>" + task.getStatus() + "</a>");
			} else if (task.getStatus().equals(TaskStatus.EXECUTION_END)) {
				object.put("status", "<a class='btn btn-success btn-xs'>" + task.getStatus() + "</a>");
			} else if (task.getStatus().equals(TaskStatus.EXECUTION_ERROR)) {
				object.put("status", "<a class='btn btn-danger btn-xs'>" + task.getStatus() + "</a>");
			} else if (task.getStatus().equals(TaskStatus.EXECUTION_STOP)) {
				object.put("status", "<a class='btn btn-info btn-xs'>" + task.getStatus() + "</a>");
			} else if (task.getStatus().equals(TaskStatus.EXECUTION_START)) {
				object.put("status", "<a class='btn btn-info btn-xs'>" + task.getStatus() + "</a>");
			}
			
			if (task.getLog() != "" && task.getLog() != null) {
				object.put("log", "<a name='operate_task_log' href='#" + id + "'>Detail</a>");
			} else {
				object.put("log", task.getLog());
			}
			if (download != null && download != "") {
				object.put("result", "<a href='/assert/hc/export/" + task.getDownload() + "'>Download</a>");
			} else {
				object.put("result", "");
			}
			
			try {
				object.put("process", "<a name='operate_process_modal' href='#" + id + "'>" + task.getProcess() + "</a>");
			} catch (Exception e) {
				object.put("process", "0.00%");
				e.printStackTrace();
			}
			object.put("filesize", StrUtils.stringify(task.getFileSize()));
			object.put("stime", task.getStartTime());
			object.put("etime", task.getEndTime());
			if (task.getStatus().equals(CommonClientConfigs.TaskStatus.EXECUTION_STOP)) {
				object.put("operate",
						"<div class='btn-group'><button class='btn btn-primary btn-xs dropdown-toggle' type='button' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>Action <span class='caret'></span></button><ul class='dropdown-menu dropdown-menu-right'><li><a name='operater_modal_setting' tag='modify' href='#"
								+ id + "'>Crontab</a></li><li><a name='operater_modal_setting' tag='start' href='#" + id + "'>Start</a></li><li><a name='operater_modal' href='#"
								+ id + "'>Edit</a></li><li><a name='operater_modal_setting' tag='cancle' href='#" + id + "'>Cancle</a></li></ul></div>");
			} else {
				object.put("operate",
						"<div class='btn-group'><button class='btn btn-primary btn-xs dropdown-toggle' type='button' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>Action <span class='caret'></span></button><ul class='dropdown-menu dropdown-menu-right'><li><a name='operater_modal_setting' tag='modify' href='#"
								+ id + "'>Crontab</a></li><li><a name='operater_modal_setting' tag='stop' href='#" + id + "'>Stop</a></li><li><a name='operater_modal' href='#" + id
								+ "'>Edit</a></li><li><a name='operater_modal_setting' tag='cancle' href='#" + id + "'>Cancle</a></li></ul></div>");
			}

			aaDatas.add(object);
		}

		Map<String, Object> sender = new HashMap<>();
		sender.put("is_auto", CommonClientConfigs.TaskType.AUTO);
		sender.put("owner", "");
		int count = taskService.count(sender);

		JSONObject target = new JSONObject();
		target.put("sEcho", sEcho);
		target.put("iTotalRecords", count);
		target.put("iTotalDisplayRecords", count);
		target.put("aaData", aaDatas);
		try {
			byte[] output = target.toJSONString().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Modify auto quartz. */
	@RequestMapping(value = "/auto/quartz/modify/", method = RequestMethod.POST)
	public String modifyScheduleTask(HttpSession session, HttpServletRequest request) {
		String id = request.getParameter("hc_task_name_id");
		String cron = request.getParameter("hc_task_quartz");
		String ref = request.getParameter("hc_task_name_ref");
		String action = request.getParameter("hc_task_name_action");
		Scheduler scheduler = new Scheduler();
		scheduler.setTaskId(Integer.valueOf(id));
		if (CommonClientConfigs.Scheduler.STOP.equals(action)) {
			scheduler.setTaskSwitch(1);// stop
			scheduler.setAuto(CommonClientConfigs.TaskType.AUTO);
		} else if (CommonClientConfigs.Scheduler.MODIFY.equals(action) || CommonClientConfigs.Scheduler.ADD.equals(action) || CommonClientConfigs.Scheduler.START.equals(action)) {
			scheduler.setTaskSwitch(0);// add or modify
			scheduler.setAuto(CommonClientConfigs.TaskType.AUTO);
		} else if (CommonClientConfigs.Scheduler.CANCLE.equals(action)) {
			scheduler.setAuto(CommonClientConfigs.TaskType.MANUAL);
		}
		scheduler.setCronExpression(cron);
		scheduler.setType(action);
		int code = schedulerService.updateScheduler(scheduler);
		if (code > 0) {
			return "redirect:/tasks/auto/" + ref;
		} else {
			return "redirect:/errors/500";
		}
	}

}
