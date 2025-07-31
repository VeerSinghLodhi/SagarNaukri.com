package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Qualification_two;

import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeeker;
import SagarNaukriMerge.SagarNaukriMerge.JobSeeker.JobSeekerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class QualificationService_two {

    private static final Logger logger = LoggerFactory.getLogger(QualificationService_two.class);

    private final QualificationRepository_two qualificationRepository_two;
    private final JobSeekerRepository jobSeekerRepository;
    private final InstituteService_two instituteService_two; // Inject InstituteService

    @Autowired
    public QualificationService_two(QualificationRepository_two qualificationRepository_two,
                                    JobSeekerRepository jobSeekerRepository,
                                    InstituteService_two instituteService_two) {
        this.qualificationRepository_two = qualificationRepository_two;
        this.jobSeekerRepository = jobSeekerRepository;
        this.instituteService_two = instituteService_two; // Initialize
    }

    @Transactional(readOnly = true)
    public List<Qualification_two> getQualificationsByJobSeekerId(Long jsId) {
        logger.info("Fetching qualifications for JobSeeker ID: {}", jsId);
        return qualificationRepository_two.findByJobSeeker_JSId(jsId);
    }

    @Transactional(readOnly = true)
    public Optional<Qualification_two> getQualificationById(Long id) {
        logger.info("Fetching qualification by ID: {}", id);
        return qualificationRepository_two.findById(id);
    }

    @Transactional
    public Qualification_two saveQualification(Qualification_two qualification) {
        logger.info("Attempting to save new qualification.");

        // IMPORTANT: Ensure JobSeeker is managed
        if (qualification.getJobSeeker() == null || qualification.getJobSeeker().getJSId() == null) {
            throw new IllegalArgumentException("Qualification must be associated with an existing JobSeeker.");
        }
        JobSeeker managedJobSeeker = jobSeekerRepository.findById(qualification.getJobSeeker().getJSId())
                .orElseThrow(() -> new IllegalArgumentException("JobSeeker with ID " + qualification.getJobSeeker().getJSId() + " not found."));
        qualification.setJobSeeker(managedJobSeeker);

        // IMPORTANT: Ensure Institute is managed
        Institute_two incomingInstitute = qualification.getInstitute();
        if (incomingInstitute == null) {
            throw new IllegalArgumentException("Qualification must have an associated institute.");
        }

        Institute_two managedInstitute;
        // If the incoming institute has an ID, try to find it.
        // If it doesn't have an ID (i.e., it's a new institute), instituteService_two.saveInstitute will handle it.
        if (incomingInstitute.getId() != null) {
            managedInstitute = instituteService_two.getInstituteById(incomingInstitute.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Institute with ID " + incomingInstitute.getId() + " not found."));
        } else {
            // This means it's a new institute that needs to be saved/found by name
            managedInstitute = instituteService_two.saveInstitute(incomingInstitute); // This handles finding existing by name or saving new
        }
        qualification.setInstitute(managedInstitute);


        // Add more server-side validation if needed
        if (qualification.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required.");
        }
        if (qualification.getEndDate() != null && qualification.getEndDate().isBefore(qualification.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        // Check for duplicate qualification: same job seeker, institute, and degree program
        if (qualificationRepository_two.existsByJobSeeker_JSIdAndInstitute_IdAndDegreeProgramIgnoreCase(
                qualification.getJobSeeker().getJSId(),
                qualification.getInstitute().getId(), // Use the ID of the managed institute
                qualification.getDegreeProgram())) {
            // Allow update if it's the same qualification being edited
            if (qualification.getId() == null || !qualificationRepository_two.findById(qualification.getId())
                    .map(q -> q.getInstitute().getId().equals(qualification.getInstitute().getId()) &&
                            q.getDegreeProgram().equalsIgnoreCase(qualification.getDegreeProgram()))
                    .orElse(false)) {
                throw new IllegalArgumentException("A qualification with the same institute and degree program already exists.");
            }
        }


        try {
            Qualification_two savedQualification = qualificationRepository_two.save(qualification);
            logger.info("Qualification saved successfully with ID: {}", savedQualification.getId());
            return savedQualification;
        } catch (Exception e) {
            logger.error("Error saving qualification: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save qualification: " + e.getMessage(), e);
        }
    }


    @Transactional
    public Qualification_two updateQualification(Long id, Qualification_two updatedQualification) {
        logger.info("Attempting to update qualification with ID: {}", id);

        return qualificationRepository_two.findById(id).map(existingQualification -> {
            // IMPORTANT: Ensure JobSeeker is managed
            // The jobSeeker should already be set on the existingQualification, no need to refetch unless you're updating it.
            // If the incoming updatedQualification has a different JobSeeker, you'd handle that here.
            // For now, assume JobSeeker doesn't change on update.

            // IMPORTANT: Ensure Institute is managed
            Institute_two incomingInstitute = updatedQualification.getInstitute();
            if (incomingInstitute == null) {
                throw new IllegalArgumentException("Qualification must have an associated institute.");
            }

            Institute_two managedInstitute;
            if (incomingInstitute.getId() != null) {
                managedInstitute = instituteService_two.getInstituteById(incomingInstitute.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Institute with ID " + incomingInstitute.getId() + " not found."));
            } else {
                managedInstitute = instituteService_two.saveInstitute(incomingInstitute); // Handles finding existing by name or saving new
            }
            existingQualification.setInstitute(managedInstitute);


            // Update fields from updatedQualification
            existingQualification.setType(updatedQualification.getType());
            existingQualification.setDegreeProgram(updatedQualification.getDegreeProgram());
            existingQualification.setMajorSpecialization(updatedQualification.getMajorSpecialization());
            existingQualification.setStartDate(updatedQualification.getStartDate());
            existingQualification.setEndDate(updatedQualification.getEndDate());
            existingQualification.setGradeGPA(updatedQualification.getGradeGPA());
            existingQualification.setDescription(updatedQualification.getDescription());

            // Add more server-side validation if needed
            if (existingQualification.getStartDate() == null) {
                throw new IllegalArgumentException("Start date is required.");
            }
            if (existingQualification.getEndDate() != null && existingQualification.getEndDate().isBefore(existingQualification.getStartDate())) {
                throw new IllegalArgumentException("End date cannot be before start date.");
            }

            // Check for duplicate qualification on update:
            // Allow update if it's the same qualification being edited, otherwise check for new duplicates
            if (qualificationRepository_two.existsByJobSeeker_JSIdAndInstitute_IdAndDegreeProgramIgnoreCase(
                    existingQualification.getJobSeeker().getJSId(),
                    existingQualification.getInstitute().getId(),
                    existingQualification.getDegreeProgram())) {

                // If a duplicate exists, ensure it's *this same* qualification being updated
                Optional<Qualification_two> existingDuplicate = qualificationRepository_two.findById(existingQualification.getId());
                if (existingDuplicate.isEmpty() || // Should not happen if coming from findById(id)
                        !existingDuplicate.get().getInstitute().getId().equals(existingQualification.getInstitute().getId()) ||
                        !existingDuplicate.get().getDegreeProgram().equalsIgnoreCase(existingQualification.getDegreeProgram())) {
                    throw new IllegalArgumentException("Another qualification with the same institute and degree program already exists.");
                }
            }


            try {
                Qualification_two savedQualification = qualificationRepository_two.save(existingQualification);
                logger.info("Qualification with ID {} updated successfully.", savedQualification.getId());
                return savedQualification;
            } catch (Exception e) {
                logger.error("Error updating qualification with ID {}: {}", id, e.getMessage(), e);
                throw new RuntimeException("Failed to update qualification: " + e.getMessage(), e);
            }
        }).orElseThrow(() -> new IllegalArgumentException("Qualification with ID " + id + " not found."));
    }

    @Transactional
    public void deleteQualification(Long id) {
        logger.info("Attempting to delete qualification with ID: {}", id);
        if (!qualificationRepository_two.existsById(id)) {
            throw new IllegalArgumentException("Qualification with ID " + id + " not found.");
        }
        qualificationRepository_two.deleteById(id);
        logger.info("Qualification with ID {} deleted successfully.", id);
    }
}