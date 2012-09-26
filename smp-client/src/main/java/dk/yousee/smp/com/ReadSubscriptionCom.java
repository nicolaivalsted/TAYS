package dk.yousee.smp.com;

import com.sigmaSystems.schemas.x31.smpCBECoreSchema.AssocListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.AssocType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityKeyListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityKeyType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityParamListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.GetEntityByKeyExceptionDocument;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.GetEntityByKeyRequestDocument;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.GetEntityByKeyResponseDocument;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubAddressType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubContactType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcKeyType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcType;
import com.sigmaSystems.schemas.x31.smpCommonValues.NamedQuery;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamListType;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamType;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.GetServiceByKeyExceptionDocument;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.SubSvcStateType;
import com.sun.java.products.oss.xml.cbe.core.EntityValue;
import com.sun.java.products.oss.xml.common.ManagedEntityKey;
import com.sun.java.products.oss.xml.serviceActivation.GetOrderByKeyExceptionDocument;
import com.sun.java.products.oss.xml.serviceActivation.QueryOrdersExceptionDocument;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.Response;
import dk.yousee.smp.order.model.ResponseAssociation;
import dk.yousee.smp.order.model.ResponseEntity;
import dk.yousee.smp.order.model.ResponseEntityLevel;
import dk.yousee.smp.order.model.SmpXml;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 01/06/12
 * Time: 23.51
 * Read subscription based on subscriber Id
 */
public class ReadSubscriptionCom extends SmpCom<Acct, Response> {

    @Override
    protected String convertRequest(Acct acct) {
        return new Pack().generateRequest(acct);
    }

    private static class Pack extends RequestPack<Acct> {
        @Override
        public XmlObject createXml(Acct acct) {
            GetEntityByKeyRequestDocument queryDoc = GetEntityByKeyRequestDocument.Factory.newInstance();
            GetEntityByKeyRequestDocument.GetEntityByKeyRequest getEntityrequest = queryDoc.addNewGetEntityByKeyRequest();
            getEntityrequest.setCascadeLoading(true);

            NamedQuery namedQuery = NamedQuery.Factory.newInstance();
            namedQuery.setQueryName("get_id_by_parm");
            ParamListType paramList = namedQuery.addNewInputParamList();
            ParamType param = paramList.addNewParam();
            param.setName("acct");
            param.setStringValue(acct.toString());


            EntityKeyType entKey = EntityKeyType.Factory.newInstance();
            entKey.setType("SubSpec:-");
            entKey.setNameQuery(namedQuery);

            EntityKeyListType entityKeyList = getEntityrequest.addNewEntityKeyLst();
            entityKeyList.addNewEntityKey().set(entKey);
            return queryDoc;
        }
    }

    @Override
    protected Response convertResponse(SmpXml xml, Acct acct) {
        Parser parser = new Parser();
        return parser.convertResponse(xml, acct);
    }

    @Override
    public Integer getOperationTimeout() {
        return 2000;
    }

    static class Parser extends ResponseParser<Response> {

        private static final Logger logger = Logger.getLogger(Parser.class);
        private static final ServiceStateTypeConverter sstc = new ServiceStateTypeConverter();

        @Override
        public Response convertResponse(SmpXml xml) {
            throw new IllegalStateException("unused method");
        }

        private Response convertResponse(SmpXml xml, Acct acct) {
            String errorMessage = null;
            XmlObject xmlObject;
            xmlObject = parseResponse(xml.getResponse());

            /* ERROR HANDLING */
            XmlObject[] res = xmlObject.selectPath("declare namespace smpce='http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema'; $this//smpce:getEntityByKeyException");
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

//            if (ex.getRemoteException() != null) reply.setErrorMessage(ex.getRemoteException().getMessage());
//            else if (ex.getIllegalArgumentException() != null)
//                reply.setErrorMessage(ex.getIllegalArgumentException().getMessage());
            }

