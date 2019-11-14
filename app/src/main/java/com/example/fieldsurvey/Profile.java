package com.example.fieldsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fieldsurvey.DataBase.FirebaseDataHelper;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_manage);
        mAuth = FirebaseAuth.getInstance();


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

        String sessionKey = FirebaseDataHelper.Instance.CreateNewProject(projectName);
        if (sessionKey == "Invalid") {
            Toast failedToCreateSession = Toast.makeText(getApplicationContext(), "Failed to create session", Toast.LENGTH_SHORT);
            failedToCreateSession.show();
            return;
        }
        startActivity(intentProject);
    }

}