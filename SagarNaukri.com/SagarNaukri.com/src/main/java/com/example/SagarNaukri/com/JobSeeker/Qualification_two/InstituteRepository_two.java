package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Qualification_two;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstituteRepository_two extends JpaRepository<Institute_two, Long> {

    // Used for search functionality, finding institutes containing the query string (case-insensitive)
    Page<Institute_two> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Used for finding an exact match by name, case-insensitive (e.g., when adding a new institute to prevent duplicates)
    Optional<Institute_two> findByNameIgnoreCase(String name);
}