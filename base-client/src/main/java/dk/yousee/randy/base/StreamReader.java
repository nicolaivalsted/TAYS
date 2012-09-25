package dk.yousee.randy.base;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 13/08/12
 * Time: 22.13
 * Reads a stream
 */
public class StreamReader {

    public String readInputStreamAsString(InputStream is) throws Exception {
        StringBuilder out = new StringBuilder();
        final char[] buffer = new char[0x10000];
        Reader in = new InputStreamReader(is, "UTF-8");
        try {
            int read;
            do {
                read = in.read(buffer, 0, buffer.length);
                if (read > 0) {
                    out.append(buffer, 0, read);
                }
            } while (read >= 0);
        } finally {
            in.close();
        }
        return out.toString();
    }

}
