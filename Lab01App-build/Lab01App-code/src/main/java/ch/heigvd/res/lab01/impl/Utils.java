package ch.heigvd.res.lab01.impl;

import java.util.logging.Logger;

/**
 *
 * @author Olivier Liechti
 */
public class Utils {

   private static final Logger LOG = Logger.getLogger(Utils.class.getName());

   /**
    * This method looks for the next new line separators (\r, \n, \r\n) to extract
    * the next line in the string passed in arguments.
    *
    * @param lines a string that may contain 0, 1 or more lines
    * @return an array with 2 elements; the first element is the next line with the
    * line separator, the second element is the remaining text. If the argument does
    * not contain any line separator, then the first element is an empty string.
    */
   public static String[] getNextLine(String lines) {
      //throw new UnsupportedOperationException("The student has not implemented this method yet.");

      // cherche la première occurence de \r ou \n
      for (int i = 0; i < lines.toCharArray().length; i++) {
         char c = lines.toCharArray()[i];
         if (i + 1 < lines.toCharArray().length && c == '\r' && lines.toCharArray()[i + 1] == '\n') {
            return new String[]{
               lines.substring(0, i + 2),
               lines.substring(i + 2)};
         }
         if (c == '\r' || c == '\n') {
            return new String[]{
               lines.substring(0, i + 1),
               lines.substring(i + 1)};
         }

      }
      return new String[]{"", lines};
   }

}
