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

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.YarnState;
import org.smartloli.hive.cube.web.service.ApplicationsService;
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
 * Applications yarn and viewer task data.
 * 
 * @author smartloli.
 *
 *         Created by Sep 6, 2017.
 */
@Controller
@RequestMapping("/applications")
public class ApplicationsController {

	@Autowired
	private ApplicationsService applicationService;

	/** All viewer. */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ModelAndView allView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/applications/all");
		return mav;
	}

	/** Killed viewer. */
	@RequestMapping(value = "/killed", method = RequestMethod.GET)
	public ModelAndView killedView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/applications/killed");
		return mav;
	}

	/** Running viewer. */
	@RequestMapping(value = "/running", method = RequestMethod.GET)
	public ModelAndView runningdView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/applications/running");
		return mav;
	}

	/** Finished viewer. */
	@RequestMapping(value = "/finished", method = RequestMethod.GET)
	public ModelAndView finisheddView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/applications/finished");
		return mav;
	}

	/** Failed viewer. */
	@RequestMapping(value = "/failed", method = RequestMethod.GET)
	public ModelAndView faileddView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/applications/failed");
		return mav;
	}

	/** Get application dataset by type. */
	@RequestMapping(value = "/yarn/{type}/ajax", method = RequestMethod.GET)
	public void getApplicationAjax(HttpSession session, @PathVariable("type") String type, HttpServletResponse response, HttpServletRequest request) {
		String aoData = request.getParameter("aoData");
		JSONArray jsonArray = JSON.parseArray(aoData);
		int sEcho = 0, iDisplayStart = 0, iDisplayLength = 0;
		String search = "";
		for (Object obj : jsonArray) {
			JSONObject jsonObj = (JSONObject) obj;
			if ("sEcho".equals(jsonObj.getString("name"))) {
				sEcho = jsonObj.getIntValue("value");
			} else if ("iDisplayStart".equals(jsonObj.getString("name"))) {
				iDisplayStart = jsonObj.getIntValue("value");
			} else if ("iDisplayLength".equals(jsonObj.getString("name"))) {
				iDisplayLength = jsonObj.getIntValue("value");
			} else if ("sSearch".equals(jsonObj.getString("name"))) {
				search = jsonObj.getString("value");
			}
		}

		JSONArray dataSets = JSON.parseArray(applicationService.getApplications(type));
		int offset = 0;
		JSONArray target = new JSONArray();
		for (Object object : dataSets) {
			JSONObject dataset = (JSONObject) object;
			if (search.length() > 0 && search.equals(dataset.getString("appId"))) {
				JSONObject app = new JSONObject();
				app.put("id", dataset.getString("appId"));
				app.put("user", dataset.getString("user"));
				app.put("name", dataset.getString("name"));
				app.put("app_type", dataset.getString("type"));
				app.put("start_time", dataset.getString("startTime"));
				app.put("finish_time", dataset.getString("finishedTime"));
				String state = dataset.getString("appState");
				if (YarnState.RUNNING.equals(state)) {
					app.put("state", "<a class='btn btn-primary btn-xs'>" + state + "</a>");
				} else if (YarnState.KILLED.equals(state)) {
					app.put("state", "<a class='btn btn-warning btn-xs'>" + state + "</a>");
				} else if (YarnState.FINISHED.equals(state)) {
					app.put("state", "<a class='btn btn-success btn-xs'>" + state + "</a>");
				} else if (YarnState.FAILED.equals(state)) {
					app.put("state", "<a class='btn btn-danger btn-xs'>" + state + "</a>");
				}
				String final_status = dataset.getString("finalAppStatus");
				if (YarnState.SUCCEEDED.equals(final_status)) {
					app.put("final_status", "<a class='btn btn-success btn-xs'>" + final_status + "</a>");
				} else if (YarnState.KILLED.equals(final_status)) {
					app.put("final_status", "<a class='btn btn-warning btn-xs'>" + final_status + "</a>");
				} else if (YarnState.FAILED.equals(final_status)) {
					app.put("final_status", "<a class='btn btn-danger btn-xs'>" + final_status + "</a>");
				} else {
					app.put("final_status", final_status);
				}
				app.put("progress", "<a style='text-decoration:none;'>" + dataset.getString("progress") + "</a>");
				if ("RUNNING".equals(state)) {
					app.put("operate", "<a name='killed' href='#" + app.getString("appId") + "' class='btn btn-danger btn-xs'>SHUTDOWN</a>");
				} else {
					app.put("operate", "");
				}
				target.add(app);
			} else if (search.length() == 0) {
				if (offset < (iDisplayLength + iDisplayStart) && offset >= iDisplayStart) {
					JSONObject app = new JSONObject();
					app.put("id", dataset.getString("appId"));
					app.put("user", dataset.getString("user"));
					app.put("name", dataset.getString("name"));
					app.put("app_type", dataset.getString("type"));
					app.put("start_time", dataset.getString("startTime"));
					app.put("finish_time", dataset.getString("finishedTime"));
					String state = dataset.getString("appState");
					if (YarnState.RUNNING.equals(state)) {
						app.put("state", "<a class='btn btn-primary btn-xs'>" + state + "</a>");
					} else if (YarnState.KILLED.equals(state)) {
						app.put("state", "<a class='btn btn-warning btn-xs'>" + state + "</a>");
					} else if (YarnState.FINISHED.equals(state)) {
						app.put("state", "<a class='btn btn-success btn-xs'>" + state + "</a>");
					} else if (YarnState.FAILED.equals(state)) {
						app.put("state", "<a class='btn btn-danger btn-xs'>" + state + "</a>");
					}
					String final_status = dataset.getString("finalAppStatus");
					if (YarnState.SUCCEEDED.equals(final_status)) {
						app.put("final_status", "<a class='btn btn-success btn-xs'>" + final_status + "</a>");
					} else if (YarnState.KILLED.equals(final_status)) {
						app.put("final_status", "<a class='btn btn-warning btn-xs'>" + final_status + "</a>");
					} else if (YarnState.FAILED.equals(final_status)) {
						app.put("final_status", "<a class='btn btn-danger btn-xs'>" + final_status + "</a>");
					} else {
						app.put("final_status", final_status);
					}
					app.put("progress", "<a style='text-decoration:none;'>" + dataset.getString("progress") + "</a>");
					if ("RUNNING".equals(state)) {
						app.put("operate", "<a name='killed' href='#" + app.getString("appId") + "' class='btn btn-danger btn-xs'>SHUTDOWN</a>");
					} else {
						app.put("operate", "");
					}
					target.add(app);
				}
				offset++;
			}
		}

		JSONObject object = new JSONObject();
		object.put("sEcho", sEcho);
		object.put("iTotalRecords", dataSets.size());
		object.put("iTotalDisplayRecords", dataSets.size());
		object.put("aaData", target);
		try {
			byte[] output = object.toJSONString().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Killed application by id. */
	@RequiresPermissions("/application/yarn/del")
	@RequestMapping(value = "/yarn/{type}/{appId}/del", method = RequestMethod.GET)
	public ModelAndView killApplication(@PathVariable("type") String type, @PathVariable("appId") String appId) {
		if (applicationService.exist(appId)) {
			if (applicationService.killApplication(appId)) {
				return new ModelAndView("redirect:/applications/" + type);
			} else {
				return new ModelAndView("redirect:/errors/500");
			}
		} else {
			return new ModelAndView("redirect:/errors/404");
		}
	}

}
