package dk.yousee.smp5.casemodel.vo;

/**
 * Business Position is the position for a service
 * <br/>
 * This value is a subscribers key to a service plan
 * <br/>
 * The value is unique per service plan for a user
 * <br/>
 * The value can be "1" for customer X and also "1" for customer Y
 * <br/>
 * The value cannot be "1" for customer A's first BB and "1" for the second BB, for the second it could be "2"
 * <br/>
 * Business Position can be a large value (like modemID), but this is not needed.
 */
public class BusinessPosition {

    private String id;

    public BusinessPosition(String id) {
        if(id==null || id.trim().length()==0)throw new IllegalArgumentException("Business position cannot be null");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
        	return true;
        }
        if (o == null || getClass() != o.getClass()){
        	return false;
        }
        BusinessPosition modemId = (BusinessPosition) o;
        return id.equals(modemId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getId();
    }

    public static BusinessPosition create(String position) {
        if(position!=null && position.trim().length()!=0){
            return new BusinessPosition(position);
        } else {
            return null;
        }
    }
}
