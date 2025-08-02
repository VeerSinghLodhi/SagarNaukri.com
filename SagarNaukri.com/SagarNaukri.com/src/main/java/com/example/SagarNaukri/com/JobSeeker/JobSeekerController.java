package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Skills_two.Skill;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Qualification_two.*;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Skills_two.SkillService;



import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Skills_two.SkillService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

// --- IMPORT Qualification_two related classes ---
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Qualification_two.Qualification_two;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
// --- REMOVE old Qualification imports if no longer needed anywhere else ---
// import Qualification.Qualification; // REMOVE THIS
// import Qualification.QualificationRepository; // REMOVE THIS
// import Qualification.QualificationService; // REMOVE THIS


@Controller
@RequestMapping("/jobseeker")
public class JobSeekerController {
    private static final Logger logger = LoggerFactory.getLogger(JobSeekerController.class);

    @Autowired
    private JobSeekerSkillService jobSeekerSkillService;

    @Autowired
    private SkillService skillService;

    @GetMapping({"/skills", "/skills/{jobSeekerId}"})
    public String showJobSeekerSkills(
            @PathVariable(required = false) Long jobSeekerId,
            HttpSession session,
            Model model) {

        // Use provided jobSeekerId or fall back to session
        Long jsId = jobSeekerId != null ? jobSeekerId : (Long) session.getAttribute("jobSeekerId");
        if (jsId == null) {
            return "redirect:/jobseeker/login";
        }

        List<JobSeekerSkill> jobSeekerSkills = jobSeekerSkillService.getSkillsForJobSeeker(jsId);
        List<Skill> allSkills = skillService.getAllSkills();

        model.addAttribute("jobSeekerSkills", jobSeekerSkills);
        model.addAttribute("allSkills", allSkills);
        model.addAttribute("currentJobSeekerId", jsId); // Add this for testing display

        return "jobseeker_skills";
    }

