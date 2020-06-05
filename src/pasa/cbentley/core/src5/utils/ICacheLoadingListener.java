/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.core.src5.utils;

import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src5.interfaces.INameable;

public interface ICacheLoadingListener<E extends INameable<V>, V extends IStringable> extends IStringable {

   public void modelDidFinishLoading(HashMapCache<E,V> model);
}
