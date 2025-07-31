package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Skills_two;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    private final GeminiApiService geminiApiService;

    @Autowired
    public SkillService(SkillRepository skillRepository, GeminiApiService geminiApiService) {
        this.skillRepository = skillRepository;
        this.geminiApiService = geminiApiService;
    }

    @Transactional(readOnly = true)
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Transactional
    public Skill saveSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    @Transactional(readOnly = true)
    public Optional<Skill> getSkillByName(String name) {
        return skillRepository.findByNameIgnoreCase(name);
    }
    @Transactional(readOnly = true)
    public SkillSuggestionResponse getSkillSuggestions(String skillName) {
        return geminiApiService.getSkillSuggestions(skillName);
    }
}