package com.example.quiz_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button trueButton, falseButton;
    Question question;

    private StorageManager storageManager=new StorageManager();
    ArrayList<HistoryAnswer> historyList = new ArrayList<>();
    AttempAnswer attempAnswer = new AttempAnswer();
    QuestionBank questionsBank=new QuestionBank();
    int indexQuestion = 0;
    int attempCount = 0;
    int correctAnswer=0;
    private AlertDialog.Builder builder;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                getTheAverage();
                return true;
            case R.id.item2:
                Toast.makeText(this, "Item2 was called", Toast.LENGTH_SHORT).show();
                navigateToQuestionListView(new View(this));
                return true;
            case R.id.item3:
                Toast.makeText(this, "All previous results was deleted", Toast.LENGTH_SHORT).show();
                attempAnswer.resetNew();
                attempCount=0;
                storageManager.saveAttempAnswer(this,attempAnswer);
                return true;
            case R.id.item4:
                Toast.makeText(this, "Edit Question was called", Toast.LENGTH_SHORT).show();
                navigateToQuestionManager(new View(this));
                return true;
            case R.id.item5:
                navigateToHomePage(new View(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void navigateToHomePage(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("questionsBank", questionsBank);
        bundle.putParcelableArrayList("historyList", historyList);
        bundle.putParcelable("attempAnswer", attempAnswer);
        bundle.putInt("attempCount", attempCount);
        bundle.putInt("indexQuestion", indexQuestion);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }

    public void navigateToQuestionListView(View v){
        Intent myIntent = new Intent(this, QuestionList.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("questionsBank", questionsBank);
        bundle.putParcelableArrayList("historyList", historyList);
        bundle.putParcelable("attempAnswer", attempAnswer);
        bundle.putInt("indexQuestion", indexQuestion);
        bundle.putInt("attempCount", attempCount);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }

    public void navigateToQuestionManager(View v) {
        Intent myIntent = new Intent(this, QuestionsManager.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("questionsBank", questionsBank);
        bundle.putParcelableArrayList("historyList", historyList);
        bundle.putParcelable("attempAnswer", attempAnswer);
        bundle.putInt("attempCount", attempCount);
        bundle.putInt("indexQuestion", indexQuestion);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide(); //hide actionbar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide status
        setContentView(R.layout.activity_main);

        if(this.getIntent().getExtras()!=null){
            questionsBank = this.getIntent().getExtras().getParcelable("questionsBank");
            historyList = this.getIntent().getExtras().getParcelableArrayList("historyList");
            attempAnswer = this.getIntent().getExtras().getParcelable("attempAnswer");
            indexQuestion=this.getIntent().getExtras().getInt("indexQuestion");
            attempCount=this.getIntent().getExtras().getInt("attempCount");
            attempCount=attempAnswer.getNumOfAttemp();
        }
        if(savedInstanceState != null){
            questionsBank = savedInstanceState.getParcelable("questionsBank");
            historyList = savedInstanceState.getParcelableArrayList("historyList");
            attempAnswer = savedInstanceState.getParcelable("attempAnswer");
            indexQuestion=savedInstanceState.getInt("indexQuestion");
            attempCount=savedInstanceState.getInt("attempCount");
        }
        //assign
        trueButton = (Button) findViewById(R.id.trueButton);
        falseButton = (Button) findViewById(R.id.falseButton);

        if(!storageManager.load(this, questionsBank)){
            System.out.println("LOADED QUESTIONS FAILED");
            trueButton.setVisibility(View.GONE);
            falseButton.setVisibility(View.GONE);
        }else{
            System.out.println("LOADED QUESTIONS SUCCESSFUL");
            if(questionsBank.size()!=0){
                trueButton.setVisibility(View.VISIBLE);
                falseButton.setVisibility(View.VISIBLE);
                question = questionsBank.get(indexQuestion);
            }else{
                trueButton.setVisibility(View.GONE);
                falseButton.setVisibility(View.GONE);
                System.out.println("QUESTION BANKS == 0");
                questionsBank.resetQuestionBank();
            }
        }

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                indexQuestion++;
                if (indexQuestion == questionsBank.size()) {
                    getDialog();
                }else{
                    openFragment();
                }
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                indexQuestion++;
                if (indexQuestion == questionsBank.size()) {
                    getDialog();
                }else{
                    openFragment();
                }
            }
        });
        openFragment();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("questionsBank", questionsBank);
        outState.putParcelableArrayList("historyList", historyList);
        outState.putParcelable("attempAnswer", attempAnswer);
        outState.putInt("attempCount", attempCount);
        outState.putInt("indexQuestion", indexQuestion);
        super.onSaveInstanceState(outState);
    }

    private void getDialog() {
        questionsBank.shuffleQuestions();
        attempCount++;
        attempAnswer = new AttempAnswer(correctAnswer, questionsBank.size(), attempCount, false);
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Result").setMessage("Your score is: " + attempAnswer.getNumOfCorrectAnswer() + " out of " + questionsBank.size())
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//              finish();
//              Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
//                        Toast.LENGTH_SHORT).show();
                        attempAnswer.setSaved(true);
                        saveAttemp();
                        Toast.makeText(getApplicationContext(), "You saved this attemp. Let's Restart!", Toast.LENGTH_SHORT).show();
                        indexQuestion = 0;
                        correctAnswer=0;
                        historyList=new ArrayList<>();
                        openFragment();
                    }
                })
                .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                  finish();
                    attempAnswer.setSaved(false);
                    saveAttemp();
                    Toast.makeText(getApplicationContext(), "You ignored it. Let's Restart!", Toast.LENGTH_SHORT).show();
                    indexQuestion = 0;
                    correctAnswer=0;
                    historyList=new ArrayList<>();
                    openFragment();
                    }
                });
        builder.show();
    }
    public void saveAttemp(){
        storageManager.saveAttempAnswer(this,attempAnswer);
    }

    private void getTheAverage() {
        builder = new AlertDialog.Builder(this);
        if(attempAnswer.isEmpty()){
            builder.setTitle("Result").setMessage("You do not have any previous attempt!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
//              finish();
//              Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
//                        Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            String saveStatus;
            if(attempAnswer.isSaved()){
                saveStatus="Saved";
            }else{
                saveStatus="Not Save!";
            }
            builder.setTitle("Result").setMessage("Your correct answers are " + attempAnswer.getNumOfCorrectAnswer() + " in " + attempAnswer.getNumOfAttemp() + " attempts | Status: "+ saveStatus)
                    .setCancelable(false)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
//              finish();
//              Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
//                        Toast.LENGTH_SHORT).show();
                            if(!attempAnswer.isSaved() && historyList.size()!=questionsBank.size()) {
                                attempAnswer.setSaved(true);
                                Toast.makeText(getApplicationContext(), "You saved this attemp", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "You saved it before!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
//                finish();
                        }
                    });
        }
        builder.show();
    }

    private void checkAnswer(boolean answer) {
        question = questionsBank.get(indexQuestion);
        HistoryAnswer history;
        //snackbar begin
        String snackbarText = "";
        if (answer == question.getAnswer()) {
            correctAnswer++;
            snackbarText = "Correct";
        } else {
            snackbarText = "Uncorrect";
        }
        history = new HistoryAnswer(question, answer);
        historyList.add(history);
        System.out.println(question.toString() + " | ANSWER: " + answer + " | index: " + indexQuestion + " | Res: " + snackbarText);

        View contextView = (View) findViewById(R.id.snackBar);
        contextView.getLayoutParams().height = 10;

        Snackbar mSnackbar = Snackbar.make(contextView, snackbarText, Snackbar.LENGTH_LONG);
        View mView = mSnackbar.getView();

        TextView mTextView = (TextView) mView.findViewById(R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        mSnackbar.setBackgroundTint(getColor(R.color.purple_200)).setTextColor(getColor(R.color.black));
        mSnackbar.show();

        //snackbar end
    }

    private void openFragment() {
        Bundle bundle = new Bundle();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if(questionsBank != null && questionsBank.size()!=0){
            question = questionsBank.get(indexQuestion);
            int progress = (int) ((double) (((double) (indexQuestion + 1)) / questionsBank.size()) * 100);
            progressBar.setProgress(progress);
        }else{
            question=new Question("You do not have any question. Please add!", true);
        }

        bundle.putParcelable("question", question);

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        bundle.putString("color", String.valueOf(color));

        Fragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        System.out.println("Question Display: " + question.toString() + " | index: " + indexQuestion);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
    }


}