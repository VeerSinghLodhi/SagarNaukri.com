package SagarNaukriMerge.SagarNaukriMerge.Jobs;


import SagarNaukriMerge.SagarNaukriMerge.CompaniesPackage.Company;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeekerRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class JobsController {

    @GetMapping("/addjob")
    public String addjob(Model model ,HttpSession session){
        Company company=(Company) session.getAttribute("comdata");
        model.addAttribute("jobs" , new Jobs());
        model.addAttribute("comdata" , company);
        return "JobsHtml/addjobs";
    }

    @GetMapping("/back")
    public String getBack(HttpSession session,Model model){
        Company comdata=(Company)session.getAttribute("comdata");
        int totalJobs = jobsRepository.getCountJobs(comdata.getCompanyid());
        model.addAttribute("totaljobs" , totalJobs);
        model.addAttribute("comdata",comdata);
        return "CompaniesHTML/companydashboard";
    }

    @Autowired
    JobsRepository jobsRepository;
    @PostMapping("/company/savejob")
    public String saveJobs(@Valid @ModelAttribute("jobs") Jobs jobs , BindingResult bindingResult ,  HttpSession session , Model model){
        if(bindingResult.hasErrors()){
            return "JobsHtml/addjobs";
        }

        Company company = (Company) session.getAttribute("comdata");

        Date date=new Date();
        Jobs jobs1 = new Jobs();
        jobs1.setCompanyid(company.getCompanyid());
        jobs1.setJobtitle(jobs.getJobtitle());
        jobs1.setJob_type(jobs.getJob_type());
        jobs1.setDescription(jobs.getDescription());
        jobs1.setExperience(jobs.getExperience());
        jobs1.setLocation(jobs.getLocation());
        jobs1.setDeadline(jobs.getDeadline());
        jobs1.setDateposted(date);
        jobs1.setSkills(jobs.getSkills());
        jobs1.setMin_salary(jobs.getMin_salary());
        jobs1.setMax_salary(jobs.getMax_salary());
        jobs1.setIsenabled(true);
        jobsRepository.save(jobs1);
        model.addAttribute("comdata" , company);
        model.addAttribute("totaljobs" , jobsRepository.getCountJobs(company.getCompanyid()));
        session.setAttribute("comdata" , company);
        model.addAttribute("newjob","new job added");
        return "CompaniesHTML/companydashboard";
    }

    @GetMapping("/getshowalljobs")
    public String getShowAllJobs(HttpSession session ,Model model){
        Company company = (Company) session.getAttribute("comdata");
    int companyid =company.getCompanyid();

        List<Jobs> jobsList = jobsRepository.findByCompanyid(companyid);
        session.setAttribute("comdata" , company);
        model.addAttribute("jobsList" , jobsList);
        model.addAttribute("comdata" ,company);
        return "JobsHtml/showalljobs";
    }

    @GetMapping("/company/updatejob")
    public String updateJob(@RequestParam("jobid") String jobid, Model model, HttpSession session){
        System.out.println("Job is "+jobid);
        Company company = (Company) session.getAttribute("comdata");
        Optional<Jobs>  optional =  jobsRepository.findByJobid(Integer.parseInt(jobid));
        if(!optional.isPresent()){
            List<Jobs> jobsList = jobsRepository.findByCompanyid(company.getCompanyid());
            session.setAttribute("comdata",company);
            model.addAttribute("jobsList" , jobsList);
            model.addAttribute("comdata" ,company);
            return "JobsHtml/showalljobs";
        }
        Jobs jobs=optional.get();
        session.setAttribute("comdata",company);
        model.addAttribute("jobs",jobs);
        return "JobsHtml/updatejobs";
    }

    @PostMapping("/company/jobupdate")
    public String jobUpdate(@Valid @ModelAttribute("jobs") Jobs jobs,BindingResult bindingResult,HttpSession session , Model model){
        if(bindingResult.hasErrors()){
            return "JobsHtml/addjobs";
        }
        Optional<Jobs> optional = jobsRepository.findByJobid(jobs.getJobid());
        Jobs jobs1 = optional.get();

        jobs1.setJobtitle(jobs.getJobtitle());
        jobs1.setDescription(jobs.getDescription());
        jobs1.setLocation(jobs.getLocation());
        jobs1.setMax_salary(jobs.getMax_salary());
        jobs1.setMin_salary(jobs.getMin_salary());
        jobs1.setDeadline(jobs.getDeadline());
        jobs1.setExperience(jobs.getExperience());
        jobs1.setSkills(jobs.getSkills());
        jobs1.setJob_type(jobs.getJob_type());
        jobsRepository.save(jobs1);
        Company company = (Company) session.getAttribute("comdata");
        List<Jobs> jobsList = jobsRepository.findByCompanyid(company.getCompanyid());
        session.setAttribute("comdata" , company);
        model.addAttribute("jobsList" , jobsList);
        model.addAttribute("comdata" ,company);
        model.addAttribute("isPopupEnabled",true);
        model.addAttribute("popupType" , "jobUpdated");
        return "JobsHtml/showalljobs";
    }

    @GetMapping("/company/jobdelete")
    public String jobDelete(@RequestParam("jobid") int jobid , HttpSession session , Model model){

        jobsRepository.deleteJobById(jobid);
        Company company = (Company) session.getAttribute("comdata");
        List<Jobs> jobsList = jobsRepository.findByCompanyid(company.getCompanyid());
        session.setAttribute("comdata" , company);
        model.addAttribute("jobsList" , jobsList);
        model.addAttribute("comdata" ,company);
        model.addAttribute("isPopupEnabled",true);
        model.addAttribute("popupType" , "jobDeleted");
        return "JobsHtml/showalljobs";
    }

    @Autowired
    JobSeekerRepository jobSeekerRepository;

    @GetMapping("/alljobs")
    public String getAllJobs(Model model, HttpSession session){
//
        JobSeeker jobSeeker = jobSeekerRepository.findById((Long)session.getAttribute("jobSeekerId")).orElse(null);
        model.addAttribute("jobSeeker",jobSeeker);
        model.addAttribute("jobsList",jobsRepository.findAll());
        session.setAttribute("jobSeekerId", jobSeeker.getJSId());
        session.setAttribute("jobSeekerName", jobSeeker.getName());
        return "JobsHtml/allcompaniesjobs";
    }


}
