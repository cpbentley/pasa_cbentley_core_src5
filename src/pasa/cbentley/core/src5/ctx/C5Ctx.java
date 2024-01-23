/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.core.src5.ctx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pasa.cbentley.core.src4.ctx.ACtx;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.io.BAByteIS;
import pasa.cbentley.core.src4.io.BAByteOS;
import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.logging.IUserLog;
import pasa.cbentley.core.src5.interfaces.IMem5;
import pasa.cbentley.core.src5.interfaces.INameable;
import pasa.cbentley.core.src5.interfaces.ITechJava5Props;
import pasa.cbentley.core.src5.utils.TextUtils;

/**
 * Code ctx that provides access to all Java 5 constructs and classes.
 * 
 * 
 * @author Charles Bentley
 *
 */
public class C5Ctx extends ACtx implements ICtx {

   public static final int CTX_ID    = 2;

   //#debug
   protected final C5Debug c5Debug;

   private IMem5           mem5;

   private TextUtils       textUtils = null;

   public C5Ctx(UCtx uc) {
      super(uc);

      //#debug
      c5Debug = new C5Debug(this);
   }

   public int getCtxID() {
      return CTX_ID;
   }

   public IUserLog getLog() {
      return uc.getUserLog();
   }

   public IMem5 getMem() {
      if (mem5 == null) {
         mem5 = new Mem5(this);
      }
      return mem5;
   }

   public String getSeparatorFile() {
      return System.getProperty(ITechJava5Props.SEPARATOR_FILE);
   }

   public TextUtils getTextUtils() {
      if (textUtils == null) {
         textUtils = new TextUtils(this);
      }
      return textUtils;
   }

   public String getUserDir() {
      return System.getProperty(ITechJava5Props.DIR_USER);
   }

   /**
    * Get system property {@link ITechJava5Props#DIR_HOME}
    * @return
    */
   public String getUserHome() {
      return System.getProperty(ITechJava5Props.DIR_HOME);
   }

   /**
    * Does not load if file does not exists
    * @param fileName
    * @throws IOException
    */
   public void loadCtxSettingsFromUserHome(String fileName) throws IOException {
      String homeDir = getUserHome();
      //read the state asap from a file
      File f = new File(homeDir, fileName);
      if (f.exists()) {
         FileInputStream fis = new FileInputStream(f);
         byte[] bytes = uc.getIOU().streamToByte(fis);
         BAByteIS bis = new BAByteIS(uc, bytes);
         BADataIS bais = new BADataIS(uc, bis);
         uc.getCtxManager().stateRead(bais);
      }
   }

   /**
    * 
    * @param fileName
    * @throws IOException
    */
   public void saveCtxSettingsToUserHome(String fileName) throws IOException {
      String homeDir = getUserHome();
      File f = new File(homeDir, fileName);

      //#debug
      toDLog().pFlow("" + f.getAbsolutePath(), this, C5Ctx.class, "saveCtxSettingsToUserHome", LVL_05_FINE, true);

      BAByteOS bis = new BAByteOS(uc);
      BADataOS bais = new BADataOS(uc, bis);
      uc.getCtxManager().stateWrite(bais);

      FileOutputStream fos = new FileOutputStream(f);
      try {
         fos.write(bis.getArrayRef(), 0, bis.getByteWrittenCount());
      } catch (Exception e) {
         fos.close();
      }
   }

   //#mdebug
   public C5Debug to5D() {
      return c5Debug;
   }

   public void toString(Dctx dc) {
      dc.root(this, "C5Ctx");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "C5Ctx");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   public void toStringFile(Dctx dc, File file, String title) {
      if (file == null) {
         dc.append("File " + title + " is null");
      } else {
         dc.append(title);
         dc.tab();
         dc.appendVarWithSpace("path", file.getAbsolutePath());
         dc.appendVarWithSpace("canExecute", file.canExecute());
         dc.appendVarWithSpace("canRead", file.canRead());
         dc.appendVarWithSpace("canWrite", file.canWrite());
         dc.tabRemove();
      }
   }