            /* PARSE RESULTS */
            res = xmlObject.selectPath("declare namespace smpce='http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema'; $this//smpce:getEntityByKeyResponse");
            logger.debug("Select getEntityByKeyResponse, res length: " + res.length);
            // parse the
            ResponseEntity smp = null;
            if (res.length > 0) {
                GetEntityByKeyResponseDocument doc = (GetEntityByKeyResponseDocument) xmlObject;
                GetEntityByKeyResponseDocument.GetEntityByKeyResponse entity = doc.getGetEntityByKeyResponse();
                smp = parseSmpRoot(entity);
            }
            Response reply;
            reply = new Response(acct, null, xml, errorMessage, smp);
            return reply;
        }

        ResponseEntity parseSmpRoot(final GetEntityByKeyResponseDocument.GetEntityByKeyResponse entity) {
            ResponseEntity smp = new ResponseEntity();
            smp.setLevel(ResponseEntityLevel.TOP);

            List<ResponseEntity> almostServicePlans = new ArrayList<ResponseEntity>();
            if (entity != null) {

                for (EntityValue value : entity.getEntityValueArray()) {
                    if (value instanceof SubType) {
                        SubType subType = (SubType) value;
                        //todo subType.getServiceProvider();
                        smp.setExternalKey(subType.getKey().getExternalKey());
                        EntityListType entListType = subType.getEntityList();
//                    Subscriber subscriber = new Subscriber();
//                    subscriber.setEksisterendeKunde(true);
//                    subscriber.setKundeId(subType.getKey().getExternalKey());
//                    subscriber.setInternId(String.valueOf(subType.getKey().getPrimaryKey()));
                        for (EntityValue entityValue : entListType.getEntityValueArray()) {
                            ResponseEntity dataChild;
                            dataChild = parseEntityValue(entityValue);
                            if (dataChild != null) almostServicePlans.add(dataChild);
                        }
                    } else {
                        logger.warn("Response EntityValue was not instanceof SubType");
                    }
                }
            }
            smp.setEntities(almostServicePlans);
            return smp;
        }


        public ResponseEntity parseEntityValue(EntityValue entVal) {
            ResponseEntity res = new ResponseEntity();
//        logger.debug("EntVal is of class: " + entVal.getClass().toString());
            if (entVal instanceof SubContactType) { // service activation schema
                SubContactType subKey = (SubContactType) entVal;
                res.setLevel(ResponseEntityLevel.CONTACT);
                EntityKeyType entKey = subKey.getKey();
                res.setValue(new OrderDataType(String.valueOf(entKey.getType())));
                res.setState(sstc.find(subKey.getState()));
                res.setExternalKey(entKey.getExternalKey());
                EntityParamListType entParamList = subKey.getParamList();
                res.setParams(convertList2map(entParamList));
//            logger.debug("Contactof type: " + entKey.getType());
                res.setAssociations(parseAssociations(subKey.getAssociationList()));

            } else if (entVal instanceof SubAddressType) {
                SubAddressType subKey = (SubAddressType) entVal;
                res.setLevel(ResponseEntityLevel.ADDRESS);
                res.setState(sstc.find(subKey.getState()));
                res.setValue(new OrderDataType(subKey.getKey().getType()));
                res.setExternalKey(subKey.getKey().getExternalKey());
                EntityParamListType entParamList = subKey.getParamList();
                res.setParams(convertList2map(entParamList));
                res.setAssociations(parseAssociations(subKey.getAssociationList()));

            } else if (entVal instanceof SubSvcType) {
//            logger.debug("SubSvcType");
                SubSvcType subKey = (SubSvcType) entVal;
                res.setLevel(ResponseEntityLevel.SERVICE);
                res.setState(sstc.find((SubSvcStateType) subKey.xgetServiceState()));
                res.setValue(new OrderDataType(subKey.getServiceKey().getType()));
                res.setExternalKey(subKey.getServiceKey().getExternalKey());
                SubSvcKeyType parentKey = subKey.getParentServiceKey(); // link back to parent service
                if (parentKey != null) {
                    String parentExtKey = parentKey.getExternalKey();
                    if (parentExtKey != null) {
                        logger.debug("Parent service key");
//                    OrderData parentKeyData = OrderHelper.createService(null, parentExtKey, Constants.SERVICE_TYPE_PARENT_SERVICE_KEY);
                        // returnData.addChild(parentKeyData);
                    }
                }
                EntityParamListType entParamList = subKey.getParamList();
//            logger.debug("Subsvctype of type: " + res.getType() + " with key: " + res.getExternalKey());
                res.setParams(convertList2map(entParamList));
                res.setAssociations(parseAssociations(subKey.getAssociationList()));
                SubSvcType.ChildServiceList childserviceList = subKey.getChildServiceList();
                res.getEntities().addAll(parseChildServiceList(childserviceList));
            } else {
                logger.warn("Unknown entity value");
            }
            return (res);
        }

