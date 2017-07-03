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
import java.util.EnumSet;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.ApplicationNotFoundException;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.pojo.Task;

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
