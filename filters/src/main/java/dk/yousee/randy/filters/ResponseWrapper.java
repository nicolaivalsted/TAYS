package dk.yousee.randy.filters;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Jacob Lorensen, YouSee, 2012-06-15
 */
public class ResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayOutputStream output;

    @Override
    public String toString() {
        return output.toString();
    }

    public ResponseWrapper(HttpServletResponse response, ByteArrayOutputStream output) {
        super(response);
        this.output = output;
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(output);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public void write(int i) throws IOException {
                output.write(i);
            }
        };
    }
}