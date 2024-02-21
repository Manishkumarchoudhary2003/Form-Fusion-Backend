package Manish.FormFusion.entity;

import jakarta.persistence.*;

@Entity
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;
    private String optionData;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public Options (){

    }

    public Options(String optionData, Question question) {
        this.optionData = optionData;
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public String getOptionData() {
        return optionData;
    }

    public void setOptionData(String optionData) {
        this.optionData = optionData;
    }

    @Override
    public String toString() {
        return "{" +
                "\"optionId\":" + optionId +
                ", \"optionData\":\"" + optionData + "\"" +
                "}";
    }

}
