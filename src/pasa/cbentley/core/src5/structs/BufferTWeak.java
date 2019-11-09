package pasa.cbentley.core.src5.structs;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.interfaces.IStrComparator;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.utils.ArrayUtils;
import pasa.cbentley.core.src4.utils.IntUtils;
import pasa.cbentley.core.src4.utils.StringUtils;

/**
 * Src 4 compatible for Strings only.
 * 
 * core.src5 has a BufferObject for generics.
 * 
 * @author Charles Bentley
 *
 */
public class BufferTWeak<T> implements IStringable {

   private int            count     = 0;

   private int            offset    = 0;

   /**
    * must be 1 or higher
    */
   private int            increment = 5;


   /**
    * index 0 starts at 0 and gives the last used value.
    * Therefore it also gives the number of elements
    * buffer size = ints.length - 1 - int[0]
    */
   private Object[]       objects;

   private UCtx           uc;

   /**
    * 1,1
    */
   public BufferTWeak(UCtx uc) {
      this(uc, 1, 1, 0);
   }

   public BufferTWeak(UCtx uc, int num) {
      this(uc, num, 3, 0);
   }

   /**
    * 
    * @param startSize
    * @param increment few adds take a small increment., lots take a big increment
    */
   public BufferTWeak(UCtx uc, int startSize, int increment, int bufferFront) {
      set(new Object[startSize]);
      this.increment = increment;
      this.uc = uc;
      this.offset = bufferFront;
   }

   /**
    * Uses the reference!
    * @param ar
    */
   public BufferTWeak(UCtx uc, T[] ar) {
      set(ar);
      this.uc = uc;
   }

   /**
    * append String to the {@link BufferTWeak}
    * @param str
    */
   public void add(T obj) {
      set(uc.getMem().ensureCapacity(objects, count, increment));
      objects[offset + count] = obj;
      count += 1;
   }

   public void add(T obj1, T obj2) {
      set(uc.getMem().ensureCapacity(objects, count + 2, increment));
      objects[offset + count] = obj1;
      objects[offset + count + 1] = obj2;
      count += 2;
   }

   /**
    * Add the Object in front of the array
    * @param obj
    */
   public void addLeft(T obj) {
      if (offset == 0) {
         set(uc.getMem().ensureCapacity(objects, count + 2, increment));
         //shift them all up by 2 and
         ArrayUtils.shiftIntUp(objects, 2, offset, offset + count - 1, false);
         offset += 2;
      }
      objects[offset - 1] = obj;
      count++;
   }

   public void appendBufferToArrayAt(T[] array, int offset) {
      int start = this.offset;
      int end = start + count;
      for (int j = start; j < end; j++) {
         @SuppressWarnings("unchecked")
         T v = (T)objects[j];
         array[offset] = v;
         offset++;
      }
   }

   public void appendBufferToArrayAt(T[] array, int offset, int startOffset, int skip) {
      int start = this.offset;
      int end = start + count;
      for (int j = start; j < end; j++) {
         @SuppressWarnings("unchecked")
         T v = (T)objects[j];
         array[offset] = v;
         offset++;
      }
   }

   /**
    * O(1)
    */
   public void clear() {
      count = 0;
   }

   /**
    * Clone tit for tat
    * @return
    */
   public BufferTWeak cloneMe() {
      BufferTWeak ib = new BufferTWeak(uc);
      ib.increment = increment;
      ib.set(new Object[objects.length]);
      for (int i = 0; i < objects.length; i++) {
         ib.objects[i] = this.objects[i];
      }
      ib.offset = this.offset;
      ib.count = this.count;
      return ib;
   }

   /**
    * 0 based index
    * <br>
    * <br>
    * 
    * @param index
    * @return
    */
   public T get(int index) {
      @SuppressWarnings("unchecked")
      T v = (T)objects[offset + index];
      return v;
   }

   /**
    * 
    * @param index
    * @param obj
    */
   public void setUnsafe(int index, T obj) {
      objects[offset + index] = obj;
   }

   /**
    * Returns a truncated copy of the int array with only the integer data.
    * <br> 
    * The size header and trailing buffer are removed.
    * @return
    */
   public T[] getClonedTrimmed(T[] ar) {
      for (int i = offset; i < offset + count; i++) {
         @SuppressWarnings("unchecked")
         T v = (T)objects[i];
         ar[i] = v;
      }
      return ar;
   }

   /**
    * Returns the reference to the integer array. This mean the int[0] still means the number of elements.
    * <br>
    * <br>
    * A direct reference should be used with caution and is inherently unsafe unless
    * you know the implementation details of {@link BufferTWeak}
    * @return
    */
   public Object[] getIntsRef() {
      return objects;
   }

   /**
    * Returns the last element in the {@link BufferTWeak}.
    * <br>
    * <br>
    * Does not remove it.
    * @return null if no elements
    */
   public T getLast() {
      if (count > 0) {
         @SuppressWarnings("unchecked")
         T last = (T)objects[offset + count - 1];
         return last;
      }
      return null;
   }

   /**
    * Returns the last element in the {@link BufferTWeak}.
    * <br>
    * <br>
    * Does not remove it.
    * @return null if no elements
    */
   public T getFirst() {
      if (count > 0) {
         @SuppressWarnings("unchecked")
         T first = (T)objects[offset];
         return first;
      }
      return null;
   }

   /**
    * Number of elements.
    * @return
    */
   public int getSize() {
      return count;
   }


   /**
    * 
    * @return null if size is zero
    */
   public T removeFirst() {
      if (count != 0) {
         @SuppressWarnings("unchecked")
         T v = (T)objects[offset];
         offset += 1;
         count--;
         return v;
      } else {
         return null;
      }
   }

   /**
    * Removes the last element and returns it.
    * <br>
    * Returns 0 when {@link BufferTWeak} is emptys
    * @return
    */
   public T removeLast() {
      if (count != 0) {
         @SuppressWarnings("unchecked")
         T v = (T)objects[offset + count - 1];
         objects[offset + count] = null;
         count--;
         return v;
      } else {
         return null;
      }
   }

   /**
    * Remove size elements starting at index
    * @param index
    * @param size
    */
   public void removeAtIndexFor(int index, int size) {
      int start = offset + index + size;
      int end = offset + count;
      ArrayUtils.shiftIntDown(objects, size, start, end, false);
      count -= size;
   }

   /**
    * Sets the reference of the buffer.
    * @param ints
    */
   public void set(Object[] objects) {
      this.objects = objects;
   }


   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "BufferT");
      dc.append(" #" + count);
      dc.append(':');
      for (int i = 0; i < count; i++) {
         dc.append("" + objects[i]);
         dc.append(' ');
      }
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "BufferT");
      dc.append(" #" + count);
      dc.append(':');
      for (int i = 0; i < count; i++) {
         dc.append("" + objects[i]);
         dc.append(' ');
      }
   }

   public UCtx toStringGetUCtx() {
      return uc;
   }

   //#enddebug

}
