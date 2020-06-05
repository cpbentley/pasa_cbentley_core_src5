/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.core.src5.bundle;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src5.ctx.C5Ctx;

public class CombinedResourceBundle extends ResourceBundle implements IStringable {

   private List<String>        bundleNames;

   private Map<String, String> combinedResources = new HashMap<String, String>();

   private Control             control;

   private Locale              locale;

   private C5Ctx            sc;

   public CombinedResourceBundle(C5Ctx sc, List<String> bundleNames, Locale locale) {
      this(sc, bundleNames, locale, ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_DEFAULT));
   }

   public CombinedResourceBundle(C5Ctx sc, List<String> bundleNames, Locale locale, Control control) {
      super();
      this.sc = sc;
      this.bundleNames = bundleNames;
      this.locale = locale;
      this.control = control;
   }

   public Enumeration<String> getKeys() {
      return Collections.enumeration(combinedResources.keySet());
   }

   protected Object handleGetObject(String key) {
      return combinedResources.get(key);
   }

   /**
    * Don't forget to load!
    */
   public void load() {
      for (String bundleName : bundleNames) {
         try {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, control);
            Enumeration<String> en = bundle.getKeys();
            while (en.hasMoreElements()) {
               String key = en.nextElement();
               combinedResources.put(key, bundle.getString(key));
            }
         } catch (MissingResourceException e) {
            e.printStackTrace();
            sc.getLog().consoleLogError("Could not find resource file for " + bundleName);
         }
      }
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "CombinedResourceBundle");
      sc.to5D().toStringLocale(locale, dc);
      dc.nl();
      for (String name : bundleNames) {
         dc.append(name);
         dc.append(",");
      }
      dc.reverse(1);
      dc.nl();
      dc.append("Strings");
      dc.tab();
      for (String key : combinedResources.keySet()) {
         dc.nl();
         dc.appendVarWithSpace(key, combinedResources.get(key));
      }
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "CombinedResourceBundle");
   }

   public UCtx toStringGetUCtx() {
      return sc.getUCtx();
   }

   //#enddebug

}
