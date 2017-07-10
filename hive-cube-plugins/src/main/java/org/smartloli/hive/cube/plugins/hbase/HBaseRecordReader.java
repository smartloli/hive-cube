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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.pojo.HBaseSchema;
import org.smartloli.util.JSqlUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Used to execute statements after parsing SQL, query the data records in the
 * HBase table according to the table name and query conditions.
 * 
 * @author smartloli.
 *
 *         Created by Jun 29, 2017
 */
public class HBaseRecordReader extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(HBaseRecordReader.class);

	public static Map<String, Object> result = new ConcurrentHashMap<>();
	private HBaseSchema hbaseSchema;

	public HBaseSchema getHbaseSchema() {
		return hbaseSchema;
	}

	public void setHbaseSchema(HBaseSchema hbaseSchema) {
		this.hbaseSchema = hbaseSchema;
	}

	// batch size should not exceed max allowed record count
	private final int TARGET_RECORD_COUNT = 4000;

	private JSONArray getBatch(HBaseScanSpec scanSpec) throws Exception {
		HBaseStoragePlugin storagePlugin = new HBaseStoragePlugin();
		Table table = storagePlugin.getConnection().getTable(TableName.valueOf(scanSpec.tableName));
		Scan hbaseScan = new Scan(scanSpec.getStartRow(), scanSpec.getStopRow());
		hbaseScan.setFilter(scanSpec.getFilter()).setCaching(TARGET_RECORD_COUNT);
		ResultScanner scanner = table.getScanner(hbaseScan);
		JSONArray datas = new JSONArray();
		for (Result result : scanner) {
			JSONObject column = new JSONObject();
			for (Cell cell : result.rawCells()) {
				String rowkey = new String(CellUtil.cloneRow(cell));
				column.put(HConstants.ROW_KEY, rowkey);
				String qualifies = new String(CellUtil.cloneQualifier(cell));
				String value = HBaseUtils.convertFrom(CellUtil.cloneValue(cell)).toString();
				column.put(qualifies, value);
			}
			datas.add(column);
		}
		closeScanner(scanner);
		return datas;
	}

	private void closeScanner(ResultScanner scanner) {
		if (scanner != null)
			scanner.close();
	}

	public JSONObject sql(HBaseSchema schema) {
		try {
			HBaseScanSpec scanSpec = OdpsSqlParser.sqlParser(schema.getSql());
			JSONArray dataSets = getBatch(scanSpec);
			JSONArray hbaseScanResult = JSON.parseArray(JSqlUtils.query(schema.getSchema(), scanSpec.getTableName(), dataSets, schema.getSql()));
			JSONObject result = new JSONObject();
			result.put("column", JSON.parseObject(hbaseScanResult.get(0).toString()).keySet());
			result.put("result", hbaseScanResult);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Query hbase has error,msg is " + e.getMessage());
		}
		return null;
	}

	public void run() {
		result.put(this.hbaseSchema.getJobId(), sql(this.hbaseSchema));
	}

}
