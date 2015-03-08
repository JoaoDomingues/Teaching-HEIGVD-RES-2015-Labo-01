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

   public FileNumberingFilterWriter(Writer out) {
      super(out);
   }

   @Override
   public void write(String str, int off, int len) throws IOException {
      String[] tmp = new String[2];
      tmp[1] = str.substring(off, off + len);
      String toWrite = "";
      int nbAjout = 0;

      //vérifie si c'est la première fois que l'on écrit dans la sortie
      if (nbLine == 0) {
         nbLine++;
         nbAjout++;
         toWrite += String.valueOf(nbLine) + '\t';
      }

      while (true) {
         tmp = Utils.getNextLine(tmp[1]);
         if (tmp[0].isEmpty()) {
            break;
         }

         nbAjout++;
         nbLine++;

         toWrite += tmp[0] + String.valueOf(nbLine) + '\t';
      }
      toWrite += tmp[1];

      out.write(toWrite, 0, len + nbAjout * 2);
   }

   @Override
   public void write(char[] cbuf, int off, int len) throws IOException {
      write(new String(cbuf), off, len);
   }

   @Override
   public void write(int c) throws IOException {
      //laisse un doute quand un \r et rentré, car un \n peut être écris juste aprés
      if (c == '\r' && out.toString().toCharArray()[out.toString().length() - 1] != '\r') {
         out.write(c);
      } else {
         write((char) c + "", 0, 1);
      }
   }
}
