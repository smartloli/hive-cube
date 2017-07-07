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
 * DataNode pojo.
 * 
 * @author smartloli.
 *
 *         Created by Jul 7, 2017
 */
public class DataNode {
	private String hostname = "";
	private String state = "";
	private String update_date = "";
	private String capacity = "";
	private String used = "";
	private String non_dfs_used = "";
	private String remaining = "";
	private String block_pool_used = "";

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public String getNon_dfs_used() {
		return non_dfs_used;
	}

	public void setNon_dfs_used(String non_dfs_used) {
		this.non_dfs_used = non_dfs_used;
	}

	public String getRemaining() {
		return remaining;
	}

	public void setRemaining(String remaining) {
		this.remaining = remaining;
	}

	public String getBlock_pool_used() {
		return block_pool_used;
	}

	public void setBlock_pool_used(String block_pool_used) {
		this.block_pool_used = block_pool_used;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
