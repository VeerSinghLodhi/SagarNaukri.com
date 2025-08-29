package SagarNaukriMerge.SagarNaukriMerge.CompaniesPackage;

import SagarNaukriMerge.SagarNaukriMerge.ApplicationPackage.ApplicationRepository;
import SagarNaukriMerge.SagarNaukriMerge.ApplicationPackage.Applications;
import SagarNaukriMerge.SagarNaukriMerge.EmailServices.EmailService2;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeekerRepository;
import SagarNaukriMerge.SagarNaukriMerge.Jobs.Jobs;
import SagarNaukriMerge.SagarNaukriMerge.Jobs.JobsRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class CompanyController {

    @GetMapping("/company/companyform")
    public String getCompanyPage(Model model) {
        model.addAttribute("comdata", new Company());
        return "CompaniesHTML/companyform";
    }

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    EmailService2 emailService;

    @Autowired
    CompanyLogoService companyLogoService;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    JobSeekerRepository jobSeekerRepository;

    @PostMapping("/company/companyregister")
    public String getSaveCompannyDetails(HttpSession session,
                                         @RequestParam("userotp") String userOTP,
                                         Model model) {
        try {

            Company comdata = (Company) session.getAttribute("comdata");
            String systemOTP = (String) session.getAttribute("systemotp");
            LocalTime tenMinutesLater = (LocalTime) session.getAttribute("otptime");// Added plus 10m
            LocalTime currentTime = LocalTime.now();

            // If OTPs didn't match
            if (!systemOTP.equals(userOTP)) {
                model.addAttribute("comdata", comdata);
                model.addAttribute("error", "Invalid OTP");
                return "CompaniesHTML/companyotp";
            }

            //If OTP time expired
            if (currentTime.isAfter((tenMinutesLater))) {
                model.addAttribute("comdata", comdata);
                model.addAttribute("error", "OTP expired. Request a new one.");
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
            emailService.getAfterRegistrationMail(comdata.getEmail(), comdata.getCompanyname());
            System.out.println("Successful");
//                session.setAttribute("comdata",comdata);
            model.addAttribute("register", true);
            return "CompaniesHTML/companylogin";
        } catch (Exception e) {
            model.addAttribute("error", "Error processing files: " + e.getMessage());
            return "CompaniesHTML/companydashboard";
        }
    }

    @GetMapping("/company/companylogin")
    public String getCompanyLogin() {
        return "CompaniesHTML/companylogin";
    }

    @Autowired
    JobsRepository jobsRepository;

    @PostMapping("/company/login")
    public String getCheckLogin(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {

        Optional<Company> optional = companyRepository.findByEmailAndPassword(email, password);
        System.out.println("Login Result " + optional.isPresent());
        if (optional.isPresent()) {
            int totalJobs = jobsRepository.getCountJobs(optional.get().getCompanyid());
            model.addAttribute("totaljobs", totalJobs);
            session.setAttribute("comdata", optional.get());// c
            model.addAttribute("comdata", optional.get()); //c
            return "CompaniesHTML/companydashboard";
        }

        model.addAttribute("error", "Invalid email or password");
        return "CompaniesHTML/companylogin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }

    @PostMapping("/company/getotp")
    public String getOTPHTML(@Valid @ModelAttribute("comdata") Company comdata, BindingResult bindingResult, @RequestParam("company_logo") MultipartFile companylogo, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("Inside the validation Errors");
            bindingResult.getAllErrors().forEach(err -> System.out.println(err.getDefaultMessage()));
            return "CompaniesHTML/companyform";
        } else {
            try {
            System.out.println("Inside the OTP Page");
                // If Email Already Exists
                if (companyRepository.findByEmail(comdata.getEmail()).isPresent()) {
                    model.addAttribute("error", "Email address already exists");
                    return "CompaniesHTML/companyform";
                }

                comdata.setCompanylogo(companylogo.getBytes());
                String otp = emailService.sendOTP(comdata.getEmail(), comdata.getCompanyname());
                LocalTime currentTime = LocalTime.now();
                System.out.println("OTP sent time " + currentTime);
                // Add 10 minutes
                LocalTime tenMinutesLater = currentTime.plusMinutes(10);

                session.setAttribute("comdata", comdata);
                session.setAttribute("systemotp", otp);
                session.setAttribute("otptime", tenMinutesLater);  // +10m

                System.out.println("Completed till OTP");
                return "CompaniesHTML/companyotp";
            } catch (Exception e) {
                return "error";
            }
        }

    }

    @GetMapping("/company/getresendotp")
    public String getResendOTPHTML(HttpSession session, Model model) {
        try {
            // Company Object

            Company comdata = (Company) session.getAttribute("comdata");

            String otp = emailService.sendOTP(comdata.getEmail(), comdata.getCompanyname());
            LocalTime currentTime = LocalTime.now();
            System.out.println("OTP sent time " + currentTime);
            LocalTime tenMinutesLater = currentTime.plusMinutes(10);

            model.addAttribute("comdata", comdata);
            session.setAttribute("comdata", comdata);
            session.setAttribute("systemotp", otp);
            session.setAttribute("otptime", tenMinutesLater);  // +10m
            System.out.println("completed Resend OTP Method");
            return "CompaniesHTML/companyotp";

        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/companylogo")
    public ResponseEntity<byte[]> getCompanyLogo(HttpSession session) {
        Company company = (Company) session.getAttribute("comdata");

        if (company == null) {
            return ResponseEntity.notFound().build();
        }
        int companyid = company.getCompanyid();

        Company company1 = companyRepository.findById(companyid).orElse(null);
        if (company1 == null || company1.getCompanylogo() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(company1.getCompanylogo());
    }

    @GetMapping("/company/update")
    public String getCompanyUpdatePage(HttpSession session, Model model) {
        Company company = (Company) session.getAttribute("comdata");
        model.addAttribute("comdata", company);
        return "CompaniesHTML/updatecompany";
    }

    @GetMapping("/company/profile")
    public String getCompanyProfile(HttpSession session, Model model) {
        Company company = (Company) session.getAttribute("comdata");
        model.addAttribute("comdata", company);
        return "CompaniesHTML/companyprofile";
    }

    @PostMapping("company/getcompanyupdate")
    public String getCompanyUpdate(@Valid @ModelAttribute("comdata") Company company, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> System.out.println(err.getDefaultMessage()));
            return "CompaniesHTML/updatecompany";
        }
        Company company1 = (Company) session.getAttribute("comdata");
        System.out.println("Company Id : " + company1.getCompanyid());
        Company company2 = companyRepository.findById(company1.getCompanyid()).orElse(null);
        if (company2 == null) {
            model.addAttribute("error", "Id not found!");
            return "CompaniesHTML/companydashboard";
        }

        company2.setCompanyname(company.getCompanyname());
        company2.setAddress(company.getAddress());
        company2.setContact(company.getContact());
        company2.setEmail(company.getEmail());
        company2.setDescription(company.getDescription());
        System.out.println("company logo " + company2.getCompanylogo());// byte

        companyRepository.save(company2);
        model.addAttribute("updated", "Company Info Updated");
        model.addAttribute("comdata", company2);
        session.setAttribute("comdata", company2);
        model.addAttribute("totaljobs", jobsRepository.getCountJobs(company2.getCompanyid()));
        return "CompaniesHTML/companydashboard";
    }

    @GetMapping("/forgotpassword")
    public String forgotPassword() {
        return "CompaniesHTML/forgotpassword";
    }

    @PostMapping("/company/checkemail")
    public String setForgotOTP(@RequestParam("email") String email, Model model, HttpSession session) {
        if (!companyRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email address is not register!");
            return "CompaniesHTML/forgotpassword";
        }
        try {
            String systemOTP = emailService.getPasswordResetOTP(email, companyRepository.findCompanyNameByEmail(email));
            model.addAttribute("email", email);
            LocalTime tenMinutesLater = LocalTime.now().plusMinutes(10);
            System.out.println("OTP sent time +10 added, " + tenMinutesLater);
            session.setAttribute("systemOTP", systemOTP);
            session.setAttribute("tenMinutesLater", tenMinutesLater);
            session.setAttribute("companyName", companyRepository.findCompanyNameByEmail(email));
            session.setAttribute("email", email);
            return "CompaniesHTML/verifyOTP";
        } catch (Exception e) {
            System.out.println("Error is " + e);
            return "CompaniesHTML/confirmationpage";
        }
    }

    @GetMapping("/company/verifyotp")
    public String getVerifyOTP(@RequestParam("userotp") String userOTP, HttpSession session, Model model) {

        String systemOTP = session.getAttribute("systemOTP").toString();
        LocalTime tenMinutesLater = (LocalTime) session.getAttribute("tenMinutesLater");// Added plus 10m
        LocalTime currentTime = LocalTime.now();
        System.out.println("Current time " + currentTime);
        System.out.println("Time Condition " + currentTime.isAfter(tenMinutesLater));

        // If OTPs didn't match
        if (!systemOTP.equals(userOTP)) {
            model.addAttribute("error", "Invalid OTP");
            return "CompaniesHTML/verifyOTP";
        }

        //If OTP time expired
        if (currentTime.isAfter((tenMinutesLater))) {
            model.addAttribute("error", "OTP expired. Request a new one.");
            return "CompaniesHTML/verifyOTP";
        }

        return "CompaniesHTML/resetpassword";
    }

    @GetMapping("/company/confirm")
    public String getConfirm() {
        return "CompaniesHTML/confirmationpage";
    }

    @PostMapping("/company/setpassword")
    public String getSetPassword(@RequestParam("password") String newPassword, HttpSession session, Model model) {
        String email = session.getAttribute("email").toString();
        System.out.println("Email : " + email);
        System.out.println("New Password : " + newPassword);
        companyRepository.getResetPassword(email, newPassword);
        model.addAttribute("passwordUpdated", "Password Updated");
        return "CompaniesHTML/companylogin";
    }

    @PostMapping("/company/profile/update")
    public String getUpdateProfileImage(@RequestParam()MultipartFile file,HttpSession session,Model model){
        try {
            Company company=(Company) session.getAttribute("comdata");
            companyLogoService.getLogoUpdate(file.getBytes(),company);
            model.addAttribute("comdata", company);
            model.addAttribute("profileUpdated","updated");
            return "CompaniesHTML/companyprofile";
        }catch(Exception e){
            System.out.println("Error is "+e);
            return "CompaniesHTML/companyprofile";
        }

    }

    // **7
    @GetMapping("/company/getapplicants")
    public String getApplicants(Model model ,HttpSession session){
        Company company = (Company) session.getAttribute("comdata");
        int companyId=company.getCompanyid();
//veer 10
        List<Jobs> allJobsList=jobsRepository.findByCompanyid(companyId); //10

        List<Applications>applicationsList=new ArrayList<>();
        List<JobSeeker>jobSeekerList=new ArrayList<>();
        List<Jobs>allAppliedJobs=new ArrayList<>();

        System.out.println("==========================Before For loop===============================");

        for (Jobs jobs : allJobsList) {
            int jobId = jobs.getJobid();

            List<Applications> applications = applicationRepository.findApplicationsByJobid(jobId);

            if (applications != null && !applications.isEmpty()) {
                applicationsList.addAll(applications);  // add all instead of one
            }
        }


        System.out.println(applicationsList);

        for (Applications application : applicationsList){
            Long jobSeekerId=(long) application.getJsid();//1
            int jobId=application.getJobid();
            JobSeeker jobSeeker=jobSeekerRepository.findById(jobSeekerId).get();
            Jobs jobs=jobsRepository.findById(jobId).get();
            jobSeekerList.add(jobSeeker);
            allAppliedJobs.add(jobs);
        }

        model.addAttribute("allAppliedJobs",allAppliedJobs);
        model.addAttribute("jobSeekerList",jobSeekerList);
        model.addAttribute("applicationsList",applicationsList);
        model.addAttribute("comdata",company);


        return "/ApplicationHTML/companyapplicants";
    }

}
