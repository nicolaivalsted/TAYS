package dk.yousee.randy.yspro;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 12/09/12
 * Time: 16.49
 * Vo that handles YS-pro date time
 */
public class YsProTime {

    static final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //"2012-09-03 18:00:00");
    static final SimpleDateFormat formatMidnight=new SimpleDateFormat("yyyy-MM-dd 00:00:00"); //"2012-09-03 00:00:00");
    public static String formatDate(Date date){
        return format.format(date);
    }
    private String dateTime;

    public YsProTime(String dateTime) {
        if(blank(dateTime))throw new IllegalArgumentException("YsProTime is never blank");
        this.dateTime = dateTime;
    }
    public YsProTime(Date now) {
        this.dateTime = formatDate(now);
    }

    @Override
    public String toString() {
        return dateTime;
    }

    private static boolean blank(String dateTime) {
        return dateTime ==null || dateTime.trim().length()==0;
    }

    public static YsProTime create(String dateTime){
        if(blank(dateTime)){
            return null;
        } else {
            return new YsProTime(dateTime);
        }
    }
//    private Date parseDate(JsonObject element,String key) {
//        String dateAsString=element.get(key).getAsString();
//        Date res=null;
//        try {
//            res= format.parse(dateAsString);
//        } catch (ParseException e) {
//            errorMessage.append(String.format("Error parsing %s date, from string %s, got message: %s", key, dateAsString, e.getMessage()));
//        }
//        return res;
//    }

    public boolean between(YsProTime from, YsProTime to) {
        boolean low=from!=null && (from.toString().compareTo(dateTime)<0);
        boolean high=to!=null && (to.toString().compareTo(dateTime)>=0);
        return low && high;
    }

    public static YsProTime createFromLastHour() {
        Date now=new Date(System.currentTimeMillis()-3600L*1000);
        return new YsProTime(now);
    }

    public static YsProTime midNight(Date writeTime) {
        return create(formatMidnight.format(writeTime));

    }
}

