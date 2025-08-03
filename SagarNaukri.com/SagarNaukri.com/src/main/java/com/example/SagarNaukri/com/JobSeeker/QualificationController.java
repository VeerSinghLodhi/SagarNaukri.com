package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;

//import JobSeeker.JobSeekerRepository;
//import JobSeeker.Qualification;
//import JobSeeker.QualificationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/qualification")
public class QualificationController {
    @Autowired
    private QualificationService qualificationService;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @GetMapping("/add")
    public String showAddQualificationForm(Model model, HttpSession session) {
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return "redirect:/jobseeker/login";
        }
        // It's good practice to add the jobSeeker object to the model here
        // for the navbar (profile picture, name) like you did in the Experience form.
        jobSeekerRepository.findById(jobSeekerId).ifPresent(js -> model.addAttribute("jobSeeker", js));

        model.addAttribute("qualification", new Qualification());
        return "Jobseeker_qualification";
    }


    @PostMapping("/add")
    public String addQualification(@Valid @ModelAttribute Qualification qualification,
                                   BindingResult result,
                                   HttpSession session,
                                   Model model) { // Model is already here, good.
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return "redirect:/jobseeker/login";
        }

        if (result.hasErrors()) {
            // Re-add jobSeeker to model if there are errors, so the navbar displays correctly
            jobSeekerRepository.findById(jobSeekerId).ifPresent(js -> model.addAttribute("jobSeeker", js));
            return "Jobseeker_qualification";
        }

        qualificationService.addQualification(qualification, jobSeekerId);
        // Consider redirecting to the profile page to see the newly added qualification.
        // Or if there's a dedicated page to list qualifications, redirect there.
        return "redirect:/jobseeker/dashboard"; // Or "redirect:/jobseeker/dashboard" if that's where it should appear immediately.
    }
    @PostMapping("/delete/{id}")
    public String deleteExperience(@PathVariable Long id) {
        qualificationService.deleteQualification(id);
        return "redirect:/jobseeker/dashboard";
    }
}