        /**
         * @param associationList xml element with associations
         * @return a list of all associations, null if none
         */
        private List<ResponseAssociation> parseAssociations(AssocListType associationList) {
            List<ResponseAssociation> res = null;
            if (associationList != null && associationList.sizeOfAssociationArray() != 0) {
                res = new ArrayList<ResponseAssociation>();
                for (AssocType one : associationList.getAssociationArray()) {
                    ResponseAssociation ra = new ResponseAssociation();
                    ra.setAssociationType(one.getAssociationType());
                    ManagedEntityKey zxx = one.getZEndKey();
                    ra.setType(new OrderDataType(zxx.getType()));
                    String oneTxt = one.toString();
                    ra.setPrimaryKey(grepPrimaryKey(oneTxt));
                    res.add(ra);
                }
            }
            return res;
        }

        static String grepPrimaryKey(final String source) {
            final String key = "primaryKey>";
            int pos0 = source.indexOf(key);
            if (pos0 == -1) return null;
            pos0 = pos0 + key.length();
            int pos1 = source.indexOf("<", pos0);
            if (pos1 == -1) return null;
            return source.substring(pos0, pos1);
        }

        public Map<String, String> convertList2map(EntityParamListType paramL) {
            Map<String, String> params = new TreeMap<String, String>();
            if (paramL != null) {
                for (ParamType param : paramL.getParamArray()) {
                    if (param.getStringValue() != null) {
                        params.put(param.getName(), param.getStringValue());
                    }
                }
            }
            return params;
        }

        public List<ResponseEntity> parseChildServiceList(SubSvcType.ChildServiceList childserviceList) {
            List<ResponseEntity> dataList = new ArrayList<ResponseEntity>();
            if (childserviceList != null) {
                for (SubSvcType s : childserviceList.getServiceValueArray()) {
                    ResponseEntity dataItem;
                    dataItem = new ResponseEntity();
                    dataItem.setLevel(ResponseEntityLevel.CHILD_SERVICE);
                    dataItem.setValue(new OrderDataType(s.getServiceKey().getType()));
//                logger.debug("ChildService of type: " + dataItem.getType());
                    dataItem.setState(sstc.find((SubSvcStateType) s.xgetServiceState()));
                    dataItem.setExternalKey(s.getServiceKey().getExternalKey());
                    dataItem.setParams(convertList2map(s.getParamList()));
                    dataItem.setAssociations(parseAssociations(s.getAssociationList()));

//                SubSvcKeyType parentKey = s.getParentServiceKey(); // link back to parent service
//                if (parentKey != null) {
//                    String parentExtKey = parentKey.getExternalKey();
//                    if (parentExtKey != null) {
////                        logger.debug("Parent service key");
////                        OrderData parentKeyData = OrderHelper.createService(null, parentExtKey, Constants.SERVICE_TYPE_PARENT_SERVICE_KEY);
////                        dataItem.addChild(parentKeyData);
//                    }
//                }
                    dataList.add(dataItem);
                }
            }
            return dataList;
        }
    }


}
