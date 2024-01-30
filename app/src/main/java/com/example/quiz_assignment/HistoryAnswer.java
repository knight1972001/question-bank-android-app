package com.example.quiz_assignment;

import android.os.Parcel;
import android.os.Parcelable;

public class HistoryAnswer implements Parcelable {
    private boolean userAnswer;
    private Question question;
    public HistoryAnswer(Question question, boolean userAnswer) {
        this.question=question;
        this.userAnswer=userAnswer;
    }

    protected HistoryAnswer(Parcel in) {
        userAnswer = in.readByte() != 0;
        question = in.readParcelable(Question.class.getClassLoader());
    }

    public static final Creator<HistoryAnswer> CREATOR = new Creator<HistoryAnswer>() {
        @Override
        public HistoryAnswer createFromParcel(Parcel in) {
            return new HistoryAnswer(in);
        }

        @Override
        public HistoryAnswer[] newArray(int size) {
            return new HistoryAnswer[size];
        }
    };

    @Override
    public String toString() {
        return question.toString()+","+this.userAnswer;
    }

    public boolean getUserAnswer() {
        return userAnswer;
    }

    public Question getQuestion() {
        return question;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (userAnswer ? 1 : 0));
        dest.writeParcelable(question, flags);
    }
}
