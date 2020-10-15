/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.core.src5.structs;

import java.lang.reflect.Array;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.memory.MemorySimpleCreator;
import pasa.cbentley.core.src4.utils.ArrayUtils;
import pasa.cbentley.core.src5.ctx.C5Ctx;

/**
 * Src 4 compatible for Strings only.
 * 
 * core.src5 has a BufferObject for generics.
 * 
 * @author Charles Bentley
 *
 */
public class BufferTStrong<T> implements IStringable {

   private int      count     = 0;

   private int      offset    = 0;

   /**
    * must be 1 or higher
    */
   private int      increment = 5;

   /**
    * index 0 starts at 0 and gives the last used value.
    * Therefore it also gives the number of elements
    * buffer size = ints.length - 1 - int[0]
    */
   private T[]      objects;

   private C5Ctx    c5;

   private Class<T> c;

   /**
    * 1,1
    */
   public BufferTStrong(C5Ctx uc, Class<T> c) {
      this(uc, c, 1, 1, 0);
   }

   public BufferTStrong(C5Ctx uc, Class<T> c, int num) {
      this(uc, c, num, 3, 0);
   }

   /**
    * 
    * @param startSize
    * @param increment few adds take a small increment., lots take a big increment
    */
   public BufferTStrong(C5Ctx uc, Class<T> c, int startSize, int increment, int bufferFront) {
      this.c5 = uc;
      this.c = c;
      this.increment = increment;
      this.offset = bufferFront;

      set(createNewArray(startSize));

   }

   private T[] createNewArray(int size) {
      @SuppressWarnings("unchecked")
      final T[] array = (T[]) Array.newInstance(c, size);
      return array;
   }

   /**
    * Remove all references of t.
    * @param t
    */
   public void removeAll(T t) {
      for (int i = 0; i < objects.length; i++) {
         
      }
   }
   /**
    * Uses the reference!
    * @param ar
    */
   public BufferTStrong(C5Ctx uc, Class<T> c, T[] ar) {
      this.c5 = uc;
      this.c = c;

      set(ar);
   }

   /**
    * append String to the {@link BufferTStrong}
    * @param str
    */
   public void add(T obj) {
      set(ensureCapacity(objects, count, increment));
      objects[offset + count] = obj;
      count += 1;
   }

   public void add(T obj1, T obj2) {
      set(ensureCapacity(objects, count + 2, increment));
      objects[offset + count] = obj1;
      objects[offset + count + 1] = obj2;
      count += 2;
   }

   private T[] ensureCapacity(T[] ar, int size, int grow) {
      if (ar == null) {
         return createNewArray(size + grow);
      } 
      if (size + grow < ar.length) {
         return ar;
      }
      Object[] oldData = ar;
      ar = createNewArray(size + grow);
      System.arraycopy(oldData, 0, ar, 0, oldData.length);
      return ar;
   }

   /**
    * Add the Object in front of the array
    * @param obj
    */
   public void addLeft(T obj) {
      if (offset == 0) {
         set(ensureCapacity(objects, count + 2, increment));
         //shift them all up by 2 and
         ArrayUtils.shiftUp(objects, 2, offset, offset + count - 1, false);
         offset += 2;
      }
      objects[offset - 1] = obj;
      count++;
   }

   public void appendBufferToArrayAt(T[] array, int offset) {
      int start = this.offset;
      int end = start + count;
      for (int j = start; j < end; j++) {
         array[offset] = objects[j];
         offset++;
      }
   }

   public void appendBufferToArrayAt(T[] array, int offset, int startOffset, int skip) {
      int start = this.offset;
      int end = start + count;
      for (int j = start; j < end; j++) {
         array[offset] = objects[j];
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
   public BufferTStrong cloneMe() {
      int size = objects.length;
      final T[] ar = createNewArray(size);
      BufferTStrong<T> ib = new BufferTStrong<T>(c5, c, ar);
      ib.increment = increment;
      ib.set(ar);
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
      return objects[offset + index];
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
   public T[] getClonedTrimmed() {
      int size = count;
      final T[] ar = createNewArray(size);
      for (int i = offset; i < offset + count; i++) {
         ar[i] = objects[i];
      }
      return ar;
   }

   /**
    * Returns the reference to the integer array. This mean the int[0] still means the number of elements.
    * <br>
    * <br>
    * A direct reference should be used with caution and is inherently unsafe unless
    * you know the implementation details of {@link BufferTStrong}
    * @return
    */
   public T[] getIntsRef() {
      return objects;
   }

   /**
    * Returns the last element in the {@link BufferTStrong}.
    * <br>
    * <br>
    * Does not remove it.
    * @return null if no elements
    */
   public T getLast() {
      if (count > 0) {
         return objects[offset + count - 1];
      }
      return null;
   }

   /**
    * Returns the last element in the {@link BufferTStrong}.
    * <br>
    * <br>
    * Does not remove it.
    * @return null if no elements
    */
   public T getFirst() {
      if (count > 0) {
         return objects[offset];
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
         T v = objects[offset];
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
    * Returns 0 when {@link BufferTStrong} is emptys
    * @return
    */
   public T removeLast() {
      if (count != 0) {
         T v = objects[offset + count - 1];
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
      ArrayUtils.shiftDown(objects, size, start, end, false);
      count -= size;
   }

   /**
    * Sets the reference of the buffer.
    * @param ints
    */
   public void set(T[] objects) {
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
      return c5.getUCtx();
   }

   //#enddebug

}
