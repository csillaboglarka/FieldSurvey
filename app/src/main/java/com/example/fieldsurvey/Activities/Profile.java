package com.example.fieldsurvey.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fieldsurvey.Adapters.MyAdapter;
import com.example.fieldsurvey.Classes.Project;
import com.example.fieldsurvey.DataBase.FirebaseDataHelper;
import com.example.fieldsurvey.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EmailPassword";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Project> projectlist;
    MyAdapter adapter,projectAdapter;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference surveyReference = database.getReference().child("Survey");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_manage);
        mAuth = FirebaseAuth.getInstance();
        projectlist = new ArrayList<>();
        final String currentUser=FirebaseDataHelper.Instance.getCurentUser();
        surveyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String txt = item.child("userId").getValue().toString();
                    //itt az volt a gond hogy nem csak projektek voltak a databesben ezert itt errort adott a txt-nel
                    Log.i("fbdb",txt);
                    if (txt.equals(currentUser)) {
                        String q = item.child("projectName").getValue().toString();
                        Project newProject = new Project(q);
                        projectlist.add(newProject);

                    }

                }

                projectAdapter = new MyAdapter(projectlist);
                recyclerView = findViewById(R.id.recyclerview_projects);
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(projectAdapter);
                recyclerView.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        recyclerView=findViewById(R.id.recyclerview_projects);
//        recyclerView.setHasFixedSize(true);
//        layoutManager=new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        //recyclerView.setItemAnimator(new DefaultItemAnimator());
//        //recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
//
//        //FirebaseDataHelper.Instance.GetProjects(FirebaseDataHelper.Instance.getCurentUser());
//
//        adapter=new MyAdapter(projectlist);
//
//        recyclerView.setAdapter(adapter);

        /*adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                projectlist.get(position);
            }
        });*/

    }


    public void signOut(View view) {
        mAuth.signOut();
        Log.d(TAG, "onAuthStateChanged:signed_out");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void CreateNewProject(View View) {
        Intent intentProject = new Intent(Profile.this, ProjectActivity.class);

        EditText projectET= findViewById(R.id.etNewProject);
        String projectName = projectET.getText().toString();

        if (projectName == null || projectName.isEmpty()) {
            Toast addSessionNameToast = Toast.makeText(getApplicationContext(), "Add session name", Toast.LENGTH_SHORT);
            addSessionNameToast.show();
            return;
        }
        String currentUser=FirebaseDataHelper.Instance.getCurentUser();
        intentProject.putExtra("Name",projectName);
        intentProject.putExtra("user",currentUser);
        Project project=new Project(projectName,currentUser);

        String sessionKey = FirebaseDataHelper.Instance.CreateNewProject(project);
        if (sessionKey == "Invalid") {
            Toast failedToCreateSession = Toast.makeText(getApplicationContext(), "Failed to create session", Toast.LENGTH_SHORT);
            failedToCreateSession.show();
            return;
        }
        startActivity(intentProject);
    }

}