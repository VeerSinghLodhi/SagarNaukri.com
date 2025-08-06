package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {
    Optional<JobSeeker> findByEmail(String email);
    Optional<JobSeeker> findById(Long id);
    Optional<JobSeeker> findByVerificationToken(String token);

    @Query("select j from JobSeeker j where j.JSId=:jobSeekerId")
    JobSeeker veer(@Param("jobSeekerId")Long jobSeekerId);
}

