package SagarNaukriMerge.SagarNaukriMerge.JobSeeker.Skills_two;



// src/main/java/com/example/app/external_api/SkillSuggestionResponse.java


import java.util.List;

// POJO to encapsulate the response from your backend API endpoint
public class SkillSuggestionResponse {
    private boolean success;
    private String message;
    private String suggestedCategory;
    private List<String> relatedSkills;

    public SkillSuggestionResponse() {
    }

    public SkillSuggestionResponse(boolean success, String message, String suggestedCategory, List<String> relatedSkills) {
        this.success = success;
        this.message = message;
        this.suggestedCategory = suggestedCategory;
        this.relatedSkills = relatedSkills;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuggestedCategory() {
        return suggestedCategory;
    }

    public void setSuggestedCategory(String suggestedCategory) {
        this.suggestedCategory = suggestedCategory;
    }

    public List<String> getRelatedSkills() {
        return relatedSkills;
    }

    public void setRelatedSkills(List<String> relatedSkills) {
        this.relatedSkills = relatedSkills;
    }
}