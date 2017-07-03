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
package org.smartloli.hive.cube.web.task;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.smartloli.hive.cube.common.pojo.Queue;
import org.smartloli.hive.cube.common.util.SystemConfig;

/**
 * The queue thread executes management and allocates thread parameters that
 * execute the task queue.
 * 
 * @author smartloli.
 *
 *         Created by Jun 5, 2017
 */
public class ThreadExecutor {

	private static ThreadPoolExecutor executor = null;

	static {
		int pool = SystemConfig.getIntProperty("hive.cube.task.thread.pool");
		int maxPool = SystemConfig.getIntProperty("hive.cube.task.thread.max.pool");
		executor = new ThreadPoolExecutor(pool, maxPool, 1, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>());
	}

	/** Add task to queue. */
	public static void addTask(int vip, Queue queue) {
		executor.execute(new RunnablePriority(vip, queue));
	}

}
