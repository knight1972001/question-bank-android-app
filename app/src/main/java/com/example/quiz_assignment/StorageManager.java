package com.example.quiz_assignment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StorageManager {
    private static final String QUESTION_FILE_NAME = "questions.csv";
    private static final String HISTORY_ANSWER_FILE_NAME = "answer.csv";

    public void saveAttempAnswer(Activity activity, AttempAnswer attempList) {
        FileOutputStream fos = null;
        try {
            fos = activity.openFileOutput(HISTORY_ANSWER_FILE_NAME, MODE_PRIVATE);
            StringBuilder sb = new StringBuilder();
            sb.append(attempList.getSb());
            fos.write(sb.toString().getBytes());
            System.out.println("SAVED " + HISTORY_ANSWER_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean loadAttempAnswer(Activity activity,AttempAnswer attempAnswer) {
        String line = "";
        FileInputStream fis=null;
        try{
            fis= activity.openFileInput(HISTORY_ANSWER_FILE_NAME);
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader br=new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine())!=null){
                String[] data = line.split(",");
                String correct = data[0];
                String question = data[1];
                String attemp = data[2];
                String isSaved = data[3];
                boolean ans;
                if (isSaved == "True") {
                    ans = true;
                } else {
                    ans = false;
                }
                attempAnswer = new AttempAnswer(Integer.parseInt(correct), Integer.parseInt(question), Integer.parseInt(attemp), ans);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveQuestions(Activity activity, QuestionBank questionsBank) {
        FileOutputStream fos = null;
        try {
            fos = activity.openFileOutput(QUESTION_FILE_NAME, MODE_PRIVATE);
            StringBuilder sb = new StringBuilder();
            sb = questionsBank.getStringBuilder();
            fos.write(sb.toString().getBytes());
            System.out.println("SAVED " + QUESTION_FILE_NAME);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean load(Activity activity,QuestionBank questionsBank) {
        String line = "";
        questionsBank.resetQuestionBank();
        ArrayList<Question> questionsList = new ArrayList<>();

        FileInputStream fis=null;
        try{
            fis= activity.openFileInput(QUESTION_FILE_NAME);
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader br=new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine())!=null){
                String[] data = line.split(",");
                String question = data[0];
                String answer = data[1];
                boolean ans;
                if (answer.equals("true")) {
                    ans = true;
                } else {
                    ans = false;
                }
                Question tempQuestion = new Question(question, ans);
                questionsList.add(tempQuestion);
            }
            questionsBank.setBank(questionsList);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
