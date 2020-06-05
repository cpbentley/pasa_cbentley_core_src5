/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.core.src5.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.structs.BufferString;
import pasa.cbentley.core.src5.ctx.C5Ctx;

/**
 * Implementation of {@link Map} <String,String> with a single array
 * 
 * A key and its value are contiguous inside the array
 * 
 * TODO test it
 * @author Charles Bentley
 *
 */
public class StringArrayMap implements Map<String, String> {


   private BufferString  strings;

   private int           count;

   protected final C5Ctx c5;

   public StringArrayMap(C5Ctx c5, int capacity) {
      this.c5 = c5;
      strings = new BufferString(c5.getUCtx(), capacity * 2);
   }

   public int size() {
      return count / 2;
   }

   public boolean isEmpty() {
      return count == 0;
   }

   public boolean containsKey(Object key) {
      return getIndexKey(key) != -1;
   }

   public int getIndexValue(Object value) {
      if (value instanceof String) {
         String valueStr = (String) value;
         return strings.getFirstIndexEqual(valueStr, 1, 2);
      }
      return -1;
   }

   public int getIndexKey(Object key) {
      if (key instanceof String) {
         String keyStr = (String) key;
         return strings.getFirstIndexEqual(keyStr, 0, 2);
      }
      return -1;
   }

   public boolean containsValue(Object value) {
      return getIndexValue(value) != -1;
   }

   public String get(Object key) {
      int index = getIndexKey(key);
      if (index != -1) {
         return strings.getStr(index + 1);
      }
      return null;
   }

   public String put(String key, String value) {
      int indexKey = getIndexKey(key);
      if (indexKey == -1) {
         strings.addStrs(key, value);
         return null;
      } else {
         String oldValue = strings.getStr(indexKey);
         strings.setStrUnsafe(indexKey + 1, value);
         return oldValue;
      }
   }

   public String remove(Object key) {
      int indexKey = getIndexKey(key);
      if (indexKey != -1) {
         String value = strings.getStr(indexKey + 1);
         strings.removeAtIndexFor(indexKey, 2);
         return value;
      } else {
         return null;
      }
   }

   public void putAll(Map<? extends String, ? extends String> m) {
      Set<? extends String> keySet = m.keySet();
      for (String key : keySet) {
         this.put(key, m.get(key));
      }
   }

   public void clear() {
      count = 0;
      strings.clear();
   }

   public Set<String> keySet() {
      Set<String> keys = new HashSet<String>(size());
      for (int i = 0; i < count; i = i + 2) {
         keys.add(strings.getStr(i));
      }
      return keys;
   }

   public Collection<String> values() {
      ArrayList<String> values = new ArrayList<String>(size());
      for (int i = 1; i < count; i = i + 2) {
         values.add(strings.getStr(i));
      }
      return values;
   }

   /**
    * not supported
    */
   public Set<Entry<String, String>> entrySet() {
      throw new RuntimeException();
   }

}
