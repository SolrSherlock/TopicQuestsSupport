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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Title: TopicSpaces Subject Map Engine</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006, Jack Park</p>
 * <p>Company: NexistGroup</p>
 * @author Jack Park
 */
public class LRUCache implements IRemovableCache {
  private Map<Object,Object> cache;
  private int MAX_SIZE = 100;

  public LRUCache(int maxSize) {
    MAX_SIZE = maxSize;
    cache = new LinkedHashMap<Object,Object>(maxSize+1, .75F, true) {
    	private static final long serialVersionUID = 1;
    		//@override
    		protected boolean removeEldestEntry(Map.Entry eldest) {
    			return size() > MAX_SIZE;
    		}};
  }

  public void add(Object key, Object value) {
    synchronized(cache) {
      cache.put(key,value);
    }
  }

  public Object get(Object key) {
    synchronized(cache) {
      if (cache.containsKey(key))
        return cache.get(key);
    }
    return null;
  }

  public void remove(Object key) {
    synchronized(cache) {
      if (cache.containsKey(key))
        cache.remove(key);
    }
  }

  public void clear() {
	  synchronized(cache) {
		  cache.clear();
	  }
  }
  public int size() {
    synchronized(cache) {
      return cache.size();
    }
  }
}
