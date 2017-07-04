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
import org.smartloli.hive.cube.common.pojo.Rowkey;
import org.smartloli.hive.cube.common.pojo.Signiner;
import org.smartloli.hive.cube.web.service.HiveService;
import org.smartloli.hive.cube.web.service.RowkeyService;
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
 * System module service control.
 * 
 * @author smartloli.
 *
 *         Created by Sep 6, 2016.
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

	@Autowired
	private HiveService hiveService;

	@Autowired
	private RowkeyService rowkeyService;

	/** Config hive view. */
	@RequiresPermissions("/config/hive")
	@RequestMapping(value = "/hive", method = RequestMethod.GET)
	public ModelAndView hiveView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/config/hive");
		return mav;
	}

	/** Config hbase view. */
	@RequiresPermissions("/config/hbase")
	@RequestMapping(value = "/hbase", method = RequestMethod.GET)
	public ModelAndView hbaseView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/config/hbase");
		return mav;
	}

	/** Config hive columns view. */
	@RequiresPermissions("/config/hive")
	@RequestMapping(value = "/hive/{tableName}/", method = RequestMethod.GET)
	public ModelAndView hiveColumnView(@PathVariable("tableName") String tableName, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if (hiveService.findTableByName(tableName) > 0) {
			mav.setViewName("/config/hive_column");
		} else {
			mav.setViewName("/errors/404");
		}
		return mav;
	}

	/** Get hive table list. */
	@RequestMapping(value = "/hive/all/table/ajax", method = RequestMethod.GET)
	public void getHiveTableAjax(HttpServletResponse response, HttpServletRequest request) {
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

		JSONArray tables = JSON.parseArray(hiveService.getHiveSyncStatus(map).toString());
		JSONArray aaDatas = new JSONArray();
		for (Object object : tables) {
			JSONObject table = (JSONObject) object;
			JSONObject obj = new JSONObject();
			int code = table.getInteger("code");
			String tableName = table.getString("tableName");
			obj.put("tablename", "<a href='/hc/config/hive/" + tableName + "/'>" + tableName + "</a>");
			obj.put("aliasname", table.getString("aliasName"));
			if (code > 0) {
				obj.put("status", "<a' class='btn btn-success btn-xs'>Success</a>");
			} else {
				obj.put("status", "<a' class='btn btn-danger btn-xs'>Failed</a>");
			}
			obj.put("time", table.getString("time"));
			obj.put("operate", "<a id='operater_modal' name='operater_modal' href='#" + tableName + "' class='btn btn-primary btn-xs'>Edit</a>");
			aaDatas.add(obj);
		}

		int count = hiveService.count();
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

	/** Get hive table columns by table name. */
	@RequestMapping(value = "/hive/columns/table/{tableName}/ajax", method = RequestMethod.GET)
	public void getHiveTableColumnsAjax(@PathVariable("tableName") String tableName, HttpServletResponse response, HttpServletRequest request) {
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

		JSONArray tables = hiveService.getHiveTableColumnByName(tableName);

		int offset = 0;
		JSONArray aaDatas = new JSONArray();
		for (Object object : tables) {
			JSONObject table = (JSONObject) object;
			String columnName = table.getString("col_name");
			if (search.length() > 0 && search.equals(table.getString("col_name"))) {
				JSONObject obj = new JSONObject();
				obj.put("column_name", columnName);
				obj.put("comment", table.getString("comment"));
				obj.put("type", table.getString("data_type"));
				obj.put("operate", "<a id='operater_modal' name='operater_modal' href='#" + columnName + "' class='btn btn-primary btn-xs'>Edit</a>");
				aaDatas.add(obj);
			} else if (search.length() == 0) {
				if (offset < (iDisplayLength + iDisplayStart) && offset >= iDisplayStart) {
					JSONObject obj = new JSONObject();
					obj.put("column_name", columnName);
					obj.put("comment", table.getString("comment"));
					obj.put("type", table.getString("data_type"));
					obj.put("operate", "<a id='operater_modal' name='operater_modal' href='#" + columnName + "' class='btn btn-primary btn-xs'>Edit</a>");
					aaDatas.add(obj);
				}
				offset++;
			}
		}

		JSONObject target = new JSONObject();
		target.put("sEcho", sEcho);
		target.put("iTotalRecords", tables.size());
		target.put("iTotalDisplayRecords", tables.size());
		target.put("aaData", aaDatas);
		try {
			byte[] output = target.toJSONString().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Get hbase rowkey from table. */
	@RequestMapping(value = "/hbase/rowkey/table/ajax", method = RequestMethod.GET)
	public void getHBaseRowkeyAjax(HttpServletResponse response, HttpServletRequest request) {
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

		List<Rowkey> rowkeys = rowkeyService.getRowkeys(map);
		JSONArray aaDatas = new JSONArray();
		for (Rowkey rowkey : rowkeys) {
			JSONObject object = new JSONObject();
			object.put("tname", rowkey.getTname());
			object.put("regular", "<a name='operate_modal_regular' href='#" + rowkey.getTname() + "'>View</a>");
			object.put("author", rowkey.getAuthor());
			object.put("time", rowkey.getTm());
			object.put("operate",
					"<div class='btn-group'><button class='btn btn-primary btn-xs dropdown-toggle' type='button' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>Action <span class='caret'></span></button><ul class='dropdown-menu dropdown-menu-right'><li><a name='operate_modal_edit_rowkey' href='#"
							+ rowkey.getTname() + "/'>Edit</a></li><li><a href='/hc/config/hbase/rowkey/delete/" + rowkey.getTname() + "/'>Delete</a></li></ul></div>");
			aaDatas.add(object);
		}

		int count = rowkeyService.count();
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

	/** Get hbase rowkey regular by table name. */
	@RequestMapping(value = "/hbase/rowkey/regular/{tname}/ajax", method = RequestMethod.GET)
	public void getHBaseRowkeyByTNameAjax(@PathVariable("tname") String tname, HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = rowkeyService.findRowkeyByName(tname).getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Sync hive all tables. */
	@RequestMapping(value = "/hive/sync/all/", method = RequestMethod.GET)
	public String syncHiveAllTable(HttpSession session, HttpServletRequest request) {
		hiveService.synchronizeAllTableFromHive();
		return "redirect:/config/hive";
	}

	/** Sync hive one table. */
	@RequestMapping(value = "/hive/sync/one/", method = RequestMethod.POST)
	public String syncHiveOneTable(HttpSession session, HttpServletRequest request) {
		String tableName = request.getParameter("hc_name_hive_table");
		String aliasName = request.getParameter("hc_alias_hive_table");
		hiveService.synchronizeTableFromHiveByName(tableName, aliasName);
		return "redirect:/config/hive";
	}

	/** Replace hive one table content. */
	@RequestMapping(value = "/hive/replace/one/", method = RequestMethod.POST)
	public String replaceHiveOneTable(HttpSession session, HttpServletRequest request) {
		String tableName = request.getParameter("hc_name_hive_table");
		String columnName = request.getParameter("hc_column_name_hive_table");
		String comment = request.getParameter("hc_comment_hive_table");
		hiveService.modifyColumnCommentByName(tableName, columnName, comment);
		return "redirect:/config/hive/" + tableName + "/";
	}

	/** Add hbase rowkey. */
	@RequestMapping(value = "/hbase/rowkey/add/", method = RequestMethod.POST)
	public String rowkeyHBaseAddForm(HttpSession session, HttpServletRequest request) {
		String tname = request.getParameter("hc_hbase_table");
		String regular = request.getParameter("hc_hbase_rowkey_content");
		Signiner signin = (Signiner) SecurityUtils.getSubject().getSession().getAttribute(Login.SESSION_USER);

		Rowkey rowkey = new Rowkey();
		rowkey.setRegular(regular.trim().replaceAll(" ", ""));
		rowkey.setTname(tname);
		rowkey.setAuthor(signin.getRealname());

		int code = rowkeyService.replace(rowkey);
		if (code > 0) {
			return "redirect:/config/hbase";
		} else {
			return "redirect:/errors/500";
		}
	}

	/** Delete hbase rowkey by table name. */
	@RequestMapping(value = "/hbase/rowkey/delete/{tname}/", method = RequestMethod.GET)
	public String rowkeyDelete(@PathVariable("tname") String tname, HttpSession session, HttpServletRequest request) {
		int code = rowkeyService.deleteRowkeyByName(tname);
		if (code > 0) {
			return "redirect:/config/hbase";
		} else {
			return "redirect:/errors/500";
		}
	}
}
