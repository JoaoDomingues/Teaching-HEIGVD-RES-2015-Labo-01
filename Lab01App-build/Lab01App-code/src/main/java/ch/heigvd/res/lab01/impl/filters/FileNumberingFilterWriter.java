package ch.heigvd.res.lab01.impl.filters;

import ch.heigvd.res.lab01.impl.Utils;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer. When
 * filter encounters a line separator, it sends it to the decorated writer. It then
 * sends the line number and a tab character, before resuming the write process.
 *
 * Hello\nWorld -> 1\tHello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

   private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());
   private int nbLine;
   private boolean isLastBackslashR = false;

   public FileNumberingFilterWriter(Writer out) {
      super(out);

   }

   @Override
   public void write(String str, int off, int len) throws IOException {
      String[] lines = new String[2];
      lines[1] = str.substring(off, off + len);
      String toWrite = "";

      //vérifie si c'est la première fois que l'on écrit dans la sortie
      if (nbLine == 0) {
         toWrite += String.valueOf(++nbLine) + '\t';
      }

      //cherche tout les sauts de ligne
      while (!(lines = Utils.getNextLine(lines[1]))[0].isEmpty()) {
         toWrite += lines[0] + String.valueOf(++nbLine) + '\t';
      }
      //ajoute la chaîne restante
      toWrite += lines[1];

      out.write(toWrite);
   }

   @Override
   public void write(char[] cbuf, int off, int len) throws IOException {
      write(new String(cbuf), off, len);
   }

   @Override
   public void write(int c) throws IOException {
      //laisse un doute quand un \r et rentré, car un \n peut être écris juste aprés
      if (c == '\r' && !isLastBackslashR) {
         isLastBackslashR = true;
         out.write(c);
      //si le caractère suivant est différent \n et que le précdédant était un \r
      } else if (c != '\n' && isLastBackslashR) {
         isLastBackslashR = false;
         out.write(String.valueOf(++nbLine) + '\t' + (char) c); //indiquer numéro de ligne
      } else {
         isLastBackslashR = false;
         write(String.valueOf((char) c)); //écris normalement
      }
   }
}
