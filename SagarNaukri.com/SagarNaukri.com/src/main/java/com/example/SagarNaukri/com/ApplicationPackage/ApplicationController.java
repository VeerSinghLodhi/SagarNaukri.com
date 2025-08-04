package SagarNaukriMerge.SagarNaukriMerge.ApplicationPackage;

import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker;
import SagarNaukriMerge.SagarNaukriMerge.Jobs.Jobs;
import SagarNaukriMerge.SagarNaukriMerge.Jobs.JobsRepository;
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

import java.util.Date;
import java.util.List;

@Controller
public class ApplicationController {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    JobsRepository jobsRepository;

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

    @GetMapping("/jobseeker/applications")
    public String showApplications(Model model, HttpSession session) {

        List<Applications> applications = applicationRepository.findByJsid((Long)session.getAttribute("jobSeekerId"));
        model.addAttribute("applications", applications);
        return "/ApplicationHTML/jobapplied";
    }



}
