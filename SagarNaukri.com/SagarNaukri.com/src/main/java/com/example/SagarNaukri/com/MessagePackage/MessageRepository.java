package SagarNaukriMerge.SagarNaukriMerge.MessagePackage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findByConversationOrderByTimestampAsc(Conversation conversation);
}
