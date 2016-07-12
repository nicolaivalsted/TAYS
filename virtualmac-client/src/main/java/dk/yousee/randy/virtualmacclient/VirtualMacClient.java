package dk.yousee.randy.virtualmacclient;

import com.sun.jersey.api.client.WebResource;
import dk.yousee.randy.virtualmacclient.OKResponse;
import dk.yousee.randy.virtualmacclient.VirtualMacAllocationVO;
import dk.yousee.randy.virtualmacclient.VirtualMacAllocationVOes;
import java.util.List;

/**
 * Virtual Mac address service client.
 * Contains methods to interact with the virtual mac REST service providing
 * mac addresses for "delete" equipment use case in SMP. TAYS-???
 * @author Jacob Lorensen, YouSee, 2011-12-03
 */
public class VirtualMacClient {
    VirtualMacAllocationService_JerseyClient client;

    public VirtualMacClient(String endPoint) {
        client = new VirtualMacAllocationService_JerseyClient(endPoint);
    }

    /**
     * Produce a number of YouSee-network-unique mac addresses to be used for a specific subscriber.
     * In the underlying database the mac addresses are registered to the supplied subscriber, enabling
     * an automatic cleaning/release periodic cycle to run.
     * <p>
     * This method does not do any html quoting or other security checks on the supplied parameter
     * for subscriber&mdash;this should be fixed to avoid potential security risks.
     * @param subscriber the subscriber to whom these mac addresses will be used
     * @param n number of mac addresses to allocate
     * @return a list of VirtualMacAllocationVO objects containing the mac address.
     */
    public List<VirtualMacAllocationVO> allocVirtualMac(String subscriber, int n) {
        return ((VirtualMacAllocationVOes) client.allocateMac(
                VirtualMacAllocationVOes.class,
                "subscriber=" + subscriber + "&count=" + n)).getVirtualMacAllocationVO();
    }

    public String alive() {
        return client.getAlive_XML(OKResponse.class).getMessage();
    }

    /**
     * Auto-generated Jersey client stub
     */
    static class VirtualMacAllocationService_JerseyClient {
        private com.sun.jersey.api.client.WebResource webResource;
        private com.sun.jersey.api.client.Client client;
        private final String BASE_URI;

        public VirtualMacAllocationService_JerseyClient(String baseUri) {
            BASE_URI = baseUri;
            com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
            client = com.sun.jersey.api.client.Client.create(config);
            webResource = client.resource(BASE_URI);
        }

        public <T> T getMacStatus_XML(Class<T> responseType, String mac) throws com.sun.jersey.api.client.UniformInterfaceException {
            WebResource resource = webResource;
            resource = resource.path(java.text.MessageFormat.format("mac/{0}", new Object[]{mac}));
            return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        }

        public <T> T getMacStatus_JSON(Class<T> responseType, String mac) throws com.sun.jersey.api.client.UniformInterfaceException {
            WebResource resource = webResource;
            resource = resource.path(java.text.MessageFormat.format("mac/{0}", new Object[]{mac}));
            return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        }

        public <T> T allocateMac(Class<T> responseType, Object requestEntity) throws com.sun.jersey.api.client.UniformInterfaceException {
            return webResource.path("allocate").type(javax.ws.rs.core.MediaType.TEXT_PLAIN).put(responseType, requestEntity);
        }

        public <T> T getVirtualMacAllocations_XML(Class<T> responseType) throws com.sun.jersey.api.client.UniformInterfaceException {
            WebResource resource = webResource;
            resource = resource.path("allocations");
            return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        }

        public <T> T getVirtualMacAllocations_JSON(Class<T> responseType) throws com.sun.jersey.api.client.UniformInterfaceException {
            WebResource resource = webResource;
            resource = resource.path("allocations");
            return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        }

        public void releaseMac(Object requestEntity) throws com.sun.jersey.api.client.UniformInterfaceException {
            webResource.path("release").type(javax.ws.rs.core.MediaType.TEXT_PLAIN).delete(requestEntity);
        }

        public <T> T getVirtualMacs_XML(Class<T> responseType) throws com.sun.jersey.api.client.UniformInterfaceException {
            WebResource resource = webResource;
            resource = resource.path("all");
            return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        }

        public <T> T getVirtualMacs_JSON(Class<T> responseType) throws com.sun.jersey.api.client.UniformInterfaceException {
            WebResource resource = webResource;
            resource = resource.path("all");
            return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        }

        public <T> T getStatistics_XML(Class<T> responseType) throws com.sun.jersey.api.client.UniformInterfaceException {
            WebResource resource = webResource;
            resource = resource.path("statistics");
            return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        }

        public <T> T getStatistics_JSON(Class<T> responseType) throws com.sun.jersey.api.client.UniformInterfaceException {
            WebResource resource = webResource;
            resource = resource.path("statistics");
            return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        }

        public <T> T getAlive_XML(Class<T> responseType) throws com.sun.jersey.api.client.UniformInterfaceException {
            WebResource resource = webResource;
            resource = resource.path("alive");
            return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        }

        public void close() {
            client.destroy();
        }
    }
}
