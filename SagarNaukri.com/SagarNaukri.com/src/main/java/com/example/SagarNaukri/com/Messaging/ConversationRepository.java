package SagarNaukriMerge.SagarNaukriMerge.Messaging;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // This query finds a conversation that has BOTH specified job seekers as participants.
    @Query("SELECT c FROM Conversation c " +
            "WHERE EXISTS (SELECT p FROM c.jobSeekerParticipants p WHERE p.JSId = :jobSeekerId1) " +
            "AND EXISTS (SELECT p FROM c.jobSeekerParticipants p WHERE p.JSId = :jobSeekerId2)")
    Optional<Conversation> findByJobSeekerParticipants(@Param("jobSeekerId1") Long jobSeekerId1, @Param("jobSeekerId2") Long jobSeekerId2);

    @Query("SELECT c FROM Conversation c " +
            "WHERE EXISTS (SELECT comp FROM c.companyParticipants comp WHERE comp.companyid = :companyId) " +
            "AND EXISTS (SELECT js FROM c.jobSeekerParticipants js WHERE js.JSId = :jobSeekerId)")
    Optional<Conversation> findByCompanyAndJobSeeker(@Param("companyId") Long companyId, @Param("jobSeekerId") Long jobSeekerId);

    @Query("SELECT c FROM Conversation c JOIN c.jobSeekerParticipants p WHERE p.JSId = :jobSeekerId")
    List<Conversation> findByParticipantId(@Param("jobSeekerId") Long jobSeekerId);
}