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
package org.smartloli.hive.cube.plugins.server;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.util.SystemConfig;
import org.smartloli.hive.cube.plugins.util.DomUtils;

/**
 * Listen tomcat server port
 *
 * @author smartloli.
 *
 *         Created by Aug 11, 2016
 */
public class TomcatServerListen {
	private static Logger LOG = LoggerFactory.getLogger(TomcatServerListen.class);

	public static void main(String[] args) {
		try {
			String target = System.getProperty("user.dir") + "/kms/conf/server.xml";
			String port = SystemConfig.getProperty("hive.cube.webui.port");
			String docBase = SystemConfig.getProperty("hive.cube.task.export.path");
			if (!docBase.endsWith(File.separator)) {
				docBase += File.separator;
			}
			DomUtils.setPort(target, port, docBase);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("Listen Tomcat Server Port Has Error,Msg is " + ex.getMessage());
		}
	}
}
