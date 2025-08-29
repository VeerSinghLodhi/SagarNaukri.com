package SagarNaukriMerge.SagarNaukriMerge.MessagePackage;

import SagarNaukriMerge.SagarNaukriMerge.CompaniesPackage.Company;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversationid;

    @ManyToOne
    @JoinColumn(name = "companyid", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "jsid", nullable = false)
    private JobSeeker jobSeeker;

    private LocalDateTime created_at = LocalDateTime.now();


    public Conversation() {
    }

    public Conversation(Long conversationid, Company company, JobSeeker jobSeeker, LocalDateTime created_at) {
        this.conversationid = conversationid;
        this.company = company;
        this.jobSeeker = jobSeeker;
        this.created_at = created_at;
    }

    public Long getConversationid() {
        return conversationid;
    }

    public void setConversationid(Long conversationid) {
        this.conversationid = conversationid;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public JobSeeker getJobSeeker() {
        return jobSeeker;
    }

    public void setJobSeeker(JobSeeker jobSeeker) {
        this.jobSeeker = jobSeeker;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
