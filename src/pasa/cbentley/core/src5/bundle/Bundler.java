package pasa.cbentley.core.src5.bundle;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.interfaces.IPrefs;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src5.ctx.C5Ctx;
import pasa.cbentley.core.src5.ctx.ITechPrefsC5;

public class Bundler implements IStringable {

   protected final C5Ctx          c5;

   private CombinedResourceBundle resBund;

   private IPrefs prefs;

   public Bundler(C5Ctx c5, IPrefs prefs) {
      this.c5 = c5;
      this.prefs = prefs;
   }

   private List<String> bundleNames;

   private Locale       locale;

   /**
    * Sets the list of bundles.
    * @param bundleNames
    */
   public void setBundleList(List<String> bundleNames) {
      this.bundleNames = bundleNames;
   }
   private boolean                isResMissingLog  = true;

   public void setLocale(Locale locale) {
      this.locale = locale;
       if (bundleNames == null) {
         throw new NullPointerException("SwingCtx:BundleNames not initialized");
      }
      resBund = new CombinedResourceBundle(c5, bundleNames, locale, new UTF8Control());
      resBund.load();
   }

   public String getResString(String key) {
      try {
         //#mdebug
         if (key == null) {
            throw new NullPointerException();
         }
         //#enddebug
         return resBund.getString(key);
      } catch (MissingResourceException e) {
         //check if we need to log this
         if (isResMissingLog()) {
            c5.getLog().consoleLogError("Cannot find String resource with ID:" + key);
         }
         return key;
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
      dc.root(this, "Bundler");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "Bundler");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return c5.getUCtx();
   }

   public boolean isResMissingLog() {
      return isResMissingLog;
   }

   public void setResMissingLog(boolean isResMissingLog) {
      this.isResMissingLog = isResMissingLog;
   }

   //#enddebug

}
