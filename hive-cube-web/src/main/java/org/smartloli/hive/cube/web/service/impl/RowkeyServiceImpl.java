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

import org.smartloli.hive.cube.common.pojo.Rowkey;
import org.smartloli.hive.cube.web.dao.RowkeyDao;
import org.smartloli.hive.cube.web.service.RowkeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * Rowkey service interface.
 * 
 * @author smartloli.
 *
 *         Created by Jun 15, 2017
 */
@Service
public class RowkeyServiceImpl implements RowkeyService {

	@Autowired
	private RowkeyDao rowKeyDao;

	@Override
	public int replace(Rowkey rowkey) {
		return rowKeyDao.replace(rowkey);
	}

	@Override
	public int deleteRowkeyByName(String tname) {
		return rowKeyDao.deleteRowkeyByName(tname);
	}

	@Override
	public List<Rowkey> getRowkeys(Map<String, Object> params) {
		return rowKeyDao.getRowkeys(params);
	}

	@Override
	public int count() {
		return rowKeyDao.count();
	}

	@Override
	public String findRowkeyByName(String tname) {
		JSONObject object = new JSONObject();
		object.put("regular", rowKeyDao.findRowkeyByName(tname).getRegular());
		object.put("tname", rowKeyDao.findRowkeyByName(tname).getTname());
		return object.toJSONString();
	}

}
