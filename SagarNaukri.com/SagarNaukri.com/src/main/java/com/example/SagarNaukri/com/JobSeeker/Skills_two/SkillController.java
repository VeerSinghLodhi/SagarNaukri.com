package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Skills_two;

//src/main/java/com/example/app/skill/SkillController.java

//import com.example.emailVerification.emailverification.Skill_two.SkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/skills")
public class SkillController {

    private static final Logger logger = LoggerFactory.getLogger(SkillController.class);

    private final SkillService skillService; // Use the service instead of direct repo access

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public String showSkillForm(Model model) {
        model.addAttribute("skill", new Skill());
        List<Skill> allSkills = skillService.getAllSkills(); // Use service
        model.addAttribute("allSkills", allSkills);
        return "skill_form";
    }

    @PostMapping("/add")
    public String addSkill(@ModelAttribute("skill") @Valid Skill skill,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.warn("Skill form has errors: {}", bindingResult.getAllErrors());
            model.addAttribute("errorMessage", "Please correct the highlighted errors.");
            model.addAttribute("allSkills", skillService.getAllSkills()); // Use service
            return "skill_form";
        }

        try {
            skillService.saveSkill(skill); // Use service
            logger.info("Skill added successfully: {} (Category: {})", skill.getName(), skill.getCategory());
            redirectAttributes.addFlashAttribute("successMessage", "Skill '" + skill.getName() + "' added successfully!");
            return "redirect:/skills";
        } catch (IllegalArgumentException e) { // Catch the specific IllegalArgumentException from service
            logger.warn("Validation error adding skill: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("allSkills", skillService.getAllSkills()); // Use service
            return "skill_form";
        } catch (Exception e) {
            logger.error("Error adding skill: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "An unexpected error occurred while adding skill. Please try again.");
            model.addAttribute("allSkills", skillService.getAllSkills()); // Use service
            return "skill_form";
        }
    }

    // NEW REST ENDPOINT FOR AJAX CALLS
    @PostMapping("/api/suggest-category")
    @ResponseBody // This tells Spring to return the data directly as JSON
    public ResponseEntity<SkillSuggestionResponse> suggestSkillCategory(@RequestBody Map<String, String> payload) {
        String skillName = payload.get("skill");
        logger.info("API request received for skill suggestion for: {}", skillName);

        if (skillName == null || skillName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new SkillSuggestionResponse(false, "Skill name cannot be empty.", null, null));
        }

        try {
            // Call the service to get suggestions from Gemini API
            SkillSuggestionResponse response = skillService.getSkillSuggestions(skillName);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation error during skill suggestion: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new SkillSuggestionResponse(false, e.getMessage(), null, null));
        } catch (Exception e) {
            logger.error("Error calling Gemini API for skill '{}': {}", skillName, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SkillSuggestionResponse(false, "Failed to get suggestions due to an internal error.", null, null));
        }
    }
}