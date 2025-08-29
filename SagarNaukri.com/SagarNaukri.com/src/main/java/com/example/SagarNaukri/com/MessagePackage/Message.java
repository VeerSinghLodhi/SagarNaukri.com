package SagarNaukriMerge.SagarNaukriMerge.MessagePackage;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageid;

    @ManyToOne
    @JoinColumn(name = "conversationid", nullable = false)
    private Conversation conversation;

    @Column(nullable = false)
    private String sender_type; // "COMPANY" or "JOBSEEKER"

    @Column(nullable = false)
    private Long sender_id;     // companyid OR jsid

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private LocalDateTime timestamp = LocalDateTime.now();

    public Message() {
    }

    public Message( Conversation conversation,Long sender_id, String content, LocalDateTime timestamp) {
        this.sender_id = sender_id;
        this.content = content;
        this.timestamp = timestamp;
        this.conversation = conversation;
    }

    public Message(Long messageid, Conversation conversation, String sender_type, Long sender_id, String content, LocalDateTime timestamp) {
        this.messageid = messageid;
        this.conversation = conversation;
        this.sender_type = sender_type;
        this.sender_id = sender_id;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getMessageid() {
        return messageid;
    }

    public void setMessageid(Long messageid) {
        this.messageid = messageid;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public Long getSender_id() {
        return sender_id;
    }

    public void setSender_id(Long sender_id) {
        this.sender_id = sender_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
