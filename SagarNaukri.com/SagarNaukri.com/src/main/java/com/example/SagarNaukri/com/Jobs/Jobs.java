package com.example.SagarNaukri.com.Jobs;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int jobid;
    int companyid;
   String jobtitle , description, location , min_salary, max_salary , experience , job_type , skills;
   Date dateposted;
   LocalDate deadline;
   boolean isenabled;

    public Jobs() {
    }

    public Jobs(int jobid, int companyid, String jobtitle, String description, String location, String min_salary, String max_salary, String experience, String job_type, String skills, Date dateposted, LocalDate deadline, boolean isenabled) {
        this.jobid = jobid;
        this.companyid = companyid;
        this.jobtitle = jobtitle;
        this.description = description;
        this.location = location;
        this.min_salary = min_salary;
        this.max_salary = max_salary;
        this.experience = experience;
        this.job_type = job_type;
        this.skills = skills;
        this.dateposted = dateposted;
        this.deadline = deadline;
        this.isenabled = isenabled;
    }

    public boolean isIsenabled() {
        return isenabled;
    }

    public void setIsenabled(boolean isenabled) {
        this.isenabled = isenabled;
    }

    public int getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMin_salary() {
        return min_salary;
    }

    public void setMin_salary(String min_salary) {
        this.min_salary = min_salary;
    }

    public String getMax_salary() {
        return max_salary;
    }

    public void setMax_salary(String max_salary) {
        this.max_salary = max_salary;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Date getDateposted() {
        return dateposted;
    }

    public void setDateposted(Date dateposted) {
        this.dateposted = dateposted;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
