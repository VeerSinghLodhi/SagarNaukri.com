package SagarNaukriMerge.SagarNaukriMerge.ApplicationPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Applications,Integer> {

    List<Applications>findByJsid(Long jobseekerId);

    @Query("select a from Applications a where a.jobid=:jobid")
    Applications findApplicationByJobid7(@Param("jobid")int jobid);

    @Query("SELECT a FROM Applications a WHERE a.jobid = :jobid")
    List<Applications> findApplicationsByJobid(@Param("jobid") int jobid);


}
