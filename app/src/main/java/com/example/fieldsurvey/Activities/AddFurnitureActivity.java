package com.example.fieldsurvey.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fieldsurvey.DataBase.FirebaseDataHelper;
import com.example.fieldsurvey.R;

public class AddFurnitureActivity extends AppCompatActivity {
    Spinner spinnerType, spinnerMat;
    String projectName,currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_furniture);
        Intent i = getIntent();
        projectName=i.getStringExtra("Name");
        currentUser=i.getStringExtra("user");


        spinnerType=findViewById(R.id.spinnerfurntype);
        spinnerMat=findViewById(R.id.spinnerfurnmaterial);

        LayoutInflater inflater = AddFurnitureActivity.this.getLayoutInflater();

        ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(AddFurnitureActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Type));
        dataAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(dataAdapterType);

        ArrayAdapter<String> dataAdapterMat = new ArrayAdapter<String>(AddFurnitureActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Material));
        dataAdapterMat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMat.setAdapter(dataAdapterMat);
    }
    public void  AddtoDataBase(View view) {
        String type= spinnerType.getSelectedItem().toString();
        String material= spinnerMat.getSelectedItem().toString();
        FirebaseDataHelper.Instance.UploadFurniture(type,material,projectName,currentUser);



    }
}
