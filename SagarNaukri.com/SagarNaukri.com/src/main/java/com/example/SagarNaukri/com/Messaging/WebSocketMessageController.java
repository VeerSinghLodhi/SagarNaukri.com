package SagarNaukriMerge.SagarNaukriMerge.Messaging;

import SagarNaukriMerge.SagarNaukriMerge.Messaging.dto.MessageMapper;
import SagarNaukriMerge.SagarNaukriMerge.Messaging.dto.MessageResponseDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketMessageController {

    private final MessageService messageService;

    public WebSocketMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chat/{conversationId}")
    @SendTo("/topic/messages/{conversationId}")

    public MessageResponseDTO sendMessage(@DestinationVariable Long conversationId, MessagePayload payload) {

        // 1. Save the message to the database using the service
        Message savedMessage = messageService.sendMessage(
                conversationId,
                payload.getSenderId(),
                payload.getSenderType(),
                payload.getContent()
        );

        // 2. Convert the saved entity to a DTO before returning
        return MessageMapper.toDTO(savedMessage);
    }
}