    @PostMapping({"/skills/add", "/skills/add/{jobSeekerId}"})
    public String addSkillToJobSeeker(
            @PathVariable(required = false) Long jobSeekerId,
            @RequestParam(required = false) Long skillId,
            @RequestParam(required = false) String newSkillName,
            @RequestParam(required = false) String newSkillCategory,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long jsId = jobSeekerId != null ? jobSeekerId : (Long) session.getAttribute("jobSeekerId");
        if (jsId == null) {
            return "redirect:/jobseeker/login";
        }

        try {
            if (skillId != null) {
                // Case 1: Existing skill selected from dropdown
                jobSeekerSkillService.addSkillToJobSeeker(jsId, skillId);
                redirectAttributes.addFlashAttribute("successMessage", "Skill added successfully!");
            } else if (newSkillName != null && !newSkillName.trim().isEmpty()) {
                // Case 2: New skill entered manually
                // First check if skill already exists
                Optional<Skill> existingSkill = skillService.getSkillByName(newSkillName.trim());

                if (existingSkill.isPresent()) {
                    // Skill exists - just create the association
                    jobSeekerSkillService.addSkillToJobSeeker(jsId, existingSkill.get().getId());
                    redirectAttributes.addFlashAttribute("successMessage",
                            "Skill '" + newSkillName + "' was already in our system and has been added to your profile!");
                } else {
                    // Skill doesn't exist - create new skill and association
                    Skill newSkill = new Skill(newSkillName.trim(),
                            newSkillCategory != null ? newSkillCategory.trim() : "Uncategorized");
                    Skill savedSkill = skillService.saveSkill(newSkill);
                    jobSeekerSkillService.addSkillToJobSeeker(jsId, savedSkill.getId());
                    redirectAttributes.addFlashAttribute("successMessage",
                            "New skill '" + newSkillName + "' added successfully!");
                }
            }
        } catch (DataIntegrityViolationException e) {
            logger.error("Duplicate skill error", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "This skill already exists in our system. Please select it from the dropdown.");
        } catch (Exception e) {
            logger.error("Error adding skill", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error adding skill: " + e.getMessage());
        }

        return "redirect:/jobseeker/dashboard" + (jobSeekerId != null ? "/" + jobSeekerId : "");
    }

    @PostMapping("/skills/remove")
    public String removeSkillFromJobSeeker(
            @RequestParam("skillId") Long skillId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return "redirect:/jobseeker/login";
        }

        try {
            jobSeekerSkillService.removeSkillFromJobSeeker(jobSeekerId, skillId);
            redirectAttributes.addFlashAttribute("successMessage", "Skill removed successfully!");
        } catch (Exception e) {
            logger.error("Error removing skill", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error removing skill: " + e.getMessage());
        }

        return "redirect:/jobseeker/dashboard#skills";
    }
    @Autowired
    ExperienceRepository experienceRepository;
    @Autowired
    ExperienceService experienceService;

    @Autowired
    QualificationService_two qualification_twoService;
    @Autowired
    QualificationRepository_two qualification_twoRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    @Autowired
    private JobSeekerService jobSeekerService;
    @Autowired
    private InstituteRepository_two instituteRepository;
    @Autowired
    private InstituteService_two instituteService_two; // This is the autowired instance

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("jobSeeker", new JobSeeker());
        return "jobseeker_register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute JobSeeker jobSeeker,
                           @RequestParam("profilePictureFile") MultipartFile profilePictureFile,
                           @RequestParam("resumeFile") MultipartFile resumeFile,
                           Model model) throws IOException {
        try {
            jobSeekerService.registerJobSeeker(jobSeeker, profilePictureFile, resumeFile);
            model.addAttribute("message", "Registration successful! Please check your email to verify your account.");
            return "jobseeker_register";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "jobseeker_register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginJobSeeker(@RequestParam String email,
                                 @RequestParam String password,
                                 HttpSession session,
                                 Model model) {
        Optional<JobSeeker> optional = jobSeekerRepository.findByEmail(email);

        if (optional.isPresent()) {
            JobSeeker jobSeeker = optional.get();
            if (!jobSeeker.isVerified()) {
                model.addAttribute("error", "Your account is not verified. Please check your email for the verification link.");
                return "login";
            }
            if (jobSeeker.getPassword().equals(password)) {
                session.setAttribute("jobSeekerId", jobSeeker.getJSId());
                session.setAttribute("jobSeekerName", jobSeeker.getName());
                return "redirect:/jobseeker/dashboard";
            }
        }

        model.addAttribute("error", "Invalid email or password");
        return "login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return "redirect:/jobseeker/login";
        }

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElse(null);
        if (jobSeeker == null) {
            return "redirect:/jobseeker/login";
        }

        if (!jobSeeker.isVerified()) {
            session.invalidate();
            model.addAttribute("error", "Your account is not verified. Please verify your email to access the dashboard.");
            return "login";
        }

        List<Qualification_two> qualifications = qualification_twoService.getQualificationsByJobSeekerId(jobSeekerId);
        List<Experience> experiences = experienceService.getExperiencesByJobSeeker(jobSeekerId);
        List<JobSeekerSkill> jobSeekerSkills = jobSeekerSkillService.getSkillsForJobSeeker(jobSeekerId);

        model.addAttribute("jobSeeker", jobSeeker);
        model.addAttribute("qualifications", qualifications);
        model.addAttribute("experiences", experiences);
        model.addAttribute("jobSeekerSkills", jobSeekerSkills);

        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/jobseeker/login";
    }

    @GetMapping("/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(HttpSession session) {
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return ResponseEntity.notFound().build();
        }

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElse(null);
        if (jobSeeker == null || jobSeeker.getProfilePicture() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG
                .body(jobSeeker.getProfilePicture());
    }

    @GetMapping("/resume")
    public ResponseEntity<byte[]> getResume(HttpSession session) {
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return ResponseEntity.notFound().build();
        }

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElse(null);
        if (jobSeeker == null || jobSeeker.getResume() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF) // or whatever format you accept
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\"")
                .body(jobSeeker.getResume());
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return "redirect:/jobseeker/login";
        }

        JobSeeker js = jobSeekerRepository.findById(jobSeekerId).orElse(null);
        if (js == null) return "redirect:/jobseeker/login";

        // --- CHANGE STARTS HERE ---
        List<Qualification_two> quals =qualification_twoService.getQualificationsByJobSeekerId(jobSeekerId); // Changed to qualification_twoService
        // --- CHANGE ENDS HERE ---

        List<Experience> exps = experienceService.getExperiencesByJobSeeker(jobSeekerId);
       // List<JobSeekerSkill> skills = skillService.getJobSeekerSkills(jobSeekerId);

        model.addAttribute("jobSeeker", js);
     //   model.addAttribute("jobSeekerSkills", skills);
        model.addAttribute("experiences", exps);
        model.addAttribute("qualifications", quals); // This attribute name can remain the same

        return "profile";
    }

