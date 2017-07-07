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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.HDFS;
import org.smartloli.hive.cube.web.service.HdfsService;
import org.smartloli.hive.cube.web.service.MetricsService;
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
 * Metrics cluster and viewer task data.
 * 
 * @author smartloli.
 *
 *         Created by Sep 6, 2017.
 */
@Controller
@RequestMapping("/metrics")
public class MetricsController {

	private Logger LOG = LoggerFactory.getLogger(MetricsController.class);

	@Autowired
	private HdfsService hdfsService;

	@Autowired
	private MetricsService metricsService;

	/** HBase viewer. */
	@RequestMapping(value = "/hbase", method = RequestMethod.GET)
	public ModelAndView hbaseView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/metrics/hbase");
		return mav;
	}

	/** Hdfs viewer. */
	@RequestMapping(value = "/hdfs", method = RequestMethod.GET)
	public ModelAndView hdfsView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/metrics/hdfs");
		return mav;
	}

	/** Hadoop viewer. */
	@RequestMapping(value = "/hadoop", method = RequestMethod.GET)
	public ModelAndView hadoop(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/metrics/hadoop");
		return mav;
	}

	/** Yarn viewer. */
	@RequestMapping(value = "/yarn", method = RequestMethod.GET)
	public ModelAndView yarnView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/metrics/yarn");
		return mav;
	}

	/** Download file from hdfs. */
	@RequestMapping("/hdfs/{fileName}/download")
	public String download(@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
		String path = fileName.replaceAll(HDFS.UNDERLINE, File.separator);
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + path.split("/")[path.split("/").length - 1]);
		InputStream inputStream = null;
		OutputStream os = null;
		try {
			inputStream = hdfsService.download(File.separator + path);
			os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
		} catch (Exception e) {
			LOG.error("Download hdfs result has error,msg is " + e.getMessage());
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				LOG.error("Close IO has error,msg is " + e.getMessage());
			}
		}
		return null;
	}

	/** Get hbase region server data. */
	@RequestMapping(value = "/hbase/region/server/ajax", method = RequestMethod.GET)
	public void dashBoardAjax(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = metricsService.getHBaseRegionServer().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Get hdfs menu data. */
	@RequestMapping(value = "/hdfs/{path}/ajax", method = RequestMethod.GET)
	public void getHdfsDirAjax(HttpSession session, @PathVariable("path") String path, HttpServletResponse response, HttpServletRequest request) {
		String aoData = request.getParameter("aoData");
		JSONArray jsonArray = JSON.parseArray(aoData);
		int sEcho = 0, iDisplayStart = 0, iDisplayLength = 0;
		String search = "";
		for (Object object : jsonArray) {
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

		JSONArray dataSets = hdfsService.dir(path.equals("root") ? "" : path.replaceAll(HDFS.UNDERLINE, File.separator) + File.separator);
		int offset = 0;
		JSONArray targets = new JSONArray();
		for (Object object : dataSets) {
			JSONObject dataset = (JSONObject) object;
			if (search.length() > 0 && search.equals(dataset.getString("name"))) {
				JSONObject target = new JSONObject();
				String type = dataset.getString("type");
				if (type.equals(HDFS.FILE)) {
					target.put("name", "<a name='hdfs_info' href='#" + dataset.getString("path") + "'><i class='fa fa-file-text fa-fw'></i> " + dataset.getString("name") + "</a>");
				} else {
					target.put("name",
							"<a href='/hc/metrics/hdfs?" + dataset.getString("path") + "'><i class='fa fa-folder-open fa-fw'></i> " + dataset.getString("name") + "</a>");
				}
				target.put("permisson", dataset.getString("permission"));
				target.put("owner", dataset.getString("own"));
				target.put("group", dataset.getString("group"));
				target.put("size", dataset.getString("length"));
				target.put("replication", dataset.getInteger("replication"));
				target.put("block_size", dataset.getString("blockSize"));
				target.put("modify", dataset.getString("modify"));
				target.put("children_num", dataset.getInteger("childrenNum"));
				target.put("type", dataset.getString("type"));
				target.put("operate", "<a name='killed' href='#" + dataset.getString("path") + "' class='btn btn-danger btn-xs'>DELETE</a>");
				targets.add(target);
			} else if (search.length() == 0) {
				if (offset < (iDisplayLength + iDisplayStart) && offset >= iDisplayStart) {
					JSONObject target = new JSONObject();
					String type = dataset.getString("type");
					if (type.equals(HDFS.FILE)) {
						target.put("name",
								"<a name='hdfs_info' href='#" + dataset.getString("path") + "'><i class='fa fa-file-text fa-fw'></i> " + dataset.getString("name") + "</a>");
					} else {
						target.put("name",
								"<a href='/hc/metrics/hdfs?" + dataset.getString("path") + "'><i class='fa fa-folder-open fa-fw'></i> " + dataset.getString("name") + "</a>");
					}
					target.put("permisson", dataset.getString("permission"));
					target.put("owner", dataset.getString("own"));
					target.put("group", dataset.getString("group"));
					target.put("size", dataset.getString("length"));
					target.put("replication", dataset.getInteger("replication"));
					target.put("block_size", dataset.getString("blockSize"));
					target.put("modify", dataset.getString("modify"));
					target.put("children_num", dataset.getInteger("childrenNum"));
					target.put("type", dataset.getString("type"));
					target.put("operate", "<a name='killed' href='#" + dataset.getString("path") + "' class='btn btn-danger btn-xs'>DELETE</a>");
					targets.add(target);
				}
				offset++;
			}
		}

		JSONObject object = new JSONObject();
		object.put("sEcho", sEcho);
		object.put("iTotalRecords", dataSets.size());
		object.put("iTotalDisplayRecords", dataSets.size());
		object.put("aaData", targets);
		try {
			byte[] output = object.toJSONString().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Get hdfs file content. */
	@RequestMapping(value = "/hdfs/context/{path}/ajax", method = RequestMethod.GET)
	public void getHdfsFileContentAjax(@PathVariable("path") String path, HttpSession session, HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = hdfsService.read(path).getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Get hadoop nodes data. */
	@RequestMapping(value = "/hadoop/nodes/info/ajax", method = RequestMethod.GET)
	public void getHadoopNodesAjax(HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = metricsService.getHadoopNodes().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
	}

	/** Get hadoop chart data. */
	@RequestMapping(value = "/hadoop/chart/info/ajax", method = RequestMethod.GET)
	public void getHadoopChartAjax(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = metricsService.getHadoopChart().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Get yarn resource data. */
	@RequestMapping(value = "/yarn/metrics/ajax", method = RequestMethod.GET)
	public void GetYarnResourceAjax(HttpServletResponse response, HttpServletRequest request) {
		try {
			byte[] output = metricsService.getYarnResource().getBytes();
			BaseController.response(output, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
	}

	/** Delete file from hdfs. */
	@RequiresPermissions("/metrics/hdfs/del")
	@RequestMapping(value = "/hdfs/{path}/del", method = RequestMethod.GET)
	public ModelAndView hdfsMenuOrFileDel(HttpSession session, @PathVariable("path") String path) {
		hdfsService.delete(File.separator + path.replaceAll(HDFS.UNDERLINE, File.separator));
		return new ModelAndView("redirect:/metrics/hdfs");
	}
}
