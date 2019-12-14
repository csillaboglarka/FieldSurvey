package com.example.fieldsurvey.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fieldsurvey.Adapters.MyAdapter;
import com.example.fieldsurvey.Classes.Project;
import com.example.fieldsurvey.DataBase.FirebaseDataHelper.Instance;
import com.example.fieldsurvey.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Project> listOfProjects;
    MyAdapter projectAdapter;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference surveyReference = database.getReference().child("Survey");
    BottomNavigationView bottomNavigationView;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_manage);
        mAuth = FirebaseAuth.getInstance();
        InitializeUI();
        ProgressBarInit();
        MenuInit();
        GetProjects();


    }

    private void GetProjects() {
        listOfProjects = new ArrayList<>();
        final String currentUser= Instance.getCurentUser();
        surveyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String userId = item.child("userId").getValue().toString();
                    //itt az volt a gond hogy nem csak projektek voltak a databesben ezert itt errort adott a txt-nel
                    if (userId.equals(currentUser)) {
                        String projectName = item.child("projectName").getValue().toString();
                        Project newProject = new Project(projectName);
                        listOfProjects.add(newProject);

                    }

                }

                projectAdapter = new MyAdapter(listOfProjects,Profile.this,currentUser);
                recyclerView = findViewById(R.id.recyclerview_projects);
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(projectAdapter);
                recyclerView.setHasFixedSize(true);
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void MenuInit() {
        bottomNavigationView.getMenu().removeItem(R.id.navigation_home);
        bottomNavigationView.getMenu().removeItem(R.id.navigation_addItem);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(Profile.this, Profile.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_logout: SignOut();
                        return true;
                    case R.id.navigation_addProject: CreateNewProject();
                        return true;
                }
                return false;
            }
        });
    }

    private void ProgressBarInit() {
        progressBar.setMax(100);
        progressBar.setProgress(20);
    }

    private void InitializeUI() {
        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView= findViewById(R.id.navigationView);
    }


    public void SignOut() {
        final Intent intent = new Intent(Profile.this, MainActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this, R.style.MyDialogTheme);
        builder.setTitle("You want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Log.d(TAG, "onAuthStateChanged:signed_out");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();


    }

    public void CreateNewProject() {
        final Intent intentProject = new Intent(Profile.this, ProjectActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this, R.style.MyDialogTheme);
        builder.setTitle("Create new Project");
        final EditText etProjectName = new EditText(getApplicationContext());
        etProjectName.setHint(R.string.projectName);
        etProjectName.setTextColor(Color.parseColor("#FFFFFF"));
        etProjectName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        etProjectName.setHintTextColor(Color.parseColor("#977C7B7B"));
        LinearLayout layout= new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(etProjectName);
        builder.setView(layout);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!etProjectName.getText().toString().isEmpty()) {
                    String currentUser = Instance.getCurentUser();
                    intentProject.putExtra("Name", etProjectName.getText().toString());
                    intentProject.putExtra("user", currentUser);
                    Project project = new Project(etProjectName.getText().toString(), currentUser);

                    String sessionKey = Instance.CreateNewProject(project);
                    if (sessionKey.equals("Invalid")) {
                        Toast failedToCreateSession = Toast.makeText(getApplicationContext(), "Failed to create session", Toast.LENGTH_SHORT);
                        failedToCreateSession.show();
                        return;
                    }
                    intentProject.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intentProject.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentProject);

                }

            }
        });
        builder.show();

    }


}