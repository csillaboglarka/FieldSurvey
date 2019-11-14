package com.example.fieldsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fieldsurvey.DataBase.FirebaseDataHelper;

public class AddFurnitureActivity extends AppCompatActivity {
    Spinner spinnerType, spinnerMat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_furniture);

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
        String key = FirebaseDataHelper.Instance.UploadFurniture(type,material);
        if (key== "Invalid") {
            Toast.makeText(getApplicationContext(),"Nem sikerult",Toast.LENGTH_SHORT).show();


        }
        else {
            Toast.makeText(getApplicationContext(), key, Toast.LENGTH_SHORT).show();
        }


    }
}
