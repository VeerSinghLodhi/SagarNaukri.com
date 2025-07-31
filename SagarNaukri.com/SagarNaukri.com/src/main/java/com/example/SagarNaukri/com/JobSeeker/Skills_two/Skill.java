package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Skills_two;



// src/main/java/com/example/app/skill/Skill.java


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "skills_two")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Skill name is required")
    @Size(max = 100, message = "Skill name cannot exceed 100 characters")
    @Column(unique = true) // Skills should probably be unique by name
    private String name;

    @NotBlank(message = "Skill category is required")
    @Size(max = 50, message = "Category cannot exceed 50 characters")
    private String category; // e.g., "Programming", "Soft Skill", "Design"

    // Constructors
    public Skill() {
    }

    public Skill(String name, String category) {
        this.name = name;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}