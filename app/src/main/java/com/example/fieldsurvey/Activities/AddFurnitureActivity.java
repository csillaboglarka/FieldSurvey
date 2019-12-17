package com.example.fieldsurvey.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
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

public class AddFurnitureActivity extends AppCompatActivity {
    Spinner spinnerType, spinnerMat;
    String projectName,currentUser;
    ImageView imageView;
    static final int CAMERA_REQUEST_CODE =1;
    StorageReference mStorage;
    byte[] photo;
    Button btn_addImage;
    EditText et_locationNumber;
    BottomNavigationView bottomNavigationView;
    String photoName;
    FirebaseAuth mAuth;
    TextView tv_imageLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_furniture);
        Intent i = getIntent();
        projectName=i.getStringExtra("Name");
        currentUser=i.getStringExtra("user");
        mStorage= FirebaseStorage.getInstance().getReference();
        InitializeUI();
        SetMenu();

        //fenykep keszites
        btn_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);

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
        spinnerType=findViewById(R.id.spinnerfurntype);
        spinnerMat=findViewById(R.id.spinnerfurnmaterial);
        imageView=findViewById(R.id.imageViewFurniture);
        et_locationNumber=findViewById(R.id.etLocationNumber);
        btn_addImage=findViewById(R.id.addImage);
        tv_imageLabel=findViewById(R.id.imageLabel);
        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView=findViewById(R.id.navigationView);
        ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(AddFurnitureActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Type));
        dataAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(dataAdapterType);
        ArrayAdapter<String> dataAdapterMat = new ArrayAdapter<String>(AddFurnitureActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Material));
        dataAdapterMat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMat.setAdapter(dataAdapterMat);
    }
    //kijelentkezik es vissza dob a bejelentkezes oldalra, a kijelentkezes a beepitett fugvennyel
    //tortenik
    public void SignOut() {
        final Intent intent = new Intent(AddFurnitureActivity.this, MainActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFurnitureActivity.this, R.style.MyDialogTheme);
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
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            photo = baos.toByteArray();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            photoName= projectName + "_" + timeStamp;
            tv_imageLabel.setText(photoName);

        }
    }
    //az adatlap feltoltese tortenik az osszes mezo kotelezoen kitoltendo
    //eloszor a kepfeltoltes tortenik meg , utana pedig maga az elemek feltoltes
    //majd vissza visz az a projekt oldalra (Project activity)
    //ez akkor valosul meg ha a hozzadas gombra kattintunk
    public void AddToDataBase(View view) {
        Intent intent = new Intent(AddFurnitureActivity.this,ProjectActivity.class);
        if(validateForm()) {
            String type = spinnerType.getSelectedItem().toString();
            String material = spinnerMat.getSelectedItem().toString();
            String locationNumb = et_locationNumber.getText().toString();
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
            FirebaseDataHelper.Instance.UploadFurniture(type, material, projectName, currentUser, photoName, locationNumb);
            intent.putExtra("Name", projectName);
            intent.putExtra("user", currentUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }


//Mezok validalasa
    private boolean validateForm() {
        if (TextUtils.isEmpty(et_locationNumber.getText().toString())) {
            et_locationNumber.setError("Required.");
            return false;
        }
        else { if (TextUtils.isEmpty(tv_imageLabel.getText().toString())) {
            Toast.makeText(getApplicationContext(),"Please insert photo",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            et_locationNumber.setError(null);

        }

        }
            return true;
    }
}
