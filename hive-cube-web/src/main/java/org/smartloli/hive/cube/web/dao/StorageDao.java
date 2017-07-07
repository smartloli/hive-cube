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
package org.smartloli.hive.cube.web.dao;

import java.util.List;
import java.util.Map;

import org.smartloli.hive.cube.common.pojo.OdpsContent;

/**
 * StorageDao interface.
 * 
 * @author smartloli.
 *
 *         Created by Jul 7, 2017
 */
public interface StorageDao {

	/** Get page count. */
	public int count(Map<String, Object> param);

	/** Delete storage plugins by id. */
	public int delete(int id);
	
	/** Checked id whether exist. */
	public int exist(int id);

	/** Find storage by id. */
	public OdpsContent findStorageById(int id);

	/** Get storage plugins data from mysql table. */
	public List<OdpsContent> get(Map<String, Object> param);

	/** Add or modify storage plugins information. */
	public int replace(OdpsContent odps);

}
