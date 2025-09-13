package SagarNaukriMerge.SagarNaukriMerge.Messaging;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public MessageService(MessageRepository messageRepository, ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    public Message sendMessage(Long conversationId, Long senderId, String senderType, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));



        Message message = new Message();
        message.setConversation(conversation);
        message.setSenderId(senderId);
        message.setSenderType(senderType);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<Message> getMessagesForConversation(Long conversationId) {
        return messageRepository.findAllByConversationIdOrderByTimestampAsc(conversationId);
    }
}