package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {
    Optional<JobSeeker> findByEmail(String email);
    Optional<JobSeeker> findById(Long id);
    Optional<JobSeeker> findByVerificationToken(String token);
}

