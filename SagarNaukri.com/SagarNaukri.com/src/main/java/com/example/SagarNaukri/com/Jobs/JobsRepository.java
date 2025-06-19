package com.example.SagarNaukri.com.Jobs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobsRepository extends JpaRepository<Jobs , Integer> {
   
    @Query("select e from Jobs e where e.companyid=:companyid order by e.jobid")
    List<Jobs>findByCompanyid(@Param("companyid")int companyid);


//    List<Jobs> findByCompanyid(int companyid);
    Optional<Jobs>findByJobid(int id);
}
