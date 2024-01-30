package com.example.quiz_assignment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AttempAnswer implements Parcelable{
    private int numOfCorrectAnswer;
    private int numOfQuestions;
    private int numOfAttemp;
    private boolean isSaved;

    public AttempAnswer() {
        this.isSaved=false;
        this.numOfAttemp=0;
        this.numOfCorrectAnswer=0;
        this.numOfQuestions=0;
    }

    public boolean isEmpty(){
        return !isSaved && this.numOfAttemp == 0 && this.numOfCorrectAnswer == 0 && this.numOfQuestions ==0;
    }

    public void increaseCorrect(){
        this.numOfCorrectAnswer++;
    }

    public void resetCorrect(){
        this.numOfCorrectAnswer=0;
    }

    public void increaseAttemp(){
        this.numOfAttemp++;
    }

    protected AttempAnswer(Parcel in) {
        numOfCorrectAnswer = in.readInt();
        numOfQuestions = in.readInt();
        numOfAttemp = in.readInt();
        isSaved = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numOfCorrectAnswer);
        dest.writeInt(numOfQuestions);
        dest.writeInt(numOfAttemp);
        dest.writeByte((byte) (isSaved ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AttempAnswer> CREATOR = new Creator<AttempAnswer>() {
        @Override
        public AttempAnswer createFromParcel(Parcel in) {
            return new AttempAnswer(in);
        }

        @Override
        public AttempAnswer[] newArray(int size) {
            return new AttempAnswer[size];
        }
    };

    public void resetNew(){
        this.isSaved=false;
        this.numOfAttemp=0;
        this.numOfCorrectAnswer=0;
        this.numOfQuestions=0;
    }

    public AttempAnswer(int numOfCorrectAnswer, int numOfQuestions, int numOfAttemp, boolean isSaved) {
        this.numOfCorrectAnswer = numOfCorrectAnswer;
        this.numOfQuestions = numOfQuestions;
        this.numOfAttemp = numOfAttemp;
        this.isSaved = isSaved;
    }

    public StringBuilder getSb(){
        StringBuilder result= new StringBuilder();
        result.append(this.numOfCorrectAnswer+","+this.numOfQuestions+","+this.numOfAttemp+","+this.isSaved);
        return result;
    }

    public int getNumOfCorrectAnswer() {
        return numOfCorrectAnswer;
    }

    public void setNumOfCorrectAnswer(int numOfCorrectAnswer) {
        this.numOfCorrectAnswer = numOfCorrectAnswer;
    }

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public int getNumOfAttemp() {
        return numOfAttemp;
    }

    public void setNumOfAttemp(int numOfAttemp) {
        this.numOfAttemp = numOfAttemp;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}
