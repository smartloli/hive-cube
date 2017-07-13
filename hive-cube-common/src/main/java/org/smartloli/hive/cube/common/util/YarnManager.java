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
package org.smartloli.hive.cube.common.util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.NodeReport;
import org.apache.hadoop.yarn.api.records.NodeState;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.ApplicationNotFoundException;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.YarnState;
import org.smartloli.hive.cube.common.pojo.ApplicationContent;
import org.smartloli.hive.cube.common.pojo.Task;
import org.smartloli.hive.cube.common.pojo.YarnClusterMetrics;
import org.smartloli.hive.cube.common.pojo.YarnNode;

/**
 * Get the Hadoop resource management information, task details, interfaces.
 *
 * @author smartloli.
 *
 *         Created by Sep 26, 2016
 */
public class YarnManager {

	private final static Logger LOG = LoggerFactory.getLogger(YarnManager.class);
	private final static Configuration conf = new Configuration();
	static {
		try {
			conf.setBoolean("mapreduce.app-submission.cross-platform", true);
			conf.set("fs.defaultFS", SystemConfig.getProperty("hive.cube.hdfs.uri"));
			conf.set("mapreduce.framework.name", "yarn");
			conf.set("yarn.resourcemanager.address", SystemConfig.getProperty("hive.cube.yarn.rm.uri"));
			conf.set("yarn.resourcemanager.scheduler.address", SystemConfig.getProperty("hive.cube.yarn.scheduler.uri"));
		} catch (Exception ex) {
			LOG.error("Initialize yarn uri address has error,msg is " + ex.getMessage());
		}
	}

	/** Checked application id whether exist. */
	public static boolean exist(String appId) throws YarnException, IOException, InterruptedException, ClassNotFoundException {
		YarnClient client = YarnClient.createYarnClient();
		client.init(conf);
		client.start();

		EnumSet<YarnApplicationState> appStates = EnumSet.noneOf(YarnApplicationState.class);
		if (appStates.isEmpty()) {
			appStates.add(YarnApplicationState.RUNNING);
			appStates.add(YarnApplicationState.FAILED);
			appStates.add(YarnApplicationState.KILLED);
			appStates.add(YarnApplicationState.FINISHED);
		}
		List<ApplicationReport> appsReport = client.getApplications(appStates);
		for (ApplicationReport appReport : appsReport) {
			if (appReport.getApplicationId().toString().equals(appId)) {
				if (client != null) {
					client.close();
				}
				return true;
			}
		}
		if (client != null) {
			client.close();
		}

		return false;
	}

