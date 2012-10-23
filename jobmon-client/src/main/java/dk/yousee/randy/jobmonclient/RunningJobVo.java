package dk.yousee.randy.jobmonclient;

import java.util.Date;

/**
 *
 * @author jablo
 */
public class RunningJobVo {
    private String id;
    private Date starttime;
    private String status;
    private JobState state;
    private String progress;
    private String result;
    private String detaillink;
    private String estimateend;

    public String getDetaillink() {
        return detaillink;
    }

    public void setDetaillink(String detaillink) {
        this.detaillink = detaillink;
    }

    public String getEstimateend() {
        return estimateend;
    }

    public void setEstimateend(String estimateend) {
        this.estimateend = estimateend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JobState getState() {
        return state;
    }

    public void setState(JobState state) {
        this.state = state;
    }
}
