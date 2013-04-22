/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.filters.monitoring;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author jablo
 */
public class GraphiteConnection {
    private final Socket graphiteSocket;
    private final DataOutputStream outToServer;

    public GraphiteConnection(InetAddress host, int port) throws IOException {
        graphiteSocket = new Socket(host, port);
        outToServer = new DataOutputStream(graphiteSocket.getOutputStream());
    }

    public void sendData(String graph, double data) throws IOException {
        long timestamp = (new Date()).getTime() / 1000;
        outToServer.writeBytes(graph + " " + data + " " + timestamp + '\n');
    }

    public void close() throws IOException {
        outToServer.close();
        graphiteSocket.close();
    }
}
