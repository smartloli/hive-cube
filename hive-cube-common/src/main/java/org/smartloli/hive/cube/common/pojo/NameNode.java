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
 * NameNode pojo.
 * 
 * @author smartloli.
 *
 *         Created by Jul 7, 2017
 */
public class NameNode {
	private String version = "";
	private long capacity = 0L;
	private long dfsUsed = 0L;
	private long nonDFSUsed = 0L;
	private long dfsRemaining = 0L;
	private long liveNodes = 0L;
	private long deadNodes = 0L;
	private long decomNodes = 0L;
	private String clusterStartTime = "";

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public long getCapacity() {
		return capacity;
	}

	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}

	public long getDfsUsed() {
		return dfsUsed;
	}

	public void setDfsUsed(long dfsUsed) {
		this.dfsUsed = dfsUsed;
	}

	public long getNonDFSUsed() {
		return nonDFSUsed;
	}

	public void setNonDFSUsed(long nonDFSUsed) {
		this.nonDFSUsed = nonDFSUsed;
	}

	public long getDfsRemaining() {
		return dfsRemaining;
	}

	public void setDfsRemaining(long dfsRemaining) {
		this.dfsRemaining = dfsRemaining;
	}

	public long getLiveNodes() {
		return liveNodes;
	}

	public void setLiveNodes(long liveNodes) {
		this.liveNodes = liveNodes;
	}

	public long getDeadNodes() {
		return deadNodes;
	}

	public void setDeadNodes(long deadNodes) {
		this.deadNodes = deadNodes;
	}

	public long getDecomNodes() {
		return decomNodes;
	}

	public void setDecomNodes(long decomNodes) {
		this.decomNodes = decomNodes;
	}

	public String getClusterStartTime() {
		return clusterStartTime;
	}

	public void setClusterStartTime(String clusterStartTime) {
		this.clusterStartTime = clusterStartTime;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
