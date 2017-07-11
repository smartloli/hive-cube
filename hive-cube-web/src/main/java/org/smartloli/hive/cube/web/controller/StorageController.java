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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.smartloli.hive.cube.common.pojo.OdpsContent;
import org.smartloli.hive.cube.plugins.util.JConstants;
import org.smartloli.hive.cube.web.service.StorageService;
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
 * Storage viewer task describer,such as : hbase,mysql,redis.mongo etc.
 * 
 * @author smartloli.
 *
 *         Created by Sep 6, 2016.
 */
@Controller
@RequestMapping("/storage")
public class StorageController {

	@Autowired
	private StorageService storageService;

	/** MySql viewer. */
	@RequestMapping(value = "/mysql", method = RequestMethod.GET)
	public ModelAndView mysqlView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/storage/mysql");
		return mav;
	}

	/** MySql viewer. */
	@RequestMapping(value = "/{type}/{id}/console", method = RequestMethod.GET)
	public ModelAndView mysqlDbView(@PathVariable("type") String type, @PathVariable("id") int id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if (storageService.exist(id)) {
			if (JConstants.MYSQL.equals(type)) {
				mav.setViewName("/storage/mysqldb");
			}
		}
		return mav;
	}

	/** HBase viewer. */
	@RequestMapping(value = "/hbase", method = RequestMethod.GET)
	public ModelAndView hbaseView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/storage/hbase");
		return mav;
	}

	/** Add or modify storage plugins information. */
	@RequiresPermissions("/storage/plugin/add")
	@RequestMapping(value = "/{type}/add/", method = RequestMethod.POST)
	public String addStorageForm(@PathVariable("type") String type, HttpSession session, HttpServletRequest request) {
		String host = request.getParameter("hc_mysql_host");
		String port = request.getParameter("hc_mysql_port");

		OdpsContent odps = new OdpsContent();
		odps.setHost(host);
		odps.setType(type);

		String id = request.getParameter("hc_mysql_id");
		if (id != null && id != "") {
			odps.setId(Integer.valueOf(id));
		}

		try {
			odps.setPort(Integer.valueOf(port));
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/errors/500";
		}
		if (JConstants.MYSQL.equals(type)) {
			String username = request.getParameter("hc_mysql_username");
			String password = request.getParameter("hc_mysql_password");
			odps.setUsername(username);
			odps.setPassword(password);
		}
		if (storageService.replace(odps) > 0) {
			return "redirect:/storage/" + type;
		} else {
			return "redirect:/errors/500";
		}
	}

	/** Delete storage plugins information. */
	@RequiresPermissions("/storage/plugin/delete")
	@RequestMapping(value = "/{type}/{id}/delete/", method = RequestMethod.GET)
	public String deleteStorageForm(@PathVariable("type") String type, @PathVariable("id") int id, HttpSession session, HttpServletRequest request) {
		if (storageService.delete(id) > 0) {
			return "redirect:/storage/" + type;
		} else {
			return "redirect:/errors/500";
		}
	}

	/** Find storage by id. */
	@RequestMapping(value = "/find/{id}/ajax", method = RequestMethod.GET)
	public void findStorageByIdAjax(@PathVariable("id") int id, HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = storageService.findStorageById(id).getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
	}

	/** Get storage plugins dataset. */
	@RequestMapping(value = "/plugins/{type}/table/ajax", method = RequestMethod.GET)
	public void getStorageAjax(@PathVariable("type") String type, HttpServletResponse response, HttpServletRequest request) {
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
		map.put("search", search);
		map.put("type", type);
		map.put("start", iDisplayStart);
		map.put("size", iDisplayLength);

		JSONArray storages = JSON.parseArray(storageService.get(map).toString());
		JSONArray aaDatas = new JSONArray();
		for (Object object : storages) {
			JSONObject storage = (JSONObject) object;
			JSONObject aaData = new JSONObject();
			int id = storage.getInteger("id");
			aaData.put("host", "<a href='/hc/storage/" + storage.getString("type") + "/" + id + "/console'>" + storage.getString("host") + "</a>");
			aaData.put("port", storage.getInteger("port"));
			aaData.put("modify", storage.getString("modify"));
			aaData.put("operate",
					"<div class='btn-group'><button class='btn btn-primary btn-xs dropdown-toggle' type='button' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>Action <span class='caret'></span></button><ul class='dropdown-menu dropdown-menu-right'><li><a id='operater_modal' name='operater_modal' href='#"
							+ id + "/'>Modify</a></li><li><a href='/hc/storage/" + storage.getString("type") + "/" + id + "/delete/'>Delete</a></li></ul></div>");
			aaDatas.add(aaData);
		}

		int count = storageService.count(map);
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

	/** Get storage specify by id & action. */
	@RequestMapping(value = "/specify/{id}/{action}/ajax", method = RequestMethod.GET)
	public void getStorageSpecifyAjax(@PathVariable("id") int id, @PathVariable("action") String action, HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = storageService.getSpecifyById(id, action, request).getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
	}

	/** Get hbase sql schema . */
	@RequestMapping(value = "/specify/hbase/schema/ajax", method = RequestMethod.GET)
	public void getStorageHBaseSchemaAjax(HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = storageService.getHBaseSchema(request.getParameter("sql")).getBytes();;
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}

	}

	/** Get hbase query result by job id. */
	@RequestMapping(value = "/hbase/query/result/ajax/", method = RequestMethod.GET)
	public void getStorageHBaseDataAjax(HttpServletResponse response, HttpServletRequest request) {
		String aoData = request.getParameter("aoData");
		JSONArray params = JSON.parseArray(aoData);
		int sEcho = 0, iDisplayStart = 0, iDisplayLength = 0;
		for (Object object : params) {
			JSONObject param = (JSONObject) object;
			if ("sEcho".equals(param.getString("name"))) {
				sEcho = param.getIntValue("value");
			} else if ("iDisplayStart".equals(param.getString("name"))) {
				iDisplayStart = param.getIntValue("value");
			} else if ("iDisplayLength".equals(param.getString("name"))) {
				iDisplayLength = param.getIntValue("value");
			}
		}

		JSONArray results = storageService.getSpecifyHBase(request.getParameter("sql"));
		JSONArray targets = new JSONArray();
		int offset = 0;
		if (results != null) {
			for (Object object : results) {
				JSONObject result = (JSONObject) object;
				if (offset < (iDisplayLength + iDisplayStart) && offset >= iDisplayStart) {
					JSONObject obj = new JSONObject();
					for (String key : result.keySet()) {
						obj.put(key, result.get(key));
					}
					targets.add(obj);
				}
				offset++;
			}
		}

		JSONObject object = new JSONObject();
		object.put("sEcho", sEcho);
		object.put("iTotalRecords", results == null ? 0 : results.size());
		object.put("iTotalDisplayRecords", results == null ? 0 : results.size());
		object.put("aaData", targets);
		try {
			byte[] output = object.toJSONString().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Get query mysql datasets. */
	@RequestMapping(value = "/specify/{id}/{action}/query/ajax", method = RequestMethod.GET)
	public void queryMySqlAjax(@PathVariable("id") int id, @PathVariable("action") String action, HttpServletResponse response, HttpServletRequest request) {
		String aoData = request.getParameter("aoData");
		JSONArray params = JSON.parseArray(aoData);
		int sEcho = 0, iDisplayStart = 0, iDisplayLength = 0;
		for (Object object : params) {
			JSONObject param = (JSONObject) object;
			if ("sEcho".equals(param.getString("name"))) {
				sEcho = param.getIntValue("value");
			} else if ("iDisplayStart".equals(param.getString("name"))) {
				iDisplayStart = param.getIntValue("value");
			} else if ("iDisplayLength".equals(param.getString("name"))) {
				iDisplayLength = param.getIntValue("value");
			}
		}

		JSONArray targets = new JSONArray();
		String datasets = storageService.getSpecifyById(Integer.valueOf(id), action, request);
		JSONArray results = JSON.parseArray(datasets);
		int offset = 0;
		for (Object object : results) {
			JSONObject result = (JSONObject) object;
			if (offset < (iDisplayLength + iDisplayStart) && offset >= iDisplayStart) {
				targets.add(result);
			}
			offset++;
		}

		JSONObject object = new JSONObject();
		object.put("sEcho", sEcho);
		object.put("iTotalRecords", results.size());
		object.put("iTotalDisplayRecords", results.size());
		object.put("aaData", targets);
		try {
			byte[] output = object.toJSONString().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
