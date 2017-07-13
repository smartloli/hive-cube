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
package org.smartloli.hive.cube.core.metrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.HBase;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.HDFS;
import org.smartloli.hive.cube.common.pojo.DataNode;
import org.smartloli.hive.cube.common.pojo.HdfsDir;
import org.smartloli.hive.cube.common.pojo.NameNode;
import org.smartloli.hive.cube.common.pojo.RegionServer;
import org.smartloli.hive.cube.common.util.CalendarUtils;
import org.smartloli.hive.cube.common.util.SystemConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Achieve hadoop cluster, namenode & datanode information access and
 * processing.
 * 
 * @author smartloli.
 *
 *         Created by Jul 13, 2017
 */
public class HadoopMetricsServiceImpl implements HadoopMetricsService {

	private Logger LOG = LoggerFactory.getLogger(HadoopMetricsServiceImpl.class);

	/** Get datanodes information. */
	public List<DataNode> datanodes() {
		List<DataNode> list = new ArrayList<DataNode>();
		FileSystem fs = null;
		try {
			String uri = SystemConfig.getProperty("hive.cube.hdfs.uri");
			String user = SystemConfig.getProperty("hive.cube.hadoop.user");
			Configuration conf = new Configuration();
			fs = FileSystem.get(new URI(uri), conf, user);
			DistributedFileSystem hdfs = (DistributedFileSystem) fs;
			DatanodeInfo[] dis = hdfs.getDataNodeStats();
			for (DatanodeInfo info : dis) {
				DataNode hadoopNode = new DataNode();
				hadoopNode.setHostname(info.getHostName() + " (" + info + ")");
				hadoopNode.setState(info.getAdminState().toString());
				hadoopNode.setUpdate_date(CalendarUtils.convertUnixTime(info.getLastUpdate()));
				hadoopNode.setCapacity("<a class='btn btn-primary btn-xs'>" + String.format("%.2f", info.getCapacity() * 1.0 / (1024 * 1024 * 1024)) + " GB</a>");
				if (info.getDfsUsed() * 1.0 / (info.getCapacity() * 1024 * 1024 * 1024) < 0.6) {
					hadoopNode.setUsed("<a class='btn btn-success btn-xs'>" + String.format("%.2f", info.getDfsUsed() * 1.0 / (1024 * 1024 * 1024)) + " GB</a>");
				} else if (info.getDfsUsed() * 1.0 / (info.getCapacity() * 1024 * 1024 * 1024) >= 0.6
						&& info.getDfsUsed() * 1.0 / (info.getCapacity() * 1024 * 1024 * 1024) < 0.8) {
					hadoopNode.setUsed("<a class='btn btn-warning btn-xs'>" + String.format("%.2f", info.getDfsUsed() * 1.0 / (1024 * 1024 * 1024)) + " GB</a>");
				} else {
					hadoopNode.setUsed("<a class='btn btn-danger btn-xs'>" + String.format("%.2f", info.getDfsUsed() * 1.0 / (1024 * 1024 * 1024)) + " GB</a>");
				}
				hadoopNode.setNon_dfs_used(String.format("%.2f", info.getNonDfsUsed() * 1.0 / (1024 * 1024 * 1024)) + " GB");
				hadoopNode.setRemaining(String.format("%.2f", info.getRemaining() * 1.0 / (1024 * 1024 * 1024)) + " GB");
				if (info.getBlockPoolUsedPercent() < 60.0) {
					hadoopNode.setBlock_pool_used("<a class='btn btn-success btn-xs'>" + String.format("%.2f", info.getBlockPoolUsed() * 1.0 / (1024 * 1024 * 1024)) + " GB ("
							+ String.format("%.2f", info.getBlockPoolUsedPercent()) + "%)</a>");
				} else if (info.getBlockPoolUsedPercent() >= 60.0 && info.getBlockPoolUsedPercent() < 80.0) {
					hadoopNode.setBlock_pool_used("<a class='btn btn-warning btn-xs'>" + String.format("%.2f", info.getBlockPoolUsed() * 1.0 / (1024 * 1024 * 1024)) + " GB ("
							+ String.format("%.2f", info.getBlockPoolUsedPercent()) + "%)</a>");
				} else {
					hadoopNode.setBlock_pool_used("<a class='btn btn-danger btn-xs'>" + String.format("%.2f", info.getBlockPoolUsed() * 1.0 / (1024 * 1024 * 1024)) + " GB ("
							+ String.format("%.2f", info.getBlockPoolUsedPercent()) + "%)</a>");
				}
				list.add(hadoopNode);
			}
		} catch (Exception ex) {
			LOG.error("Get Hadoop datanodes has error,msg is" + ex.getMessage());
		} finally {
			if (fs != null) {
				try {
					fs.close();
					fs = null;
				} catch (IOException e) {
					LOG.error("Close hdfs[DN] has error,msg is " + e.getMessage());
				}
			}
		}
		return list;
	}

