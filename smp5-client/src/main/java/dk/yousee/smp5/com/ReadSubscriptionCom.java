package dk.yousee.smp5.com;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

import com.sigmaSystems.schemas.x31.smpCBECoreSchema.AssocListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.AssocType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityKeyListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityKeyType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityParamListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.GetEntityByKeyExceptionDocument;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.GetEntityByKeyRequestDocument;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.GetEntityByKeyResponseDocument;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.GetEntityByKeyResponseDocument.GetEntityByKeyResponse;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubAddressType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubContactType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcKeyType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcType.ChildServiceList;
import com.sigmaSystems.schemas.x31.smpCommonValues.NamedQuery;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamListType;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamType;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.GetServiceByKeyExceptionDocument;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.SubSvcStateType;
import com.sun.java.products.oss.xml.cbe.core.EntityValue;
import com.sun.java.products.oss.xml.common.ManagedEntityKey;
import com.sun.java.products.oss.xml.serviceActivation.GetOrderByKeyExceptionDocument;
import com.sun.java.products.oss.xml.serviceActivation.QueryOrdersExceptionDocument;

import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.Response;
import dk.yousee.smp5.order.model.ResponseAssociation;
import dk.yousee.smp5.order.model.ResponseEntity;
import dk.yousee.smp5.order.model.ResponseEntityLevel;
import dk.yousee.smp5.order.model.Smp5Xml;

public class ReadSubscriptionCom extends Smp5Com<Acct, Response> {

	@Override
	protected String convertRequest(Acct acct) {
		return new Pack().generateRequest(acct);
	}

	@Override
	protected Response convertResponse(Smp5Xml xml, Acct acct) {
		Parser parser = new Parser();
		return parser.convertResponse(xml, acct);
	}

	@Override
	protected Integer getOperationTimeout() {
		return 5000;
	}

	public static class Pack extends RequestPack<Acct> {
		@Override
		public XmlObject createXml(Acct acct) {
			GetEntityByKeyRequestDocument queryDoc = GetEntityByKeyRequestDocument.Factory
					.newInstance();
			GetEntityByKeyRequestDocument.GetEntityByKeyRequest getEntityrequest = queryDoc
					.addNewGetEntityByKeyRequest();
			getEntityrequest.setCascadeLoading(true);

			NamedQuery namedQuery = NamedQuery.Factory.newInstance();
			namedQuery.setQueryName("get_id_by_parm");
			ParamListType paramList = namedQuery.addNewInputParamList();
			ParamType param = paramList.addNewParam();
			param.setName("acct");
			param.setStringValue(acct.toString());

			EntityKeyType entKey = EntityKeyType.Factory.newInstance();
			entKey.setType("SubSpec:-");
			//TODO remove
//			entKey.setExternalKey("YouSee:user_" + acct.toString());
			entKey.setNameQuery(namedQuery);

			EntityKeyListType entityKeyList = getEntityrequest
					.addNewEntityKeyLst();
			entityKeyList.addNewEntityKey().set(entKey);
			return queryDoc;
		}
	}

	public static class Parser extends ResponseParser<Response> {
		private static final Logger logger = Logger.getLogger(Parser.class);
		private static final ServiceStateTypeConverter sstc = new ServiceStateTypeConverter();

		@Override
		public Response convertResponse(Smp5Xml xml) {
			throw new IllegalStateException("unused method");
		}

