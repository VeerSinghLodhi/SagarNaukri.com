package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Qualification_two;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InstituteService_two {

    private static final Logger logger = LoggerFactory.getLogger(InstituteService_two.class);

    private final InstituteRepository_two instituteRepository_two; // Correct: instance variable

    @Autowired // Correct: Constructor injection
    public InstituteService_two(InstituteRepository_two instituteRepository_two) {
        this.instituteRepository_two = instituteRepository_two;
    }

    @Transactional(readOnly = true)
    public Page<Institute_two> searchInstitutes(String query, Pageable pageable) {
        logger.info("Searching institutes with query: '{}', Page: {}, Size: {}", query, pageable.getPageNumber(), pageable.getPageSize());
        if (query == null || query.trim().isEmpty()) {
            return instituteRepository_two.findAll(pageable); // Correct: calling on instance variable
        }
        return instituteRepository_two.findByNameContainingIgnoreCase(query.trim(), pageable); // Correct: calling on instance variable
    }

    @Transactional(readOnly = true)
    public Optional<Institute_two> getInstituteById(Long id) {
        logger.info("Fetching institute by ID: {}", id);
        return instituteRepository_two.findById(id); // Correct: calling on instance variable
    }

    /**
     * Saves a new institute or returns an existing one if a match by name (case-insensitive) is found.
     * This prevents duplicate institute entries.
     *
     * @param institute The Institute_two object to save. Must have a name.
     * @return The saved or existing Institute_two entity.
     * @throws IllegalArgumentException if the institute name is null or empty.
     * @throws RuntimeException if a race condition occurs and the institute cannot be retrieved.
     */
    @Transactional
    // Corrected: REMOVED 'static' keyword
    public Institute_two saveInstitute(Institute_two institute) {
        logger.info("Attempting to save/find institute: {}", institute);
        if (institute.getName() == null || institute.getName().trim().isEmpty()) {
            logger.error("Validation Error: Institute name must be provided.");
            throw new IllegalArgumentException("Institute name must be provided");
        }

        String cleanName = institute.getName().trim();
        institute.setName(cleanName); // Ensure the name is clean before checking/saving

        // Corrected: calling on instance variable
        Optional<Institute_two> existingInstitute = instituteRepository_two.findByNameIgnoreCase(cleanName);
        if (existingInstitute.isPresent()) {
            logger.info("Institute '{}' already exists with ID: {}. Returning existing entity.", cleanName, existingInstitute.get().getId());
            return existingInstitute.get(); // Return the managed existing entity
        }

        try {
            // Corrected: calling on instance variable
            Institute_two savedInstitute = instituteRepository_two.save(institute);
            logger.info("Successfully saved new institute: ID={}, Name='{}'", savedInstitute.getId(), savedInstitute.getName());
            return savedInstitute;
        } catch (DataIntegrityViolationException e) {
            logger.warn("Data integrity violation (possible race condition) while saving institute '{}'. Retrying findByNameIgnoreCase.", cleanName, e);
            // Corrected: calling on instance variable
            return instituteRepository_two.findByNameIgnoreCase(cleanName)
                    .orElseThrow(() -> {
                        logger.error("Failed to find institute '{}' after DataIntegrityViolationException. Original error: {}", cleanName, e.getMessage(), e);
                        return new RuntimeException("Failed to save institute, possibly due to a race condition or other constraint violation.", e);
                    });
        } catch (Exception e) {
            logger.error("An unexpected error occurred while saving institute '{}': {}", cleanName, e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while saving institute: " + e.getMessage(), e);
        }
    }
}