package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Qualification_two;

import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker; // This import is fine for the commented out section
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qualifications_two")
public class QualificationController_two {

    private static final Logger logger = LoggerFactory.getLogger(QualificationController_two.class);

    private final QualificationService_two qualificationService_two; // Correct: instance variable

    @Autowired // Correct: Constructor injection
    public QualificationController_two(QualificationService_two qualificationService_two) {
        this.qualificationService_two = qualificationService_two;
    }

    @GetMapping("/jobseeker/{jsId}")
    public ResponseEntity<List<Qualification_two>> getQualificationsByJobSeeker(@PathVariable Long jsId) {
        logger.info("Received request to get qualifications for JobSeeker ID: {}", jsId);
        // Correct: calling instance method on autowired instance
        List<Qualification_two> qualifications = qualificationService_two.getQualificationsByJobSeekerId(jsId);
        return ResponseEntity.ok(qualifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Qualification_two> getQualificationById(@PathVariable Long id) {
        logger.info("Received request to get qualification by ID: {}", id);
        // Correct: calling instance method on autowired instance
        return qualificationService_two.getQualificationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Qualification with ID {} not found.", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping // This maps to /api/qualifications_two
    public ResponseEntity<?> createQualification(@RequestBody Qualification_two qualification) {
        // The JobSeeker ID is expected to be part of the Qualification_two object in the request body
        // e.g., {"jobSeeker": {"JSId": 123}, ...}
        logger.info("Received request to create qualification.");
        try {
            // Correct: calling instance method on autowired instance
            Qualification_two savedQualification = qualificationService_two.saveQualification(qualification);
            logger.info("Qualification saved successfully with ID: {}", savedQualification.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedQualification);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation error while creating qualification: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error saving qualification: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to save qualification due to an internal server error."));
        }
    }

    // ... (Your commented out createQualificationWithPathVar method is fine for a REST context if you need it) ...

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQualification(@PathVariable Long id, @RequestBody Qualification_two qualification) {
        logger.info("Received request to update qualification with ID: {}", id);
        try {
            // Correct: calling instance method on autowired instance
            Qualification_two updated = qualificationService_two.updateQualification(id, qualification);
            logger.info("Qualification with ID {} updated successfully.", updated.getId());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation error while updating qualification with ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating qualification with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update qualification due to an internal server error."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQualification(@PathVariable Long id) {
        logger.info("Received request to delete qualification with ID: {}", id);
        try {
            // Correct: calling instance method on autowired instance
            qualificationService_two.deleteQualification(id);
            logger.info("Qualification with ID {} deleted successfully.", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Error deleting qualification with ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Error deleting qualification with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}