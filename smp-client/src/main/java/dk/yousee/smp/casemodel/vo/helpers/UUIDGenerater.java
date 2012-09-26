package dk.yousee.smp.casemodel.vo.helpers;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 26, 2010
 * Time: 11:30:26 AM<br/>
 * To generate UUID.
 * The version field holds a value that describes the type of this UUID.
 * <p>
 * There are four different basic types of UUIDs: time-based, DCE security, name-based,
 * and randomly generated UUIDs. These types have a version value of 1, 2, 3 and 4, respectively.
 * </p>
 * And this UUIDGenerater is a type 4 -  Random-UUID  Generater
 */
public class UUIDGenerater {

    public static String generateKey(){
       UUID uuid = UUID.randomUUID();
       return uuid.toString();
    }


}
