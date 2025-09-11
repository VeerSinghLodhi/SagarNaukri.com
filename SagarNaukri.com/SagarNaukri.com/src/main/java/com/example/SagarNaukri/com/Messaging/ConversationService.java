package SagarNaukriMerge.SagarNaukriMerge.Messaging;

import SagarNaukriMerge.SagarNaukriMerge.CompaniesPackage.Company;
import SagarNaukriMerge.SagarNaukriMerge.CompaniesPackage.CompanyRepository;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeekerRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final CompanyRepository companyRepository;

    public ConversationService(ConversationRepository conversationRepository,
                               JobSeekerRepository jobSeekerRepository,
                               CompanyRepository companyRepository) {
        this.conversationRepository = conversationRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.companyRepository = companyRepository;
    }

    public Conversation startOrGetConversation(Long jobSeekerId1, Long jobSeekerId2) {
        // Check if a conversation already exists
        Optional<Conversation> existingConversation = conversationRepository.findByJobSeekerParticipants(jobSeekerId1, jobSeekerId2);
        if (existingConversation.isPresent()) {
            return existingConversation.get();
        }

        // If not, create a new one
        JobSeeker user1 = jobSeekerRepository.findById(jobSeekerId1)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + jobSeekerId1));
        JobSeeker user2 = jobSeekerRepository.findById(jobSeekerId2)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + jobSeekerId2));

        Conversation newConversation = new Conversation();
        newConversation.setCreatedAt(LocalDateTime.now());
        newConversation.setJobSeekerParticipants(Set.of(user1, user2));

        return conversationRepository.save(newConversation);
    }
    public Conversation startOrGetConversationWithCompany(Long companyId, Long jobSeekerId) {

        Optional<Conversation> existing = conversationRepository.findByCompanyAndJobSeeker(companyId, jobSeekerId);
        if (existing.isPresent()) {
            return existing.get();
        }


        Company company = companyRepository.findById(Math.toIntExact(companyId))
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("JobSeeker not found with id: " + jobSeekerId));

        Conversation newConversation = new Conversation();
        newConversation.setCreatedAt(LocalDateTime.now());
        newConversation.getCompanyParticipants().add(company);
        newConversation.getJobSeekerParticipants().add(jobSeeker);

        return conversationRepository.save(newConversation);
    }

    public List<Conversation> getConversationsForUser(Long jobSeekerId) {

        if (!jobSeekerRepository.existsById(jobSeekerId)) {

            throw new RuntimeException("User not found with id: " + jobSeekerId);
        }


        return conversationRepository.findByParticipantId(jobSeekerId);
    }
}