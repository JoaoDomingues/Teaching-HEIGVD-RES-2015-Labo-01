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

      //vérifie si c'est la première fois que l'on écrit dans la sortie
      if (nbLine == 0) {
         toWrite += String.valueOf(++nbLine) + '\t';
      }

      //cherche tout les sauts de ligne
      while (!(tmp = Utils.getNextLine(tmp[1]))[0].isEmpty()) {
         toWrite += tmp[0] + String.valueOf(++nbLine) + '\t';
      }
      //ajoute la chaîne restante
      toWrite += tmp[1];

      out.write(toWrite);
   }

   @Override
   public void write(char[] cbuf, int off, int len) throws IOException {
      write(new String(cbuf), off, len);
   }

   @Override
   public void write(int c) throws IOException {
      //laisse un doute quand un \r et rentré, car un \n peut être écris juste aprés
      if (out.toString().length() > 0 && c == '\r' && out.toString().charAt(out.toString().length() - 1) != '\r') {
         out.write(c);
      //si le caractère suivant est différent \n et que le précdédant était un \r
      } else if(out.toString().length() > 0 && c != '\n' && out.toString().charAt(out.toString().length() - 1) == '\r'){
         out.write(nbLine + '\t' + (char) c); //indiquer numéro de ligne
      } else {
         write(String.valueOf((char)c)); //écris normalement
      }
   }
}
