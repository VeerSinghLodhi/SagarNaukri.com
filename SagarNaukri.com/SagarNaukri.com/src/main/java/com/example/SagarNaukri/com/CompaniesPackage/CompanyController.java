package com.example.SagarNaukri.com.CompaniesPackage;

import com.example.SagarNaukri.com.EmailServices.EmailService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

@Controller
public class CompanyController {


    @GetMapping("/company/companyform")
    public String getCompanyPage(Model model){
        model.addAttribute("comdata",new Company());
        return "CompaniesHTML/companyform";
    }

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    EmailService emailService;

    @Autowired CompanyLogoService companyLogoService;

    @PostMapping("/company/companyregister")
    public String getSaveCompannyDetails(HttpSession session,
                                         @RequestParam("userotp")String userOTP,
                                         Model model){
            try {

                Company comdata=(Company)session.getAttribute("comdata");
                String systemOTP=(String) session.getAttribute("systemotp");
                LocalTime tenMinutesLater=(LocalTime) session.getAttribute("otptime");// Added plus 10m
                LocalTime currentTime=LocalTime.now();

                // If OTPs didn't match
                if(!systemOTP.equals(userOTP)){
                    model.addAttribute("comdata",comdata);
                    model.addAttribute("error","Invalid OTP");
                    return "CompaniesHTML/companyotp";
                }

                //If OTP time expired
                if(currentTime.isAfter((tenMinutesLater))){
                    model.addAttribute("comdata",comdata);
                    model.addAttribute("error","OTP expired. Request a new one.");
                    return "CompaniesHTML/companyotp";
                }

                Date date = new Date();
                System.out.println(date);
                companyLogoService.getSaveDetails(
                        comdata.getCompanyname(),
                        comdata.getEmail(),
                        comdata.getContact(),
                        comdata.getAddress(),
                        comdata.getDescription(),
                        comdata.getCompanylogo(),
                        comdata.getPassword(),
                    date,
                    'Y'
                );
                emailService.getAfterRegistrationMail(comdata.getEmail(),comdata.getCompanyname());
                System.out.println("Successful");
                session.setAttribute("comdata",comdata);
                model.addAttribute("comdata",comdata);
                return "CompaniesHTML/companydashboard";
            } catch (Exception e) {
                model.addAttribute("error","Error processing files: " + e.getMessage());
                return "CompaniesHTML/companydashboard";
            }
    }

    @GetMapping("/company/companylogin")
    public String getCompanyLogin(){
        return "CompaniesHTML/companylogin";
    }

    @PostMapping("/company/login")
    public String getCheckLogin(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session,
                                Model model){

        Optional<Company>optional=companyRepository.findByEmailAndPassword(email,password);
        if(optional.isPresent()){
            session.setAttribute("comdata",optional.get());
            model.addAttribute("comdata",optional.get());
            return "CompaniesHTML/companydashboard";
        }

        model.addAttribute("error","Invalid email or password");
        return "CompaniesHTML/companylogin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/company/companylogin";
    }


    @PostMapping("/company/getotp")
    public String getOTPHTML(@Valid @ModelAttribute("comdata")Company comdata, BindingResult bindingResult, @RequestParam("company_logo") MultipartFile companylogo,HttpSession session,Model model){
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(err->System.out.println(err.getDefaultMessage()));
            return "CompaniesHTML/companyform";
        }else {
            try {

                // If Email Already Exists
                if(companyRepository.findByEmail(comdata.getEmail()).isPresent()){
                    model.addAttribute("error","Email address already exists");
                    return "CompaniesHTML/companyform";
                }


                comdata.setCompanylogo(companylogo.getBytes());
                String otp = emailService.sendOTP(comdata.getEmail(), comdata.getCompanyname());
                LocalTime currentTime = LocalTime.now();
                System.out.println("OTP sent time "+currentTime);
                // Add 10 minutes
                LocalTime tenMinutesLater = currentTime.plusMinutes(10);

                session.setAttribute("comdata",comdata);
                session.setAttribute("systemotp", otp);
                session.setAttribute("otptime",tenMinutesLater);  // +10m

                return "CompaniesHTML/companyotp";
            } catch (Exception e) {
                return "error";
            }
        }

    }

    @GetMapping("/company/getresendotp")
    public String getResendOTPHTML(HttpSession session,Model model){
            try {
                // Company Object
                Company comdata=(Company)session.getAttribute("comdata");

                String otp = emailService.sendOTP(comdata.getEmail(), comdata.getCompanyname());
                LocalTime currentTime = LocalTime.now();
                System.out.println("OTP sent time "+currentTime);
                LocalTime tenMinutesLater = currentTime.plusMinutes(10);

                model.addAttribute("comdata",comdata);
                session.setAttribute("comdata",comdata);
                session.setAttribute("systemotp", otp);
                session.setAttribute("otptime",tenMinutesLater);  // +10m
                System.out.println("completed Resend OTP Method");
                return "CompaniesHTML/companyotp";

            } catch (Exception e) {
                return "error";
            }
        }

        @GetMapping("/companylogo")
        public ResponseEntity<byte[]>getCompanyLogo(HttpSession session){
            Company company=(Company) session.getAttribute("comdata");

            if(company==null){
                return ResponseEntity.notFound().build();
            }
            int companyid=company.getCompanyid();

            Company company1=companyRepository.findById(companyid).orElse(null);
            if(company1==null || company1.getCompanylogo()==null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(company1.getCompanylogo());
        }

}
