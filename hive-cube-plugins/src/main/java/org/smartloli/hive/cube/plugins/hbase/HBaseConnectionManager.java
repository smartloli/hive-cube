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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.plugins.hbase.HBaseStoragePlugin.HBaseConnectionKey;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.util.concurrent.UncheckedExecutionException;

/**
 * A singleton class which manages the lifecycle of HBase connections. One
 * connection per storage plugin instance is maintained.
 * 
 * @author smartloli.
 *
 *         Created by Jun 29, 2017
 */
public class HBaseConnectionManager extends CacheLoader<HBaseConnectionKey, Connection> implements RemovalListener<HBaseConnectionKey, Connection> {

	private static final Logger LOG = LoggerFactory.getLogger(HBaseConnectionManager.class);

	public static final HBaseConnectionManager INSTANCE = new HBaseConnectionManager();

	private final LoadingCache<HBaseConnectionKey, Connection> connectionCache;

	/** Connections will be closed after 1 hour of inactivity. */
	private HBaseConnectionManager() {
		this.connectionCache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).removalListener(this).build(this);
	}

	private boolean isValid(Connection conn) {
		return conn != null && !conn.isAborted() && !conn.isClosed();
	}

	@Override
	public void onRemoval(RemovalNotification<HBaseConnectionKey, Connection> notification) {
		try {
			Connection conn = notification.getValue();
			if (isValid(conn)) {
				conn.close();
			}
			LOG.info("HBase connection '{}' closed.", conn);
		} catch (Throwable t) {
			LOG.error("Error while closing HBase connection.", t);
		}

	}

	@Override
	public Connection load(HBaseConnectionKey key) throws Exception {
		Connection connection = ConnectionFactory.createConnection(key.getHBaseConf());
		LOG.info("HBase connection '{}' created.", connection);
		return connection;
	}

	public Connection getConnection(HBaseConnectionKey key) {
		Preconditions.checkNotNull(key);
		try {
			Connection conn = connectionCache.get(key);
			if (!isValid(conn)) {
				key.lock();
				try {
					conn = connectionCache.get(key);
					if (!isValid(conn)) {
						connectionCache.invalidate(key);
						conn = connectionCache.get(key);
					}
				} finally {
					key.unlock();
				}
			}
			return conn;
		} catch (ExecutionException | UncheckedExecutionException e) {
			LOG.error("Data read error:" + e.getMessage());
			return null;
		}
	}

	public void closeConnection(HBaseConnectionKey key) {
		Preconditions.checkNotNull(key);
		connectionCache.invalidate(key);
	}

}
