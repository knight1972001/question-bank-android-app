package com.example.quiz_assignment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionBank implements Parcelable {
    private ArrayList<Question> questionsBank;

    public QuestionBank() {
        questionsBank=new ArrayList<>();
    }

    public void resetQuestionBank(){
        questionsBank=new ArrayList<>();
    }

    public QuestionBank(ArrayList<Question> questionsBank) {
        this.questionsBank = questionsBank;
    }

    public void setBank(ArrayList<Question> questionsBank){
        this.questionsBank=questionsBank;
    }

    protected QuestionBank(Parcel in) {
        questionsBank = in.createTypedArrayList(Question.CREATOR);
    }

    public static final Creator<QuestionBank> CREATOR = new Creator<QuestionBank>() {
        @Override
        public QuestionBank createFromParcel(Parcel in) {
            return new QuestionBank(in);
        }

        @Override
        public QuestionBank[] newArray(int size) {
            return new QuestionBank[size];
        }
    };

    public int size(){
        return questionsBank.size();
    }

    public void removeQuestion(Question question){
        questionsBank.remove(question);
    }

    public void addQuestion(Question question){
        questionsBank.add(question);
    }

    public StringBuilder getStringBuilder() {
        StringBuilder result= new StringBuilder();
        for(int i=0;i<questionsBank.size();i++){
            result.append(questionsBank.get(i).toString());
            result.append('\n');
        }
        return result;
    }

    public Question get(int i){
        return questionsBank.get(i);
    }

    public int getIndex(Question question){
        return questionsBank.indexOf(question);
    }

    public void shuffleQuestions(){
        //shuffle questions
        Collections.shuffle(questionsBank);
    }

    public ArrayList<Question> getQuestionsList(){
        return questionsBank;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(questionsBank);
    }
}
