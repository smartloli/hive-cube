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
package org.smartloli.hive.cube.plugins.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.smartloli.hive.cube.common.pojo.OdpsContent;
import org.smartloli.hive.cube.plugins.util.JConstants;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Use sql query data from mysql.
 * 
 * @author smartloli.
 *
 *         Created by Jul 7, 2017
 */
public class MySqlRecordReader {

	/** Get columns. */
	public static String column(OdpsContent odps) {
		JSONObject result = new JSONObject();
		Connection connection = MySqlStoragePlugin.getInstance(odps.getJdbc(), odps.getUsername(), odps.getPassword());
		ResultSet rs = null;
		List<String> list = new ArrayList<>();
		try {
			rs = connection.createStatement().executeQuery(odps.getSql());
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				list.add(rsmd.getColumnName(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		MySqlStoragePlugin.close(connection);
		result.put("column", list);
		return result.toJSONString();
	}

	/** Get mysql databases datasets. */
	public static String database(OdpsContent odps) {
		JSONObject datasets = new JSONObject();
		Connection connection = MySqlStoragePlugin.getInstance(odps.getHost() + ":" + odps.getPort(), odps.getUsername(), odps.getPassword());
		ResultSet rs = null;
		List<String> list = new ArrayList<>();
		try {
			rs = connection.createStatement().executeQuery(JConstants.SHOW_DATABASES);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					list.add(rs.getString(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		MySqlStoragePlugin.close(connection);
		datasets.put("db", list);
		return datasets.toJSONString();
	}

	/** Executor mysql sql and get executor result. */
	public static String sql(OdpsContent odps) {
		JSONArray datasets = new JSONArray();
		Connection connection = MySqlStoragePlugin.getInstance(odps.getJdbc(), odps.getUsername(), odps.getPassword());
		ResultSet rs = null;
		try {
			rs = connection.createStatement().executeQuery(odps.getSql());
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				for (int i = 1; i <= columnCount; i++) {
					obj.put(rsmd.getColumnName(i), rs.getString(i));
				}
				datasets.add(obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		MySqlStoragePlugin.close(connection);
		return datasets.toJSONString();
	}

}
