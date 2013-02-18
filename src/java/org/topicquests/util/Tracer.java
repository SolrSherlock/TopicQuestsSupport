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
import java.io.*;
import java.util.*;
import org.nex.util.DateUtil;
/**
 * @author park
 *
 */
public class Tracer extends Thread {
	private LoggingPlatform log;
	private String fileName;
	private List<Long>timestamps;
	private List<String>messages;
	private boolean isRunning = true;
	private PrintWriter out;
	private TextFileHandler handler;
	private long lineCount = 0;
	private long flushCount = 100;
	private long maxLineCount = 100000;
	
	
	public Tracer(String agentName, LoggingPlatform p) {
		log = p;
		fileName = agentName;
		timestamps = new ArrayList<Long>();
		messages = new ArrayList<String>();
		handler = new TextFileHandler();
		createFile();
		isRunning = true;
		lineCount = 0;
		this.start();
	}

	private void createFile() {
		try {
			if (out != null) {
				out.flush();
				out.close();
			}
			String fname = fileName+DateUtil.defaultTimestamp(System.currentTimeMillis())+".txt.gz";
System.out.println(fname);
			fname = fname.replaceAll(" ", "_");
			fname = fname.replaceAll(":", "_");
System.out.println(fname);
			out = handler.getGZipWriter(fname);
		} catch (Exception e) {
			log.logError(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void shutDown() {
		synchronized(messages) {
			isRunning = false;
			messages.notifyAll();
		}
		try {
			if (out != null) {
				out.flush();
				out.close();
			}			
		} catch (Exception e) {
			log.logError(e.getMessage(), e);
		}
	}
	
	/**
	 *<p> Called by agents sending in trace messages</p>
	 *<p>Note: if <code>timestamp</code> == 0, then timestamp
	 * is ignored</p>
	 * @param timestamp
	 * @param message
	 */
	public void trace(long timestamp, String message) {
		synchronized(messages) {
			timestamps.add(new Long(timestamp));
			messages.add(message);
			messages.notifyAll();
		}
	}
	
	public void run() {
		Long theTime = null;
		String theMessage = null;
		while (isRunning) {
			synchronized(messages) {
				if (messages.isEmpty()) {
					try {
						messages.wait();
					} catch (Exception e) {}
				} else if (isRunning && !messages.isEmpty()) {
					theMessage = messages.remove(0);
					theTime = timestamps.remove(0);
				}
			}
			if (isRunning && theMessage != null) {
				handleMessage(theMessage,theTime);
				theMessage = null;
				theTime = null;
			}
		}
	}
	
	void handleMessage(String msg, Long t) {
//		System.out.println("handling "+msg);
		long tv = t.longValue();
		String dt = "";
		if (tv > 0)
			dt = DateUtil.defaultTimestamp(t.longValue())+" ";
		out.print(dt+msg+"\n");
		lineCount++;
		if ((lineCount % flushCount) == 0) {
			try {
				out.flush();
			} catch (Exception e) {
				log.logError(e.getMessage(), e);
			}
		}
		if (lineCount > maxLineCount)
			createFile();
	}
}
