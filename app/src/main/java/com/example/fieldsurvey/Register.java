package com.example.fieldsurvey;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    Button register;
    EditText uName, uEmail, uPass;
    Intent intent;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register= findViewById(R.id.RegistrationButton);;
        uName = findViewById(R.id.Name);
        uEmail = findViewById(R.id.UserEmail);
        uPass = findViewById(R.id.UserPassword);
        mAuth = FirebaseAuth.getInstance();
    }



    public boolean validateForm() {
        if (TextUtils.isEmpty(uEmail.getText().toString())) {
            uEmail.setError("Required.");
            return false;
        } else if (TextUtils.isEmpty(uPass.getText().toString())) {
            uPass.setError("Required.");
            return false;
        } else {
            uEmail.setError(null);
            uPass.setError(null);
            return true;
        }
    }
   public void createAccount(View view) {

        if (!validateForm()) {
            return;
        }
        String email = uEmail.getText().toString();
        String password = uPass.getText().toString();
        //showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                   uEmail.setTextColor(Color.RED);
                   uEmail.setText(task.getException().getMessage());
                    Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Register.this, "Succesful", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(),DataManage.class);
                    startActivity(intent);
                }
                //hideProgressDialog();
            }
        });
    }
}
