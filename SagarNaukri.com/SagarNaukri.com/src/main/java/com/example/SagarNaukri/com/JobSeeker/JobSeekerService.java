package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID; // Import UUID

@Service
public class JobSeekerService {

    @Autowired
    private JobSeekerRepository jobSeekerRepo;

    @Autowired
    private EmailService emailService; // Autowire EmailService

    public JobSeeker registerJobSeeker(JobSeeker jobSeeker,
                                       MultipartFile profilePictureFile,
                                       MultipartFile resumeFile) {
        if (jobSeekerRepo.findByEmail(jobSeeker.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        try {
            if (profilePictureFile != null && !profilePictureFile.isEmpty()) {
                jobSeeker.setProfilePicture(profilePictureFile.getBytes());
            }

            if (resumeFile != null && !resumeFile.isEmpty()) {
                jobSeeker.setResume(resumeFile.getBytes());
            }

            // Set verification token and status
            jobSeeker.setVerificationToken(UUID.randomUUID().toString());
            jobSeeker.setVerified(false);

            JobSeeker savedJobSeeker = jobSeekerRepo.save(jobSeeker);

            // Send verification email
            emailService.sendVerificationEmail(savedJobSeeker.getEmail(), savedJobSeeker.getVerificationToken());

            return savedJobSeeker;
        } catch (IOException e) {
            throw new RuntimeException("Failed to process files", e);
        }
    }

    public boolean verifyJobSeeker(String token) {
        Optional<JobSeeker> optionalJobSeeker = jobSeekerRepo.findByVerificationToken(token);
        if (optionalJobSeeker.isPresent()) {
            JobSeeker jobSeeker = optionalJobSeeker.get();
            jobSeeker.setVerified(true);
            jobSeeker.setVerificationToken(null); // Clear the token after verification
            jobSeekerRepo.save(jobSeeker);
            return true;
        }
        return false;
    }
    public Optional<JobSeeker> getJobSeekerById(Long id) {
        return jobSeekerRepo.findById(id);
    }
}