//    @GetMapping("/skills_add")
//    public String addSkills(){
//        return "jobseeker-skills";
//    }

    @GetMapping("/edit")
    public String showEditForm(HttpSession session, Model model) {
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return "redirect:/jobseeker/login";
        }

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElse(null);
        if (jobSeeker == null) {
            return "redirect:/jobseeker/login";
        }

        model.addAttribute("jobSeeker", jobSeeker);
        return "edit_profile";
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute JobSeeker updatedJobSeeker,
                                @RequestParam(value = "profilePictureFile", required = false) MultipartFile profilePictureFile,
                                @RequestParam(value = "resumeFile", required = false) MultipartFile resumeFile,
                                HttpSession session,
                                Model model) {
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return "redirect:/jobseeker/login";
        }

        try {
            JobSeeker existingJobSeeker = jobSeekerRepository.findById(jobSeekerId).orElse(null);
            if (existingJobSeeker == null) {
                return "redirect:/jobseeker/login";
            }

            // Update basic info (excluding email)
            existingJobSeeker.setName(updatedJobSeeker.getName());
            existingJobSeeker.setContact(updatedJobSeeker.getContact());
            existingJobSeeker.setAddress(updatedJobSeeker.getAddress());

            // Handle profile picture update
            if (profilePictureFile != null && !profilePictureFile.isEmpty()) {
                if (profilePictureFile.getSize() > 10_000_000) {
                    model.addAttribute("error", "Profile picture too large (max 10MB)");
                    return "edit_profile";
                }
                existingJobSeeker.setProfilePicture(profilePictureFile.getBytes());
            }

            // Handle resume update
            if (resumeFile != null && !resumeFile.isEmpty()) {
                if (resumeFile.getSize() > 10_000_000) {
                    model.addAttribute("error", "Resume too large (max 10MB)");
                    return "edit_profile";
                }
                existingJobSeeker.setResume(resumeFile.getBytes());
            }

            jobSeekerRepository.save(existingJobSeeker);
            model.addAttribute("success", "Profile updated successfully!");
            return "edit_profile";

        } catch (IOException e) {
            model.addAttribute("error", "Error processing files: " + e.getMessage());
            return "edit_profile";
        }
    }

    @GetMapping("/verify")
    public String verifyJobSeeker(@RequestParam("token") String token, Model model) {
        if (jobSeekerService.verifyJobSeeker(token)) {
            model.addAttribute("message", "Your account has been successfully verified! You can now log in.");
            return "login";
        } else {
            model.addAttribute("error", "Invalid or expired verification token.");
            return "login"; // Or a dedicated error page
        }
    }