		public Response convertResponse(Smp5Xml xml, Acct acct) {
			String errorMessage = null;
			XmlObject xmlObject;
			xmlObject = parseResponse(xml.getResponse());

			/* ERROR HANDLING */
			XmlObject[] res = xmlObject
					.selectPath("declare namespace smp='http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema'; $this//smp:getEntityByKeyException");
			if (res.length > 0) {
				// This is an error
				GetEntityByKeyExceptionDocument.GetEntityByKeyException ex = (GetEntityByKeyExceptionDocument.GetEntityByKeyException) res[0];
				if (ex.getRemoteException() != null)
					errorMessage = ex.getRemoteException().getMessage();
				else if (ex.getObjectNotFoundException() != null)
					errorMessage = ex.getObjectNotFoundException().getMessage();
				else if (ex.getIllegalArgumentException() != null)
					errorMessage = ex.getIllegalArgumentException()
							.getMessage();
			}
			res = xmlObject
					.selectPath("declare namespace smp='http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema'; $this//smp:getServiceByKeyException");
			if (res.length > 0) {
				// This is an error
				GetServiceByKeyExceptionDocument.GetServiceByKeyException ex = (GetServiceByKeyExceptionDocument.GetServiceByKeyException) res[0];
				if (ex.getRemoteException() != null)
					errorMessage = ex.getRemoteException().getMessage();
				else if (ex.getObjectNotFoundException() != null)
					errorMessage = ex.getObjectNotFoundException().getMessage();
				else if (ex.getIllegalArgumentException() != null)
					errorMessage = ex.getIllegalArgumentException()
							.getMessage();

			}

			res = xmlObject
					.selectPath("declare namespace smp='http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema'; $this//smp:getOrderByKeyException");
			if (res.length > 0) {
				// This is an error
				GetOrderByKeyExceptionDocument.GetOrderByKeyException ex = (GetOrderByKeyExceptionDocument.GetOrderByKeyException) res[0];
				if (ex.getRemoteException() != null)
					errorMessage = ex.getRemoteException().getMessage();
				else if (ex.getObjectNotFoundException() != null)
					errorMessage = ex.getObjectNotFoundException().getMessage();
				else if (ex.getIllegalArgumentException() != null)
					errorMessage = ex.getIllegalArgumentException()
							.getMessage();
			}

			res = xmlObject
					.selectPath("declare namespace smp='http://java.sun.com/products/oss/xml/ServiceActivation'; $this//smp:queryOrdersException");
			if (res.length > 0) {
				// This is an error
				QueryOrdersExceptionDocument.QueryOrdersException ex = (QueryOrdersExceptionDocument.QueryOrdersException) res[0];
				if (ex.getRemoteException() != null)
					errorMessage = ex.getRemoteException().getMessage();
				else if (ex.getIllegalArgumentException() != null)
					errorMessage = ex.getIllegalArgumentException()
							.getMessage();
			}

			/* PARSE RESULTS */
			res = xmlObject
					.selectPath("declare namespace smp='http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema'; $this//smp:getEntityByKeyResponse");
			logger.debug("Select getEntityByKeyResponse, res length: "
					+ res.length);
			// parse the
			ResponseEntity smp = null;

			// the response
			if (res.length > 0) {
				GetEntityByKeyResponseDocument doc = (GetEntityByKeyResponseDocument) xmlObject;
				GetEntityByKeyResponseDocument.GetEntityByKeyResponse entity = doc
						.getGetEntityByKeyResponse();
				smp = parseSmpRoot(entity);
			}

			Response reply;
			reply = new Response(acct, null, xml, errorMessage, smp);
			return reply;
		}

		/**
		 * @param entity
		 * @return
		 */
		private ResponseEntity parseSmpRoot(GetEntityByKeyResponse entity) {
			ResponseEntity smp = new ResponseEntity();
			smp.setLevel(ResponseEntityLevel.TOP);
			List<ResponseEntity> almostServicePlans = new ArrayList<ResponseEntity>();

			if (entity != null) {
				for (EntityValue value : entity.getEntityValueArray()) {
					if (value instanceof SubType) {
						SubType subType = (SubType) value;
						smp.setExternalKey(subType.getKey().getExternalKey());
						smp.setState(sstc.find(subType.getState()));
						EntityListType entListType = subType.getEntityList();
						for (EntityValue entityValue : entListType
								.getEntityValueArray()) {
							ResponseEntity dataChild;
							dataChild = parseEntityValue(entityValue);
							if (dataChild != null) {
								almostServicePlans.add(dataChild);
							}
						}
					}
				}
			} else {
				logger.warn("Response EntityValue was not instanceof SubType");
			}
			smp.setEntities(almostServicePlans);
			return smp;
		}

