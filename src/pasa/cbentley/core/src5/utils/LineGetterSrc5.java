package pasa.cbentley.core.src5.utils;

import pasa.cbentley.core.src4.interfaces.ILineGetter;
import pasa.cbentley.core.src4.logging.LogParameters;

public class LineGetterSrc5 implements ILineGetter {

   public String getLine(int value) {
      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      int val = stackTrace[4].getLineNumber();
      return String.valueOf(val);
   }

   public LogParameters getLine(Class cl, String method, int line) {
      LogParameters lp = new LogParameters();
      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      //lp.cl = cl;
      int val = stackTrace[4].getLineNumber();
      String methodName = stackTrace[4].getMethodName();
      lp.className = stackTrace[4].getClassName();
      lp.method = methodName + "@" + val;
      try {
         lp.cl = Class.forName(lp.className);
      } catch (ClassNotFoundException e) {
         lp.cl = cl;
      }
      return lp;
   }

}
