package pasa.cbentley.core.src5.ctx;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;

public abstract class ObjectC5  implements  IStringable  {

   protected final C5Ctx c5;

   public ObjectC5(C5Ctx c5) {
      this.c5 = c5;
   }
   
   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, ObjectC5.class, "@line5");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ObjectC5.class);
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return c5.getUC();
   }

   //#enddebug
   

}
