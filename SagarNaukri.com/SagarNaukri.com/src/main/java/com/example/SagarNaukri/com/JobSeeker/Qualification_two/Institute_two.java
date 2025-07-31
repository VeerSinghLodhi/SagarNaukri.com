package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Qualification_two;

import jakarta.persistence.*;

@Entity
@Table(name = "institute_two") // Ensure this table name matches your actual DB table
public class Institute_two {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // If your database ID column is INT, change this to private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    private String address;

    // Constructors
    public Institute_two() {
    }

    public Institute_two(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}