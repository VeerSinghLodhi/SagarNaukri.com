package SagarNaukriMerge.SagarNaukriMerge.MessagePackage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Long, WebSocketSession> userSessions = new HashMap<>();

//    @Autowired
//    private UserRepository userRepo;
    @Autowired private ConversationRepository convRepo;
    @Autowired private MessageRepository msgRepo;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Later: extract userId from JWT/Session and map it
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        ChatMessageDTO dto = objectMapper.readValue(textMessage.getPayload(), ChatMessageDTO.class);

        // Save to DB
//        Message msg = new Message(dto.getConversationId(), dto.getSenderId(), dto.getContent(), LocalDateTime.now());
        Message msg =new Message();
        msg.setSender_id(dto.getSenderId());
        msg.setContent((dto.getContent()));
        msg.setTimestamp(LocalDateTime.now());
        msgRepo.save(msg);

        // Send to both users in conversation
        Conversation conv = convRepo.findById(dto.getConversationId()).orElseThrow();
        Long receiverId = conv.getCompany().getCompanyid()==(dto.getSenderId()) ?
                conv.getJobSeeker().getJSId() : conv.getCompany().getCompanyid();

        // Send to sender
        session.sendMessage(new TextMessage(dto.getContent()));

        // If receiver is online
        if (userSessions.containsKey(receiverId)) {
            WebSocketSession receiverSession = userSessions.get(receiverId);
            if (receiverSession.isOpen()) {
                receiverSession.sendMessage(new TextMessage(dto.getContent()));
            }
        }
    }
}
