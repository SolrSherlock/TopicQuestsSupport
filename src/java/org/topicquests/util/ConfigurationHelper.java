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

/**
 * @author park
 *
 */
public class ConfigurationHelper {

	/**
	 * Turn any given <code>inFilePath</code> into 
	 * a proper path depending on where that file is located
	 * <em>within the program's classpath</em> or throw a
	 * {@link RuntimeException}
	 * @param inFilePath
	 * @return
	 */
	public static String findPath(String inFilePath) {
		String result = inFilePath;
		File f = new File(result);
		if (f.exists())
			return result;
		result = "config/"+inFilePath;
		f = new File(result);
		if (f.exists())
			return result;
		result = "conf/"+inFilePath;
		f = new File(result);
		if (f.exists())
			return result;
		result = "cfg/"+inFilePath;
		f = new File(result);
		if (f.exists())
			return result;
		else
			throw new RuntimeException("File "+result+" not found");
	}

}
