package com.example.SagarNaukri.com.CompaniesPackage;

import com.example.SagarNaukri.com.EmailServices.EmailService;
import com.example.SagarNaukri.com.Jobs.JobsRepository;
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
//                session.setAttribute("comdata",comdata);
                int totalJobs = jobsRepository.getCountJobs(comdata.getCompanyid());
                model.addAttribute("totaljobs" , totalJobs);
                model.addAttribute("register","Registration has been successful");
                return "CompaniesHTML/companylogin";
            } catch (Exception e) {
                model.addAttribute("error","Error processing files: " + e.getMessage());
                return "CompaniesHTML/companydashboard";
            }
    }

    @GetMapping("/company/companylogin")
    public String getCompanyLogin(){
        return "CompaniesHTML/companylogin";
    }

    @Autowired
    JobsRepository jobsRepository;


    @PostMapping("/company/login")
    public String getCheckLogin(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session,
                                Model model){

        Optional<Company>optional=companyRepository.findByEmailAndPassword(email,password);
        System.out.println("Login Result "+optional.isPresent());
        if(optional.isPresent()){
            int totalJobs = jobsRepository.getCountJobs(optional.get().getCompanyid());
            model.addAttribute("totaljobs" , totalJobs);
            session.setAttribute("comdata",optional.get());// c
            model.addAttribute("comdata",optional.get()); //c
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

        @GetMapping("/company/update")
        public String getCompanyUpdatePage(HttpSession session,Model model){
            Company company=(Company) session.getAttribute("comdata");
            model.addAttribute("comdata",company);
            return "CompaniesHTML/updatecompany";
        }

        @PostMapping("company/getcompanyupdate")
        public String getCompanyUpdate(@Valid @ModelAttribute("comdata") Company company,BindingResult bindingResult,HttpSession session,Model model){
            if(bindingResult.hasErrors()){
                bindingResult.getAllErrors().forEach(err->System.out.println(err.getDefaultMessage()));
                return "CompaniesHTML/updatecompany";
            }
            Company company1=(Company)session.getAttribute("comdata");
            System.out.println("Company Id : "+company1.getCompanyid());
            int totalJobs = jobsRepository.getCountJobs(company.getCompanyid());
            model.addAttribute("totaljobs" , totalJobs);

            Company company2 = companyRepository.findById(company1.getCompanyid()).orElse(null);
            if(company2==null) {
                model.addAttribute("error", "Id not found!");
                return "CompaniesHTML/companydashboard";
            }

            company2.setCompanyname(company.getCompanyname());
            company2.setAddress(company.getAddress());
            company2.setContact(company.getContact());
            company2.setEmail(company.getEmail());
            company2.setDescription(company.getDescription());
            System.out.println("company logo "+company2.getCompanylogo());// byte

            companyRepository.save(company2);
            model.addAttribute("updated","Company Info Updated");
            model.addAttribute("comdata",company2);
            session.setAttribute("comdata",company2);
            return "CompaniesHTML/companydashboard";
        }

        @GetMapping("/forgotpassword")
         public String forgotPassword(){
            return "CompaniesHTML/forgotpassword";
        }

//    @RequestMapping(value = "/sendforgototp")
    @PostMapping("/company/checkemail")
    public String setForgotOTP(@RequestParam("email")String email,Model model,HttpSession session){
        if(!companyRepository.findByEmail(email).isPresent()){
            model.addAttribute("error","Email address is not register!");
            return "CompaniesHTML/forgotpassword";
        }
        try {
            String systemOTP = emailService.getPasswordResetOTP(email, companyRepository.findCompanyNameByEmail(email));
            model.addAttribute("email", email);
            LocalTime tenMinutesLater = LocalTime.now().plusMinutes(10);
            System.out.println("OTP sent time +10 added, "+tenMinutesLater);
            session.setAttribute("systemOTP",systemOTP);
            session.setAttribute("tenMinutesLater",tenMinutesLater);
            session.setAttribute("companyName", companyRepository.findCompanyNameByEmail(email));
            return "CompaniesHTML/verifyOTP";
        }catch(Exception e){
            System.out.println("Error is "+e);
            return "CompaniesHTML/confirmationpage";
        }
    }

    @GetMapping("/company/verifyotp")
    public String getVerifyOTP(@RequestParam("userotp")String userOTP,HttpSession session,Model model){


        String systemOTP=session.getAttribute("systemOTP").toString();
        LocalTime tenMinutesLater=(LocalTime) session.getAttribute("tenMinutesLater");// Added plus 10m
        LocalTime currentTime=LocalTime.now();
        System.out.println("Current time "+currentTime);
        System.out.println("Time Condition "+currentTime.isAfter(tenMinutesLater));

        // If OTPs didn't match
        if(!systemOTP.equals(userOTP)){
            model.addAttribute("error","Invalid OTP");
            return "CompaniesHTML/verifyOTP";
        }

        //If OTP time expired
        if(currentTime.isAfter((tenMinutesLater))){
            model.addAttribute("error","OTP expired. Request a new one.");
            return "CompaniesHTML/verifyOTP";
        }


        return "CompaniesHTML/resetpassword";
    }

    @GetMapping("/company/confirm")
    public String getConfirm(){
        return "CompaniesHTML/confirmationpage";
    }

}
