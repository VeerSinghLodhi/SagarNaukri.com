package SagarNaukriMerge.SagarNaukriMerge.Messaging;

// A simple POJO to represent the incoming message data
public class MessagePayload {
    private Long senderId;
    private String senderType;
    private String content;

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
// Getters and Setters...
}