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

	/** Login constant property. */
	public interface Login {
		public static final String SESSION_USER = "LOGIN_USER_SESSION";
		public static final String UNKNOW_USER = "__unknow__";
		public static final String ERROR_LOGIN = "error_msg";
		public static final String REQST_URI = "/account/signin/action";
		public static final String REDIRECT = "/hc/account/signin?";
	}

	/** Task constant property. */
	public interface Task {
		public static final int MAX_SIZE_LIMIT = 5;
		public static final int TASK_COMMON = 1;
		public static final int WAIT = 1000 * 3;
		public static final int LISTEN = 1000 * 2;
		public static final int TASK_VIP = 5;
		public static final String NEW_TASK_NUMBER = "New Tasks";
		public static final String PERIOD = "period";
	}

	/** Task status constant property. */
	public interface TaskStatus {
		public static final String NOT_EXECUTED = "Unexecuted";
		public static final String EXECUTING = "Running";
		public static final String QUEUE = "Queue";
		public static final String EXECUTION_ERROR = "Error";
		public static final String EXECUTION_END = "Finished";
		public static final String EXECUTION_STOP = "Stop";
	}

	/** Task type constant property. */
	public interface TaskType {
		public static final int MANUAL = 0;
		public static final int AUTO = 1;
	}

	/** Scheduler constant property. */
	public interface Scheduler {
		public static final String NAME = "schedule";
		public static final String ADD = "add";
		public static final String MODIFY = "modify";
		public static final String STOP = "stop";
		public static final String START = "start";
		public static final String DELETE = "delete";
		public static final String CANCLE = "cancle";
	}

	/** Sql constant property. */
	public interface Sql {
		public final static String SELECT = "select";
		public final static String LOG = "The url to track the job";
	}

	/** Role is equal admin. */
	public interface Role {
		public final static String ADMIN = "admin";
	}

}
