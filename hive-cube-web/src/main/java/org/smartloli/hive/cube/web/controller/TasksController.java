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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.Login;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.TaskStatus;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.TaskType;
import org.smartloli.hive.cube.common.pojo.Signiner;
import org.smartloli.hive.cube.common.pojo.Task;
import org.smartloli.hive.cube.common.util.StrUtils;
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
 * Task control and viewer data.
 * 
 * @author smartloli.
 *
 *         Created by Sep 6, 2016.
 */
@Controller
@RequestMapping("/tasks")
public class TasksController {

	@Autowired
	private TasksService taskService;

	/** Public task viewer. */
	@RequestMapping(value = "/public", method = RequestMethod.GET)
	public ModelAndView publicView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/tasks/public");
		return mav;
	}

	/** Private task viewer. */
	@RequestMapping(value = "/private", method = RequestMethod.GET)
	public ModelAndView privateView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/tasks/private");
		return mav;
	}

	/** Delete private task by id. */
	@RequestMapping(value = "/delete/private/task/{id}/", method = RequestMethod.GET)
	public String deleteTaskById(@PathVariable("id") int id, HttpSession session, HttpServletRequest request) {
		int code = taskService.deleteTaskById(id);
		if (code > 0) {
			return "redirect:/tasks/private";
		} else {
			return "redirect:/errors/500";
		}
	}

	/** Executor private task. */
	@RequestMapping(value = "/executor/private/task/{id}/", method = RequestMethod.GET)
	public String executorPrivateTask(@PathVariable("id") int id, HttpSession session, HttpServletRequest request) {
		boolean status = taskService.executorPrivateTaskById(id);
		if (status) {
			return "redirect:/tasks/private";
		} else {
			return "redirect:/errors/500";
		}
	}

	/** Executor public task. */
	@RequiresPermissions("/tasks/executor/public/task")
	@RequestMapping(value = "/executor/public/task/{id}/", method = RequestMethod.GET)
	public String executorPublicTask(@PathVariable("id") int id, HttpSession session, HttpServletRequest request) {
		boolean status = taskService.executorTaskById(id);
		if (status) {
			return "redirect:/tasks/public";
		} else {
			return "redirect:/errors/500";
		}

	}

	/** Get public task data. */
	@RequestMapping(value = "/public/table/ajax", method = RequestMethod.GET)
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
		map.put("auto", TaskType.MANUAL);
		map.put("start", iDisplayStart);
		map.put("size", iDisplayLength);

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
			object.put("operate",
					"<div class='btn-group'><button class='btn btn-primary btn-xs dropdown-toggle' type='button' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>Action <span class='caret'></span></button><ul class='dropdown-menu dropdown-menu-right'><li><a href='/hc/tasks/executor/public/task/"
							+ id + "/'>Start</a><li><a href='/hc/tasks/kill/public/task/" + id + "/'>Stop</a><li><a name='operater_modal' href='#" + id
							+ "'>Edit</a><li><a name='operater_modal_auto' href='#" + id + "'>Auto</a></ul></div>");
			aaDatas.add(object);
		}

		Map<String, Object> sender = new HashMap<>();
		sender.put("is_auto", TaskType.MANUAL);
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

	/** Get private table data. */
	@RequestMapping(value = "/private/table/ajax", method = RequestMethod.GET)
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
		Signiner signin = (Signiner) SecurityUtils.getSubject().getSession().getAttribute(Login.SESSION_USER);

		Map<String, Object> map = new HashMap<>();
		map.put("search", "%" + search + "%");
		map.put("start", iDisplayStart);
		map.put("size", iDisplayLength);
		map.put("auto", TaskType.MANUAL);
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
			object.put("operate",
					"<div class='btn-group'><button class='btn btn-primary btn-xs dropdown-toggle' type='button' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>Action <span class='caret'></span></button><ul class='dropdown-menu dropdown-menu-right'><li><a href='/hc/tasks/executor/private/task/"
							+ id + "/'>Run</a><li><a href='/hc/tasks/kill/private/task/" + id + "/'>Stop</a><li><a name='operater_modal' href='#" + id
							+ "'>Edit</a><li><a name='operater_modal' href='/hc/tasks/delete/private/task/" + id + "/'>Delete</a></ul></div>");
			aaDatas.add(object);
		}

		Map<String, Object> sender = new HashMap<>();
		sender.put("is_auto", TaskType.MANUAL);
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

	/** Get content by task id. */
	@RequestMapping(value = "/content/{id}/ajax", method = RequestMethod.GET)
	public void getTaskByIdAjax(@PathVariable("id") int id, HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = taskService.findTaskById(id).getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Get task log by task id. */
	@RequestMapping(value = "/executor/log/{id}/ajax", method = RequestMethod.GET)
	public void getTaskLogByIdAjax(@PathVariable("id") int id, HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = taskService.getTaskLogById(id).getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Kill private task by id. */
	@RequestMapping(value = "/kill/private/task/{id}/", method = RequestMethod.GET)
	public String killPrivateTaskById(@PathVariable("id") int id, HttpSession session, HttpServletRequest request) {
		int code = taskService.killTaskById(id);
		if (code == 2) {
			return "redirect:/tasks/private";
		} else if (code == 1) {
			return "redirect:/errors/500";
		} else {
			return "redirect:/errors/100";
		}
	}

	/** Kill public task by id. */
	@RequiresPermissions("/tasks/kill/public/task/")
	@RequestMapping(value = "/kill/{type}/task/{id}/", method = RequestMethod.GET)
	public String killPublicTaskById(@PathVariable("id") int id, HttpSession session, HttpServletRequest request) {
		int code = taskService.killTaskById(id);
		if (code == 2) {
			return "redirect:/tasks/public";
		} else if (code == 1) {
			return "redirect:/errors/500";
		} else {
			return "redirect:/errors/100";
		}
	}

	/** Modify task content by task id. */
	@RequestMapping(value = "/content/modify/", method = RequestMethod.POST)
	public String modifyTaskContent(HttpSession session, HttpServletRequest request) {
		String id = request.getParameter("hc_task_id");
		String name = request.getParameter("hc_task_name");
		String email = request.getParameter("hc_task_email");
		String content = request.getParameter("hc_task_content");
		String column = request.getParameter("hc_task_column");
		String ref = request.getParameter("hc_task_ref");
		JSONObject object = new JSONObject();
		object.put("column", column);
		try {
			object.put("context", JSON.parseArray(content.trim().replaceAll("\r\n", "")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("name", name);
		map.put("email", email);
		map.put("content", object.toJSONString());
		int code = taskService.modifyTaskContentByParams(map);
		if (code > 0) {
			return "redirect:/tasks" + ref;
		} else {
			return "redirect:/errors/500";
		}

	}

}
