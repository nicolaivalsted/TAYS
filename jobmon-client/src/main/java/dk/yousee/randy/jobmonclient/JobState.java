package dk.yousee.randy.jobmonclient;

/**
 *
 * @author m27236
 */
public enum JobState {
    STARTED("STARTED"), RUNNING("RUNNING"), FAIL("FAIL"), FAIL_DEPENDENCY("FAIL-DEPENDENCY"), DONE("DONE");
    private String str;
    
    private JobState(String str){
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
