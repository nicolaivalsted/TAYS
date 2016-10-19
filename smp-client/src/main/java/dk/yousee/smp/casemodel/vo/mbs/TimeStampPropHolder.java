package dk.yousee.smp.casemodel.vo.mbs;

import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Nov 3, 2010
 * Time: 11:48:55 AM<br/>
 * A property holder that can write current time into a field
 */
public class TimeStampPropHolder extends PropHolder {

    static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    public TimeStampPropHolder(BasicUnit unit, String key) {
        super(unit, key);
    }

    /**
     * Assign current time to value of field.
     */
    public void assignCurrentTime() {
        setValue(date2string(new Date()));
    }

    /**
     * Convert date to formatted string
     *
     * @param now date to format
     * @return string with the date as a string respecting the format
     */
    String date2string(Date now) {
        return format.format(now);
    }


}
