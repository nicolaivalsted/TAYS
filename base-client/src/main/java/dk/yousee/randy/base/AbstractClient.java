package dk.yousee.randy.base;

import org.apache.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 06/09/12
 * Time: 23.32
 * Clients extends this
 */
public abstract class AbstractClient<CONNECTOR extends AbstractConnector> {


    private Integer operationTimeout;

    public Integer getOperationTimeout() {
        return operationTimeout;
    }

    public void setOperationTimeout(Integer operationTimeout) {
        this.operationTimeout = operationTimeout;
    }

    private CONNECTOR connector;

    public CONNECTOR getConnector() {
        return connector;
    }

    public void setConnector(CONNECTOR connector) {
        this.connector = connector;
        this.setOperationTimeout(connector.getOperationTimeout());
    }


    protected String parseInputStream(InputStream is) throws Exception {
        StreamReader sr = new StreamReader();
        String res;
        res = sr.readInputStreamAsString(is);
        return res;
    }


    protected String readResponse(HttpEntity entity) {
        InputStream is = null;
        String res;
        try {
            is = entity.getContent();
            res = parseInputStream(is);
        } catch (Throwable e) {
            throw new RuntimeException("Tried to read content and parse it", e);
        } finally {
            close(is);
        }
        return res;
    }

    protected void close(InputStream is) {
        if (is != null) try {
            is.close();
        } catch (IOException e) {
            System.out.println("AbstractClient, unexptected could not close input stream, message: "+ e.getMessage());
        }
    }

}
