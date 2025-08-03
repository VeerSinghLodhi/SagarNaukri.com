package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QualificationRepository extends JpaRepository<Qualification,Long> {

//    @Query("SELECT q FROM Qualification q WHERE q.jobSeeker.email = :email")
//    List<Qualification> findByJobSeekerEmail(@Param("email") String email);
    List<Qualification> findByJobSeekerJSId(Long jsId);
}
