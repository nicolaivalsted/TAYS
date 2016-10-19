/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.smp.casemodel.vo.cbp;

import dk.yousee.smp.casemodel.vo.cbp.SuspendHelper.SuspendReasonBilling;
import dk.yousee.smp.casemodel.vo.cbp.SuspendHelper.SuspendReasonAbuse;

/**
 *
 * @author rdu
 * Data structure to return to clients with suspend information
 */
public class SuspendStatus {
    
    private boolean havingStdCpe;
    private boolean courtesyBlocked;
    private SuspendReasonAbuse abuse=null;
    private SuspendReasonBilling billing=null;

    public SuspendStatus(boolean havingStdCpe) {
        this.havingStdCpe=havingStdCpe;
        this.courtesyBlocked=false;
    }

    public SuspendStatus(SuspendReasonAbuse abuse, SuspendReasonBilling billing) {
        this(true);
        this.courtesyBlocked = true;
        this.abuse = abuse;
        this.billing = billing;
    }

    public SuspendReasonAbuse getAbuse() {
        return abuse;
    }

    public SuspendReasonBilling getBilling() {
        return billing;
    }
    public boolean isCourtesyBlocked() {
        return courtesyBlocked;
    }

    public boolean isHavingStdCpe() {
        return havingStdCpe;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SuspendStatus");
        if(!isCourtesyBlocked())sb.append("{courtesyBlocked=").append(courtesyBlocked);
        if(!isHavingStdCpe())sb.append(",stdCpe=").append(havingStdCpe);
        if(getAbuse()!=null)sb.append(", abuse=").append(abuse);
        if(getBilling()!=null)sb.append(", billing=").append(billing);
        sb.append('}');
        return sb.toString();
    }
}
