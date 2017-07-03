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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smartloli.hive.cube.common.pojo.HiveSyncStatus;
import org.smartloli.hive.cube.common.pojo.HiveTable;
import org.smartloli.hive.cube.common.pojo.Task;
import org.smartloli.hive.cube.web.dao.ExportDao;
import org.smartloli.hive.cube.web.dao.HiveDao;
import org.smartloli.hive.cube.web.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Export service interface.
 * 
 * @author smartloli.
 *
 *         Created by May 28, 2017
 */
@Service
public class ExportServiceImpl implements ExportService {

	@Autowired
	private HiveDao hiveDao;
	@Autowired
	private ExportDao exportDao;

	@Override
	public JSONObject getHivePartOfTable(String name) {
		Map<String, Object> params = new HashMap<>();
		if (name != null && name != "") {
			params.put("search", "%" + name + "%");
		}
		params.put("start", 0);
		params.put("size", 10);
		List<HiveSyncStatus> hiveSyncStatuss = hiveDao.getHiveSyncStatus(params);

		int offset = 0;
		JSONArray tables = new JSONArray();
		for (HiveSyncStatus hiveSyncStatus : hiveSyncStatuss) {
			JSONObject object = new JSONObject();
			object.put("text", hiveSyncStatus.getAliasName() + "[" + hiveSyncStatus.getTableName() + "]");
			object.put("id", offset);
			tables.add(object);
			offset++;
		}

		JSONObject table = new JSONObject();
		table.put("tables", tables);
		return table;
	}

	@Override
	public String getHiveTableColumnsByTableName(String tableName) {
		HiveTable hiveTable = hiveDao.getHiveTableColumnByName(tableName);
		JSONObject object = new JSONObject();
		if (hiveTable == null) {
			object.put("code", "error");
		} else {
			object.put("code", "success");
			object.put("data", hiveTable.getTableColumnsZh());
		}
		return object.toJSONString();
	}

	@Override
	public int insertCustomTask(Task task) {
		return exportDao.insertCustomTask(task);
	}

	@Override
	public int findManualRunningTaskCount() {
		return exportDao.findManualRunningTaskCount();
	}

}
