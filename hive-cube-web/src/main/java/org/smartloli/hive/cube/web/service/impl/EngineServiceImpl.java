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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hive.jdbc.HiveStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.pojo.Task;
import org.smartloli.hive.cube.common.pojo.TaskProcess;
import org.smartloli.hive.cube.common.util.CSVUtils;
import org.smartloli.hive.cube.common.util.HiveUtils;
import org.smartloli.hive.cube.common.util.SystemConfig;
import org.smartloli.hive.cube.web.dao.ProcessDao;
import org.smartloli.hive.cube.web.dao.TasksDao;
import org.smartloli.hive.cube.web.service.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Engine service implements.
 * 
 * @author smartloli.
 *
 *         Created by Jun 8, 2017
 */
@Service
public class EngineServiceImpl implements EngineService {

	private Logger LOG = LoggerFactory.getLogger(EngineServiceImpl.class);

	@Autowired
	private ProcessDao processDao;
	@Autowired
	private TasksDao taskDao;

	class HiveLogThread extends Thread {

		private int taskId = 0;

		private Statement stmt = null;

		public Statement getStmt() {
			return stmt;
		}

		public void setStmt(Statement stmt) {
			this.stmt = stmt;
		}

		public int getTaskId() {
			return taskId;
		}

		public void setTaskId(int taskId) {
			this.taskId = taskId;
		}

		@SuppressWarnings("static-access")
		public void run() {
			if (stmt == null) {
				return;
			}
			HiveStatement hiveStatement = (HiveStatement) stmt;
			try {
				String message = "";
				String applicationId = "";
				while (!hiveStatement.isClosed() && ((HiveStatement) stmt).hasMoreLogs()) {
					try {
						for (String log : ((HiveStatement) stmt).getQueryLog(true, 100)) {
							message += log + "\n";
							TaskProcess taskProcess = new TaskProcess();
							taskProcess.setTaskId(taskId);
							taskProcess.setLog(message);
							if (log.contains("The url to track the job")) {
								applicationId = log.split("proxy")[1].split("/")[1];
								taskProcess.setAppId(applicationId);
							} else {
								taskProcess.setAppId(applicationId);
							}
							processDao.updateTaskProcess(taskProcess);
						}
						Thread.currentThread().sleep(500L);
					} catch (Exception e) {
						update(e.getMessage());
						return;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				update(e.getMessage());
				return;
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					stmt = null;
				}
			}
		}

		public void update(String message) {
			TaskProcess taskProcess = new TaskProcess();
			taskProcess.setTaskId(taskId);
			taskProcess.setLog(message);
			processDao.updateTaskProcess(taskProcess);
		}
	}

	@Override
	public String executeQuery(int id, String sql, String columns) {
		JSONObject target = new JSONObject();
		String error = "";
		String reback = "";
		HiveUtils hiveUtils = new HiveUtils();
		ResultSet rs = null;
		Statement stmt = null;
		Connection connection = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			connection = hiveUtils.getConnect();
			stmt = connection.createStatement();
			HiveLogThread hiveLog = new HiveLogThread();
			hiveLog.setTaskId(id);
			hiveLog.setStmt(stmt);
			hiveLog.start();

			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				list.add(map);
			}
			LinkedHashMap header = new LinkedHashMap<>();
			List<Map<String, Object>> bodys = new ArrayList<>();
			if (columns != "") {
				int index = 0;
				for (String column : columns.split(",")) {
					index++;
					header.put(index + "", column);
					if (list.size() == 0) {
						Map<String, Object> tmp = new LinkedHashMap<>();
						tmp.put(index + "", "");
						bodys.add(tmp);
					}
				}
				if (bodys.size() != 0) {
					list = bodys;
				}
			} else {
				if (list.size() > 0) {
					int index = 0;
					for (Entry<String, Object> entry : list.get(0).entrySet()) {
						index++;
						header.put(index + "", entry.getKey());
					}
				}
			}

			if (list.size() > 0) {
				for (Map<String, Object> map : list) {
					Map<String, Object> tmp = new LinkedHashMap<>();
					int index = 0;
					for (Entry<String, Object> entry : map.entrySet()) {
						index++;
						tmp.put(index + "", entry.getValue());
					}
					bodys.add(tmp);
				}
			}

			// Modify task status to finished and generate download file.
			String outPutPath = SystemConfig.getProperty("hive.cube.task.export.path");
			reback = CSVUtils.createCSVFile(bodys, header, outPutPath, "task_id_" + id + "_");
		} catch (Exception ex) {
			LOG.error("Exec SQL[" + sql + "] has error,msg is" + ex.getMessage());
			error = ex.getMessage();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				LOG.error("Close Hive has error,msg is " + e.getMessage());
				error = e.getMessage();
			}
		}

		target.put("error", error);
		target.put("reback", JSON.parseObject(reback));
		return target.toJSONString();
	}

	@Override
	public int modifyTaskStatus(Task task) {
		return taskDao.batchModifyTaskStatus(Arrays.asList(task));
	}

}
