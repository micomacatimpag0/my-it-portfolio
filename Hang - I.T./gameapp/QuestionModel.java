package com.example.mobilecomp;

public class QuestionModel {
    public String question;
    public String answer;
    public String timer;

    public QuestionModel() {
        // Default constructor required for Firebase
    }

    public QuestionModel(String question, String answer, String timer) {
        this.question = question;
        this.answer = answer;
        this.timer = timer;
    }
}
