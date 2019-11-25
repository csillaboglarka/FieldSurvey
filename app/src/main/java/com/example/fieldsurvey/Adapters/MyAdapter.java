package com.example.fieldsurvey.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fieldsurvey.Activities.ProjectActivity;
import com.example.fieldsurvey.Classes.Project;
import com.example.fieldsurvey.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<Project> projectlist;
    private OnItemClickListener mListener;
    Context context;
    String currentUser;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MyAdapter(ArrayList<Project> s, Context context, String currentUser) {
        this.projectlist = s;
        this.context=context;
        this.currentUser=currentUser;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        holder.textView.setText((projectlist.get(position).getProjectName()));
    }

    @Override
    public int getItemCount() {
        return projectlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.project_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent goProject = new Intent(context, ProjectActivity.class);
                        goProject.putExtra("user",currentUser);
                        goProject.putExtra("Name",textView.getText().toString());
                        context.startActivity(goProject);



                    }
                }
            });
        }
    }
}