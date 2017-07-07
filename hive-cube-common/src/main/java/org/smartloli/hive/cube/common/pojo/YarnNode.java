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
package org.smartloli.hive.cube.common.pojo;

import com.google.gson.Gson;

/**
 * YarnNode pojo.
 * 
 * @author smartloli.
 *
 *         Created by Jul 7, 2017
 */
public class YarnNode {
	private String nodeState = "";
	private String nodeAddress = "";
	private String nodeHttpAddress = "";
	private String lastHealthUpdate = "";
	private String healthReport = "";
	private long containers = 0L;
	private long memoryUsed = 0L;
	private long memoryAvail = 0L;
	private long vCoresUsed = 0L;
	private long vCoresAvail = 0L;

	public String getNodeState() {
		return nodeState;
	}

	public void setNodeState(String nodeState) {
		this.nodeState = nodeState;
	}

	public String getNodeAddress() {
		return nodeAddress;
	}

	public void setNodeAddress(String nodeAddress) {
		this.nodeAddress = nodeAddress;
	}

	public String getNodeHttpAddress() {
		return nodeHttpAddress;
	}

	public void setNodeHttpAddress(String nodeHttpAddress) {
		this.nodeHttpAddress = nodeHttpAddress;
	}

	public String getLastHealthUpdate() {
		return lastHealthUpdate;
	}

	public void setLastHealthUpdate(String lastHealthUpdate) {
		this.lastHealthUpdate = lastHealthUpdate;
	}

	public String getHealthReport() {
		return healthReport;
	}

	public void setHealthReport(String healthReport) {
		this.healthReport = healthReport;
	}

	public long getContainers() {
		return containers;
	}

	public void setContainers(long containers) {
		this.containers = containers;
	}

	public long getMemoryUsed() {
		return memoryUsed;
	}

	public void setMemoryUsed(long memoryUsed) {
		this.memoryUsed = memoryUsed;
	}

	public long getMemoryAvail() {
		return memoryAvail;
	}

	public void setMemoryAvail(long memoryAvail) {
		this.memoryAvail = memoryAvail;
	}

	public long getvCoresUsed() {
		return vCoresUsed;
	}

	public void setvCoresUsed(long vCoresUsed) {
		this.vCoresUsed = vCoresUsed;
	}

	public long getvCoresAvail() {
		return vCoresAvail;
	}

	public void setvCoresAvail(long vCoresAvail) {
		this.vCoresAvail = vCoresAvail;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
