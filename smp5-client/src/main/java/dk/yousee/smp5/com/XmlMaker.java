package dk.yousee.smp5.com;

import java.util.Map;

import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityParamListType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcKeyType;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamType;

import dk.yousee.smp5.order.model.OrderDataType;

/**
 * @author m64746
 *
 *         Date: 16/10/2015 Time: 12:54:59
 */
public abstract class XmlMaker {

	public void addParameters(EntityParamListType paramList, Map<String, String> params) {
		ParamType paramEnt;
		for (String key : params.keySet()) {
			paramEnt = paramList.addNewParam();
			paramEnt.setName(key);
			paramEnt.setStringValue(params.get(key));
		}
	}

	public SubSvcKeyType initiateSubSvcKeyType(OrderDataType type, String externalKey) {
		SubSvcKeyType keyType = SubSvcKeyType.Factory.newInstance();
		keyType.setType(type.toString());
		keyType.setExternalKey(externalKey);
		return keyType;
	}

}
