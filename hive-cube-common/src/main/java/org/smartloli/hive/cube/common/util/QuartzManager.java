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

import java.util.ArrayList;
import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.smartloli.hive.cube.common.client.CommonClientConfigs;

/**
 * Task scheduler by v1.8.6
 * 
 * @author smartloli.
 *
 *         Created by Mar 21, 2017
 */
public class QuartzManager {

	private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	private static final String JOB_GROUP_NAME = "MF_JOBGROUP_NAME";
	private static final String TRIGGER_GROUP_NAME = "MF_TRIGGERGROUP_NAME";

	/** Add a new job to quartz. */
	public static void addJob(String jobName, Class cls, String time) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, cls);
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put(CommonClientConfigs.Scheduler.NAME, jobName);
			jobDetail.setJobDataMap(jobDataMap);
			CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);
			trigger.setCronExpression(time);
			sched.scheduleJob(jobDetail, trigger);
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Add a new job to quartz by trigger. */
	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, String time) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass);
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put(CommonClientConfigs.Scheduler.NAME, jobName);
			jobDetail.setJobDataMap(jobDataMap);
			CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);
			trigger.setCronExpression(time);
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Get all job names. */
	public static List<String> getJobNames() {
		List<String> jobNames = new ArrayList<>();
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			for (String name : sched.getJobNames(JOB_GROUP_NAME)) {
				jobNames.add(name);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return jobNames;
	}

	/** Modify a quartz crontab date. */
	public static void modifyJobTime(String jobName, String time) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName, TRIGGER_GROUP_NAME);
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobDetail jobDetail = sched.getJobDetail(jobName, JOB_GROUP_NAME);
				Class objJobClass = jobDetail.getJobClass();
				removeJob(jobName);
				addJob(jobName, objJobClass, time);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Modify a quartz crontab date by trigger. */
	public static void modifyJobTime(String triggerName, String triggerGroupName, String time) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName, triggerGroupName);
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				CronTrigger ct = (CronTrigger) trigger;
				ct.setCronExpression(time);
				sched.resumeTrigger(triggerName, triggerGroupName);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Stop all quartz jobs. */
	public static void shutdownAllJobs() {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Start all quartz jobs. */
	public static void startAllJobs() {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Remove a quartz job. */
	public static void removeJob(String jobName) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);
			sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);
			sched.deleteJob(jobName, JOB_GROUP_NAME);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Remove a quartz job by trigger. */
	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			sched.pauseTrigger(triggerName, triggerGroupName);
			sched.unscheduleJob(triggerName, triggerGroupName);
			sched.deleteJob(jobName, jobGroupName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
