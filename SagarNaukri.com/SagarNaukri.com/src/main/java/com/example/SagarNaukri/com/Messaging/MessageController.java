package SagarNaukriMerge.SagarNaukriMerge.Messaging;

import SagarNaukriMerge.SagarNaukriMerge.CompaniesPackage.Company;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker; // Import JobSeeker
import java.util.List;

@Controller
//@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public Message sendMessage(@RequestParam Long conversationId, @RequestParam String content, HttpSession session) {
        JobSeeker loggedInUser = (JobSeeker) session.getAttribute("jobSeeker");
        if (loggedInUser == null) {
            throw new RuntimeException("User not logged in");
        }

        return messageService.sendMessage(conversationId, loggedInUser.getJSId(), "JOBSEEKER", content);
    }

    @GetMapping
    public List<Message> getMessages(@RequestParam Long conversationId) {
        return messageService.getMessagesForConversation(conversationId);
    }

    @PostMapping("/company/messages")
    public String sendCompanyMessage(@RequestParam Long conversationId,
                                     @RequestParam String content,
                                     HttpSession session) {
        Company loggedInCompany = (Company) session.getAttribute("comdata");
        if (loggedInCompany == null) {
            return "redirect:/company/companylogin";
        }

        messageService.sendMessage(conversationId, (long) loggedInCompany.getCompanyid(), "COMPANY", content);

        return "redirect:/company/conversations/" + conversationId;
    }

    // ADD THIS METHOD
    @PostMapping("/jobseeker/messages")
    public String sendJobSeekerMessage(@RequestParam Long conversationId,
                                       @RequestParam String content,
                                       HttpSession session) {
        // 1. Get the ID from the session using the correct key "jobSeekerId"
        Long jobSeekerId = (Long) session.getAttribute("jobSeekerId");
        if (jobSeekerId == null) {
            return "redirect:/jobseeker/login";
        }

        // 2. Call the service to send the message
        messageService.sendMessage(conversationId, jobSeekerId, "JOBSEEKER", content);

        // 3. Redirect back to the conversation page
        return "redirect:/jobseeker/conversations/" + conversationId;
    }
}