		/**
		 * @param entityValue
		 * @return
		 */
		private ResponseEntity parseEntityValue(EntityValue entityValue) {
			ResponseEntity res = new ResponseEntity();
			if (entityValue instanceof SubContactType) {
				SubContactType contactType = (SubContactType) entityValue;
				EntityKeyType entKey = contactType.getKey();
				EntityParamListType entParamList = contactType.getParamList();
				res.setLevel(ResponseEntityLevel.CONTACT);
				res.setExternalKey(entKey.getExternalKey());
				res.setValue(new OrderDataType(String.valueOf(entKey.getType())));
				res.setParams(convertList2map(entParamList));
				res.setAssociations(parseAssociations(contactType.getAssociationList()));
				res.setState(sstc.find(contactType.getState()));

			} else if (entityValue instanceof SubAddressType) {
				SubAddressType subKey = (SubAddressType) entityValue;
				EntityParamListType entParamList = subKey.getParamList();
				res.setLevel(ResponseEntityLevel.ADDRESS);
				res.setState(sstc.find(subKey.getState()));
				res.setValue(new OrderDataType(subKey.getKey().getType()));
				res.setExternalKey(subKey.getKey().getExternalKey());
				res.setParams(convertList2map(entParamList));
				res.setAssociations(parseAssociations(subKey.getAssociationList()));
				
			} else if (entityValue instanceof SubSvcType) {
				SubSvcType subKey = (SubSvcType) entityValue;
				res.setLevel(ResponseEntityLevel.SERVICE);
	            res.setState(sstc.find((SubSvcStateType) subKey.xgetServiceState()));
	            res.setValue(new OrderDataType(subKey.getServiceKey().getType()));
                res.setExternalKey(subKey.getServiceKey().getExternalKey());
                SubSvcKeyType parentKey = subKey.getParentServiceKey(); // link back to parent service
                if (parentKey != null) {
                    String parentExtKey = parentKey.getExternalKey();
                    if (parentExtKey != null) {
                        logger.debug("Parent service key");
                    }
                }
                EntityParamListType entParamList = subKey.getParamList();
                res.setParams(convertList2map(entParamList));
                res.setAssociations(parseAssociations(subKey.getAssociationList()));
                SubSvcType.ChildServiceList childserviceList = subKey.getChildServiceList();
                res.getEntities().addAll(parseChildServiceList(childserviceList));
                
			} else {
				logger.warn("Unknown entity value");
			}
			return res;
		}

	     /**
		 * @param childserviceList
		 * @return
		 */
		 public List<ResponseEntity> parseChildServiceList(ChildServiceList childserviceList) {
			 List<ResponseEntity> dataList = new ArrayList<ResponseEntity>();
	            if (childserviceList != null) {
	                for (SubSvcType s : childserviceList.getServiceValueArray()) {
	                    ResponseEntity dataItem;
	                    dataItem = new ResponseEntity();
	                    dataItem.setLevel(ResponseEntityLevel.CHILD_SERVICE);
	                    dataItem.setValue(new OrderDataType(s.getServiceKey().getType()));
	                    dataItem.setState(sstc.find((SubSvcStateType) s.xgetServiceState()));
	                    dataItem.setExternalKey(s.getServiceKey().getExternalKey());
	                    dataItem.setParams(convertList2map(s.getParamList()));
	                    dataItem.setAssociations(parseAssociations(s.getAssociationList()));
	                    SubSvcType.ChildServiceList subChildserviceList = s.getChildServiceList();
	                    dataItem.getEntities().addAll(parseChildServiceList(subChildserviceList));
	                    dataList.add(dataItem);
	                }
	            }
	            return dataList;
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
                     //TODO ver qual é o metodo para ir buscar a primaryKEy
                     ra.setType(new OrderDataType(zxx.getType()));
                     String oneTxt = one.toString();
                     ra.setPrimaryKey(grepPrimaryKey(oneTxt));
                     res.add(ra);
                 }
             }
             return res;
		}
        
        private static String grepPrimaryKey(final String source) {
            final String key = "primaryKey>";
            int pos0 = source.indexOf(key);
            if (pos0 == -1){
            	return null;
            }
            pos0 = pos0 + key.length();
            int pos1 = source.indexOf("<", pos0);
            if (pos1 == -1) {
            	return null;
            }
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

	}

}
