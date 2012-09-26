package dk.yousee.smp.com;

import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityParamListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.GetEntityByKeyExceptionDocument;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamListType;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamType;
import com.sigmaSystems.schemas.x31.smpCommonValues.SmpQueryValue;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.GetServiceByKeyExceptionDocument;
import com.sun.java.products.oss.xml.common.ArrayOfManagedEntityValue;
import com.sun.java.products.oss.xml.common.ArrayOfString;
import com.sun.java.products.oss.xml.common.ManagedEntityValue;
import com.sun.java.products.oss.xml.common.QueryManagedEntitiesRequestDocument;
import com.sun.java.products.oss.xml.common.QueryManagedEntitiesResponseDocument;
import com.sun.java.products.oss.xml.serviceActivation.GetOrderByKeyExceptionDocument;
import com.sun.java.products.oss.xml.serviceActivation.QueryOrdersExceptionDocument;
import dk.yousee.smp.order.model.Constants;
import dk.yousee.smp.order.model.CustomerInfo;
import dk.yousee.smp.order.model.SearchCustomersRequest;
import dk.yousee.smp.order.model.SearchCustomersResponse;
import dk.yousee.smp.order.model.SmpXml;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 01/06/12
 * Time: 21.30
 * Search for customer
 */
public class FindSubscriberCom extends SmpCom<SearchCustomersRequest, SearchCustomersResponse> {

    @Override
    protected String convertRequest(SearchCustomersRequest searchCustomersRequest) {
        XmlObject xmlOrder = searchCustomersXmlQueryOrder(searchCustomersRequest);
        return xmlOrder.toString();
    }

    private XmlObject searchCustomersXmlQueryOrder(SearchCustomersRequest searchCustomersRequest) {
        return searchSubscriberMultiple(searchCustomersRequest);
    }

    private static final String SERVICE_PHONE = "service_phone";

    private QueryManagedEntitiesRequestDocument searchSubscriberMultiple(SearchCustomersRequest searchCustomersRequest) {

        SmpQueryValue queryValue;
        if (searchCustomersRequest.getVoipPhoneNumber() != null) {
            queryValue = byServicePhoneQuery(searchCustomersRequest);
        } else {
            queryValue = basicSearchQuery(searchCustomersRequest);
        }
        QueryManagedEntitiesRequestDocument requestDocument = QueryManagedEntitiesRequestDocument.Factory.newInstance();
        QueryManagedEntitiesRequestDocument.QueryManagedEntitiesRequest request = requestDocument.addNewQueryManagedEntitiesRequest();
        request.setQuery(queryValue);
        ArrayOfString arrayOfString = ArrayOfString.Factory.newInstance(); // Fields to get back as result-set.
        arrayOfString.addItem("acct");
        arrayOfString.addItem(Constants.FIRST_NAME);
        arrayOfString.addItem(Constants.LAST_NAME);
        arrayOfString.addItem("address1");
        arrayOfString.addItem("address2");
        arrayOfString.addItem("zipcode");
        arrayOfString.addItem("country");
        arrayOfString.addItem("district");
        arrayOfString.addItem("ntd_return_segment");
        arrayOfString.addItem("city");
        arrayOfString.addItem("status");
        request.setAttrNames(arrayOfString);
        return requestDocument;
    }

