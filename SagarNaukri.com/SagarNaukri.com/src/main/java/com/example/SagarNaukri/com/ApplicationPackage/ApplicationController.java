package SagarNaukriMerge.SagarNaukriMerge.ApplicationPackage;

import SagarNaukriMerge.SagarNaukriMerge.CompaniesPackage.Company;
import SagarNaukriMerge.SagarNaukriMerge.CompaniesPackage.CompanyRepository;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeekerRepository;
import SagarNaukriMerge.SagarNaukriMerge.Jobs.Jobs;
import SagarNaukriMerge.SagarNaukriMerge.Jobs.JobsRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ApplicationController {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    JobsRepository jobsRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    JobSeekerRepository jobSeekerRepository;

    @GetMapping("/apply/{jobId}")
    public String showApplicationForm(@PathVariable("jobId") int jobId, Model model,HttpSession session) {
//        Jobs job = jobsRepository.findById(jobId).orElse(null);

        // Add job and empty JobSeeker (or logged-in JobSeeker) to model
        model.addAttribute("jobId", jobId);
        model.addAttribute("jobSeekerId", session.getAttribute("jobSeekerId")); // or fetch the logged-in one

        return "/ApplicationHTML/apply"; // return apply.html
    }



    @PostMapping("/submitApplication")
    public String submitApplication(@RequestParam("jobId") int jobId,
                                    @RequestParam("jobSeekerId") int jobSeekerId,
                                    @RequestParam("coverLetter") String coverLetter,
                                    @RequestParam("resumeFile") MultipartFile resumeFile,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        byte [] resume=null;
        // Save resume to a folder or DB
        if(!resumeFile.isEmpty()) {
//            String fileName = resumeFile.getOriginalFilename();
            try {
                resume = resumeFile.getBytes();
            }catch(Exception e){
                System.out.println("Error is "+e);
            }
        }

        System.out.println(resume);
        // Create application entry
        Applications application = new Applications();
        application.setJobid(jobId);
        application.setJsid(jobSeekerId);
        application.setResume(resume); // path or URL
        application.setDateapplied(new Date());
        application.setApplicationstatus("Pending");

        applicationRepository.save(application);

        redirectAttributes.addAttribute("jobMessage", true);
        return "redirect:/jobseeker/dashboard"; // Dashboard mapping
    }

//    @GetMapping("/jobseeker/applications")
//    public String showApplications(Model model, HttpSession session) {
//
//        List<Applications> applications = (List<Applications>)applicationRepository.findById((Integer) session.getAttribute("jobSeekerId"));
//        System.out.println("Application Size "+applications.size());
//
//        List<Jobs>appliedJobs=null;
//        for(int i=0;i<applications.size();i++){
//            System.out.println("Veer - "+i);
//            appliedJobs.add(jobsRepository.findByJobId2(appliedJobs.get(i).getJobid()));
//        }
//
//        model.addAttribute("applications", applications);
//        model.addAttribute("appliedJobs", applications);
//        return "/ApplicationHTML/jobapplied";
//    }

    @GetMapping("/jobseeker/applications")
    public String showApplications(Model model, HttpSession session) {
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");

        // Step 1: Fetch all Applications by jobSeekerId
        List<Applications> applications = applicationRepository.findByJsid(jobSeekerId);
        System.out.println("Application Size: " + applications.size());

        // Step 2: Initialize the list for jobs
        List<Jobs> appliedJobs = new ArrayList<>();
        List<Company>companyList=new ArrayList<>();

        // Step 3: Loop through Applications and fetch jobs using jobid field
        for (Applications app : applications) {
            int jobId = app.getJobid();  // Assuming your Applications entity has jobid field
            Jobs job = jobsRepository.findByJobId2(jobId);  // Your custom query
            appliedJobs.add(job);
        }


        for (Jobs jobs : appliedJobs) {
            int jobId = jobs.getCompanyid();
            Company company = companyRepository.findByCompany2(jobId);  // Your custom query
            companyList.add(company);
        }

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElse(null);
        if (jobSeeker == null) {
            return "redirect:/jobseeker/login";
        }

        // Step 4: Pass both to the view
        model.addAttribute("applications", applications);
        model.addAttribute("appliedJobs", appliedJobs);
        model.addAttribute("companyList", companyList);
        model.addAttribute("jobSeeker", jobSeeker);

        return "/ApplicationHTML/jobapplied";
    }




}
