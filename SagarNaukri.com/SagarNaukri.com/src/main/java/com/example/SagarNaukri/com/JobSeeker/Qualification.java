package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "qualification") // Explicit table name
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "q_id") // Explicit column name for the ID
    private Long QId;

    @ManyToOne
    @JoinColumn(name = "js_id", referencedColumnName = "JSId")
    private JobSeeker jobSeeker;

    private String degree;
    private String institution;

    @Column(name = "year_of_passing")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Add this annotation// Good practice to specify column names
    private LocalDate yearOfPassing;

    private String category;
    private String grade;

    // Constructors
    public Qualification() {
    }

    public Qualification(Long QId, String degree, String institution,
                         LocalDate yearOfPassing, String category, String grade) {
        this.QId = QId;
        this.degree = degree;
        this.institution = institution;
        this.yearOfPassing = yearOfPassing;
        this.category = category;
        this.grade = grade;
    }

    public JobSeeker getJobSeeker() {
        return jobSeeker;
    }

    public void setJobSeeker(JobSeeker jobSeeker) {
        this.jobSeeker = jobSeeker;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public LocalDate getYearOfPassing() {
        return yearOfPassing;
    }

    public void setYearOfPassing(LocalDate yearOfPassing) {
        this.yearOfPassing = yearOfPassing;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Long getQId() {
        return QId;
    }

    public void setQId(Long QId) {
        this.QId = QId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
