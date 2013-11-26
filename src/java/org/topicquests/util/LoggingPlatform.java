/*
 * Copyright 2013, TopicQuests
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.topicquests.util;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.util.*;

/**
 * @author park
 *
 */
public class LoggingPlatform {
	private static LoggingPlatform instance = null;
	private List<Tracer> tracers;
	
	private Logger log;
	
	/**
	 * NOTE: this will check in several paths in case the logger properties
	 * file is kept in other directores in the classpath
	 * @param loggerPropertiesFilePath
	 * 
	 */
	protected LoggingPlatform(String loggerPropertiesFilePath) {
		PropertyConfigurator.configure(ConfigurationHelper.findPath(loggerPropertiesFilePath));
		log = Logger.getLogger(LoggingPlatform.class);
		tracers = new ArrayList<Tracer>();
	}


	/**
	 * This method will throw a {@link RuntimeException} if
	 * the other $getInstance was not called
	 * @return
	 */
	public static LoggingPlatform getLiveInstance() {
		if (instance==null)
			throw new RuntimeException("LoggingPlatform not initialized");
		return instance;
	}
	
	/**
	 * This is the first method to call
	 * @param loggerPropertiesFilePath
	 * @return
	 */
	public static LoggingPlatform getInstance(String loggerPropertiesFilePath) {
		if (instance == null)
			instance = new LoggingPlatform(loggerPropertiesFilePath);
		return instance;
	}
	
	public void logDebug(String msg) {
		log.debug(msg);
	}
	
	public void logError(String msg, Exception e) {
		if (e == null)
			log.error(msg);
		else
			log.error(msg,e);
	}
	
	public void record(String msg) {
		log.info(msg);
	}

	/**
	 * Returns an instance of {@link Tracer} to be used
	 * by agents to record messages (not debug, not errors)
	 * @param agentName
	 * @return
	 */
	public Tracer getTracer(String agentName) {
		Tracer t = new Tracer(agentName,this);
		tracers.add(t);
		return t;
	}
	
	/**
	 * Must be called if any Tracers are active
	 */
	public void shutDown() {
		Iterator<Tracer>itr = tracers.iterator();
		while(itr.hasNext())
			itr.next().shutDown();
	}
	
}
