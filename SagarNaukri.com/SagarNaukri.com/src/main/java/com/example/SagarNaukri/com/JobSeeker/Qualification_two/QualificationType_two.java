package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Qualification_two;



// File: JobSeeker/Qualification_two/QualificationType_two.java
// Ensure this matches your package structure

public enum QualificationType_two {
    SCHOOL("School"),
    DIPLOMA("Diploma"),
    ASSOCIATE_DEGREE("Associate's Degree"),
    BACHELORS_DEGREE("Bachelor's Degree"),
    MASTERS_DEGREE("Master's Degree"),
    PHD("PhD"),
    CERTIFICATION("Certification"),
    VOCATIONAL("Vocational"),
    OTHER("Other");

    private final String displayName;

    // Constructor to set the display name
    QualificationType_two(String displayName) {
        this.displayName = displayName;
    }

    // Getter method for the display name
    public String getDisplayName() {
        return displayName;
    }
}