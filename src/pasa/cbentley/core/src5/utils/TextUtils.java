/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.core.src5.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pasa.cbentley.core.src4.helpers.StringBBuilder;
import pasa.cbentley.core.src5.ctx.C5Ctx;

public class TextUtils {

   protected final C5Ctx c5;

   public TextUtils(C5Ctx c5) {
      this.c5 = c5;
   }

   public List<String> fullJustify(String[] words, int maxWidth) {
      int n = words.length;
      List<String> justifiedText = new ArrayList<String>();
      int currLineIndex = 0;
      int nextLineIndex = getNextLineIndex(currLineIndex, maxWidth, words);
      while (currLineIndex < n) {
         StringBuilder line = new StringBuilder();
         for (int i = currLineIndex; i < nextLineIndex; i++) {
            line.append(words[i] + " ");
         }
         currLineIndex = nextLineIndex;
         nextLineIndex = getNextLineIndex(currLineIndex, maxWidth, words);
         justifiedText.add(line.toString());
      }
      for (int i = 0; i < justifiedText.size() - 1; i++) {
         String fullJustifiedLine = getFullJustifiedString(justifiedText.get(i).trim(), maxWidth);
         justifiedText.remove(i);
         justifiedText.add(i, fullJustifiedLine);
      }
      String leftJustifiedLine = getLeftJustifiedLine(justifiedText.get(justifiedText.size() - 1).trim(), maxWidth);
      justifiedText.remove(justifiedText.size() - 1);
      justifiedText.add(leftJustifiedLine);
      return justifiedText;
   }

   public int getNextLineIndex(int currLineIndex, int maxWidth, String[] words) {
      int n = words.length;
      int width = 0;
      while (currLineIndex < n && width < maxWidth) {
         width += words[currLineIndex++].length() + 1;
      }
      if (width > maxWidth + 1)
         currLineIndex--;
      return currLineIndex;
   }

   public String getFullJustifiedString(String line, int maxWidth) {
      StringBuilder justifiedLine = new StringBuilder();
      String[] words = line.split(" ");
      int occupiedCharLength = 0;
      for (String word : words) {
         occupiedCharLength += word.length();
      }
      int remainingSpace = maxWidth - occupiedCharLength;
      int spaceForEachWordSeparation = words.length > 1 ? remainingSpace / (words.length - 1) : remainingSpace;
      int extraSpace = remainingSpace - spaceForEachWordSeparation * (words.length - 1);
      for (int j = 0; j < words.length - 1; j++) {
         justifiedLine.append(words[j]);
         for (int i = 0; i < spaceForEachWordSeparation; i++)
            justifiedLine.append(" ");
         if (extraSpace > 0) {
            justifiedLine.append(" ");
            extraSpace--;
         }
      }
      justifiedLine.append(words[words.length - 1]);
      for (int i = 0; i < extraSpace; i++)
         justifiedLine.append(" ");
      return justifiedLine.toString();
   }

   public String getLeftJustifiedLine(String line, int maxWidth) {
      int lineWidth = line.length();
      StringBuilder justifiedLine = new StringBuilder(line);
      for (int i = 0; i < maxWidth - lineWidth; i++)
         justifiedLine.append(" ");
      return justifiedLine.toString();
   }

   /**
    * Add space between words that are in the dictionary
    * 
    * Words that are not in the dictionnary are left as is
    * 
    * Borderline cases
    * <li> Empty dic returns input string
    * 
    * TODO O(dicSize) because we have to compute max word length.. usually we want a dic that computes
    * this information by construction.
    * 
    * @param inputWithNoSpaces
    * @param dictionary
    * @return
    */
   public String addSpaces(String inputWithNoSpaces, Set<String> dictionary) {
      if(inputWithNoSpaces == null || inputWithNoSpaces.equals("")) {
         return inputWithNoSpaces;
      }
      if(dictionary.size() == 0) {
         return inputWithNoSpaces;
      }
      int offsetStart = 0;
      StringBBuilder sb = new StringBBuilder(c5.getUC());
      int maxWordSize = getSizeWordMax(dictionary);
      if(maxWordSize == 0) {
         //we have a nasty dic with just an empty string!
         return inputWithNoSpaces;
      }
      boolean isStartUnknownWord = true;
      while (offsetStart < inputWithNoSpaces.length()) {
         //find the longest word first and go down
         int lastNumChars = inputWithNoSpaces.length() - offsetStart;
         int i = Math.min(lastNumChars, maxWordSize);
         boolean wasAdded = false;
         for (; i > 1; i--) {
            String str = inputWithNoSpaces.substring(offsetStart, offsetStart + i);
            boolean isWord = dictionary.contains(str);
            if(isWord) {
               if(sb.getCount() != 0) {
                  sb.append(' ');
               }
               sb.append(str);
               wasAdded = true;
               break; //out of for loop
            }
         }
         if(wasAdded) {
            isStartUnknownWord = true;
            offsetStart = offsetStart + i;
         } else {
            //check if not very start of string and if we are at the start of an unknown word
            if(sb.getCount() != 0 && isStartUnknownWord) {
               sb.append(' ');
            }
            //append letter in unknown word
            sb.append(inputWithNoSpaces.charAt(offsetStart));
            offsetStart = offsetStart + 1;
            isStartUnknownWord = false;
         }
      }
      if (sb.getCount() == 0) {
         return inputWithNoSpaces;
      } else {
         return sb.toString();
      }
   }
   
   public int getSizeWordMax(Set<String> dictionary) {
      int max = 0;
      for (String string : dictionary) {
         if(string.length() > max) {
            max = string.length();
         }
      }
      return max;
   }
}
