package com.example.quiz_assignment;

import static java.lang.Integer.parseInt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainFragment extends Fragment {

    TextView quest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main, container, false);

        quest = (TextView) view.findViewById(R.id.quest);

        if(getArguments() != null){
            int color=parseInt(getArguments().getString("color"));
            quest.setBackgroundColor(color);
            Question question = getArguments().getParcelable("question");
            quest.setText(question.getQuestion());
        }

        return view;
    }



}