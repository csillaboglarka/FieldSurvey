package com.example.fieldsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

    public void addPlant(View view) {
        setContentView(R.layout.plantform);
    }

    public void addFurniture(View view) {
        setContentView(R.layout.furnitureform);
    }
}
