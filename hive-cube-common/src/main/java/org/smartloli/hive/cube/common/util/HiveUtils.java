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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hive JDBC Class Utils.
 *
 * @author smartloli.
 *
 *         Created by Nov 4, 2015
 */
public class HiveUtils {

	private final static Logger LOG = LoggerFactory.getLogger(HiveUtils.class);
	private Connection conn = null;

	static {
		try {
			Class.forName("org.apache.hive.jdbc.HiveDriver");
			LOG.info("Hive init driver success.");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Hive init driver failed,msg is " + e.getMessage());
		}
	}

	public Connection getConnect() {
		return this.conn;
	}

	public HiveUtils() {
		try {
			String[] urls = SystemConfig.getProperty("hive.cube.hive.url").split(",");
			for (String url : urls) {
				String connect = url.split("&")[0];
				String user = url.split("&")[1];
				String pwd = url.split("&")[2];
				try {
					conn = DriverManager.getConnection(connect, user, pwd);
					if (conn != null) {
						break;
					}
				} catch (SQLException ex) {
					LOG.error("URL[" + url + "] has error");
				}
			}
		} catch (Exception e) {
			LOG.error("Config file has error url.");
		}
	}

	/** Executes the given SQL statement, which returns a single. */
	public ResultSet executeQuery(String hql) throws SQLException {
		Statement stmt = null;
		ResultSet res = null;

		if (conn != null) {
			LOG.info("HQL[" + hql + "]");
			stmt = conn.createStatement();
			res = stmt.executeQuery(hql);
		} else {
			LOG.info("Object [conn] is null");
		}
		return res;
	}

	/** Executes the given SQL statement, which may return multiple results. */
	public void execute(String hql) throws SQLException {
		if (conn != null) {
			conn.createStatement().execute(hql);
		} else {
			LOG.info("Object [conn] is null");
		}
	}

	/**
	 * Releases this Connection object's database and JDBC resources immediately
	 * instead of waiting for them to be automatically released.
	 */
	public void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (!conn.isClosed()) {
				conn.close();
			}
		} catch (Exception ex) {
			LOG.error("Close hive connect object has error,msg is " + ex.getMessage());
		}
	}
}
