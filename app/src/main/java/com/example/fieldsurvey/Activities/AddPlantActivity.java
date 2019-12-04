package com.example.fieldsurvey.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fieldsurvey.DataBase.FirebaseDataHelper;
import com.example.fieldsurvey.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddPlantActivity extends AppCompatActivity {
    Spinner spinnerSpecies;
    EditText et_hunName, et_latinName;
    String projectName,currentUser;
    Button btn_addImage;
    ImageView imageView;
    private static final int CAMERA_REQUEST_CODE =1;
    private StorageReference mStorage;
    private  Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        mStorage= FirebaseStorage.getInstance().getReference();
        Intent i = getIntent();
        projectName=i.getStringExtra("Name");
        currentUser=i.getStringExtra("user");

        et_hunName = findViewById(R.id.etHunName);
        et_latinName = findViewById(R.id.etLatinName);
        spinnerSpecies = findViewById(R.id.spinnerspecies);
        btn_addImage= findViewById(R.id.addImage);
        imageView= findViewById(R.id.imageViewPlant);
        LayoutInflater inflater = AddPlantActivity.this.getLayoutInflater();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddPlantActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Species));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecies.setAdapter(dataAdapter);

        btn_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
                //intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode==RESULT_OK){
            Uri uri=data.getData();

            /*StorageReference filepath= mStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddPlantActivity.this,"Upload finished!", Toast.LENGTH_LONG).show();
                }
            });*/

            Bitmap bitmap=(Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    public void  AddTODataBase(View view) {
        String species= spinnerSpecies.getSelectedItem().toString();
        String hunNamePlant= et_hunName.getText().toString();
        String latinNamePlant=et_latinName.getText().toString();
        FirebaseDataHelper.Instance.UploadPlant(species,hunNamePlant,latinNamePlant,projectName,currentUser);
    }

}
