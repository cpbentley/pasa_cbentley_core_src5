/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.core.src5.task;

import java.io.IOException;

import pasa.cbentley.core.src5.ctx.C5Ctx;

public class SaveCtxTask implements Runnable {

   protected final C5Ctx  c5;

   protected final String file;

   public SaveCtxTask(C5Ctx c5, String file) {
      this.c5 = c5;
      this.file = file;
   }

   public void run() {
      try {
         c5.saveCtxSettingsToUserHome(file);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

}