    private SmpQueryValue basicSearchQuery(SearchCustomersRequest searchCustomersRequest) {
        SmpQueryValue queryValue;
        queryValue = SmpQueryValue.Factory.newInstance();
        queryValue.setQueryName("BasicDataSearch");
        ParamListType paramList = queryValue.addNewParamList();
        if (searchCustomersRequest.getKundeId() != null && searchCustomersRequest.getKundeId().toString() != null) {
            ParamType param = paramList.addNewParam();
            param.setName("acct");
            param.setStringValue(searchCustomersRequest.getKundeId().toString());
        }
        if (searchCustomersRequest.getCpe_mac() != null) {
            ParamType param = paramList.addNewParam();
            param.setName("cpe_mac");
            param.setStringValue(searchCustomersRequest.getCpe_mac());
        }
        if (searchCustomersRequest.getCm_mac() != null) {
            ParamType param = paramList.addNewParam();
            param.setName("cm_mac");
            param.setStringValue(searchCustomersRequest.getCm_mac());
        }
        if (searchCustomersRequest.getFornavn() != null) {
            ParamType param = paramList.addNewParam();
            param.setName(Constants.FIRST_NAME);
            param.setStringValue(searchCustomersRequest.getFornavn());
        }
        if (searchCustomersRequest.getEfternavn() != null) {
            ParamType param = paramList.addNewParam();
            param.setName(Constants.LAST_NAME);
            param.setStringValue(searchCustomersRequest.getEfternavn());
        }
        if (searchCustomersRequest.getCity() != null) {
            ParamType param = paramList.addNewParam();
            param.setName("city");
            param.setStringValue(searchCustomersRequest.getCity());
        }
        if (searchCustomersRequest.getZipcode() != null) {
            ParamType param = paramList.addNewParam();
            param.setName("zipcode");
            param.setStringValue(searchCustomersRequest.getZipcode());
        }
        if (searchCustomersRequest.getAddress1() != null) {
            ParamType param = paramList.addNewParam();
            param.setName("address1");
            param.setStringValue(searchCustomersRequest.getAddress1());
        }
        if (searchCustomersRequest.getAddress2() != null) {
            ParamType param = paramList.addNewParam();
            param.setName("address2");
            param.setStringValue(searchCustomersRequest.getAddress2());
        }
        if (searchCustomersRequest.getDistrict() != null) {
            ParamType param = paramList.addNewParam();
            param.setName("district");
            param.setStringValue(searchCustomersRequest.getDistrict());
        }
        return queryValue;
    }

    private SmpQueryValue byServicePhoneQuery(SearchCustomersRequest searchCustomersRequest) {
        SmpQueryValue queryValue;
        queryValue = SmpQueryValue.Factory.newInstance();
        queryValue.setQueryName("byServicePhone");
        ParamListType paramList = queryValue.addNewParamList();
        if (searchCustomersRequest.getVoipPhoneNumber() != null) {
            ParamType param = paramList.addNewParam();
            param.setName(SERVICE_PHONE);
            param.setStringValue(searchCustomersRequest.getVoipPhoneNumber());
        }
        return queryValue;
    }


    private static final Logger logger = Logger.getLogger(FindSubscriberCom.class);


    @Override
    public Integer getOperationTimeout() {
        return 50000;
    }

    @Override
    protected SearchCustomersResponse convertResponse(SmpXml xml, SearchCustomersRequest input) {
        Parser parser=new Parser();
        return parser.convertResponse(xml);
    }


