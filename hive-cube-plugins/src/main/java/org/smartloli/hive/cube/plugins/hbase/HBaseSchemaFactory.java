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
package org.smartloli.hive.cube.plugins.hbase;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Table;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Mapping the HBase table structure to construct a structured data model for
 * SQL queries.
 * 
 * @author smartloli.
 *
 *         Created by Jun 29, 2017
 */
public class HBaseSchemaFactory {

//	public static JSONObject build(String table) {
//		RowkeyServiceImpl rowkeyService = StartupListener.getBean("rowkeyServiceImpl", RowkeyServiceImpl.class);
//		JSONObject schema = JSON.parseObject(rowkeyService.findRowkeyByName(table)).getJSONObject(HConstants.ROW_KEY_REGULAR).getJSONObject(HConstants.TABLE_SCHEMA);
//		schema.put(HConstants.ROW_KEY, HConstants.ROW_KEY_TYPE);
//		return schema;
//	}

	public static JSONObject getTableDescribe(String tableName) {
		HBaseStoragePlugin storagePlugin = new HBaseStoragePlugin();
		try {
			Table table = storagePlugin.getConnection().getTable(TableName.valueOf(tableName));
			String value = table.getTableDescriptor().getFamilies().toString();
			String qualifier = value.replace("[", "").replace("]", "").replaceAll("=>", ":");
			return JSON.parseObject(qualifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
