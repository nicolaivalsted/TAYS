package dk.yousee.smp.order.model;

/**
 * Created by IntelliJ IDEA.
 * User: m26778
 * Date: 15-06-2010
 * Time: 07:58:03
 * Alive service. Can be used for alive testers at client sites.
 */
public interface AliveService  {

    /**
     * Default 2 seconds to ask this question as max.
     */
    public static final int OPERATION_TIMEOUT=2000;

    /**
     * Reads assigned connection timeout - the default value if not other assigned
     * @return ms
     */
    public Integer getConnectionTimeout();

    /**
     * Alter operation timeout, good in tests to get connection to fail.
     * @param operationTimeout ms
     */
    public void setOperationTimeout(Integer operationTimeout);

    /**
     * Reads assigned operation timeout - the default value if not other assigned
     * @return ms
     */
    public Integer getOperationTimeout();
    /**
     * Use this service see that bss-adapter can answer queries.
     * @return a string with the current time (on the server)
     * @deprecated use ping2 and use exception to realize that server is not alive
     */
    public String ping();
    /**
     * Use this service see that bss-adapter can process operations.
     * @return a string with information about current service
     * @throws Exception when service is not available. Exception message contains information
     */
    public String ping2() throws Exception;
}