	/** Get application data by type. */
	public static List<ApplicationContent> getApplications(String type) throws YarnException, IOException, InterruptedException, ClassNotFoundException {
		YarnClient client = YarnClient.createYarnClient();
		client.init(conf);
		client.start();

		EnumSet<YarnApplicationState> appStates = EnumSet.noneOf(YarnApplicationState.class);
		if (appStates.isEmpty()) {
			if ("all".equals(type)) {
				appStates.add(YarnApplicationState.RUNNING);
				appStates.add(YarnApplicationState.FAILED);
				appStates.add(YarnApplicationState.KILLED);
				appStates.add(YarnApplicationState.FINISHED);
			} else if ("killed".equals(type)) {
				appStates.add(YarnApplicationState.KILLED);
			} else if ("running".equals(type)) {
				appStates.add(YarnApplicationState.RUNNING);
			} else if ("finished".equals(type)) {
				appStates.add(YarnApplicationState.FINISHED);
			} else if ("failed".equals(type)) {
				appStates.add(YarnApplicationState.FAILED);
			}
		}
		List<ApplicationReport> appsReport = client.getApplications(appStates);
		List<ApplicationContent> list = new ArrayList<ApplicationContent>();
		for (ApplicationReport appReport : appsReport) {
			ApplicationContent yarnState = new ApplicationContent();
			yarnState.setAppId(appReport.getApplicationId().toString());
			yarnState.setAppState(appReport.getYarnApplicationState().toString());
			yarnState.setFinalAppStatus(appReport.getFinalApplicationStatus().toString());
			yarnState.setFinishedTime(CalendarUtils.convertUnixTime(appReport.getFinishTime()));
			yarnState.setName(appReport.getName());
			DecimalFormat formatter = new DecimalFormat("###.##%");
			yarnState.setProgress(formatter.format(appReport.getProgress()));
			yarnState.setStartTime(CalendarUtils.convertUnixTime(appReport.getStartTime()));
			yarnState.setType(appReport.getApplicationType());
			yarnState.setUser(appReport.getUser());
			list.add(yarnState);
		}
		if (client != null) {
			client.close();
		}

		// Sort
		Collections.sort(list, new Comparator<ApplicationContent>() {
			public int compare(ApplicationContent arg0, ApplicationContent arg1) {
				long hits0 = Long.parseLong(arg0.getAppId().split("_")[1]) + Long.parseLong(arg0.getAppId().split("_")[2]);
				long hits1 = Long.parseLong(arg1.getAppId().split("_")[1]) + Long.parseLong(arg1.getAppId().split("_")[2]);

				if (hits1 > hits0) {
					return 1;
				} else if (hits1 == hits0) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		return list;
	}

	/** Get hadoop task proccess by application id. */
	public static String getProcessByAppId(String appId) throws YarnException, IOException, InterruptedException, ClassNotFoundException {
		YarnClient client = YarnClient.createYarnClient();
		client.init(conf);
		client.start();
		String process = "0.00%";

		EnumSet<YarnApplicationState> appStates = EnumSet.noneOf(YarnApplicationState.class);
		if (appStates.isEmpty()) {
			appStates.add(YarnApplicationState.RUNNING);
			appStates.add(YarnApplicationState.FAILED);
			appStates.add(YarnApplicationState.KILLED);
			appStates.add(YarnApplicationState.FINISHED);
		}
		List<ApplicationReport> appsReport = client.getApplications(appStates);
		for (ApplicationReport appReport : appsReport) {
			if (appReport.getApplicationId().toString().equals(appId)) {
				DecimalFormat formatter = new DecimalFormat("###.##%");
				process = formatter.format(appReport.getProgress());

				if (client != null) {
					client.close();
				}
				return process;
			}
		}
		if (client != null) {
			client.close();
		}

		return process;
	}

	/** Batch get hadoop task process by application id. */
	public static void getProcessByAppIds(List<Task> tasks) throws YarnException, IOException, InterruptedException, ClassNotFoundException {
		YarnClient client = YarnClient.createYarnClient();
		client.init(conf);
		client.start();

		EnumSet<YarnApplicationState> appStates = EnumSet.noneOf(YarnApplicationState.class);
		if (appStates.isEmpty()) {
			appStates.add(YarnApplicationState.RUNNING);
			appStates.add(YarnApplicationState.FAILED);
			appStates.add(YarnApplicationState.KILLED);
			appStates.add(YarnApplicationState.FINISHED);
		}
		List<ApplicationReport> appsReport = client.getApplications(appStates);

		for (Task task : tasks) {
			for (ApplicationReport appReport : appsReport) {
				if (appReport.getApplicationId().toString().equals(task.getAppId())) {
					DecimalFormat formatter = new DecimalFormat("###.##%");
					task.setProcess(formatter.format(appReport.getProgress()));
					break;
				}
			}

		}
		if (client != null) {
			client.close();
		}
	}

	/** Get yarn cluster data. */
	public static YarnClusterMetrics getYarnClusterMetrics() throws YarnException, IOException {
		YarnClient client = YarnClient.createYarnClient();
		client.init(conf);
		client.start();
		List<ApplicationReport> appsReport = client.getApplications();
		long appsRunning = 0L;
		for (ApplicationReport applicationReport : appsReport) {
			if (YarnState.RUNNING.equals(applicationReport.getYarnApplicationState().toString())) {
				appsRunning++;
			}
		}
		YarnClusterMetrics yarnClusterMetrics = new YarnClusterMetrics();
		yarnClusterMetrics.setAppsSubmitted(client.getApplications().size());
		yarnClusterMetrics.setAppsRunning(appsRunning);
		yarnClusterMetrics.setAppsCompleted(client.getApplications().size() - appsRunning);
		List<YarnNode> yarnNodes = getYarnNodes();
		for (YarnNode yarnNodeDomain : yarnNodes) {
			if (YarnState.RUNNING.equals(yarnNodeDomain.getNodeState())) {
				yarnClusterMetrics.setMemoryTotal(yarnClusterMetrics.getMemoryTotal() + yarnNodeDomain.getMemoryAvail());
				yarnClusterMetrics.setMemoryUsed(yarnClusterMetrics.getMemoryUsed() + yarnNodeDomain.getMemoryUsed());
				yarnClusterMetrics.setvCoresTotal(yarnClusterMetrics.getvCoresTotal() + yarnNodeDomain.getvCoresAvail());
				yarnClusterMetrics.setvCoresUsed(yarnClusterMetrics.getvCoresUsed() + yarnNodeDomain.getvCoresUsed());
				yarnClusterMetrics.setContainersRunning(yarnClusterMetrics.getContainersRunning() + yarnNodeDomain.getContainers());

				yarnClusterMetrics.setActiveNodes(yarnClusterMetrics.getActiveNodes() + 1);
			} else if (YarnState.UNHEALTHY.equals(yarnNodeDomain.getNodeState())) {
				yarnClusterMetrics.setUnhealthyNodes(yarnClusterMetrics.getUnhealthyNodes() + 1);
			} else if (YarnState.DECOMMISSIONED.equals(yarnNodeDomain.getNodeState())) {
				yarnClusterMetrics.setDecommissionedNodes(yarnClusterMetrics.getDecommissionedNodes() + 1);
			} else if (YarnState.LOST.equals(yarnNodeDomain.getNodeState())) {
				yarnClusterMetrics.setLostNodes(yarnClusterMetrics.getLostNodes() + 1);
			} else if (YarnState.REBOOTED.equals(yarnNodeDomain.getNodeState())) {
				yarnClusterMetrics.setRebootedNodes(yarnClusterMetrics.getRebootedNodes() + 1);
			}
		}

		if (client != null) {
			client.close();
		}

		return yarnClusterMetrics;
	}

	/** Get yarn nodes data. */
	public static List<YarnNode> getYarnNodes() throws YarnException, IOException {
		YarnClient client = YarnClient.createYarnClient();
		client.init(conf);
		client.start();
		NodeState[] nodeStates = new NodeState[]{NodeState.DECOMMISSIONED, NodeState.LOST, NodeState.REBOOTED, NodeState.RUNNING, NodeState.UNHEALTHY};

		List<NodeReport> nodeReports = client.getNodeReports(nodeStates);
		List<YarnNode> list = new ArrayList<YarnNode>();
		for (NodeReport nodeReport : nodeReports) {
			YarnNode yarnNode = new YarnNode();
			yarnNode.setContainers(nodeReport.getNumContainers());
			yarnNode.setHealthReport(nodeReport.getHealthReport());
			yarnNode.setLastHealthUpdate(CalendarUtils.convertUnixTime(nodeReport.getLastHealthReportTime()));
			yarnNode.setMemoryAvail(nodeReport.getCapability().getMemory());
			yarnNode.setMemoryUsed(nodeReport.getUsed().getMemory());
			yarnNode.setNodeAddress(nodeReport.getNodeId().getHost() + ":" + nodeReport.getNodeId().getPort());
			yarnNode.setNodeHttpAddress(nodeReport.getHttpAddress());
			yarnNode.setNodeState(nodeReport.getNodeState().toString());
			yarnNode.setvCoresAvail(nodeReport.getCapability().getVirtualCores());
			yarnNode.setvCoresUsed(nodeReport.getUsed().getVirtualCores());

			list.add(yarnNode);
		}

		if (client != null) {
			client.close();
		}

		return list;
	}

	/** Killed hadoop task by application id. */
	public static boolean killApplication(String applicationId) throws YarnException, IOException {
		boolean status = false;
		ApplicationId appId = ConverterUtils.toApplicationId(applicationId);
		ApplicationReport appReport = null;
		YarnClient client = YarnClient.createYarnClient();
		client.init(conf);
		client.start();
		try {
			appReport = client.getApplicationReport(appId);
		} catch (ApplicationNotFoundException e) {
			LOG.error("Application with id '" + applicationId + "' doesn't exist in RM. Msg is" + e.getMessage());
		}

		if (appReport.getYarnApplicationState() == YarnApplicationState.FINISHED || appReport.getYarnApplicationState() == YarnApplicationState.KILLED
				|| appReport.getYarnApplicationState() == YarnApplicationState.FAILED) {
			status = true;
			LOG.info("Application " + applicationId + " has already finished ");
		} else {
			LOG.info("Killing application " + applicationId);
			client.killApplication(appId);
			status = false;
		}
		if (client != null) {
			client.close();
		}
		return status;
	}

}
