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
 * HdfsDir pojo.
 * 
 * @author smartloli.
 *
 *         Created by Jul 6, 2017
 */
public class HdfsDir {
	private String blockSize;
	private int childrenNum;
	private long fileId;
	private String group;
	private String length;
	private String modify;
	private String own;
	private String name;
	private String permission;
	private int replication;
	private String type;
	private String path;

	public String getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(String blockSize) {
		this.blockSize = blockSize;
	}

	public int getChildrenNum() {
		return childrenNum;
	}

	public void setChildrenNum(int childrenNum) {
		this.childrenNum = childrenNum;
	}

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getModify() {
		return modify;
	}

	public void setModify(String modify) {
		this.modify = modify;
	}

	public String getOwn() {
		return own;
	}

	public void setOwn(String own) {
		this.own = own;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public int getReplication() {
		return replication;
	}

	public void setReplication(int replication) {
		this.replication = replication;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
