package SagarNaukriMerge.SagarNaukriMerge.Messaging.dto;

import SagarNaukriMerge.SagarNaukriMerge.Messaging.Message;

public class MessageMapper {

    public static MessageResponseDTO toDTO(Message message) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        dto.setSenderId(message.getSenderId());
        dto.setSenderType(message.getSenderType());
        return dto;
    }
}