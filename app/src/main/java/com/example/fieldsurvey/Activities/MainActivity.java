package com.example.fieldsurvey.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fieldsurvey.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.Task;



public class MainActivity extends AppCompatActivity {

    Button btn_login;
    EditText et_uEmail, et_uPass;
    TextView txt_register;
    Intent intent;
    CheckBox cb_remember;
    static final String TAG = "EmailPassword";
    static final String MyPREFERENCES = "MyPrefs" ;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeUI();
        mAuth = FirebaseAuth.getInstance();
        CheckPreferences();


    //figyeli hogy be van jelentkezve ,ha igen akkor egybol a Home-ra visz azaz a Profile
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent loginIntent=new Intent(MainActivity.this,Profile.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }

            }
        };

    }

    private void InitializeUI() {
        btn_login = findViewById(R.id.LoginButton);
        txt_register = findViewById(R.id.RegistrationButton);
        et_uEmail = findViewById(R.id.Email);
        et_uPass = findViewById(R.id.Password);
        cb_remember=findViewById(R.id.checkbox_remember);
    }


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

//bejelentkezes a firebase beepitett fugvenyevel tortenik az email es jelszoval
    //elotte megnezi ha bejeloltuk a negyzetet es ha igen lementi az bejelentkezesi adatokat
    public void signIn(View view) {  // Itt ellenorzi hogy megvan a user a firebase-ben es ha megvan csak akkor megy a kovetkezo oldalra
        intent = new Intent(MainActivity.this, Profile.class);
        SharedPreferences.Editor editor = sharedpreferences.edit();


        String email = et_uEmail.getText().toString();
        String password = et_uPass.getText().toString();
        if(cb_remember.isChecked()) {
            editor.putString("Email", email);
            editor.putString("Password", password);
            editor.putString("CheckBox", "true");
            editor.apply();


        }
        else {
            editor.clear();
            editor.commit();
        }

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
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }
//a mezok ellenorzese ne maradjon semmi uresen
    private boolean validateForm() {
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
    // amikor a Create one-ra kattintunk a regisztralas oldalra visz minket
    public void goRegister(View view) {
        intent = new Intent(getApplicationContext(), Register.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    //ellenorzi ha levoltak mentve a bejelentkezesi adatok, ha igen akkor betolti az adatokat
    public void CheckPreferences() {

        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains("CheckBox")){

                cb_remember.setChecked(true);
        }
        else  {
            cb_remember.setChecked(false);
        }

        if (sharedpreferences.contains("Password")) {
            et_uPass.setText(sharedpreferences.getString("Password", null));
        }
        if (sharedpreferences.contains("Email")) {
           et_uEmail.setText(sharedpreferences.getString("Email", null));


        }

    }


}




