package com.example.fieldsurvey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fieldsurvey.Classes.MyAdapter;
import com.example.fieldsurvey.Classes.Project;
import com.example.fieldsurvey.DataBase.FirebaseDataHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EmailPassword";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Project> projectlist;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_manage);
        mAuth = FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.recyclerview_projects);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        //FirebaseDataHelper.Instance.GetProjects(FirebaseDataHelper.Instance.getCurentUser());

        adapter=new MyAdapter(projectlist);

        recyclerView.setAdapter(adapter);

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
    public void addPlant(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AddPlantActivity.class);
        startActivity(intent);
    }
    public void addFurniture(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AddFurnitureActivity.class);
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