    private static class Parser extends ResponseParser<SearchCustomersResponse> {
        @Override
        public SearchCustomersResponse convertResponse(SmpXml xml) {
            XmlObject xmlObject = parseResponse(xml.getResponse());

            /* ERROR HANDLING */
            XmlObject[] res = xmlObject.selectPath("declare namespace smpce='http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema'; $this//smpce:getEntityByKeyException");
            String errorMessage = null;
            if (res.length > 0) {
                // This is an error
                GetEntityByKeyExceptionDocument.GetEntityByKeyException ex = (GetEntityByKeyExceptionDocument.GetEntityByKeyException) res[0];
                if (ex.getRemoteException() != null) errorMessage = ex.getRemoteException().getMessage();
                else if (ex.getObjectNotFoundException() != null)
                    errorMessage = ex.getObjectNotFoundException().getMessage();
                else if (ex.getIllegalArgumentException() != null)
                    errorMessage = ex.getIllegalArgumentException().getMessage();

            }
            res = xmlObject.selectPath("declare namespace smpsa='http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema'; $this//smpsa:getServiceByKeyException");
            if (res.length > 0) {
                // This is an error
                GetServiceByKeyExceptionDocument.GetServiceByKeyException ex = (GetServiceByKeyExceptionDocument.GetServiceByKeyException) res[0];
                if (ex.getRemoteException() != null) errorMessage = ex.getRemoteException().getMessage();
                else if (ex.getObjectNotFoundException() != null)
                    errorMessage = ex.getObjectNotFoundException().getMessage();
                else if (ex.getIllegalArgumentException() != null)
                    errorMessage = ex.getIllegalArgumentException().getMessage();

            }

            res = xmlObject.selectPath("declare namespace smpce='http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema'; $this//smpce:getOrderByKeyException");
            if (res.length > 0) {
                // This is an error
                GetOrderByKeyExceptionDocument.GetOrderByKeyException ex = (GetOrderByKeyExceptionDocument.GetOrderByKeyException) res[0];
                if (ex.getRemoteException() != null) errorMessage = ex.getRemoteException().getMessage();
                else if (ex.getObjectNotFoundException() != null)
                    errorMessage = ex.getObjectNotFoundException().getMessage();
                else if (ex.getIllegalArgumentException() != null)
                    errorMessage = ex.getIllegalArgumentException().getMessage();
            }

            res = xmlObject.selectPath("declare namespace smpce='http://java.sun.com/products/oss/xml/ServiceActivation'; $this//smpce:queryOrdersException");
            if (res.length > 0) {
                // This is an error
                QueryOrdersExceptionDocument.QueryOrdersException ex = (QueryOrdersExceptionDocument.QueryOrdersException) res[0];
                if (ex.getRemoteException() != null) errorMessage = ex.getRemoteException().getMessage();
                else if (ex.getIllegalArgumentException() != null)
                    errorMessage = ex.getIllegalArgumentException().getMessage();
            }
            if (errorMessage != null) {
                return new SearchCustomersResponse(errorMessage, xml);
            } else {

                /* PARSE RESULTS */
                res = xmlObject.selectPath("declare namespace cmn='http://java.sun.com/products/oss/xml/Common'; $this//cmn:queryManagedEntitiesResponse");
                logger.debug("Select queryManagedEntitiesResponse, res length: " + res.length);
                // parse the
                if (res.length > 0) {
                    ArrayList<CustomerInfo> list = new ArrayList<CustomerInfo>();
                    QueryManagedEntitiesResponseDocument responseDocument = (QueryManagedEntitiesResponseDocument) xmlObject;
                    QueryManagedEntitiesResponseDocument.QueryManagedEntitiesResponse entity = responseDocument.getQueryManagedEntitiesResponse();
                    if (entity != null) {
                        ArrayOfManagedEntityValue arrayOfManagedEntityValue = entity.getValue();
                        if (arrayOfManagedEntityValue != null) {
                            for (ManagedEntityValue managedEntityValue : arrayOfManagedEntityValue.getItemArray()) {
                                if (managedEntityValue instanceof EntityType) {
                                    EntityType entityType = (EntityType) managedEntityValue;
                                    EntityParamListType entityParamListType = entityType.getParamList();
                                    if (entityParamListType != null) {
                                        ParamType[] paramTypes = entityParamListType.getParamArray();
                                        CustomerInfo customerInfo = convertXml2customerInfo(paramTypes);
                                        list.add(customerInfo);
                                    }
                                }
                            }
                        }
                    }
                    return new SearchCustomersResponse(list, xml);

                } else {
                    return new SearchCustomersResponse("queryManagedEntitiesResponse not found in xml", xml);
                }
            }
        }

        /**
         * Convert result set into response object
         *
         * @param paramTypes list of qyery name value pairs from xml
         * @return vo with one line in result set
         */
        private CustomerInfo convertXml2customerInfo(ParamType[] paramTypes) {
            CustomerInfo ci = new CustomerInfo();
            for (ParamType paramType : paramTypes) {
                String key = paramType.getName();
                String value = paramType.getStringValue();
                if (key != null) {
                    if (key.equalsIgnoreCase("acct")) {
                        ci.setAcct(value);
                    } else if (key.equalsIgnoreCase(Constants.FIRST_NAME)) {
                        ci.setFirst_name(value);
                    } else if (key.equalsIgnoreCase(Constants.LAST_NAME)) {
                        ci.setLast_name(value);
                    } else if (key.equalsIgnoreCase("address1")) {
                        ci.setAddress1(value);
                    } else if (key.equalsIgnoreCase("address2")) {
                        ci.setAddress2(value);
                    } else if (key.equalsIgnoreCase("city")) {
                        ci.setCity(value);
                    } else if (key.equalsIgnoreCase("zipcode")) {
                        ci.setZipcode(value);
                    } else if (key.equalsIgnoreCase("district")) {
                        ci.setDistrict(value);
                    } else if (key.equalsIgnoreCase("ntd_return_segment")) {
                        ci.setNtd_return_segment(value);
                    } else if (key.equalsIgnoreCase("status")) {
                        ci.setStatus(value);
                    }
                }
            }
            return ci;
        }
    }
}