//    @GetMapping("/add-qualification")
//    public String showAddQualificationForm(HttpSession session, Model model) {
//        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
//        if (jobSeekerId == null) {
//            return "redirect:/jobseeker/login";
//        }
//
//        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElse(null);
//        if (jobSeeker == null) {
//            return "redirect:/jobseeker/login";
//        }
//
//        // Create a new qualification with the job seeker pre-set
//        Qualification_two qualification = new Qualification_two();
//        qualification.setJobSeeker(jobSeeker);  // This ensures the relationship is established
//
//        model.addAttribute("qualification", qualification);
//        model.addAttribute("qualificationTypes", QualificationType_two.values());
//        return "qualification_form";
//    }
@GetMapping("/add-qualification")
public String showAddQualificationForm(HttpSession session, Model model) {
    Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
    if (jobSeekerId == null) {
        return "redirect:/jobseeker/login";
    }

    JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElse(null);
    if (jobSeeker == null) {
        return "redirect:/jobseeker/login";
    }
   //___________
//    Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
//    if (jobSeekerId == null) {
//        return "redirect:/jobseeker/login";
//    }
//
//    // Fetch JobSeeker and add to model for profile picture and name
    Optional<JobSeeker> jobSeekerOptional = jobSeekerService.getJobSeekerById(jobSeekerId);
    if (jobSeekerOptional.isEmpty()) {
        return "redirect:/jobseeker/login";
    }
    model.addAttribute("jobSeeker", jobSeekerOptional.get());
//
//    model.addAttribute("experience", new Experience());
//    return "add_experience";
//


  //  _________
    List<Institute_two> institutes = instituteRepository.findAll(); // Assuming you have instituteRepository injected
    Qualification_two qualification = new Qualification_two();
    qualification.setJobSeeker(jobSeeker);
   // model.addAttribute("jobSeeker",jobSeeker);
    model.addAttribute("jobSeekerId", jobSeekerId);
    model.addAttribute("qualification", qualification);
    model.addAttribute("institutes", institutes);
    model.addAttribute("qualificationTypes", QualificationType_two.values());

    return "qualification_form";
}

    // POST method to handle form submission (no changes needed here, service handles logic)
    @PostMapping("/add-qualification")
    public String addQualification(@ModelAttribute("qualification") Qualification_two qualification,
                                   Model model,
                                   HttpSession session) {
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");

        if (jobSeekerId == null) {
            return "redirect:/jobseeker/login";
        }

        try {
            // This line is crucial and correct: re-attaches the managed JobSeeker entity
            JobSeeker currentJobSeeker = jobSeekerRepository.findById(jobSeekerId)
                    .orElseThrow(() -> new IllegalArgumentException("JobSeeker not found."));
            qualification.setJobSeeker(currentJobSeeker);

            // The service now handles whether the institute is new or existing
            qualification_twoService.saveQualification(qualification);
            model.addAttribute("successMessage", "Qualification added successfully!");
            return "redirect:/jobseeker/dashboard#qualifications";
        } catch (IllegalArgumentException e) {
            logger.warn("Error adding qualification: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            // Re-add necessary attributes for the form to be displayed again with errors
            model.addAttribute("institutes", instituteRepository.findAll()); // Re-fetch institutes
            model.addAttribute("qualificationTypes", QualificationType_two.values());
            return "qualification_form";
        } catch (Exception e) {
            logger.error("An unexpected error occurred while adding qualification", e);
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            model.addAttribute("institutes", instituteRepository.findAll()); // Re-fetch institutes
            model.addAttribute("qualificationTypes", QualificationType_two.values());
            return "qualification_form";
        }
    }
//
//    @PostMapping("/save-qualification")
//    public String saveQualification(@ModelAttribute("qualification") @Valid Qualification_two qualification,
//                                    BindingResult bindingResult,
//                                    @RequestParam(value = "instituteSelect", required = false) String instituteSelectValue, // Keep this for JS logic feedback
//                                    @RequestParam(value = "newInstituteName", required = false) String newInstituteName,
//                                    HttpSession session,
//                                    Model model,
//                                    RedirectAttributes redirectAttributes) {
//
//        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
//        if (jobSeekerId == null) {
//            return "redirect:/jobseeker/login";
//        }
//
//        // --- Server-side validation check using BindingResult ---
//        if (bindingResult.hasErrors()) {
//            logger.warn("Form has binding errors: {}", bindingResult.getAllErrors());
//            // Re-populate model attributes for the form to be displayed correctly
//            model.addAttribute("institutes", instituteRepository.findAll());
//            model.addAttribute("qualificationTypes", QualificationType_two.values());
//            model.addAttribute("jobSeekerId", jobSeekerId);
//            // This is crucial: if an error occurs, ensure newInstituteGroup visibility is maintained
//            if ("NEW".equals(instituteSelectValue) || (newInstituteName != null && !newInstituteName.trim().isEmpty())) {
//                model.addAttribute("showNewInstituteGroup", true);
//            }
//            model.addAttribute("errorMessage", "Please correct the errors in the form.");
//            return "qualification_form";
//        }
//
//        try {
//            // Re-attach the managed JobSeeker entity to the Qualification object
//            JobSeeker currentJobSeeker = jobSeekerRepository.findById(jobSeekerId)
//                    .orElseThrow(() -> new IllegalArgumentException("JobSeeker not found."));
//            qualification.setJobSeeker(currentJobSeeker);
//
//            // --- Simplified Institute Handling ---
//            // Now we rely on the hidden input for existing institute ID,
//            // and newInstituteName for new institute.
//            // The `qualification.institute` object might come with just an ID
//            // or be a new object with only a name.
//
//            // Case 1: "Other (Add New Institute)" was selected
//            if ("NEW".equals(instituteSelectValue)) {
//                if (newInstituteName == null || newInstituteName.trim().isEmpty()) {
//                    // This error is caught by BindingResult if you add @NotBlank to Institute_two.name
//                    // But explicitly throwing it here too is fine for a specific message
//                    throw new IllegalArgumentException("New institute name cannot be empty when 'Other' is selected.");
//                }
//                // Create a new Institute_two object, ONLY with the name from the form
//                // The service will handle finding/saving it.
//                Institute_two newInst = new Institute_two();
//                newInst.setName(newInstituteName.trim());
//                qualification.setInstitute(newInst);
//            }
//            // Case 2: An existing institute was selected from the dropdown
//            // In this case, qualification.institute.id should already be populated by th:field="*{institute.id}"
//            // The qualification.institute object will likely be a detached entity with just an ID set.
//            // The QualificationService_two will handle fetching the managed version.
//            // No 'else' here, as the model binding takes care of it, and service validates existence.
//
//            // Critical check: Ensure an institute object is set at all
//            if (qualification.getInstitute() == null ||
//                    (qualification.getInstitute().getId() == null && (newInstituteName == null || newInstituteName.trim().isEmpty()))) {
//                throw new IllegalArgumentException("Please select an institute or provide a new institute name.");
//            }
//
//
//            // Save/Update the qualification using the service, which now handles Institute management
//            String successMsg;
//            if (qualification.getId() == null) { // This is an add operation
//                qualification_twoService.saveQualification(qualification);
//                successMsg = "Qualification added successfully!";
//            } else { // This is an update operation
//                qualification_twoService.updateQualification(qualification.getId(), qualification);
//                successMsg = "Qualification updated successfully!";
//            }
//
//            redirectAttributes.addFlashAttribute("successMessage", successMsg);
//            return "redirect:/jobseeker/dashboard#qualifications";
//        } catch (IllegalArgumentException e) {
//            logger.warn("Error saving/updating qualification: {}", e.getMessage());
//            model.addAttribute("errorMessage", e.getMessage());
//            // Re-add necessary attributes for the form to be displayed again with errors
//            model.addAttribute("institutes", instituteRepository.findAll());
//            model.addAttribute("qualificationTypes", QualificationType_two.values());
//            model.addAttribute("jobSeekerId", jobSeekerId);
//            model.addAttribute("qualification", qualification);
//            // Maintain the "showNewInstituteGroup" visibility on error
//            if (e.getMessage().contains("New institute name cannot be empty.") || "NEW".equals(instituteSelectValue)) {
//                model.addAttribute("showNewInstituteGroup", true);
//            }
//            return "qualification_form";
//        } catch (Exception e) {
//            logger.error("An unexpected error occurred while saving/updating qualification", e);
//            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
//            model.addAttribute("institutes", instituteRepository.findAll());
//            model.addAttribute("qualificationTypes", QualificationType_two.values());
//            model.addAttribute("jobSeekerId", jobSeekerId);
//            model.addAttribute("qualification", qualification);
//            return "qualification_form";
//        }
//    }
@PostMapping("/save-qualification")
public String saveQualification(@ModelAttribute("qualification") @Valid Qualification_two qualification,
                                BindingResult bindingResult,
                                @RequestParam(value = "instituteSelect", required = false) String instituteSelectValue,
                                @RequestParam(value = "newInstituteName", required = false) String newInstituteName,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {

    Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
    if (jobSeekerId == null) {
        return "redirect:/jobseeker/login";
    }

    // Server-side validation
    if (bindingResult.hasErrors()) {
        logger.warn("Form has binding errors: {}", bindingResult.getAllErrors());
        model.addAttribute("institutes", instituteRepository.findAll());
        model.addAttribute("qualificationTypes", QualificationType_two.values());
        model.addAttribute("jobSeekerId", jobSeekerId);
        if ("NEW".equals(instituteSelectValue) || (newInstituteName != null && !newInstituteName.trim().isEmpty())) {
            model.addAttribute("showNewInstituteGroup", true);
        }
        model.addAttribute("errorMessage", "Please correct the errors in the form.");
        return "qualification_form";
    }

    try {
        // Re-attach the managed JobSeeker entity
        JobSeeker currentJobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new IllegalArgumentException("JobSeeker not found."));
        qualification.setJobSeeker(currentJobSeeker);

        // Debug logging for institute selection
        logger.info("Institute selection - select value: {}, new name: {}, qualification institute: {}",
                instituteSelectValue, newInstituteName, qualification.getInstitute());

        // Handle institute selection
        boolean isNewInstituteSelected = "NEW".equals(instituteSelectValue) ||
                (qualification.getInstitute() != null &&
                        qualification.getInstitute().getId() == null &&
                        newInstituteName != null &&
                        !newInstituteName.trim().isEmpty());

        if (isNewInstituteSelected) {
            if (newInstituteName == null || newInstituteName.trim().isEmpty()) {
                throw new IllegalArgumentException("New institute name cannot be empty when 'Other' is selected.");
            }

            // Create and save new institute
            Institute_two newInst = new Institute_two();
            newInst.setName(newInstituteName.trim());
            Institute_two savedInstitute = instituteService_two.saveInstitute(newInst);
            qualification.setInstitute(savedInstitute);
            logger.info("Saved new institute with ID: {}", savedInstitute.getId());
        } else {
            // For existing institute, check if one was selected
            if (qualification.getInstitute() == null || qualification.getInstitute().getId() == null) {
                throw new IllegalArgumentException("Please select an institute from the dropdown or choose 'Other' to add a new one.");
            }

            // Verify the institute exists
            Optional<Institute_two> existingInstitute = instituteService_two.getInstituteById(qualification.getInstitute().getId());
            if (existingInstitute.isEmpty()) {
                throw new IllegalArgumentException("The selected institute could not be found. Please select a valid institute.");
            }
            qualification.setInstitute(existingInstitute.get());
            logger.info("Using existing institute with ID: {}", qualification.getInstitute().getId());
        }

        // Save/Update qualification
        String successMsg;
        if (qualification.getId() == null) {
            Qualification_two savedQualification = qualification_twoService.saveQualification(qualification);
            successMsg = "Qualification added successfully!";
            logger.info("Saved new qualification with ID: {}", savedQualification.getId());
        } else {
            Qualification_two updatedQualification = qualification_twoService.updateQualification(qualification.getId(), qualification);
            successMsg = "Qualification updated successfully!";
            logger.info("Updated qualification with ID: {}", updatedQualification.getId());
        }

        redirectAttributes.addFlashAttribute("successMessage", successMsg);
        return "redirect:/jobseeker/dashboard#qualifications";
    } catch (IllegalArgumentException e) {
        logger.warn("Validation error saving/updating qualification: {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("institutes", instituteRepository.findAll());
        model.addAttribute("qualificationTypes", QualificationType_two.values());
        model.addAttribute("jobSeekerId", jobSeekerId);
        model.addAttribute("qualification", qualification);
        if (e.getMessage().contains("New institute name") ||
                (qualification.getInstitute() != null && qualification.getInstitute().getId() == null)) {
            model.addAttribute("showNewInstituteGroup", true);
        }
        return "qualification_form";
    } catch (Exception e) {
        logger.error("Unexpected error saving/updating qualification", e);
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        model.addAttribute("institutes", instituteRepository.findAll());
        model.addAttribute("qualificationTypes", QualificationType_two.values());
        model.addAttribute("jobSeekerId", jobSeekerId);
        model.addAttribute("qualification", qualification);
        return "qualification_form";
    }
}
    @PostMapping("/qualification/delete/{id}")
    public String deleteQualification(@PathVariable Long id,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return "redirect:/jobseeker/login";
        }

        try {
            Optional<Qualification_two> qualification = qualification_twoService.getQualificationById(id);
            if (qualification.isPresent() && qualification.get().getJobSeeker().getJSId().equals(jobSeekerId)) {
                qualification_twoService.deleteQualification(id);
                redirectAttributes.addFlashAttribute("successMessage", "Qualification deleted successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting qualification");
        }

        return "redirect:/jobseeker/dashboard#qualifications";
    }
}