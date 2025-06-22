package com.example.SagarNaukri.com.Jobs;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobsRepository extends JpaRepository<Jobs , Integer> {

    @Query("select e from Jobs e where e.companyid=:companyid and e.isenabled=true order by e.jobid")
    List<Jobs>findByCompanyid(@Param("companyid")int companyid);

//    List<Jobs> findByCompanyid(int companyid);
    Optional<Jobs>findByJobid(int id);

    @Modifying
    @Transactional
    @Query("update Jobs e set e.isenabled = false where e.jobid =:jobid")
    void deleteJobById(@Param("jobid") int jobid);

    @Query("select count(e) from Jobs e where e.companyid =:companyid and e.isenabled=true")
    int getCountJobs(@Param("companyid") int companyid);
}
