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
package org.smartloli.hive.cube.web.util;

import java.io.File;
import java.util.List;

import org.smartloli.hive.cube.api.email.MailUtils;
import org.smartloli.hive.cube.common.client.CommonClientConfigs;
import org.smartloli.hive.cube.common.pojo.Signiner;
import org.smartloli.hive.cube.common.pojo.Task;
import org.smartloli.hive.cube.common.util.SystemConfig;

/**
 * Export data files, notify relevant personnel, mail writing format templates.
 * 
 * @author smartloli.
 *
 *         Created by Jun 9, 2017
 */
public class Reporter {

	/** Send export notice. */
	public static void sender(Task task) {
		if (task.getStatus().equals(CommonClientConfigs.TaskStatus.EXECUTION_END)) {
			MailUtils mail = new MailUtils();
			mail.setAddress(task.getEmail());
			mail.setSubject("Export Hint:" + task.getName());
			String domain = SystemConfig.getProperty("mf.domain.name");
			String download = "";
			if (domain.endsWith(File.separator)) {
				download = domain.substring(0, domain.length() - 1) + CommonClientConfigs.ASSERT_HC_EXPORT_CONFIG + File.separator + task.getDownload();
			} else {
				download = domain + CommonClientConfigs.ASSERT_HC_EXPORT_CONFIG + File.separator + task.getDownload();
			}
			String reback = SystemConfig.getProperty("mf.reback.user");
			mail.setContent(
					"Hive Cube Notice: The task [" + task.getName() + "] has been exported , task id is [" + task.getId() + "]<br>Download :" + download + "<br>QA : " + reback);
			mail.start();
		}
	}

	/** Create user info. */
	public static void account(Signiner signin) {
		MailUtils mail = new MailUtils();
		mail.setAddress(signin.getEmail());
		mail.setSubject("*** Password ***");
		mail.setContent("Hive Cube Notice : <br> You can user account (" + signin.getUsername() + ") or rtxno (" + signin.getRtxno() + ") login, you password is : ["
				+ signin.getPassword() + "], you can reset password in system config.");
		mail.start();
	}

	/** Send system notice. */
	public static void notice(String content, List<String> emails) {
		String reback = SystemConfig.getProperty("mf.reback.user");
		String senders = "";
		for (String email : emails) {
			senders += email + ";";
		}
		MailUtils mail = new MailUtils();
		mail.setAddress(senders.substring(0, senders.length() - 1));
		mail.setSubject("*** System Notice ***");
		mail.setContent("Hive Cube Notice : <br> " + content + " <br> QA : " + reback);
		mail.start();
	}

}
