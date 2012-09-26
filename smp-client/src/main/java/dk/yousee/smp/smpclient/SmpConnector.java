package dk.yousee.smp.smpclient;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 31/07/12
 * Time: 11.09
 * A SmpConnector interface
 */
public interface SmpConnector {

    /**
     * Default 1 seconds for connection timeout
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT=1000;

    /**
     * Default 100 seconds to ask this question as max.
     */
    public static final int DEFAULT_OPERATION_TIMEOUT=100000;

    /**
     * Default no proxy host
     */
    public static final String DEFAULT_PROXY_HOST="none";
    /**
     * Default number of connections in pool
     */
    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS=10;

    String connectInfo();
}

