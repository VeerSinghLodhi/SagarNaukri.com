package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Qualification_two;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/institutes_two")
@CrossOrigin(origins = "http://localhost:8080") // Ensure this matches your frontend URL
public class InstituteController_two {

    private static final Logger logger = LoggerFactory.getLogger(InstituteController_two.class);

    private final InstituteService_two instituteService_two; // Correct: instance variable

    @Autowired // Correct: Constructor injection
    public InstituteController_two(InstituteService_two instituteService_two) {
        this.instituteService_two = instituteService_two;
    }

    @GetMapping
    public ResponseEntity<List<Institute_two>> searchInstitutes(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort) {

        logger.info("Received request to search institutes. Search term: '{}', Page: {}, Size: {}, Sort: {}", search, page, size, String.join(",", sort));

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        String sortBy = sort[0];
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // Correct: calling instance method on autowired instance
        Page<Institute_two> pageResult = instituteService_two.searchInstitutes(search, pageable);
        return ResponseEntity.ok(pageResult.getContent());
    }

    @PostMapping
    public ResponseEntity<?> createInstitute(@RequestBody Institute_two institute) {
        logger.info("Received request to create institute: {}", institute.getName());
        try {
            // Correct: calling instance method on autowired instance
            Institute_two savedInstitute = instituteService_two.saveInstitute(institute);
            logger.info("Institute saved/retrieved successfully: ID={}, Name='{}'", savedInstitute.getId(), savedInstitute.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedInstitute); // Use 201 Created for new resources
        } catch (IllegalArgumentException e) {
            logger.warn("Validation error creating institute: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error creating institute: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error creating institute: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institute_two> getInstituteById(@PathVariable Long id) {
        logger.info("Received request to get institute by ID: {}", id);
        // Correct: calling instance method on autowired instance
        return instituteService_two.getInstituteById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Institute with ID {} not found.", id);
                    return ResponseEntity.notFound().build();
                });
    }
}