/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.core.src5.ctx;

import java.util.Locale;
import java.util.Timer;

import pasa.cbentley.core.src4.logging.Dctx;

public class C5Debug {

   protected final C5Ctx c5;

   public C5Debug(C5Ctx c5) {
      this.c5 = c5;
   }

   public void toStringLocale(Locale loc, Dctx dc) {
      dc.root(loc, "Locale");
      dc.appendVarWithSpace("country", loc.getCountry());
      dc.appendVarWithSpace("language", loc.getLanguage());
      dc.appendVarWithSpace("DisplayCountry", loc.getDisplayCountry());
      dc.appendVarWithSpace("DisplayLanguage", loc.getDisplayLanguage());
   }

   public void d(Timer timer, Dctx dc) {
      dc.root(timer, "Timer");
      if (timer != null) {
         dc.appendVar(" ", timer.toString());
      }
   }
}
