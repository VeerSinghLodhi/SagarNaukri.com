package com.example.SagarNaukri.com.CompaniesPackage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Integer> {
    Optional<Company> findByEmail(String email);
    Optional<Company> findByEmailAndPassword(String email,String password);
}
