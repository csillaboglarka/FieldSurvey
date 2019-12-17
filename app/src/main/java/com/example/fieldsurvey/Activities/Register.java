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

import com.example.fieldsurvey.Classes.User;
import com.example.fieldsurvey.DataBase.FirebaseDataHelper;
import com.example.fieldsurvey.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    Button btn_register;
    EditText et_uName, et_uEmail, et_uPass;
    Intent intent;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        InitializeUI();
        mAuth = FirebaseAuth.getInstance();
    }

    private void InitializeUI() {
        btn_register= findViewById(R.id.RegistrationButton);;
        et_uName = findViewById(R.id.Name);
        et_uEmail = findViewById(R.id.UserEmail);
        et_uPass = findViewById(R.id.UserPassword);
    }

//nem maradhat uresen az email es jelszo
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
    //uj usert hoz letre a firebase beepitett fugvennyevel ,majd a nevvel es a userid-val az adatbazisba
    //is beszurja
   public void createAccount(View view) {

        if (!validateForm()) {
            return;
        }
        final String name= et_uName.getText().toString();
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
                   User user= new User( mAuth.getCurrentUser().getUid(),name);
                   FirebaseDataHelper.Instance.insertUser(user);
                    intent = new Intent(getApplicationContext(), Profile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });
    }
}
