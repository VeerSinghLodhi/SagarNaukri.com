package SagarNaukriMerge.SagarNaukriMerge.Messaging;

import jakarta.persistence.*;

@Entity
@Table(name = "blocked_users")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The one doing the blocking
    @Column(nullable = false)
    private Long blockerId;
    @Column(nullable = false)
    private String blockerType; // "JOBSEEKER" or "COMPANY"

    // The one being blocked
    @Column(nullable = false)
    private Long blockedId;
    @Column(nullable = false)
    private String blockedType; // "JOBSEEKER" or "COMPANY"

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlockerId() {
        return blockerId;
    }

    public void setBlockerId(Long blockerId) {
        this.blockerId = blockerId;
    }

    public String getBlockerType() {
        return blockerType;
    }

    public void setBlockerType(String blockerType) {
        this.blockerType = blockerType;
    }

    public Long getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(Long blockedId) {
        this.blockedId = blockedId;
    }

    public String getBlockedType() {
        return blockedType;
    }

    public void setBlockedType(String blockedType) {
        this.blockedType = blockedType;
    }
}