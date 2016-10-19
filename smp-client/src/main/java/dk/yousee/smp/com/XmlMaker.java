package dk.yousee.smp.com;

import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityParamListType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcKeyType;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamType;
import dk.yousee.smp.order.model.OrderDataType;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 03/06/12
 * Time: 10.09
 * Class that makes xml elements ... Absolutely bound to to xml-beans !!!!
 *
 * <p>
 *     These makers are mend to fill into RequestPack classes to perform small XML things
 *     In order to obey DRY
 * </p>
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
