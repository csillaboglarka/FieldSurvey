package com.example.fieldsurvey.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fieldsurvey.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    Button btn_register;
    EditText et_uName, et_uEmail, et_uPass;
    Intent intent;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register= findViewById(R.id.RegistrationButton);;
        et_uName = findViewById(R.id.Name);
        et_uEmail = findViewById(R.id.UserEmail);
        et_uPass = findViewById(R.id.UserPassword);
        mAuth = FirebaseAuth.getInstance();
    }



    public boolean validateForm() {
        if (TextUtils.isEmpty(et_uEmail.getText().toString())) {
            et_uEmail.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(et_uPass.getText().toString())) {
            et_uPass.setError("Required.");
            return false;
        } else {
            et_uEmail.setError(null);
            et_uPass.setError(null);
            return true;
        }
    }
   public void createAccount(View view) {

        if (!validateForm()) {
            return;
        }
        String email = et_uEmail.getText().toString();
        String password = et_uPass.getText().toString();
        //showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    et_uEmail.setTextColor(Color.RED);
                    et_uEmail.setText(task.getException().getMessage());
                    Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Register.this, "Succesful", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), Profile.class);
                    startActivity(intent);
                }
                //hideProgressDialog();
            }
        });
    }
}
