package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@example.com"); // Replace with your sender email
        message.setTo(toEmail);
        message.setSubject("Account Verification - Job Seeker Portal");
        message.setText("To verify your account, please click the following link:\n"
                + "http://localhost:8081/jobseeker/verify?token=" + token); // Adjust URL if needed
        mailSender.send(message);
    }
}
