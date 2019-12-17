package com.example.fieldsurvey.DataBase;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.fieldsurvey.Classes.Furniture;
import com.example.fieldsurvey.Classes.Plant;
import com.example.fieldsurvey.Classes.Project;
import com.example.fieldsurvey.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseDataHelper {
    public static ArrayList<String> projects= new ArrayList<>();
    public static class Instance {
        static FirebaseDatabase database = FirebaseDatabase.getInstance();
        static DatabaseReference surveyReference = database.getReference().child("Survey");
        static DatabaseReference userReference = database.getReference().child("User");
        static FirebaseAuth mAuth;

        //Returns the current user id from firebase
        public static String getCurrentUser(){
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user= mAuth.getCurrentUser();
            if(user != null){
                return user.getUid();
            }else{
                return "error";
            }
        }
        //Initialize furniture elements and then finds the project with the name and userId given
        //Then uploads the Furniture to Firebase
        public static void UploadFurniture(String sType, String sMaterial, final String projectName, final String currentUser, final String photoName,String locationNumb) {
            final Furniture furniture = new Furniture(sType,sMaterial,photoName,locationNumb);
            surveyReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot item : dataSnapshot.getChildren()){
                        if(item.child("projectName").getValue().toString().equals(projectName) && item.child("userId").getValue().toString().equals(currentUser)) {
                             String key =item.getKey();
                             String itemKey = surveyReference.child(key).child("Items").child("Furniture").push().getKey();
                             surveyReference.child(key).child("Items").child("Furniture").child(itemKey).setValue(furniture);

                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("DatabaseError","UploadFurniture");
                }
            });

        }
        //Initialize plant elements and then finds the project with the project name and userId given
        //Then uploads the Plant to Firebase

        public static void UploadPlant(String sSpecies, String sHunName, String sLatinName , final String projectName, final String currentUser, String plantImage,String locationNumber) {
            final Plant plant = new Plant(sSpecies,sHunName,sLatinName,plantImage,locationNumber);

            surveyReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot item : dataSnapshot.getChildren()){
                        if(item.child("projectName").getValue().toString().equals(projectName) && item.child("userId").getValue().toString().equals(currentUser)) {
                           String key =item.getKey();
                            String itemKey = surveyReference.child(key).child("Items").child("Furniture").push().getKey();
                            surveyReference.child(key).child("Items").child("Plants").child(itemKey).setValue(plant);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("DatabaseError","UploadPlant");
                }
            });

        }

        public static String  CreateNewProject(Project projectName) {

            String key = surveyReference.push().getKey();
            surveyReference.child(key).setValue(projectName);
            return key;
        }

        public static void DeleteProject(final String projectName) {
            surveyReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot item : dataSnapshot.getChildren()) {
                        if(item.child("projectName").getValue().toString().equals(projectName)) {
                           String key =item.getKey();
                          surveyReference.child(key).removeValue();
                            Log.i("delete",key);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    public static void insertUser(User user){
           String key= userReference.push().getKey();
           userReference.child(key).setValue(user);
    }

    }
}
