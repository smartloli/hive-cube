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
package org.smartloli.hive.cube.common.client;

/**
 * Some configurations shared by hive cube.
 * 
 * @author smartloli.
 *
 *         Created by Jun 28, 2017
 */
public class CommonClientConfigs {

	/** Export task path. */
	public static final String ASSERT_HC_EXPORT_CONFIG = "/assert/hc/export";

	public interface Login {
		public static final String SESSION_USER = "LOGIN_USER_SESSION";
		public static final String UNKNOW_USER = "__unknow__";
		public static final String ERROR_LOGIN = "error_msg";
	}

	public interface Task {
		public static final int MAX_SIZE_LIMIT = 5;
		public static final int TASK_COMMON = 1;
		public static final int WAIT = 1000 * 3;
		public static final int TASK_VIP = 5;
		public static final String NEW_TASK_NUMBER = "dashboard.task.numbers";
		public static final String PERIOD = "period";
	}

	public interface TaskStatus {
		public static final String NOT_EXECUTED = "task.unexecuted";
		public static final String EXECUTING = "task.running";
		public static final String QUEUE = "task.queue";
		public static final String EXECUTION_ERROR = "task.error";
		public static final String EXECUTION_END = "task.finished";
		public static final String EXECUTION_STOP = "task.stop";
	}

	public interface TaskType {
		public static final int MANUAL = 0;
		public static final int AUTO = 1;
	}

	public interface Scheduler {
		public static final String NAME = "schedule";
		public static final String ADD = "add";
		public static final String MODIFY = "modify";
		public static final String STOP = "stop";
		public static final String START = "start";
		public static final String DELETE = "delete";
		public static final String CANCLE = "cancle";
	}

	public interface Sql {
		public final static String SELECT = "select";
	}

}
