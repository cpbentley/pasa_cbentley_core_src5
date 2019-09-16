package pasa.cbentley.core.src5.ctx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pasa.cbentley.core.src4.ctx.ACtx;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src5.interfaces.INameable;
import pasa.cbentley.core.src5.utils.TextUtils;

public class C5Ctx extends ACtx implements ICtx {

   public C5Ctx(UCtx uc) {
      super(uc);
   }

   private TextUtils textUtils = null;

   public TextUtils getTextUtils() {
      if (textUtils == null) {
         textUtils = new TextUtils(this);
      }
      return textUtils;
   }

   //#mdebug
   public void toString(Dctx dc, List<String> list, String title) {
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

   public void toStringHashMapStringString(Dctx dc, Map<String, String> map, String title, boolean keyFirst) {
      if (map == null) {
         dc.append("HashMap " + title + " is null");
      } else {
         dc.append(title);
         dc.append(" #");
         dc.append(map.size());
         dc.tab();
         Set<String> keySet = map.keySet();
         for (String keyString : keySet) {
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
            dc.nl();
         }
         dc.tabRemove();
      }
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
   //#enddebug
}
