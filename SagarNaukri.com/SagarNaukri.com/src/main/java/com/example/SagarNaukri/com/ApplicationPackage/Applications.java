package SagarNaukriMerge.SagarNaukriMerge.ApplicationPackage;

import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker;
import SagarNaukriMerge.SagarNaukriMerge.Jobs.Jobs;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="applications")
public class Applications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appid;

//    @ManyToOne
//    @JoinColumn(name = "jobid")
//    private Jobs jobs;
//
//    @ManyToOne
//    @JoinColumn(name = "jsid")
//    private JobSeeker jobSeeker;
    @Column(name="jobid")
    private int jobid;
    @Column(name="jsid")
    private int jsid;


    @Column(name="applicationstatus")
    private String applicationstatus;

    @Column(name="dateapplied")
    Date dateapplied;

    byte [] resume;

    public Applications() {
    }

    public Applications(int appid, int jobid, int jsid, String applicationstatus, Date dateapplied, byte[] resume) {
        this.appid = appid;
        this.jobid = jobid;
        this.jsid = jsid;
        this.applicationstatus = applicationstatus;
        this.dateapplied = dateapplied;
        this.resume = resume;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public int getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public int getJsid() {
        return jsid;
    }

    public void setJsid(int jsid) {
        this.jsid = jsid;
    }

    public String getApplicationstatus() {
        return applicationstatus;
    }

    public void setApplicationstatus(String applicationstatus) {
        this.applicationstatus = applicationstatus;
    }

    public Date getDateapplied() {
        return dateapplied;
    }

    public void setDateapplied(Date dateapplied) {
        this.dateapplied = dateapplied;
    }

    public byte[] getResume() {
        return resume;
    }

    public void setResume(byte[] resume) {
        this.resume = resume;
    }
}
