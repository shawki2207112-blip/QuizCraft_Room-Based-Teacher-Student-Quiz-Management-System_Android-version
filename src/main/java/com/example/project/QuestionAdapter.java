package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.Holder> {

    Context c;
    ArrayList<QuestionModel> list;
    String mode;

    public QuestionAdapter(Context c, ArrayList<QuestionModel> list, String mode) {
        this.c = c;
        this.list = list;
        this.mode = mode;
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView q;

        Holder(View v){
            super(v);
            q = v.findViewById(R.id.previewQuestionText);

            v.setOnClickListener(view -> {
                int pos = getBindingAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    QuestionModel qm = list.get(pos);

                    if("update".equals(mode)){
                        Intent i = new Intent(c, EditQuestionActivity.class);
                        i.putExtra("id", qm.getId());
                        i.putExtra("roomId", qm.getRoomId());
                        c.startActivity(i);

                    } else if("delete".equals(mode)){
                        QuestionDatabase db = new QuestionDatabase(c);
                        db.deleteQuestion(qm.getId());
                        list.remove(pos);
                        notifyItemRemoved(pos);

                    } else if("preview".equals(mode)){
                        Intent i = new Intent(c, PreviewQuestionActivity.class);
                        i.putExtra("id", qm.getId());
                        c.startActivity(i);
                    }
                }
            });
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.row_question, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder h, int position) {
        h.q.setText(list.get(position).getQuestion());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
