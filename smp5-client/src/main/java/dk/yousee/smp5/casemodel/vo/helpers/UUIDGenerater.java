/**
 * 
 */
package dk.yousee.smp5.casemodel.vo.helpers;

import java.util.UUID;

/**
 * @author m64746
 *
 * Date: 14/10/2015
 * Time: 13:32:01
 */
public class UUIDGenerater {
	
	public static String generateKey(){
	       UUID uuid = UUID.randomUUID();
	       return uuid.toString();
	    }

}
