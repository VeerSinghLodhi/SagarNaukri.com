package com.example.SagarNaukri.com.CompaniesPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CompanyLogoService {

    @Autowired CompanyRepository companyRepository;

    public Company getSaveDetails(String companyname, String email, String contact, String address, String description, byte[] companylogo, String password, Date joining_date , char status ) throws Exception{
        Company company=new Company();
        company.setCompanyname(companyname);
        company.setEmail(email);
        company.setContact(contact);
        company.setAddress(address);
        company.setDescription(description);
        company.setCompanylogo(companylogo);
        company.setPassword(password);
        company.setStatus(status);
        company.setJoining_date(joining_date);
        return companyRepository.save(company);
    }
    public void getLogoUpdate(byte[] companylogo,Company company){
        company.setCompanylogo(companylogo);
        companyRepository.save(company);
    }
}
