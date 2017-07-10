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

import java.util.concurrent.locks.ReentrantLock;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.smartloli.hive.cube.common.util.SystemConfig;

/**
 * The HBase storage plugin provides access methods and closing methods for
 * accessing HBase clusters.
 * 
 * @author smartloli.
 *
 *         Created by Jun 29, 2017
 */
public class HBaseStoragePlugin {

	private static final HBaseConnectionManager hbaseConnectionManager = HBaseConnectionManager.INSTANCE;

	private final HBaseConnectionKey connectionKey;

	public HBaseStoragePlugin() {
		this.connectionKey = new HBaseConnectionKey();
	}

	public void close() throws Exception {
		hbaseConnectionManager.closeConnection(connectionKey);
	}

	public Connection getConnection() {
		return hbaseConnectionManager.getConnection(connectionKey);
	}

	/**
	 * An internal class which serves the key in a map of
	 * {@link HBaseStoragePlugin} => {@link Connection}.
	 */
	class HBaseConnectionKey {

		private final ReentrantLock lock = new ReentrantLock();

		private HBaseConnectionKey() {
		}

		public void lock() {
			lock.lock();
		}

		public void unlock() {
			lock.unlock();
		}

		public Configuration getHBaseConf() {
			Configuration conf = HBaseConfiguration.create();
			conf.set(HConstants.ZOOKEEPER_QUORUM, SystemConfig.getProperty("hive.cube.hbase.zk.quorum"));
			conf.set(HConstants.MASTER, SystemConfig.getProperty("hive.cube.hbase.master"));
			return conf;
		}

	}
}
