import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by grago on 17/11/15.
 */
public class EnvironmentVariablesExporter {

    public static void writeFile(String[] varList, String path) {

        PrintWriter writer = null;

        try {

            writer = new PrintWriter(path, "UTF-8");

            for (String variable : varList) {
                writer.println(variable);
            }

            writer.close();

        } catch (FileNotFoundException e) {//`
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
