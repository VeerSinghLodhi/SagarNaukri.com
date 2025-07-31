package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Qualification_two;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QualificationRepository_two extends JpaRepository<Qualification_two, Long> {

    /**
     * Finds all qualifications for a specific job seeker
     * @param jobSeekerId The ID of the job seeker
     * @return List of qualifications
     */
    List<Qualification_two> findByJobSeeker_JSId(Long jobSeekerId);

    /**
     * Finds qualifications for a job seeker with pagination support
     * @param jsId The job seeker ID
     * @param pageable Pagination information
     * @return Page of qualifications
     */
    Page<Qualification_two> findByJobSeeker_JSId(Long jsId, Pageable pageable);

    /**
     * Finds qualifications by type for a specific job seeker
     * @param type The qualification type
     * @param jsId The job seeker ID
     * @return List of matching qualifications
     */
    List<Qualification_two> findByTypeAndJobSeeker_JSId(QualificationType_two type, Long jsId);

    /**
     * Finds qualifications completed before a specific date
     * @param date The cutoff date
     * @param jsId The job seeker ID
     * @return List of qualifications
     */
    @Query("SELECT q FROM Qualification_two q WHERE q.endDate <= :date AND q.jobSeeker.JSId = :jsId")
    List<Qualification_two> findCompletedBeforeDate(@Param("date") LocalDate date, @Param("jsId") Long jsId);

    /**
     * Finds qualifications in progress (no end date or end date in future)
     * @param currentDate The current date
     * @param jsId The job seeker ID
     * @return List of qualifications
     */
    @Query("SELECT q FROM Qualification_two q WHERE (q.endDate IS NULL OR q.endDate > :currentDate) AND q.jobSeeker.JSId = :jsId")
    List<Qualification_two> findInProgressQualifications(@Param("currentDate") LocalDate currentDate, @Param("jsId") Long jsId);

    /**
     * Checks if a qualification exists for a job seeker with the same institute and degree program
     * @param jsId Job seeker ID
     * @param instituteId Institute ID
     * @param degreeProgram Degree program name
     * @return true if duplicate exists
     */
    boolean existsByJobSeeker_JSIdAndInstitute_IdAndDegreeProgramIgnoreCase(
            Long jsId, Long instituteId, String degreeProgram);
}