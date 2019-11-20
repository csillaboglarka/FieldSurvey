package com.example.fieldsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fieldsurvey.DataBase.FirebaseDataHelper;

public class AddPlantActivity extends AppCompatActivity {
    Spinner spinnerSpecies;
    EditText hunName, latinName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        hunName = findViewById(R.id.etHunName);
        latinName = findViewById(R.id.etLatinName);
        spinnerSpecies = findViewById(R.id.spinnerspecies);
        LayoutInflater inflater = AddPlantActivity.this.getLayoutInflater();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddPlantActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Species));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecies.setAdapter(dataAdapter);
    }

    public void  AddTODataBase(View view) {
        String species= spinnerSpecies.getSelectedItem().toString();
        String hunNamePlant= hunName.getText().toString();
        String latinNamePlant=latinName.getText().toString();
        String key = FirebaseDataHelper.Instance.UploadPlant(species,hunNamePlant,latinNamePlant);
        if (key== "Invalid") {
            Toast.makeText(getApplicationContext(),"Nem sikerult",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), key, Toast.LENGTH_SHORT).show();
        }


    }

}