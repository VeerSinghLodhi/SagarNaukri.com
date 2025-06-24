package com.example.SagarNaukri.com.CompaniesPackage;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Integer> {
    Optional<Company> findByEmail(String email);
    Optional<Company> findByEmailAndPassword(String email,String password);

    @Query("select e.companyname from Company e where e.email= :email")
    String findCompanyNameByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("update Company e set e.password=:password where e.email=:email")
    void getResetPassword(String email,String password);

}
