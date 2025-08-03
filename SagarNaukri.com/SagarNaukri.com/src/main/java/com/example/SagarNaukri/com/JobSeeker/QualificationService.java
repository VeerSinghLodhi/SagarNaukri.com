package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualificationService {
    @Autowired
    private QualificationRepository qualificationRepository;

    public List<Qualification> getQualificationsByJobSeeker(Long jobSeekerId) {
        return qualificationRepository.findByJobSeekerJSId(jobSeekerId);
    }

    public void addQualification(Qualification qualification, Long jobSeekerId) {
        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setJSId(jobSeekerId);
        qualification.setJobSeeker(jobSeeker);
        qualificationRepository.save(qualification);
    }

    public void deleteQualification(Long id) {
        qualificationRepository.deleteById(id);

    }
}