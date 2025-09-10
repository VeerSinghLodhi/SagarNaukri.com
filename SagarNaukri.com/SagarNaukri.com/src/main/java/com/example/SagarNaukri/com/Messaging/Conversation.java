package SagarNaukriMerge.SagarNaukriMerge.Messaging; // A new package for messaging

import SagarNaukriMerge.SagarNaukriMerge.CompaniesPackage.Company;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToMany
    @JoinTable(name = "conversation_jobseekers",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "jobseeker_id"))
    private Set<JobSeeker> jobSeekerParticipants = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "conversation_companies",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id"))
    private Set<Company> companyParticipants = new HashSet<>();

    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<JobSeeker> getJobSeekerParticipants() {
        return jobSeekerParticipants;
    }

    public void setJobSeekerParticipants(Set<JobSeeker> jobSeekerParticipants) {
        this.jobSeekerParticipants = jobSeekerParticipants;
    }

    public Set<Company> getCompanyParticipants() {
        return companyParticipants;
    }

    public void setCompanyParticipants(Set<Company> companyParticipants) {
        this.companyParticipants = companyParticipants;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}