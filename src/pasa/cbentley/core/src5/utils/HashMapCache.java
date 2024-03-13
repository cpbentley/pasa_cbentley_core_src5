/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.core.src5.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src5.ctx.C5Ctx;
import pasa.cbentley.core.src5.interfaces.INameable;

/**
 * Maps a E that implements {@link INameable} to a V supporting {@link IStringable}
 * 
 * The name of E is used as a key for the {@link HashMap}
 * 
 * @author Charles Bentley
 *
 * @param <E>
 * @param <V>
 */
public class HashMapCache<E extends INameable<V>, V extends IStringable> implements IStringable {

   protected HashMap<String, E> pks;

   /**
    * When set to true when creator explicitely knows
    * all the data is loaded into the model
    */
   protected boolean            isDataLoaded = false;

   protected final C5Ctx        c5;

   public HashMapCache(C5Ctx c5c) {
      this.c5 = c5c;
      pks = new HashMap<String, E>();
   }

   /**
    * Retrieves E whose name is <code>keyString</code>
    * @param keyString
    * @return E
    */
   public E getSelectedObject(String keyString) {
      return pks.get(keyString);
   }

   /**
    * Adds in the cache using {@link INameable#getNameableString()} as key in the map
    * 
    * @param pk
    */
   public void addNamer(E pk) {
      String name = pk.getNameableString();
      pks.put(name, pk);
   }

   public Collection<E> getValues() {
      return pks.values();
   }

   public boolean isDataLoaded() {
      return isDataLoaded;
   }

   public Set<String> getKeySet() {
      return pks.keySet();
   }

   public void put(String key, E value) {
      pks.put(key, value);
   }

   public E getValue(String key) {
      return pks.get(key);
   }

   /**
    * Puts all and replace any existing mapping
    * @param into
    */
   public void putAllInto(HashMap<String, E> into) {
      into.putAll(pks);
   }

   public void addNamers(List<E> rows) {
      Iterator<E> it = rows.iterator();
      while (it.hasNext()) {
         E pk = (E) it.next();
         addNamer(pk);
      }
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "HashMapCache");
      toStringPrivate(dc);
      c5.toStringHashMap1Line(dc, pks, "HashMap");
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("isDataLoaded", isDataLoaded);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "HashMapCache");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return c5.getUC();
   }

   //#enddebug

}
