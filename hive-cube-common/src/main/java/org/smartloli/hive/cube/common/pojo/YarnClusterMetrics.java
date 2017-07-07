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
 * YarnClusterMetrics pojo.
 * 
 * @author smartloli.
 *
 *         Created by Jul 7, 2017
 */
public class YarnClusterMetrics {
	private long appsSubmitted = 0L;
	private long appsRunning = 0L;
	private long appsCompleted = 0L;
	private long containersRunning = 0L;
	private long memoryUsed = 0L;
	private long memoryTotal = 0L;
	private long vCoresUsed = 0L;
	private long vCoresTotal = 0L;
	private long activeNodes = 0L;
	private long decommissionedNodes = 0L;
	private long lostNodes = 0L;
	private long unhealthyNodes = 0L;
	private long rebootedNodes = 0L;

	public long getAppsSubmitted() {
		return appsSubmitted;
	}

	public void setAppsSubmitted(long appsSubmitted) {
		this.appsSubmitted = appsSubmitted;
	}

	public long getAppsRunning() {
		return appsRunning;
	}

	public void setAppsRunning(long appsRunning) {
		this.appsRunning = appsRunning;
	}

	public long getAppsCompleted() {
		return appsCompleted;
	}

	public void setAppsCompleted(long appsCompleted) {
		this.appsCompleted = appsCompleted;
	}

	public long getContainersRunning() {
		return containersRunning;
	}

	public void setContainersRunning(long containersRunning) {
		this.containersRunning = containersRunning;
	}

	public long getMemoryUsed() {
		return memoryUsed;
	}

	public void setMemoryUsed(long memoryUsed) {
		this.memoryUsed = memoryUsed;
	}

	public long getMemoryTotal() {
		return memoryTotal;
	}

	public void setMemoryTotal(long memoryTotal) {
		this.memoryTotal = memoryTotal;
	}

	public long getvCoresUsed() {
		return vCoresUsed;
	}

	public void setvCoresUsed(long vCoresUsed) {
		this.vCoresUsed = vCoresUsed;
	}

	public long getvCoresTotal() {
		return vCoresTotal;
	}

	public void setvCoresTotal(long vCoresTotal) {
		this.vCoresTotal = vCoresTotal;
	}

	public long getActiveNodes() {
		return activeNodes;
	}

	public void setActiveNodes(long activeNodes) {
		this.activeNodes = activeNodes;
	}

	public long getDecommissionedNodes() {
		return decommissionedNodes;
	}

	public void setDecommissionedNodes(long decommissionedNodes) {
		this.decommissionedNodes = decommissionedNodes;
	}

	public long getLostNodes() {
		return lostNodes;
	}

	public void setLostNodes(long lostNodes) {
		this.lostNodes = lostNodes;
	}

	public long getUnhealthyNodes() {
		return unhealthyNodes;
	}

	public void setUnhealthyNodes(long unhealthyNodes) {
		this.unhealthyNodes = unhealthyNodes;
	}

	public long getRebootedNodes() {
		return rebootedNodes;
	}

	public void setRebootedNodes(long rebootedNodes) {
		this.rebootedNodes = rebootedNodes;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
