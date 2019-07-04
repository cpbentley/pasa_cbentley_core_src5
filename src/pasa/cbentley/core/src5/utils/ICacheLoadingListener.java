package pasa.cbentley.core.src5.utils;

import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src5.interfaces.INameable;

public interface ICacheLoadingListener<E extends INameable<V>, V extends IStringable> extends IStringable {

   public void modelDidFinishLoading(HashMapCache<E,V> model);
}
