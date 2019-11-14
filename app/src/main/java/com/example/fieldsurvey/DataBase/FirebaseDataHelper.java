package com.example.fieldsurvey.DataBase;


import com.example.fieldsurvey.Classes.Furniture;
import com.example.fieldsurvey.Classes.Plant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseDataHelper {
    public static class Instance {
        static FirebaseDatabase database = FirebaseDatabase.getInstance();
        static DatabaseReference surveyReference = database.getReference().child("Survey");


        public static String UploadFurniture(String sType, String sMaterial) {
            Furniture furniture = new Furniture(sType,sMaterial);
            String key = surveyReference.push().getKey();
            surveyReference.child(key).setValue(furniture);
            return key;
        }

        public static String UploadPlant(String sSpecies, String sHunName, String sLatinName) {
            Plant plant = new Plant(sSpecies,sHunName,sLatinName);
            String key = surveyReference.push().getKey();
            surveyReference.child(key).setValue(plant);
            return key;
        }
        public static String  CreateNewProject(String projectName) {

            String key = surveyReference.push().getKey();
            surveyReference.child(key).setValue(projectName);
            return key;
        }

    }}
