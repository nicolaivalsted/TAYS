package dk.yousee.smp.casemodel.vo.cbp;

/**
 *
 * @author rdu
 */
public class SuspendHelper {

    public enum SuspendReasonAbuse {

        ABUSE("Abuse"), ABUSEWARNING("Abuse Warning"), UKENDT("UKENDT"), MODEM("Modem");
        
        String description;
                
	    SuspendReasonAbuse(String description) {
		    this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
        
        /**
         * Convert string value to enumeration.
         * The converter uses enum.name to compare to.
         * @param inputValue string value (read from service plan parameter)
         * @return enum instance or null if inputValue is blank/null or
         * finally UKENDT if the string does not match a known enum
         */
        public static SuspendReasonAbuse getEnum(String inputValue) {
        	if (inputValue == null || inputValue.trim().length()==0) return null;
        	for (SuspendReasonAbuse srs : SuspendReasonAbuse.values()) {
        		if (srs.name().equalsIgnoreCase(inputValue))
        			return srs;
        	}
        	return UKENDT;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{description='").append(getDescription()).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public enum SuspendReasonBilling {

        REGNING("Regning"),
        /**
         * Opsagt bruges p.t. ikke
         */
        OPSAGT("Opsagt"),
        STANDBY("Standby"),
        UKENDT("UKENDT");
   
        String description;
                
	    SuspendReasonBilling(String description) {
		   this.description = description;
        }
        
        public String getDescription() {
            return description;
        }

        /**
         * Convert string value to enumeration
         * The converter uses enum.name to compare to.
         * @param inputValue string value (read from service plan parameter)
         * @return enum instance or null if inputValue is blank/null or
         * finally UNKNOWN if the string does not match a known enum
         */
        public static SuspendReasonBilling getEnum(String inputValue) {
            if (inputValue == null || inputValue.trim().length()==0) return null;
        	for (SuspendReasonBilling srs : SuspendReasonBilling.values()) {
        		if (srs.name().equalsIgnoreCase(inputValue))
        			return srs;
        	}
        	return UKENDT;
        }
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{description='").append(getDescription()).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
