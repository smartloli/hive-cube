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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.smartloli.hive.cube.common.client.CommonClientConfigs;
import org.smartloli.hive.cube.common.pojo.Queue;

/**
 * The task queue manages the order in which the tasks are executed.
 * 
 * @author smartloli.
 *
 *         Created by Jun 5, 2017
 */
public class TaskQueue {

	private static List<Queue> queues = new CopyOnWriteArrayList<>();
	private static int counter = 0;

	public static int getCounter() {
		return counter;
	}

	public static void setCounter() {
		counter++;
	}

	/** Get all tasks from queues. */
	public static List<Queue> getQueues() {
		return queues;
	}

	/** Get private task rank in queue. */
	public static int getPrivateRank() {
		return (queues.size() + 1) - CommonClientConfigs.Task.MAX_SIZE_LIMIT;
	}

	/** Get task rank in queue. */
	public static int getRank(int index) {
		return (index + 1) - CommonClientConfigs.Task.MAX_SIZE_LIMIT;
	}

	/** Queue whether is free. */
	public static boolean hasFree() {
		int count = 0;
		for (Queue queue : queues) {
			if (queue.getRank() == 0) {
				count++;
			}
		}
		if (count < CommonClientConfigs.Task.MAX_SIZE_LIMIT) {
			return true;
		} else {
			return false;
		}
	}

	/** Queue whether is full. */
	public static boolean isFull() {
		int count = 0;
		for (Queue queue : queues) {
			if (queue.getRank() == 0) {
				count++;
			}
		}
		if (count >= CommonClientConfigs.Task.MAX_SIZE_LIMIT) {
			return true;
		} else {
			return false;
		}
	}

	/** Remove task from queue. */
	public static boolean remove(Queue queue) {
		for (int i = 0; i < queues.size(); i++) {
			Queue q = queues.get(i);
			if (q.getTask().getId() == queue.getTask().getId()) {
				return queues.remove(q);
			}
		}
		return false;
	}

	/** Update task status from queues. */
	public static void updateQueues() {
		for (Queue queue : queues) {
			if (queue.getRank() > 0) {
				if ((queue.getRank() - 1) == 0) {
					queue.setRank(0);
					queue.setStatus(CommonClientConfigs.TaskStatus.EXECUTING);
					ThreadExecutor.addTask(queue.getVip(), queue);
				} else {
					queue.setRank(queue.getRank() - 1);
					queue.setStatus(CommonClientConfigs.TaskStatus.QUEUE + "(" + queue.getRank() + ")");
				}
			}
		}
	}

}
