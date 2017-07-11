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
package org.smartloli.hive.cube.plugins.hbase;

/**
 * HConstants holds a bunch of HBase-related constants
 * 
 * @author smartloli.
 *
 *         Created by Jun 29, 2017
 */
public class HConstants {

	/** HBase query rowkey schema. */
	public static final String ROW_KEY = "rowkey";

	/** HBase rowkey schema type. */
	public static final String ROW_KEY_TYPE = "varchar";

	/** HBase rowkey regular. */
	public static final String ROW_KEY_REGULAR = "regular";

	/** HBase table schema. */
	public static final String TABLE_SCHEMA = "schema";

	/** Name of ZooKeeper quorum configuration parameter. */
	public static final String ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";

	/** HBase cluster master parameter. */
	public static final String MASTER = "master";

	/** An empty instance. */
	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	/**
	 * Used by scanners, etc when they want to start at the beginning of a
	 * region.
	 */
	public static final byte[] EMPTY_START_ROW = EMPTY_BYTE_ARRAY;

	/** Last row in a table. */
	public static final byte[] EMPTY_END_ROW = EMPTY_START_ROW;

	/** SQL equal. */
	public static final String EQUAL = "=";

	/** SQL fields regex. */
	public static final String FIELD_REGEX = "(select)(.+)(from)";

	/** SQL column include star */
	public static final String STAR = "*";

	/** SQL column comma. */
	public static final String COMMA = ",";

	/** SQL as key. */
	public static final String AS = "as";

	/** SQL column space. */
	public static final String SPACE = "\"";

	/** SQL column quotes. */
	public static final String QUOTES = " ";

	/** SQL aggregate function. */
	public static final String COUNT = "count";
	public static final String SUM = "sum";
	public static final String AVG = "avg";
	public static final String MAX = "max";
	public static final String MIN = "min";
	/** Aggregate default field name. */
	public static final String AGG_DEFAULT_FIELD = "EXPR$0";

}
