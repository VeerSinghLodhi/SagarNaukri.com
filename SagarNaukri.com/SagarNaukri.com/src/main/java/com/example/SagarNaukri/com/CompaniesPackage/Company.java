package com.example.SagarNaukri.com.CompaniesPackage;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.util.Date;

@Entity
@Table(name="companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int companyid;

    private String companyname;

    @Email()
    private String email;

    @Pattern(regexp="^(?:(?:\\+91|91|0)[-\\s]?)?[6-9]\\d{9}$",message = "Invalid mobile number")
    private String contact;

    private String address;

    private String description;

    private byte[] companylogo;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",message = "At least 8 characters long.\n" +
            "\n" +
            "Contains at least one uppercase letter.\n" +
            "\n" +
            "Contains at least one lowercase letter.\n" +
            "\n" +
            "Contains at least one digit.\n" +
            "\n" +
            "Contains at least one special character (e.g., @, #, $, etc.).")
    private String password;

    private char status ;

    private Date joining_date;

    public Company() {
    }

    public Company(int companyid, String companyname, String email, String contact, String address, String description, byte[] companylogo, String password, char status, Date date) {
        this.companyid = companyid;
        this.companyname = companyname;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.description = description;
        this.companylogo = companylogo;
        this.password = password;
        this.status = status;
        this.joining_date = date;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getCompanylogo() {
        return companylogo;
    }

    public void setCompanylogo(byte[] companylogo) {
        this.companylogo = companylogo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public Date getJoining_date() {
        return joining_date;
    }

    public void setJoining_date(Date joining_date) {
        this.joining_date = joining_date;
    }
}
