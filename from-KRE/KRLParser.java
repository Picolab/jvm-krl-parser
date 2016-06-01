import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class KRLParser {
  public static void main(String[] args) throws Exception {
    String file_path = args[0];

    String src = readFile(new File(file_path));

    Antlr_ a = new Antlr_();

    System.out.println(a.ruleset(src));
  }

  private static String readFile(File f) throws IOException {
    return new Scanner(f).useDelimiter("\\A").next();
  }
}
