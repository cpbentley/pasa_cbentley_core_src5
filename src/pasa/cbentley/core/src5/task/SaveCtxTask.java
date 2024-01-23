/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.core.src5.task;

import java.io.IOException;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src5.ctx.C5Ctx;
import pasa.cbentley.core.src5.ctx.ObjectC5;

public class SaveCtxTask extends ObjectC5 implements Runnable {


   protected final String file;

   public SaveCtxTask(C5Ctx c5, String file) {
      super(c5);
      this.file = file;
   }

   public void run() {
      //#debug
      toDLog().pFlow("Calling c5.saveCtxSettingsToUserHome", this, SaveCtxTask.class, "run", LVL_05_FINE, true);
      try {
         c5.saveCtxSettingsToUserHome(file);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, SaveCtxTask.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("file", file);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, SaveCtxTask.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
