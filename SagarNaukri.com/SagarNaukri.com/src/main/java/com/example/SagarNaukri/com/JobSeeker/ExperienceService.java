package SagarNaukriMerge.SagarNaukriMerge.JobSeeker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExperienceService {
    @Autowired
    private ExperienceRepository experienceRepository;

    public List<Experience> getExperiencesByJobSeeker(Long jobSeekerId) {
        return experienceRepository.findByJobSeekerJSId(jobSeekerId);
    }

    public void addExperience(Experience experience, Long jobSeekerId) {
        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setJSId(jobSeekerId);
        experience.setJobSeeker(jobSeeker);
        experienceRepository.save(experience);
    }

    public void deleteExperience(Long expId) {
        experienceRepository.deleteById(expId);
    }
}