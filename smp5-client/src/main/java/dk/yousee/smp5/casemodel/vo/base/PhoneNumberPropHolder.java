/**
 * 
 */
package dk.yousee.smp5.casemodel.vo.base;

import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;

/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 13:12:24
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
	 * 
	 * @param pho
	 *            input phone number
	 * @return resulting number to be send to sigma. blank if not matching
	 *         criteria
	 */
	public String filterPhoneNumber(String pho) {
		String res;
		if (pho != null
				&& (pho.length() == 8 || pho.startsWith("+45")
						&& pho.length() == 11)) {
			res = pho;
		} else {
			res = "";
		}
		return res;
	}

}
