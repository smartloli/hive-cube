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

import java.util.ArrayList;
import java.util.List;

import org.smartloli.hive.cube.common.pojo.Queue;
import org.smartloli.hive.cube.common.pojo.Task;
import org.smartloli.hive.cube.common.util.CalendarUtils;

/**
 * The task modifies the factory class and modifies the state of the submission
 * to the task queue.
 * 
 * @author smartloli.
 *
 *         Created by Jun 6, 2017
 */
public class TaskFactory {

	/** Modify commit task. */
	public static List<Task> modify() {
		List<Queue> queues = TaskQueue.getQueues();
		List<Task> tasks = new ArrayList<>();
		for (Queue queue : queues) {
			if (queue.getRank() == 0) {
				queue.getTask().setStartTime(CalendarUtils.getDate());
			}
			queue.getTask().setStatus(queue.getStatus());
			tasks.add(queue.getTask());
		}
		return tasks;
	}

}
