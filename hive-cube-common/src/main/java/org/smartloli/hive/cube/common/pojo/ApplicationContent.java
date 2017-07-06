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
/**
 * 
 */
package org.smartloli.hive.cube.common.pojo;

import com.google.gson.Gson;

/**
 * ApplicationContent pojo.
 * 
 * @author smartloli.
 *
 *         Created by Jul 6, 2017
 */
public class ApplicationContent {
	private String user;
	private String name;
	private String type;
	private String appId;
	private String appState;
	private String finalAppStatus;
	private String progress;
	private String startTime;
	private String finishedTime;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppState() {
		return appState;
	}

	public void setAppState(String appState) {
		this.appState = appState;
	}

	public String getFinalAppStatus() {
		return finalAppStatus;
	}

	public void setFinalAppStatus(String finalAppStatus) {
		this.finalAppStatus = finalAppStatus;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(String finishedTime) {
		this.finishedTime = finishedTime;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
