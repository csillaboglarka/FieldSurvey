package com.example.fieldsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class DataManage extends AppCompatActivity {
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

    }
}
