package com.example.fieldsurvey.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fieldsurvey.DataBase.FirebaseDataHelper;
import com.example.fieldsurvey.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProjectActivity extends AppCompatActivity {
    Button addItem;
    RecyclerView recyclerView;
    ArrayList<String> s;
    String projectName;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Intent i = getIntent();
        projectName=i.getStringExtra("Name");
        currentUser= FirebaseDataHelper.Instance.getCurentUser();
        Log.i("ddd",currentUser);


        Log.i("ddd","ddd");
        //recyclerView.findViewById(R.id.recyclerview_items);



    }
    public void addMyItem(View view){

        final String[] items = {"Plant", "Furniture"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectActivity.this);
        builder.setTitle("Choose what item to Add");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                                Intent intent = new Intent(ProjectActivity.this, AddPlantActivity.class);
                                intent.putExtra("Name",projectName);
                                intent.putExtra("user",currentUser);
                                startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(ProjectActivity.this, AddFurnitureActivity.class);
                        intent.putExtra("Name",projectName);
                        intent.putExtra("user",currentUser);
                        startActivity(intent);
                    }

            }
        });
        builder.show();
    }


}
