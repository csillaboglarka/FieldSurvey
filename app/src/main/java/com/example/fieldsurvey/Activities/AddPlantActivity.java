package com.example.fieldsurvey.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.fieldsurvey.DataBase.FirebaseDataHelper;
import com.example.fieldsurvey.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.util.Date;


public class AddPlantActivity extends AppCompatActivity {
    Spinner spinnerSpecies;
    EditText et_hunName, et_latinName,et_locationNumber;
    String projectName,currentUser;
    Button btn_addImage;
    ImageView imageView;
    static final int CAMERA_REQUEST_CODE =1;
    StorageReference mStorage;
    byte[] photo;
    Bitmap bitmap;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;
    TextView tv_imageLabel;
    String photoName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        Intent i = getIntent();
        projectName=i.getStringExtra("Name");
        currentUser=i.getStringExtra("user");
        mStorage= FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        InitializeUI();
        SetMenu();



        //fenykep keszites
        btn_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_Permissions()) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);

                    }
                }

        });


    }
    // a kijelentkezes illetve a projektehez valo visszalepes navigacios gombok hozzarendelese
    private void SetMenu() {
        bottomNavigationView.getMenu().removeItem(R.id.navigation_addProject);
        bottomNavigationView.getMenu().removeItem(R.id.navigation_addItem);
        bottomNavigationView.getMenu().removeItem(R.id.navigation_save);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_logout: SignOut();
                        return true;
                }
                return false;
            }
        });

    }
    //minden elem initializalasa az xml fajl elemivel
    private void InitializeUI() {
        et_hunName = findViewById(R.id.etHunName);
        et_latinName = findViewById(R.id.etLatinName);
        et_locationNumber=findViewById(R.id.etLocationNumber);
        spinnerSpecies = findViewById(R.id.spinnerspecies);
        btn_addImage= findViewById(R.id.addImage);
        imageView= findViewById(R.id.imageViewPlant);
        bottomNavigationView=findViewById(R.id.navigationView);
        tv_imageLabel=findViewById(R.id.imageLabel);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddPlantActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Species));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecies.setAdapter(dataAdapter);
    }

    //kijelentkezik es vissza dob a bejelentkezes oldalra, a kijelentkezes a beepitett fugvennyel
    //tortenik
    public void SignOut() {
        final Intent intent = new Intent(AddPlantActivity.this, MainActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPlantActivity.this, R.style.MyDialogTheme);
        builder.setTitle("You want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
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

    // fenykep keszitese utan a kep eredmenyet bitmap-be lekerjuk
    //ezt majd megjeleniti majd a kepet byte-okba alakitsa a kep feltolteshez es nevet general

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode==RESULT_OK){

            imageView.setVisibility(View.VISIBLE);
            bitmap=(Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            photo = baos.toByteArray();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            photoName = projectName+ "_" + timeStamp;
            tv_imageLabel.setText(photoName);

        }
    }
    //az adatlap feltoltese tortenik az osszes mezo kotelezoen kitoltendo
    //eloszor a kepfeltoltes tortenik meg , utana pedig maga az elemek feltoltes
    //majd vissza visz az a projekt oldalra (Project activity)
    //ez akkor valosul meg ha a hozzadas gombra kattintunk

    public void  AddTODataBase(View view) {
        Intent intent = new Intent(AddPlantActivity.this,ProjectActivity.class);
        if(validateForm()) {
            String species = spinnerSpecies.getSelectedItem().toString();
            String hunNamePlant = et_hunName.getText().toString();
            String latinNamePlant = et_latinName.getText().toString();
            String locationNumber = et_locationNumber.getText().toString();

            mStorage.child(photoName).putBytes(photo).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                }
            });
            FirebaseDataHelper.Instance.UploadPlant(species, hunNamePlant, latinNamePlant, projectName, currentUser, photoName, locationNumber);
            intent.putExtra("Name", projectName);
            intent.putExtra("user", currentUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }

    }
    //hozzaafererest a kamerahoz, kulso memoria irasa es olvasasahoz ellenorzi
    private boolean check_Permissions(){

        boolean GRANTED;

        GRANTED= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        return GRANTED;
    }
    // a mezoket ellenorzi ne legyen semmi uresen hagyva
    private boolean validateForm() {

        if(TextUtils.isEmpty(et_hunName.getText().toString())&& TextUtils.isEmpty(et_latinName.getText().toString()) &&
                TextUtils.isEmpty(et_locationNumber.getText().toString()) && TextUtils.isEmpty(tv_imageLabel.getText().toString())) {

            Toast.makeText(getApplicationContext(), "Please fill all ", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }



}
