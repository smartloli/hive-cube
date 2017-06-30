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
package org.smartloli.hive.cube.api.email;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.smartloli.hive.cube.common.util.SystemConfig;

/**
 * The mail utility class used to send notifications.
 * 
 * @author smartloli.
 *
 *         Created by Oct 11, 2016
 */
public class MailUtils extends Thread {

	private String subject = "";
	private String address = "";
	private String content = "";
	private String attachment = null;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public void run() {
		send(subject, address, content, attachment);
	}

	/**
	 * @param subject
	 *            mail theme
	 * @param content
	 *            mail content
	 * @param address
	 *            mail address, Support multiple people send, such as
	 *            "name1@email.com;name2@email.com"
	 * @param attachments
	 *            attachments, Support multiple attachments send,such as
	 *            "fileName1;fileName2"
	 */
	private boolean send(String subject, String address, String content, String attachment) {
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(SystemConfig.getProperty("hive.cube.mail.server.host"));
		mailInfo.setMailServerPort(SystemConfig.getProperty("hive.cube.mail.server.port"));
		mailInfo.setValidate(true);
		mailInfo.setUserName(SystemConfig.getProperty("hive.cube.mail.sa"));
		mailInfo.setPassword(SystemConfig.getProperty("hive.cube.mail.password"));
		mailInfo.setFromAddress(SystemConfig.getProperty("hive.cube.mail.username"));
		mailInfo.setToAddress(address);
		mailInfo.setSubject(subject);
		mailInfo.setContent(content);

		List<File> fileList = null;
		if (StringUtils.isNotBlank(attachment)) {
			fileList = new ArrayList<File>();
			String[] attachments = attachment.split(";");
			File file = null;
			for (String fileName : attachments) {
				file = new File(fileName);
				fileList.add(file);
			}
			mailInfo.setFileList(fileList);
		}

		SimpleMailSender sms = new SimpleMailSender();
		return sms.sendHtmlMail(mailInfo);// Send html format
	}

}
