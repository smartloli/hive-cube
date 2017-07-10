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
 * Run exception information when processing client requests for HBase
 * operations.
 * 
 * @author smartloli.
 *
 *         Created by Jun 29, 2017
 */
public class HBaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -5026294052354348878L;

	public HBaseRuntimeException() {
		super();
	}

	public HBaseRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public HBaseRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public HBaseRuntimeException(String message) {
		super(message);
	}

	public HBaseRuntimeException(Throwable cause) {
		super(cause);
	}

	public static void format(String format, Object... args) {
		format(null, format, args);
	}

	public static void format(Throwable cause, String format, Object... args) {
		throw new HBaseRuntimeException(String.format(format, args), cause);
	}
}
