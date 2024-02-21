package Manish.FormFusion.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue
    private Long questionId;

    private String text;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Options> options;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    public Question() {
    }

    public Question(String text, List<Options> options, Form form, List<Answer> answers) {
        this.text = text;
        this.options = options;
        this.form = form;
        this.answers = answers;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }


//    Original
//    @Override
//    public String toString() { // Without Options
//        return "Question{" +
//                "questionId=" + questionId +
//                ", text='" + text + '\'' +
//                ", form=" + (form != null ? form.getFormId() : null) +
//                '}';
//    }

//    @Override
//    public String toString() {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("{")
//                .append("\"questionId\":").append(questionId).append(",")
//                .append("\"text\":\"").append(text).append("\",")
//                .append("\"form\":").append(form != null ? form.getFormId() : null).append(",");
//
//        if (options != null && !options.isEmpty()) {
//            stringBuilder.append("\"options\":[");
//            for (Options option : options) {
//                stringBuilder.append("{")
//                        .append("\"optionId\":").append(option.getOptionId()).append(",")
//                        .append("\"optionData\":\"").append(option.getOptionData()).append("\"},");
//            }
//            // Remove the last "," added after the last option
//            stringBuilder.setLength(stringBuilder.length() - 1);
//            stringBuilder.append("]");
//        } else {
//            stringBuilder.append("\"options\":[]");
//        }
//
//        stringBuilder.append("}");
//        return stringBuilder.toString();
//    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{")
                .append("\"questionId\":").append(questionId).append(",")
                .append("\"text\":\"").append(text).append("\",")
                .append("\"form\":").append(form != null ? form.getFormId() : null).append(",");

        if (options != null && !options.isEmpty()) {
            stringBuilder.append("\"options\":[");
            for (Options option : options) {
                stringBuilder.append("{")
                        .append("\"optionId\":").append(option.getOptionId()).append(",")
                        .append("\"optionData\":\"").append(option.getOptionData()).append("\"},");
            }
            // Remove the last "," added after the last option
            stringBuilder.setLength(stringBuilder.length() - 1);
            stringBuilder.append("]");
        } else {
            stringBuilder.append("\"options\":[]");
        }

        // Append answers
        stringBuilder.append(",\"answers\":[");
        for (Answer answer : answers) {
            stringBuilder.append("{")
                    .append("\"answerId\":").append(answer.getAnswerId()).append(",")
                    .append("\"answerData\":\"").append(answer.getAnswer()).append("\"},");
        }
        if (!answers.isEmpty()) {
            // Remove the last "," added after the last answer
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        stringBuilder.append("]}");

        return stringBuilder.toString();
    }



}
