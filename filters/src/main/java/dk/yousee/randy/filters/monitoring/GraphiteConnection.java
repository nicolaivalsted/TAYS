/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.filters.monitoring;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author jablo
 */
public class GraphiteConnection {
    private final static int timeoutMillis = 3000;
    private final Socket graphiteSocket;
    private final DataOutputStream graphite;

    public GraphiteConnection(InetAddress host, int port) throws IOException {
        graphiteSocket = new Socket();
        graphiteSocket.connect(new InetSocketAddress(host, port), timeoutMillis);
        graphite = new DataOutputStream(graphiteSocket.getOutputStream());
    }

    public void sendData(String graph, double data) throws IOException {
        long timestamp = (new Date()).getTime() / 1000;
        graphite.writeBytes(graph + " " + data + " " + timestamp + '\n');
    }

    public void close() throws IOException {
        graphite.close();
        graphiteSocket.close();
    }
}
