package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Qualification_two;

import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker; // Import your JobSeeker entity
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "qualification_two")
public class Qualification_two {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship with JobSeeker
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "js_id", nullable = false) // Foreign key to JobSeeker, using 'js_id' as in your service
    private JobSeeker jobSeeker;

    // Relationship with Institute_two
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "institute_id", nullable = false)
    private Institute_two institute;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QualificationType_two type;

    @Column(nullable = false)
    private String degreeProgram;

    private String majorSpecialization;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate; // Can be null if ongoing

    private String gradeGPA;

    @Column(length = 1000)
    private String description;

    // Constructors
    public Qualification_two() {
    }

    // Getters and Setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobSeeker getJobSeeker() {
        return jobSeeker;
    }

    public void setJobSeeker(JobSeeker jobSeeker) {
        this.jobSeeker = jobSeeker;
    }

    public Institute_two getInstitute() {
        return institute;
    }

    public void setInstitute(Institute_two institute) {
        this.institute = institute;
    }

    public QualificationType_two getType() {
        return type;
    }

    public void setType(QualificationType_two type) {
        this.type = type;
    }

    public String getDegreeProgram() {
        return degreeProgram;
    }

    public void setDegreeProgram(String degreeProgram) {
        this.degreeProgram = degreeProgram;
    }

    public String getMajorSpecialization() {
        return majorSpecialization;
    }

    public void setMajorSpecialization(String majorSpecialization) {
        this.majorSpecialization = majorSpecialization;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getGradeGPA() {
        return gradeGPA;
    }

    public void setGradeGPA(String gradeGPA) {
        this.gradeGPA = gradeGPA;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}