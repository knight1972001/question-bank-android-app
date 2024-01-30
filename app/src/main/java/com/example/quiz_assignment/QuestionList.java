package com.example.quiz_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class QuestionList extends AppCompatActivity {
    private StorageManager storageManager=new StorageManager();
    ArrayList<HistoryAnswer> historyList;
    AttempAnswer attempAnswer = new AttempAnswer();
    QuestionBank questionsBank = new QuestionBank();
    int indexQuestion = 0;
    int attempCount = 0;

    RecyclerView recyclerList;
    ItemAdapter adapter;

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
                Toast.makeText(this, "Item3 was called", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("questionsBank", questionsBank);
        outState.putParcelableArrayList("historyList", historyList);
        outState.putParcelable("attempAnswer", attempAnswer);
        outState.putInt("attempCount", attempCount);
        outState.putInt("indexQuestion", indexQuestion);
        super.onSaveInstanceState(outState);
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
        bundle.putInt("attempCount", attempCount);
        bundle.putInt("indexQuestion", indexQuestion);
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

    private void getTheAverage() {
        AlertDialog.Builder builder;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide(); //hide actionbar
        setContentView(R.layout.activity_question_list);
        recyclerList = (RecyclerView) findViewById(R.id.recyclerView);
        if(this.getIntent().getExtras()!=null){
            questionsBank = this.getIntent().getExtras().getParcelable("questionsBank");
            historyList = this.getIntent().getExtras().getParcelableArrayList("historyList");
            attempAnswer = this.getIntent().getExtras().getParcelable("attempAnswer");
            indexQuestion=this.getIntent().getExtras().getInt("indexQuestion");
            attempCount=this.getIntent().getExtras().getInt("attempCount");
        }
        if(savedInstanceState != null){
            questionsBank = savedInstanceState.getParcelable("questionsBank");
            historyList = savedInstanceState.getParcelableArrayList("historyList");
            attempAnswer = savedInstanceState.getParcelable("attempAnswer");
            indexQuestion=savedInstanceState.getInt("indexQuestion");
            attempCount=savedInstanceState.getInt("attempCount");
        }
        if(storageManager.load(this, questionsBank)){
            System.out.println("LOADED QUESTIONS");
        }

        System.out.println("Size: "+questionsBank.size());

        adapter = new ItemAdapter(this, questionsBank.getQuestionsList(), new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Question question) {
                navigateToQuestionDetail(question);
            }
        });
        recyclerList.setAdapter(adapter);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));
    }

    public void navigateToQuestionDetail(Question question){
        int indexQuestion=questionsBank.getIndex(question);
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
}