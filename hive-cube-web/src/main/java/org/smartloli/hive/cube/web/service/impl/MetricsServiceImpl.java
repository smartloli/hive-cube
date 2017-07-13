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

import java.text.DecimalFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.pojo.NameNode;
import org.smartloli.hive.cube.common.pojo.RegionServer;
import org.smartloli.hive.cube.common.util.CalendarUtils;
import org.smartloli.hive.cube.common.util.YarnManager;
import org.smartloli.hive.cube.core.metrics.HadoopMetricsFactory;
import org.smartloli.hive.cube.core.metrics.HadoopMetricsService;
import org.smartloli.hive.cube.web.service.MetricsService;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Implements metrics hadoop node healthy and yarn resource method.
 * 
 * @author smartloli.
 *
 *         Created by Jul 7, 2017
 */
@Service
public class MetricsServiceImpl implements MetricsService {

	private Logger LOG = LoggerFactory.getLogger(MetricsServiceImpl.class);

	/** Hadoop metrics method. */
	private HadoopMetricsService hms = new HadoopMetricsFactory().create();
	
	/** Get yarn resource from application api. */
	public String getYarnResource() {
		JSONObject object = new JSONObject();
		try {
			object.put("metrics", YarnManager.getYarnClusterMetrics());
			object.put("nodes", YarnManager.getYarnNodes());
		} catch (Exception ex) {
			LOG.error("Get metrics data from yarn has error,msg is " + ex.getMessage());
		}
		return object.toJSONString();
	}

	/** Get hadoop nodes data. */
	public String getHadoopNodes() {
		JSONObject object = new JSONObject();
		object.put("nodes", hms.datanodes());
		return object.toJSONString();
	}

	/** Get hadoop chart data. */
	public String getHadoopChart() {
		JSONObject object = new JSONObject();
		DecimalFormat formatter = new DecimalFormat("###.##");
		NameNode nn = hms.namenodes();
		object.put("version", nn.getVersion());
		if (String.valueOf(nn.getCapacity()).length() > 10 && String.valueOf(nn.getCapacity()).length() < 13) {// GB
			object.put("dfsUsed", formatter.format(nn.getDfsUsed() * 1.0 / (1024 * 1024 * 1024)));
			object.put("nonDFSUsed", formatter.format(nn.getNonDFSUsed() * 1.0 / (1024 * 1024 * 1024)));
			object.put("dfsRemaining", formatter.format(nn.getDfsRemaining() * 1.0 / (1024 * 1024 * 1024)));
			object.put("capacity", formatter.format(nn.getCapacity() * 1.0 / (1024 * 1024 * 1024)));
			object.put("unit", "GB");
		} else if (String.valueOf(nn.getCapacity()).length() > 13 && String.valueOf(nn.getCapacity()).length() < 16) {// TB
			object.put("dfsUsed", formatter.format(Double.parseDouble(formatter.format(nn.getDfsUsed() * 1.0 / (1024 * 1024 * 1024))) / 1024));
			object.put("nonDFSUsed", formatter.format(Double.parseDouble(formatter.format(nn.getNonDFSUsed() * 1.0 / (1024 * 1024 * 1024))) / 1024));
			object.put("dfsRemaining", formatter.format(Double.parseDouble(formatter.format(nn.getDfsRemaining() * 1.0 / (1024 * 1024 * 1024))) / 1024));
			object.put("capacity", formatter.format(Double.parseDouble(formatter.format(nn.getCapacity() * 1.0 / (1024 * 1024 * 1024))) / 1024));
			object.put("unit", "TB");
		} else if (String.valueOf(nn.getCapacity()).length() > 16) {// PB
			object.put("dfsUsed", formatter.format(Double.parseDouble(formatter.format(nn.getDfsUsed() * 1.0 / (1024 * 1024 * 1024))) / (1024 * 1024)));
			object.put("nonDFSUsed", formatter.format(Double.parseDouble(formatter.format(nn.getNonDFSUsed() * 1.0 / (1024 * 1024 * 1024))) / (1024 * 1024)));
			object.put("dfsRemaining", formatter.format(Double.parseDouble(formatter.format(nn.getDfsRemaining() * 1.0 / (1024 * 1024 * 1024))) / (1024 * 1024)));
			object.put("capacity", formatter.format(Double.parseDouble(formatter.format(nn.getCapacity() * 1.0 / (1024 * 1024 * 1024))) / (1024 * 1024)));
			object.put("unit", "PB");
		}
		try {
			object.put("clusterStartTime", CalendarUtils.formatDays(nn.getClusterStartTime()));
		} catch (Exception e) {
			LOG.error("Trans days has error,msg is " + e.getMessage());
		}
		return object.toJSONString();
	}

	/** Get hbase region server data. */
	public String getHBaseRegionServer() {
		JSONObject object = new JSONObject();
		object.put("regions", regions());
		return object.toJSONString();
	}

	private Object regions() {
		JSONObject object = new JSONObject();
		object.put("name", "Active HBase");
		List<RegionServer> regionServers = hms.regionServers();
		JSONArray targets = new JSONArray();
		for (RegionServer region : regionServers) {
			JSONObject target = new JSONObject();
			if (!region.isLive()) {
				target.put("name", region.getRegionName() + " (Dead)");
				targets.add(target);
			} else {
				target.put("name", region.getRegionName());
				targets.add(target);
			}
		}
		object.put("children", targets);
		return object;
	}

}
