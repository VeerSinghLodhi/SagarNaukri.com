package SagarNaukriMerge.SagarNaukriMerge.MessagePackage;

import SagarNaukriMerge.SagarNaukriMerge.CompaniesPackage.Company;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {
    Conversation findByCompanyAndJobSeeker(Company company, JobSeeker jobSeeker);
}
