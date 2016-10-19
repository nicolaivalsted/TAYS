package dk.yousee.smp.casemodel.vo;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 20, 2010
 * Time: 10:11:07 AM
 * Value object containing Phone number
 */
public final class PhoneNumber {

    private String phoneNumber;

    public PhoneNumber(String phoneNumber) {
        if(phoneNumber ==null || phoneNumber.trim().length()==0)throw new IllegalArgumentException("phone number cannot be null");
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber modemId = (PhoneNumber) o;
        return phoneNumber.equals(modemId.phoneNumber);
    }

    @Override
    public int hashCode() {
        return phoneNumber.hashCode();
    }

    @Override
    public String toString() {
        return getPhoneNumber();
    }

    public static PhoneNumber create(String value){
        if(value!=null && value.trim().length()!=0){
            return new PhoneNumber(value);
        } else {
            return null;
        }
    }
}
