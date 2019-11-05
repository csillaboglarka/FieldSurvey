package com.example.fieldsurvey;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;


import android.text.TextUtils;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.Task;




public class MainActivity extends AppCompatActivity {


    Button login, register;
    EditText uEmail, uPass;
    Intent intent;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.LoginButton);
        register = findViewById(R.id.RegistrationButton);
        uEmail = findViewById(R.id.Email);
        uPass = findViewById(R.id.Password);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };
    }


    /*    register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
*/
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void signIn(View view) {  // Itt ellenorzi hogy megvan a user a firebase-ben es ha megvan csak akkor megy a kovetkezo oldalra
        intent = new Intent(MainActivity.this, DataManage.class);
        String email = uEmail.getText().toString();
        String password = uPass.getText().toString();
        if (!validateForm()) {  // megnezi hogy kivan-e toltve minden sor
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
         //itt van a kapcsolat az adatbazissal itt nezi meg konkretan
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) { //Ha nem kapja meg az adatbazisba
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                } else { // mikor megkapta akkor visz az uj activityre
                    Toast.makeText(MainActivity.this, "Succesful", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateForm() {
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
    public void goRegister(View view) {
        intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
    }


}




