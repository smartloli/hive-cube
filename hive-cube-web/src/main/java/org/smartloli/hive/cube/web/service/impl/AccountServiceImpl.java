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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.smartloli.hive.cube.common.client.CommonClientConfigs;
import org.smartloli.hive.cube.common.pojo.Signiner;
import org.smartloli.hive.cube.web.dao.UserDao;
import org.smartloli.hive.cube.web.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * Account service implements.
 * 
 * @author smartloli.
 *
 *         Created by May 17, 2017
 */
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private UserDao userDao;

	@Override
	public int delete(Signiner signin) {
		return userDao.delete(signin);
	}

	@Override
	public String findUserById(int id) {
		Signiner signer = userDao.findUserById(id);
		JSONObject object = new JSONObject();
		object.put("rtxno", signer.getRtxno());
		object.put("username", signer.getUsername());
		object.put("realname", signer.getRealname());
		object.put("email", signer.getEmail());
		return object.toJSONString();
	}

	@Override
	public Signiner findUserByRtxNo(int rtxno) {
		return userDao.findUserByRtxNo(rtxno).get(0);
	}

	@Override
	public List<Signiner> findUserBySearch(Map<String, Object> params) {
		return userDao.findUserBySearch(params);
	}

	@Override
	public int insertUser(Signiner signin) {
		return userDao.insertUser(signin);
	}

	@Override
	public Signiner login(String username, String password) {
		Signiner signin = new Signiner();
		signin.setUsername(username);
		signin.setPassword(password);

		if (userDao.login(signin) == null) {
			signin.setUsername(CommonClientConfigs.Login.UNKNOW_USER);
			signin.setPassword("");
			return signin;
		}

		return userDao.login(signin);
	}

	@Override
	public int userCounts() {
		return userDao.userCounts();
	}

	@Override
	public int reset(Signiner signin) {
		return userDao.reset(signin);
	}

	@Override
	public int modify(Signiner signin) {
		return userDao.modify(signin);
	}

	@Override
	public List<String> getUserEmails() {
		List<Signiner> signers = userDao.getUserEmails();
		List<String> email = new ArrayList<>();
		for (Signiner signiner : signers) {
			email.add(signiner.getEmail());
		}
		return email;
	}

}
