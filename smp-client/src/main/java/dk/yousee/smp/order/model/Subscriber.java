package dk.yousee.smp.order.model;

import java.io.Serializable;

public final class Subscriber implements Serializable {

    static final private long serialVersionUID = -7569650801379990454L;



    private Acct kundeId;

    /**
     * @return the 9 digit customer key
     */
    public Acct getKundeId() {
        return kundeId;
    }

    /**
     * Assign customer id
     * @param kundeId 9 digit string
     */
    public void setKundeId(Acct kundeId) {
        this.kundeId = kundeId;
    }

    private String internId;
    private String fornavn;
    private String efternavn;
    private boolean eksisterendeKunde = true;


    public void setInternId(String id){
        this.internId = id;
    }
    public String getInternId(){
        return this.internId;
    }

    public String getFornavn() {
        return fornavn;
    }

    /**
     * @deprecated use SubContactSpec instead of
     * @param fornavn first name of subscriber
     */
    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getEfternavn() {
        return efternavn;
    }
    /**
     * @deprecated use SubContactSpec instead of
     * @param efternavn last name of subscriber
     */
    public void setEfternavn(String efternavn) {
        this.efternavn = efternavn;
    }

    public boolean isEksisterendeKunde(){
        return this.eksisterendeKunde;
    }

    public void setEksisterendeKunde(boolean eksisterer){
        this.eksisterendeKunde = eksisterer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{kundeId=").append(kundeId);
        sb.append(", internId='").append(internId).append('\'');
        sb.append(", fornavn='").append(fornavn).append('\'');
        sb.append(", efternavn='").append(efternavn).append('\'');
        sb.append(", eksisterendeKunde=").append(eksisterendeKunde);
        sb.append('}');
        return sb.toString();
    }
}
