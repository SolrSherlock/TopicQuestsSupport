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
package org.nex.util;

/**
 * @author park
 * @see https://issues.apache.org/jira/secure/attachment/12484764/NUTCH-1016-2.0.patch
 */
public class StringUtil {

	/**
	 * Strip all non-characters from <code>input</code>
	 * @param input
	 * @return
	 */
	public static String stripNonCharCodepoints(String input) {
		    StringBuilder retval = new StringBuilder();
		    char ch;
		
		    for (int i = 0; i < input.length(); i++) {
		      ch = input.charAt(i);
		
		      // Strip all non-characters http://unicode.org/cldr/utility/list-unicodeset.jsp?a=[:Noncharacter_Code_Point=True:]
		      // and non-printable control characters except tabulator, new line and carriage return
		      if (ch % 0x10000 != 0xffff && // 0xffff - 0x10ffff range step 0x10000
		          ch % 0x10000 != 0xfffe && // 0xfffe - 0x10fffe range
		          (ch <= 0xfdd0 || ch >= 0xfdef) && // 0xfdd0 - 0xfdef
		          (ch > 0x1F || ch == 0x9 || ch == 0xa || ch == 0xd)) {
		
		        retval.append(ch);
		      }
		    }
		
		    return retval.toString();
	  }
}
