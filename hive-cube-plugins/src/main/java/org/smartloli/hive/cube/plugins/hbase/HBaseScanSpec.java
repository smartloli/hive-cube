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

import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Do special processing when scanning the HBase.
 * 
 * @author smartloli.
 *
 *         Created by Jun 29, 2017
 */
public class HBaseScanSpec {
	protected String tableName;
	protected byte[] startRow;
	protected byte[] stopRow;

	protected Filter filter;

	protected String filterString;

	@JsonCreator
	public HBaseScanSpec(@JsonProperty("tableName") String tableName, @JsonProperty("startRow") byte[] startRow, @JsonProperty("stopRow") byte[] stopRow, @JsonProperty("filterString") String filterString) {
		this.tableName = tableName;
		this.startRow = startRow;
		this.stopRow = stopRow;
		this.filterString = filterString;
		this.filter = this.getScanFilter(filterString);
	}

	public HBaseScanSpec(String tableName, byte[] startRow, byte[] stopRow, Filter filter) {
		this.tableName = tableName;
		this.startRow = startRow;
		this.stopRow = stopRow;
		this.filter = filter;
	}

	public HBaseScanSpec(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public byte[] getStartRow() {
		return startRow == null ? HConstants.EMPTY_START_ROW : startRow;
	}

	public byte[] getStopRow() {
		return stopRow == null ? HConstants.EMPTY_START_ROW : stopRow;
	}

	@JsonIgnore
	public Filter getFilter() {
		return this.filter;
	}

	public byte[] getSerializedFilter() {
		return (this.filter != null) ? HBaseUtils.serializeFilter(this.filter) : null;
	}

	/**
	 * For : Bytes.toBytes("d"), Bytes.toBytes("_plat"), CompareOp.EQUAL,
	 * Bytes.toBytes("604")
	 */
	private Filter getScanFilter(String filterString) {
		if (filterString == "") {
			return filter;
		}
		FilterList filters = new FilterList();
		for (Object object : JSON.parseArray(filterString)) {
			JSONObject param = (JSONObject) object;
			filters.addFilter(new SingleColumnValueFilter(Bytes.toBytes(param.getString("family")), Bytes.toBytes(param.getString("qualifier")), CompareOp.EQUAL, Bytes.toBytes(param.getString("value"))));
		}
		return filters;
	}

	@Override
	public String toString() {
		return "HBaseScanSpec [tableName=" + tableName + ", startRow=" + (startRow == null ? null : Bytes.toStringBinary(startRow)) + ", stopRow=" + (stopRow == null ? null : Bytes.toStringBinary(stopRow)) + ", filter="
				+ (filter == null ? null : filter.toString()) + "]";
	}
}