	/** Get hadoop namenodes information. */
	public NameNode namenodes() {
		String uri = SystemConfig.getProperty("hive.cube.hdfs.web");
		BufferedReader bufReader = null;
		InputStreamReader input = null;
		NameNode nn = new NameNode();
		try {
			URL url = new URL(uri + HDFS.NAMENODE_JMX);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			input = new InputStreamReader(httpConn.getInputStream(), "UTF-8");
			bufReader = new BufferedReader(input);
			String line = "";
			StringBuilder contentBuf = new StringBuilder();
			while ((line = bufReader.readLine()) != null) {
				contentBuf.append(line);
			}
			JSONObject obj = JSON.parseObject(contentBuf.toString());
			JSONArray array = JSON.parseArray(obj.getString("beans"));
			JSONObject tmp = (JSONObject) array.get(0);
			nn.setCapacity(tmp.getLong("Total"));
			nn.setClusterStartTime(CalendarUtils.formatLocale(tmp.getString("NNStarted")));
			nn.setDeadNodes(JSON.parseObject(tmp.getString("DeadNodes")).size());
			nn.setDecomNodes(JSON.parseObject(tmp.getString("DecomNodes")).size());
			nn.setDfsRemaining(tmp.getLong("Free"));
			nn.setDfsUsed(tmp.getLong("Used"));
			nn.setLiveNodes(JSON.parseObject(tmp.getString("LiveNodes")).size());
			nn.setNonDFSUsed(tmp.getLong("NonDfsUsedSpace"));
			nn.setVersion(tmp.getString("SoftwareVersion"));
		} catch (Exception e) {
			LOG.error("Get hadoop namenode data has error,msg is " + e.getMessage());
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

		return nn;
	}

	/** Get hbase region servers. */
	public List<RegionServer> regionServers() {
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

	/** Get hdfs menu. */
	public List<HdfsDir> browseDirectory(String path) {
		String web = SystemConfig.getProperty("hive.cube.hdfs.web");
		BufferedReader bufReader = null;
		InputStreamReader input = null;
		List<HdfsDir> list = new ArrayList<>();
		try {
			String visitAddr = web + String.format(HDFS.WEB_HDFS, path);
			URL url = new URL(visitAddr);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			input = new InputStreamReader(httpConn.getInputStream(), "UTF-8");
			bufReader = new BufferedReader(input);
			String line = "";
			StringBuilder contentBuf = new StringBuilder();
			while ((line = bufReader.readLine()) != null) {
				contentBuf.append(line);
			}
			JSONObject bufObject = JSON.parseObject(contentBuf.toString());
			JSONArray array = bufObject.getJSONObject("FileStatuses").getJSONArray("FileStatus");
			for (Object object : array) {
				JSONObject tmp = (JSONObject) object;
				HdfsDir hdfsDir = new HdfsDir();
				int blockSize = tmp.getIntValue("blockSize");
				if (blockSize == 0) {
					hdfsDir.setBlockSize(blockSize + " B");
				} else {
					hdfsDir.setBlockSize(blockSize / (1024 * 1024) + " MB");
				}
				hdfsDir.setChildrenNum(tmp.getIntValue("childrenNum"));
				hdfsDir.setFileId(tmp.getLongValue("fileId"));
				hdfsDir.setGroup(tmp.getString("group"));
				long length = tmp.getLongValue("length");
				if (length == 0) {
					hdfsDir.setLength(length + " B");
				} else {
					DecimalFormat formatter = new DecimalFormat("###.##");
					if (length > 0 && length < 1024) {
						hdfsDir.setLength(length + " B");
					} else if (length >= 1024 && length < (1024 * 1024)) {
						hdfsDir.setLength(formatter.format(length * 1.0 / 1024) + " KB");
					} else if (length >= (1024 * 1024) && length < (1024 * 1024 * 1024)) {
						hdfsDir.setLength(formatter.format(length * 1.0 / (1024 * 1024)) + " MB");
					} else if (length >= (1024 * 1024 * 1024) && length < (1024 * 1024 * 1024 * 1024)) {
						hdfsDir.setLength(formatter.format(length * 1.0 / (1024 * 1024 * 1024)) + " GB");
					} else if (length >= (1024 * 1024 * 1024 * 1024) && length < (1024 * 1024 * 1024 * 1024 * 1024)) {
						hdfsDir.setLength(formatter.format(length * 1.0 / (1024 * 1024 * 1024 * 1024)) + " PB");
					} else if (length >= (1024 * 1024 * 1024 * 1024 * 1024)) {
						hdfsDir.setLength(formatter.format(length * 1.0 / (1024 * 1024 * 1024 * 1024 * 1024)) + " TB");
					}
				}
				hdfsDir.setModify(CalendarUtils.convertUnixTime(tmp.getLongValue("modificationTime")));
				hdfsDir.setName(tmp.getString("pathSuffix"));
				hdfsDir.setOwn(tmp.getString("owner"));
				hdfsDir.setPermission(tmp.getString("permission"));
				hdfsDir.setReplication(tmp.getIntValue("replication"));
				hdfsDir.setType(tmp.getString("type"));
				hdfsDir.setPath(path + tmp.getString("pathSuffix"));
				list.add(hdfsDir);
			}
		} catch (Exception e) {
			LOG.error("Get data from hdfs has error,msg is " + e.getMessage());
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (bufReader != null) {
					bufReader.close();
				}
			} catch (Exception e) {
				LOG.error("Close io has error,msg is " + e.getMessage());
			}
		}
		return list;
	}

}
