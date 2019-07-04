package pasa.cbentley.core.src5.interfaces;

import pasa.cbentley.core.src4.logging.IStringable;

public interface INameable<X> extends IStringable {

   /**
    * The name for this object
    * @return
    */
   public String getNameableString();
   
   /**
    * Returns the object being named
    * @return
    */
   public X getNamedObject();
}
