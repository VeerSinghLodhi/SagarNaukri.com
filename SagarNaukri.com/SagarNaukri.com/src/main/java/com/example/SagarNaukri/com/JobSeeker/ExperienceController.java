package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/experience")
public class ExperienceController {

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private JobSeekerService jobSeekerService;

    @GetMapping("/add")
    public String showAddExperienceForm(Model model, HttpSession session) {
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return "redirect:/jobseeker/login";
        }

        // Fetch JobSeeker and add to model for profile picture and name
        Optional<JobSeeker> jobSeekerOptional = jobSeekerService.getJobSeekerById(jobSeekerId);
        if (jobSeekerOptional.isEmpty()) {
            return "redirect:/jobseeker/login";
        }
        model.addAttribute("jobSeeker", jobSeekerOptional.get());

        model.addAttribute("experience", new Experience());
        return "add_experience";
    }

    @PostMapping("/add")
    public String addExperience(@Valid @ModelAttribute Experience experience,
                                BindingResult result,
                                HttpSession session,
                                Model model) {
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return "redirect:/jobseeker/login";
        }

        if (result.hasErrors()) {
            // Re-add JobSeeker and the 'experience' object with errors to the model
            Optional<JobSeeker> jobSeekerOptional = jobSeekerService.getJobSeekerById(jobSeekerId);
            if (jobSeekerOptional.isPresent()) {
                model.addAttribute("jobSeeker", jobSeekerOptional.get());
            }
            model.addAttribute("experience", experience);
            return "add_experience";
        }

        experienceService.addExperience(experience, jobSeekerId);
        // Changed redirect to profile as it's more logical to see the added experience there
        return "redirect:/jobseeker/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return "redirect:/jobseeker/dashboard";
    }
}