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
package org.smartloli.hive.cube.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HDFS file operation class.
 *
 * @author smartloli.
 *
 *         Created by Oct 9, 2016
 */
public class HadoopUtils {

	private final static Logger LOG = LoggerFactory.getLogger(HadoopUtils.class);

	private static FileSystem fs = null;

	static {
		if (fs == null) {
			fs = FileSystemSingleton.create();
		}
	}

	/** Append text to hdfs file. */
	public static void append(String path, String line) {
		OutputStream out = null;
		PrintWriter print = null;
		try {
			Path p = new Path(path);
			if (!fs.exists(p)) {
				if (!fs.createNewFile(p))
					throw new Exception("create hdfs file=" + p + " failure!");
			}
			out = fs.append(p);
			print = new PrintWriter(new OutputStreamWriter(out, "UTF-8"), true);
			print.println(line);
		} catch (Exception e) {
			LOG.error("Append hdfs has error,msg is " + e.getMessage());
		} finally {
			if (print != null)
				print.close();
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					LOG.error("Close hdfs object[OutputStream] has error,msg is " + e.getMessage());
				}
		}
	}

	/** Delete file or menu from hdfs. */
	public static void delete(String path) throws IOException {
		fs.delete(new Path(path), true);
	}

	/** Download file from hdfs. */
	public static InputStream download(String path) throws IllegalArgumentException, IOException {
		return fs.open(new Path(path));
	}

	/** Find file path from hdfs. */
	public static boolean find(String remotePath) {
		Path path = new Path(remotePath);
		try {
			return fs.exists(path);
		} catch (IOException e) {
			LOG.error("Find path[" + remotePath + "] has error,msg is " + e.getMessage());
			return false;
		}
	}

	/** Read part of content from hdfs file. */
	public static String hdfsFile(String remotePath) {
		Set<String> set = new HashSet<String>();
		FSDataInputStream fsdis = null;
		Path path = new Path(remotePath);
		try {
			if (fs.exists(path)) {
				fsdis = fs.open(path);
				String str;
				BufferedReader d = new BufferedReader(new InputStreamReader(fsdis));
				while (null != (str = d.readLine())) {
					set.add(str);
					if (set.size() > 10) {
						set.add("......");
						break;
					}
				}
			}
		} catch (Exception ex) {
			LOG.error("Read hdfs data has error,msg is " + ex.getMessage());
		} finally {
			try {
				if (fsdis != null) {
					fsdis.close();
				}
			} catch (Exception e) {

			}
		}
		String result = "";
		for (String content : set) {
			result += content + "\n";
		}
		return result;
	}

	/** Read a file content from hdfs. */
	public static String lines(String remotePath) {
		String result = "";
		FSDataInputStream is = null;
		Path path = new Path(remotePath);
		try {
			if (fs.exists(path)) {
				is = fs.open(path);
				FileStatus status = fs.getFileStatus(path);
				byte[] buffer = new byte[Integer.parseInt(String.valueOf(status.getLen()))];
				is.readFully(0, buffer);
				result = new String(buffer);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("Read hdfs data has error,msg is " + ex.getMessage());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
		}
		return result;
	}

	/** Overwrite data to hdfs file. */
	public static void overwrite(String path, String line) {
		OutputStream out = null;
		PrintWriter print = null;
		try {
			Path p = new Path(path);
			if (fs.exists(p)) {
				fs.delete(p, true);
			}
			if (!fs.exists(p)) {
				if (!fs.createNewFile(p))
					throw new Exception("Create hdfs file[" + p + "] failure");
			}
			out = fs.append(p);
			print = new PrintWriter(new OutputStreamWriter(out, "UTF-8"), true);
			print.println(line);
		} catch (Exception e) {
			LOG.error("Override hdfs has error,msg is " + e.getMessage());
		} finally {
			if (print != null)
				print.close();
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					LOG.error("Close has error,msg is " + e.getMessage());
				}
		}
	}

	/** Read file data from hdfs file. */
	public static Set<String> set(String remotePath) {
		Set<String> set = new HashSet<String>();
		FSDataInputStream fsdis = null;
		Path path = new Path(remotePath);
		try {
			if (fs.exists(path)) {
				fsdis = fs.open(path);
				String str;
				BufferedReader d = new BufferedReader(new InputStreamReader(fsdis));
				while (null != (str = d.readLine())) {
					set.add(str);
				}
			}
		} catch (Exception ex) {
			LOG.error("Read hdfs list data has error,msg is " + ex.getMessage());
		} finally {
			try {
				if (fsdis != null) {
					fsdis.close();
				}
			} catch (Exception e) {

			}
		}
		return set;
	}

}
