package com.example.fieldsurvey.DataBase;


import com.example.fieldsurvey.Classes.Furniture;
import com.example.fieldsurvey.Classes.Plant;
import com.example.fieldsurvey.Classes.Project;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseDataHelper {
    public static class Instance {
        static FirebaseDatabase database = FirebaseDatabase.getInstance();
        static DatabaseReference surveyReference = database.getReference().child("Survey");
        static FirebaseAuth mAuth;

        public static String getCurentUser(){
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user= mAuth.getCurrentUser();
            if(user != null){
                return user.getUid();
            }else{
                return "error";
            }
        }

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
        public static String  CreateNewProject(Project projectName) {

            String key = surveyReference.push().getKey();
            surveyReference.child(key).setValue(projectName);
            return key;
        }

    }
}
