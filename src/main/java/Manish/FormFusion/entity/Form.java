package Manish.FormFusion.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long formId;

    private String title;

    private String description;

    private String link;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;


    public Form() {

    }

    public Form(String title, String description, String link, User user, List<Question> questions, List<Response> responses, List<Answer> answers) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.user = user;
        this.questions = questions;
        this.responses = responses;
        this.answers = answers;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    //    @Override
//    public String toString() {
//        return "Form{" +
//                "formId=" + formId +
//                ", title='" + title + '\'' +
//                ", description='" + description + '\'' +
//                ", link='" + link + '\'' +
//                ", user=" + (user != null ? user.getUserId() : null) +
//                ", questions=" + (questions != null ? questions : null) +
//                '}';
//    }
    @Override
    public String toString() {
        return "{" +
                "\"formId\": " + formId +
                ", \"title\": \"" + title + '\"' +
                ", \"description\": \"" + description + '\"' +
                ", \"link\": \"" + link + '\"' +
                ", \"userId\": " + (user != null ? user.getUserId() : null) +
                ", \"questions\": " + (questions != null ? questions : null) +
                '}';
    }

}
