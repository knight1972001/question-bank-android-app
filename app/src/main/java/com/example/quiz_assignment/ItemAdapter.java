package com.example.quiz_assignment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    ArrayList<Question> questionList;
    Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClicked(Question question);
    }

    public ItemAdapter(Context context, ArrayList<Question> questionsBank, OnItemClickListener onItemClickListener) {
        this.context=context;
        this.questionList=questionsBank;
        this.listener = onItemClickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView question;
        private TextView answer;

        public MyViewHolder(View view) {
            super(view);
            question =(TextView) view.findViewById(R.id.textView1);
            answer =(TextView) view.findViewById(R.id.textView2);
        }

        public TextView getQuestion() {
            return question;
        }
        public TextView getAnswer() {
            return answer;
        }
    }

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row, parent, false);
        View view2 = LayoutInflater.from(context)
                .inflate(R.layout.row, parent, false);
        return new MyViewHolder(view2);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        holder.getQuestion().setText(questionList.get(position).getQuestion());
        holder.getAnswer().setText(String.valueOf(questionList.get(position).getAnswer()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(questionList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
}
