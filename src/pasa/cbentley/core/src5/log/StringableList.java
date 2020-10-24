package pasa.cbentley.core.src5.log;

import java.util.List;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;

public class StringableList implements IStringable {

   List<? extends IStringable> list;

   protected final UCtx        uc;

   public StringableList(UCtx uc, List<? extends IStringable> list) {
      this.uc = uc;
      this.list = list;
   }

   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      if (list == null) {
         dc.append("List is null");
      } else {
         for (IStringable is : list) {
            dc.nlLvl(is);
         }
      }
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      if (list == null) {
         dc.append("List is null");
      } else {
         for (IStringable is : list) {
            dc.nlLvl1Line(is);
         }
      }
   }

   public UCtx toStringGetUCtx() {
      return uc;
   }

   //#enddebug

}
