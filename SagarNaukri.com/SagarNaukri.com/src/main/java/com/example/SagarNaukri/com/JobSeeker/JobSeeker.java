package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;

import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Skills_two.Skill;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
// import java.util.UUID; // No longer directly used here, but keep if needed elsewhere

import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Qualification_two.Qualification_two; // Import the new Qualification_two

@Entity
@Table(name = "job_seeker")
public class JobSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long JSId;

//    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<JobSeekerSkill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    // --- CHANGE STARTS HERE ---
    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true) // Added orphanRemoval=true for better management
    private List<Qualification_two> qualifications = new ArrayList<>(); // Changed type to Qualification_two and initialized
    // --- CHANGE ENDS HERE ---

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String contact;
    private String address;

    @Column(nullable = false)
    private String password;

    @Column(name = "profilepicture", columnDefinition = "bytea")
    private byte[] profilePicture;

    private byte[] resume;

    // --- New fields for Email Verification ---
    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false; // Default to false

    // --- Constructors ---
    public JobSeeker() {
    }

    public JobSeeker(String name, String email, String contact, String address, String password, byte[] profilePicture, byte[] resume) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.password = password;
        this.profilePicture = profilePicture;
        this.resume = resume;
    }

    // --- Getters and Setters (only for qualifications changed) ---

    public Long getJSId() {
        return JSId;
    }

    public void setJSId(Long JSId) {
        this.JSId = JSId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public byte[] getResume() {
        return resume;
    }

    public void setResume(byte[] resume) {
        this.resume = resume;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
    // skills
    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobSeekerSkill> skills = new ArrayList<>();
    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL)
    private List<JobSeekerSkill> jobSeekerSkills;


    // Add these methods to JobSeeker.java:
    public List<JobSeekerSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<JobSeekerSkill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill skill) {
        JobSeekerSkill jobSeekerSkill = new JobSeekerSkill(this, skill);
        skills.add(jobSeekerSkill);
    }

    public void removeSkill(Skill skill) {
        for (Iterator<JobSeekerSkill> iterator = skills.iterator(); iterator.hasNext();) {
            JobSeekerSkill jobSeekerSkill = iterator.next();
            if (jobSeekerSkill.getJobSeeker().equals(this) && jobSeekerSkill.getSkill().equals(skill)) {
                iterator.remove();
                jobSeekerSkill.setJobSeeker(null);
                jobSeekerSkill.setSkill(null);
            }
        }
    }

    // --- NEW GETTER/SETTER FOR QUALIFICATION_TWO ---
    public List<Qualification_two> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<Qualification_two> qualifications) {
        this.qualifications = qualifications;
    }

    // Helper method to add a qualification_two
    public void addQualification(Qualification_two qualification) {
        this.qualifications.add(qualification);
        qualification.setJobSeeker(this);
    }

    // Helper method to remove a qualification_two
    public void removeQualification(Qualification_two qualification) {
        this.qualifications.remove(qualification);
        qualification.setJobSeeker(null);
    }
}