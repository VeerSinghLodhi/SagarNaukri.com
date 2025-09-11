package SagarNaukriMerge.SagarNaukriMerge.Messaging;

import SagarNaukriMerge.SagarNaukriMerge.CompaniesPackage.Company;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker; // Import JobSeeker
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/conversations")
public class ConversationController {
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping("/start")
    public Conversation startConversation(@RequestParam Long otherJobSeekerId, HttpSession session) {
        // Assuming the logged-in job seeker's info is in the session
        JobSeeker loggedInUser = (JobSeeker) session.getAttribute("jobSeeker"); // Use your session attribute name
        if (loggedInUser == null) {
            throw new RuntimeException("User not logged in");
        }

        return conversationService.startOrGetConversation(loggedInUser.getJSId(), otherJobSeekerId);
    }
//    @PostMapping("/company/conversations/start")
//    public String startConversationWithJobSeeker(@RequestParam Long jobSeekerId,
//                                                 HttpSession session,
//                                                 RedirectAttributes redirectAttributes) {
//        Company loggedInCompany = (Company) session.getAttribute("comdata");
//        if (loggedInCompany == null) {
//            return "redirect:/company/companylogin"; // Redirect if not logged in
//        }
//
//        try {
//            Conversation conversation = conversationService.startOrGetConversationWithCompany((long) loggedInCompany.getCompanyid(), jobSeekerId);
//            // Redirect to a new page that shows the conversation
//            return "redirect:/company/conversations/" + conversation.getId();
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "Could not start conversation: " + e.getMessage());
//            return "redirect:/company/getapplicants"; // Or wherever you want to show the error
//        }

}