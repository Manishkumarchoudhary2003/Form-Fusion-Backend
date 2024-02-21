package Manish.FormFusion.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Response {

    @Id
    @GeneratedValue
    private Long responseId;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime timestamp;

//    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Answer> answers;

    public Response() {

    }

    public Response(Form form, User user, LocalDateTime timestamp) {
        this.form = form;
        this.user = user;
        this.timestamp = timestamp;
//        this.answers = answers;
    }

    public Long getResponseId() {
        return responseId;
    }

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    //    public List<Answer> getAnswers() {
//        return answers;
//    }
//
//    public void setAnswers(List<Answer> answers) {
//        this.answers = answers;
//    }
//
//        @Override
//        public String toString() {
//            return "Response{" +
//                    "responseId=" + responseId +
//                    ", form=" + form +
//                    ", user=" + user +
//                    ", timestamp=" + timestamp +
//                    '}';
//        }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"responseId\": ").append(responseId).append(",");
        stringBuilder.append("\"form\": {");
        stringBuilder.append("\"formId\": ").append(form.getFormId()).append(",");
        stringBuilder.append("\"title\": \"").append(form.getTitle()).append("\",");
        stringBuilder.append("\"description\": \"").append(form.getDescription()).append("\",");
        stringBuilder.append("\"link\": \"").append(form.getLink()).append("\"");
        stringBuilder.append("},");
        stringBuilder.append("\"user\": {");
        stringBuilder.append("\"userId\": ").append(user.getUserId()).append(",");
        stringBuilder.append("\"username\": \"").append(user.getUsername()).append("\",");
        stringBuilder.append("\"email\": \"").append(user.getEmail()).append("\"");
        stringBuilder.append("},");
        stringBuilder.append("\"timestamp\": \"").append(timestamp).append("\",");
        // Add answers
        stringBuilder.append("\"answers\": [");
        for (Answer answer : form.getAnswers()) {
            stringBuilder.append("{");
            stringBuilder.append("\"answerId\": ").append(answer.getAnswerId()).append(",");
            stringBuilder.append("\"answer\": \"").append(answer.getAnswer()).append("\"");
            stringBuilder.append("},");
        }
        // Remove the trailing comma if answers are present
        if (!form.getAnswers().isEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("]");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }


}
