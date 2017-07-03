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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.pojo.HiveSyncStatus;
import org.smartloli.hive.cube.common.pojo.HiveTable;
import org.smartloli.hive.cube.common.util.HiveUtils;
import org.smartloli.hive.cube.web.dao.HiveDao;
import org.smartloli.hive.cube.web.service.HiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Hive service interface.
 * 
 * @author smartloli.
 *
 *         Created by May 27, 2017
 */
@Service
public class HiveServiceImpl implements HiveService {

	private Logger LOG = LoggerFactory.getLogger(HiveServiceImpl.class);

	@Autowired
	private HiveDao hiveDao;

	@Override
	public JSONArray synchronizeAllTableFromHive() {
		ResultSet rs = null;
		HiveUtils hive = null;
		List<String> tables = new ArrayList<>();
		JSONArray status = new JSONArray();
		try {
			hive = new HiveUtils();
			rs = hive.executeQuery("show tables");
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= count; i++) {
					tables.add(rs.getString(i));
				}
			}
		} catch (Exception e) {
			LOG.error("Get table from hive has error,msg is " + e.getMessage());
		} finally {
			if (rs != null) {
				hive.close(rs);
			}
		}
		for (String tableName : tables) {
			int code = hiveDao.findTableByTableName(tableName);
			if (code <= 0) {
				JSONArray array = getHiveDescribe(tableName);
				HiveTable hiveTable = new HiveTable();
				hiveTable.setTableNameEn(tableName);
				hiveTable.setTableNameZh(tableName);
				hiveTable.setTableColumnsZh(array.toJSONString());
				int syscCode = hiveDao.replaceIntoTable(hiveTable);
				JSONObject object = new JSONObject();
				object.put("name", tableName);
				object.put("code", syscCode);
				status.add(object);
				// Update hive_sync_status table
				hiveSyncStatus(tableName, tableName, syscCode);
			}
		}

		return status;
	}

	private void hiveSyncStatus(String tableName, String aliasName, int syscCode) {
		// Update hive_sync_status table
		HiveSyncStatus hiveSyncStatus = new HiveSyncStatus();
		hiveSyncStatus.setAliasName(aliasName);
		hiveSyncStatus.setCode(syscCode);
		hiveSyncStatus.setTableName(tableName);
		hiveDao.replaceIntoSyncTable(hiveSyncStatus);
	}

	private JSONArray getHiveDescribe(String tableName) {
		HiveUtils hive = null;
		ResultSet rs = null;
		JSONArray array = new JSONArray();
		try {
			hive = new HiveUtils();
			rs = hive.executeQuery("desc " + tableName);
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			boolean flag = false;
			while (rs.next()) {
				JSONObject object = new JSONObject();
				for (int i = 1; i <= count; i++) {
					String name = rsmd.getColumnName(i);
					String value = rs.getString(i);
					if (value == null || value == "") {
						flag = true;
						break;
					} else {
						object.put(name, value);
					}
				}
				if (flag) {
					break;
				}
				array.add(object);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				hive.close(rs);
			}
		}
		return array;
	}

	@Override
	public JSONObject synchronizeTableFromHiveByName(String tableName, String aliasName) {
		JSONArray array = getHiveDescribe(tableName);
		HiveTable hiveTable = new HiveTable();
		hiveTable.setTableNameEn(tableName);
		hiveTable.setTableNameZh(aliasName);
		hiveTable.setTableColumnsZh(array.toJSONString());
		int syscCode = hiveDao.replaceIntoTable(hiveTable);
		JSONObject object = new JSONObject();
		object.put("name", tableName);
		object.put("code", syscCode);

		// Update hive_sync_status table
		hiveSyncStatus(tableName, aliasName, syscCode);

		return object;
	}

	@Override
	public int count() {
		return hiveDao.count();
	}

	@Override
	public List<HiveSyncStatus> getHiveSyncStatus(Map<String, Object> params) {
		return hiveDao.getHiveSyncStatus(params);
	}

	@Override
	public int findTableByName(String tableName) {
		return hiveDao.findTableByTableName(tableName);
	}

	@Override
	public JSONArray getHiveTableColumnByName(String tableName) {
		HiveTable hiveTable = hiveDao.getHiveTableColumnByName(tableName);
		return JSON.parseArray(hiveTable.getTableColumnsZh());
	}

	@Override
	public int modifyColumnCommentByName(String tableName, String columnName, String comment) {
		HiveTable hiveTable = hiveDao.getHiveTableColumnByName(tableName);
		JSONArray columns = JSON.parseArray(hiveTable.getTableColumnsZh());
		JSONArray target = new JSONArray();
		for (Object object : columns) {
			JSONObject column = (JSONObject) object;
			if (column.getString("col_name").equals(columnName)) {
				column.put("comment", comment);
			}
			target.add(column);
		}
		hiveTable.setTableColumnsZh(target.toJSONString());
		return hiveDao.replaceIntoTable(hiveTable);
	}

}
