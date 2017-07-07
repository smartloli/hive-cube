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
/**
 * 
 */
package org.smartloli.hive.cube.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.HBase;
import org.smartloli.hive.cube.common.pojo.RegionServer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * A class of tools for handling HBase.
 * 
 * @author smartloli.
 *
 *         Created by Jul 7, 2017
 */
public class HBaseUtils {
	private static final Logger LOG = LoggerFactory.getLogger(HBaseUtils.class);

	/** Get hbase region server from hbase jmx api. */
	public static List<RegionServer> getRegionServers() {
		BufferedReader bufReader = null;
		InputStreamReader input = null;
		List<RegionServer> list = new ArrayList<>();
		try {
			URL url = new URL(HBase.HTTP + SystemConfig.getProperty("hive.cube.hbase.master") + HBase.HBASE_REGION_SERVER_JMX);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			input = new InputStreamReader(httpConn.getInputStream(), "UTF-8");
			bufReader = new BufferedReader(input);
			String line = "";
			StringBuilder contentBuf = new StringBuilder();
			while ((line = bufReader.readLine()) != null) {
				contentBuf.append(line);
			}
			JSONObject tmpBuf = JSON.parseObject(contentBuf.toString());
			JSONObject object = (JSONObject) JSON.parseArray(tmpBuf.getString("beans")).get(0);
			String[] liveRegionServers = object.getString("tag.liveRegionServers").split(";");
			for (String node : liveRegionServers) {
				if (node.length() != 0) {
					RegionServer region = new RegionServer();
					region.setRegionName(node.split(",")[0] + ":" + node.split(",")[1]);
					region.setStartTime(CalendarUtils.convertUnixTime(Long.parseLong(node.split(",")[2])));
					region.setLive(true);
					list.add(region);
				}
			}
			String[] deadRegionServers = object.getString("tag.deadRegionServers").split(";");
			for (String node : deadRegionServers) {
				if (node.length() != 0) {
					RegionServer region = new RegionServer();
					region.setRegionName(node.split(",")[0] + ":" + node.split(",")[1]);
					region.setStartTime(CalendarUtils.convertUnixTime(Long.parseLong(node.split(",")[2])));
					region.setLive(false);
					list.add(region);
				}
			}
		} catch (Exception e) {
			LOG.error("Filter[Region] HBase JMX has error,msg is " + e.getMessage());
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (bufReader != null) {
					bufReader.close();
				}
			} catch (Exception e) {
				LOG.error("Close IO has error,msg is " + e.getMessage());
			}
		}

		return list;
	}
}
