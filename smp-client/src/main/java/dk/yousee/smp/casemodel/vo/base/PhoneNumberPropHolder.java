package dk.yousee.smp.casemodel.vo.base;

import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 22, 2010
 * Time: 9:40:25 AM
 * Property holder for the Phone number
 */
public class PhoneNumberPropHolder extends PropHolder {
    public PhoneNumberPropHolder(BasicUnit unit, String key) {
        super(unit, key);
    }
    @Override
    public void setValue(String value) {
        super.setValue(filterPhoneNumber(value));
    }

    /**
     * Phonenumbers must be 8 chars or start with +45 and be 11 in length
     * @param pho input phone number
     * @return resulting number to be send to sigma. blank if not matching criteria
     */
    public String filterPhoneNumber(String pho){
        String res;
        if (pho != null && (pho.length() == 8 || pho.startsWith("+45") && pho.length() == 11)) {
            res=pho;
        } else {
            res="";
        }
        return res;
    }
}
