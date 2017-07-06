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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.util.YarnManager;
import org.smartloli.hive.cube.web.service.ApplicationsService;
import org.springframework.stereotype.Service;

/**
 * Implements application method content.
 * 
 * @author smartloli.
 *
 *         Created by Jul 6, 2017
 */
@Service
public class ApplicationsServiceImpl implements ApplicationsService {

	private Logger LOG = LoggerFactory.getLogger(ApplicationsServiceImpl.class);

	/** Get applications data by type. */
	public String getApplications(String type) {
		try {
			return YarnManager.getApplications(type).toString();
		} catch (Exception e) {
			LOG.error("Get application data from yarn by type[" + type + "] has error,msg is " + e.getMessage());
			return "";
		}
	}

	/** Kill application by id. */
	public boolean killApplication(String appId) {
		try {
			return YarnManager.killApplication(appId);
		} catch (Exception e) {
			LOG.error("Kill application from yarn by appid[" + appId + "],msg is " + e.getMessage());
			return false;
		}
	}

	/** Checked application whether exist. */
	public boolean exist(String appId) {
		try {
			return YarnManager.exist(appId);
		} catch (Exception e) {
			LOG.error("Checked appid[" + appId + "] whether exist has error,msg is " + e.getMessage());
			return false;
		}
	}

}
