package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByJobSeekerJSId(Long jsId);
}
