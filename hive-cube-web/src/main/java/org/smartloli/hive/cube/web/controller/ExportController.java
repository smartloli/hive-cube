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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.Login;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.Sql;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.TaskStatus;
import org.smartloli.hive.cube.common.pojo.Signiner;
import org.smartloli.hive.cube.common.pojo.Task;
import org.smartloli.hive.cube.web.service.ExportService;
import org.smartloli.hive.cube.web.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

/**
 * Submit task job and export data.
 * 
 * @author smartloli.
 *
 *         Created by Sep 6, 2016.
 */
@Controller
@RequestMapping("/export")
public class ExportController {

	@Autowired
	private ExportService exportService;

	@Autowired
	private TasksService tasksService;

	/** Common export viewer. */
	@RequestMapping(value = "/common", method = RequestMethod.GET)
	public ModelAndView commonView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/export/common");
		return mav;
	}

	/** Customer export viewer. */
	@RequestMapping(value = "/custom", method = RequestMethod.GET)
	public ModelAndView customView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/export/custom");
		return mav;
	}

	/** Administrator owner enter. */
	@RequiresPermissions("/export/task/vip")
	@RequestMapping(value = "/custom/task/vip/ajax", method = RequestMethod.GET)
	public void customVipTaskAjax(@RequestParam("content") String content, @RequestParam("name") String name, HttpServletResponse response, HttpServletRequest request) {
		try {
			Signiner signin = (Signiner) SecurityUtils.getSubject().getSession().getAttribute(Login.SESSION_USER);
			Task task = new Task();
			task.setStatus(TaskStatus.NOT_EXECUTED);
			task.setContent(content);
			task.setIsAuto(0);
			task.setParentId(-1);
			task.setName(name);
			task.setOwner(signin.getRealname());
			task.setEmail(signin.getEmail());
			task.setRank(0);
			exportService.insertCustomTask(task);
			JSONObject target = new JSONObject();
			if (tasksService.executorPrivateTaskById(task.getId())) {
				target.put("code", 1);
			} else {
				target.put("code", 0);
			}
			byte[] output = target.toJSONString().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Customer user enter. */
	@RequestMapping(value = "/custom/task/ajax", method = RequestMethod.GET)
	public void customTaskAjax(@RequestParam("content") String content, @RequestParam("name") String name, HttpServletResponse response, HttpServletRequest request) {
		try {
			Signiner signin = (Signiner) SecurityUtils.getSubject().getSession().getAttribute(Login.SESSION_USER);
			Task task = new Task();
			task.setStatus(TaskStatus.NOT_EXECUTED);
			task.setContent(content);
			task.setIsAuto(0);
			task.setParentId(-1);
			task.setName(name);
			task.setOwner(signin.getRealname());
			task.setEmail(signin.getEmail());
			task.setRank(0);
			JSONObject target = new JSONObject();
			if (content.toLowerCase().contains(Sql.SELECT)) {
				exportService.insertCustomTask(task);
				if (tasksService.executorPrivateTaskById(task.getId())) {
					target.put("code", 1);
				} else {
					target.put("code", 0);
				}
				byte[] output = target.toJSONString().getBytes();
				BaseController.response(output, response);
			} else {
				target.put("code", 0);
				byte[] output = target.toJSONString().getBytes();
				BaseController.response(output, response);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Get common hive table columns describer. */
	@RequestMapping(value = "/common/table/columns/{tableName}/ajax", method = RequestMethod.GET)
	public void tableColumnsAjax(@PathVariable("tableName") String tableName, HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = exportService.getHiveTableColumnsByTableName(tableName).getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Get common hive warehouse tables. */
	@RequestMapping(value = "/common/table/select/ajax", method = RequestMethod.GET)
	public void tableSelectAjax(HttpServletResponse response, HttpServletRequest request) {
		try {
			String name = request.getParameter("name");

			JSONObject table = exportService.getHivePartOfTable(name);
			JSONObject object = new JSONObject();
			object.put("items", table.getJSONArray("tables"));
			object.put("total", table.getJSONArray("tables").size());

			byte[] output = object.toJSONString().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
