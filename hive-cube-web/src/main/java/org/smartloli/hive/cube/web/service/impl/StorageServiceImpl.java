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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.smartloli.hive.cube.common.pojo.HBaseSchema;
import org.smartloli.hive.cube.common.pojo.OdpsContent;
import org.smartloli.hive.cube.plugins.hbase.HBaseRecordReader;
import org.smartloli.hive.cube.plugins.hbase.HBaseScanSpec;
import org.smartloli.hive.cube.plugins.hbase.HConstants;
import org.smartloli.hive.cube.plugins.hbase.OdpsSqlParser;
import org.smartloli.hive.cube.plugins.mysql.MySqlRecordReader;
import org.smartloli.hive.cube.plugins.util.JConstants;
import org.smartloli.hive.cube.web.controller.StartupListener;
import org.smartloli.hive.cube.web.dao.StorageDao;
import org.smartloli.hive.cube.web.exception.DataException;
import org.smartloli.hive.cube.web.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Manager storage plugins to mysql.
 * 
 * @author smartloli.
 *
 *         Created by Jul 7, 2017
 */
@Service
public class StorageServiceImpl implements StorageService {

	@Autowired
	private StorageDao storageDao;

	/** Get page count. */
	public int count(Map<String, Object> param) {
		return storageDao.count(param);
	}

	/** Delete storage plugins from mysql by id. */
	public int delete(int id) {
		return storageDao.delete(id);
	}

	/** Checked id whether exist. */
	public boolean exist(int id) {
		return storageDao.exist(id) > 0 ? true : false;
	}

	/** Find storage by id. */
	public String findStorageById(int id) {
		return JSON.parseObject(storageDao.findStorageById(id).toString()).toJSONString();
	}

	/** Get storage plugins data from mysql. */
	public List<OdpsContent> get(Map<String, Object> param) {
		return storageDao.get(param);
	}

	/** Get diff storage dataset. */
	public String getSpecifyById(int id, String action, HttpServletRequest request) {
		OdpsContent odps = storageDao.findStorageById(id);
		if (odps.getType().equals(JConstants.MYSQL)) {
			if (action.equals(JConstants.DB)) {
				return MySqlRecordReader.database(odps);
			} else if (action.equals(JConstants.COLUMN)) {
				String db = request.getParameter("db");
				String sql = request.getParameter("sql");
				odps.setSql(sql);
				odps.setJdbc(odps.getHost() + ":" + odps.getPort() + "/" + db);
				return MySqlRecordReader.column(odps);
			} else if (action.equals(JConstants.DATASETS)) {
				String db = request.getParameter("db");
				String sql = request.getParameter("sql");
				odps.setSql(sql);
				odps.setJdbc(odps.getHost() + ":" + odps.getPort() + "/" + db);
				return MySqlRecordReader.sql(odps);
			}
		}
		return "";
	}

	/** Deal with hbase query. */
	public JSONObject getSpecifyHBase(String jobId) {
		if (HBaseRecordReader.result.containsKey(jobId)) {
			return JSON.parseObject(HBaseRecordReader.result.get(jobId).toString());
		}

		return DataException.errorForQueryHBase(DataException.NO_FINISHED);
	}

	/** Add or modify storage plugins information. */
	public int replace(OdpsContent odps) {
		return storageDao.replace(odps);
	}

	/** Submit hbase query task. */
	public boolean submitHBaseTask(String sql, String jobId) {
		try {
			HBaseScanSpec scanSpec = OdpsSqlParser.sqlParser(sql);
			RowkeyServiceImpl rowkeyService = StartupListener.getBean("rowkeyServiceImpl", RowkeyServiceImpl.class);
			JSONObject schema = rowkeyService.findHBaseSchemaByName(scanSpec.getTableName()).getJSONObject(HConstants.TABLE_SCHEMA);
			HBaseSchema hSchema = new HBaseSchema();
			hSchema.setSchema(schema);
			hSchema.setSql(sql);
			hSchema.setJobId(jobId);
			HBaseRecordReader hRead = new HBaseRecordReader();
			hRead.setHbaseSchema(hSchema);
			hRead.start();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
