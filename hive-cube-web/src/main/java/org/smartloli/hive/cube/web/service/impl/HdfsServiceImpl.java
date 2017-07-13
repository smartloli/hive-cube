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
package org.smartloli.hive.cube.web.service.impl;

import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.HDFS;
import org.smartloli.hive.cube.common.util.HadoopUtils;
import org.smartloli.hive.cube.core.metrics.HadoopMetricsFactory;
import org.smartloli.hive.cube.core.metrics.HadoopMetricsService;
import org.smartloli.hive.cube.web.service.HdfsService;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Implements hdfs visit method.
 * 
 * @author smartloli.
 *
 *         Created by Dec 28, 2016
 */
@Service
public class HdfsServiceImpl implements HdfsService {
	private Logger LOG = LoggerFactory.getLogger(HdfsServiceImpl.class);

	/** Hadoop metrics method. */
	private HadoopMetricsService hms = new HadoopMetricsFactory().create();

	/** List hdfs menu. */
	public JSONArray dir(String path) {
		return JSON.parseArray(hms.browseDirectory(path).toString());
	}

	/** Delete hdfs menu or file. */
	public void delete(String path) {
		try {
			HadoopUtils.delete(path);
		} catch (Exception e) {
			LOG.error("Delete file or dir from hdfs has error,msg is " + e.getMessage());
		}
	}

	/** Download file from hdfs. */
	public InputStream download(String fileName) {
		try {
			return HadoopUtils.download(fileName);
		} catch (Exception e) {
			LOG.error("Get hdfs path has error,msg is " + e.getMessage());
		}
		return null;
	}

	/** Read data from hdfs. */
	public String read(String path) {
		JSONObject object = new JSONObject();
		object.put("context", HadoopUtils.hdfsFile(File.separator + path.replaceAll(HDFS.UNDERLINE, File.separator)));
		object.put("open", "/hc/metrics/hdfs/" + path + "/download");
		return object.toJSONString();
	}

}
