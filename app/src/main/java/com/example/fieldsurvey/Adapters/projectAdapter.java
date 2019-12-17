package com.example.fieldsurvey.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fieldsurvey.Activities.ProjectActivity;
import com.example.fieldsurvey.Classes.Project;
import com.example.fieldsurvey.DataBase.FirebaseDataHelper;
import com.example.fieldsurvey.R;

import java.util.ArrayList;

public class projectAdapter extends RecyclerView.Adapter<projectAdapter.ViewHolder> {
    private ArrayList<Project> projectlist;
    private OnItemClickListener mListener;
    private Context context;
    private String currentUser;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public projectAdapter(ArrayList<Project> s, Context context, String currentUser) {
        this.projectlist = s;
        this.context=context;
        this.currentUser=currentUser;
    }

    @NonNull
    @Override
    public projectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull projectAdapter.ViewHolder holder, int position) {
        holder.textView.setText((projectlist.get(position).getProjectName()));
    }
    public void removeItem(int position) {
        projectlist.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return projectlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.project_name);
            //a projektre visz minket
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent goProject = new Intent(context, ProjectActivity.class);
                        goProject.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        goProject.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        goProject.putExtra("user", currentUser);
                        goProject.putExtra("Name", textView.getText().toString());
                        context.startActivity(goProject);


                    }
                }
            });
            //ha hosszan kattintunk egy projektre kitorolhetjuk
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
                        builder.setTitle("You want to delete this Project");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                        FirebaseDataHelper.Instance.DeleteProject(textView.getText().toString());
                                        removeItem(position);

                            }

                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                        return true;
                   }
                 return false;
                }
            });
        }
    }
}