   public String toStringFile(File file, String title) {
      Dctx dc = new Dctx(uc);
      toStringFile(dc, file, title);
      return dc.toString();
   }

   public void toStringHashMap(Dctx dc, HashMap<String, ? extends IStringable> map, String title) {
      if (map == null) {
         dc.append("HashMap " + title + " is null");
      } else {
         dc.append(title);
         dc.tab();
         Set<String> keySet = map.keySet();
         for (String keyString : keySet) {
            IStringable stringable = map.get(keyString);
            dc.nlLvl(keyString, stringable);
         }
         dc.tabRemove();
      }
   }

   public void toStringHashMap1Line(Dctx dc, HashMap<String, ? extends IStringable> map, String title) {
      dc.nl();
      if (map == null) {
         dc.append("HashMap " + title + " is null");
      } else {
         dc.append(title);
         dc.tab();
         Set<String> keySet = map.keySet();
         for (String keyString : keySet) {
            IStringable stringable = map.get(keyString);
            dc.nl();
            dc.append(keyString);
            dc.append("->");
            dc.append(stringable.toString1Line());
         }
         dc.tabRemove();
      }
   }

   public void toStringHashMapNameable1Line(Dctx dc, HashMap<? extends INameable, ? extends IStringable> map, String title) {
      dc.nl();
      if (map == null) {
         dc.append("HashMap " + title + " is null");
      } else {
         dc.append(title);
         dc.tab();
         Set<? extends INameable> keySet = map.keySet();
         for (INameable keyString : keySet) {
            IStringable stringable = map.get(keyString);
            dc.nl();
            dc.append(keyString.getNameableString());
            dc.append("->");
            dc.append(stringable.toString1Line());
         }
         dc.tabRemove();
      }
   }

   public void toStringHashMapStringString(Dctx dc, Map<String, String> map, String title, boolean keyFirst) {
      if (map == null) {
         dc.append("HashMap " + title + " is null");
      } else {
         dc.append(title);
         dc.append(" #");
         dc.append(map.size());
         dc.tab();
         Set<String> keySet = map.keySet();
         int count = 0;
         for (String keyString : keySet) {
            count++;
            dc.nl();
            dc.append(count);
            dc.append("\t");
            String strValue = map.get(keyString);
            if (keyFirst) {
               dc.append(keyString);
               dc.append("=");
               dc.append(strValue);
            } else {
               dc.append(strValue);
               dc.append("=");
               dc.append(keyString);
            }
         }
         dc.tabRemove();
      }
   }

   public void toStringListString(Dctx dc, List<String> list, String title) {
      if (list == null) {
         dc.append("List " + title + " is null");
      } else {
         dc.append(title);
         dc.tab();
         for (String str : list) {
            dc.nl();
            dc.append(str);
         }
         dc.tabRemove();
      }
   }

   public void toStringListStringable(Dctx dc, List<? extends IStringable> list, String title) {
      if (list == null) {
         dc.append("List " + title + " is null");
      } else {
         dc.append(title);
         dc.tab();
         for (IStringable is : list) {
            dc.nlLvl(is);
         }
         dc.tabRemove();
      }
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toStringThread(Dctx dc, Thread thread, String title) {
      if (thread == null) {
         dc.append("Thread " + title + " is null");
      } else {
         dc.root(thread, "Thread");
         dc.appendWithSpace(title);
         dc.tab();
         dc.appendVarWithSpace("name", thread.getName());
         dc.appendVarWithSpace("priority", thread.getPriority());
         dc.appendVarWithSpace("id", thread.getId());
         dc.appendVarWithSpace("isAlive", thread.isAlive());
         dc.appendVarWithSpace("isDaemon", thread.isDaemon());
         dc.appendVarWithSpace("isInterrupted", thread.isInterrupted());
         dc.tabRemove();
      }
   }

   public String toStringThread(Thread thread, String title) {
      Dctx dc = new Dctx(uc);
      toStringThread(dc, thread, title);
      return dc.toString();
   }

   public String toStringThreadCurrent() {
      return toStringThread(Thread.currentThread(), "current");
   }

   //#enddebug

}
