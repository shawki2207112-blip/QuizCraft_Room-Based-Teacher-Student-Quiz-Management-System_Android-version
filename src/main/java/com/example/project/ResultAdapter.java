package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.Holder> {

    Context c;
    ArrayList<ResultModel> list;

    public ResultAdapter(Context c, ArrayList<ResultModel> list) {
        this.c = c;
        this.list = list;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView name, score;

        Holder(View v){
            super(v);
            name = v.findViewById(R.id.resultStudentName);
            score = v.findViewById(R.id.resultScore);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.row_result, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder h, int position) {
        ResultModel rm = list.get(position);
        h.name.setText(rm.getStudentName());
        h.score.setText(rm.getScore() + "/" + rm.getTotal());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
