package com.example.fieldsurvey.DataBase;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fieldsurvey.Activities.AddFurnitureActivity;
import com.example.fieldsurvey.Classes.Furniture;
import com.example.fieldsurvey.Classes.Plant;
import com.example.fieldsurvey.Classes.Project;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
        static FirebaseAuth mAuth;

        String Uid;

        public static String getCurentUser(){
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user= mAuth.getCurrentUser();
            if(user != null){
                return user.getUid();
            }else{
                return "error";
            }
        }

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

                }
            });

        }

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

                }
            });

        }
        public static String  CreateNewProject(Project projectName) {

            String key = surveyReference.push().getKey();
            surveyReference.child(key).setValue(projectName);
            return key;
        }
        public static void ProjectsUpdate(ArrayList<String> q) {
            projects.addAll(q);

        }
public static ArrayList<String> GetQuestionsForSession(final String Uid) {

//    surveyReference.addChildEventListener(new ChildEventListener() {
//        @Override
//        public void onChildAdded(@NonNull DataSnapshot dataSnapshot,@Nullable String s) {
//            Project p =dataSnapshot.getValue(Project.class).getUserId();
//                if(==Uid ){
//            }
//
//                //if ( dataSnapshot.child("userId").getValue().toString().equals(Uid)) {
//                    String q = item.child("projectName").getValue().toString();
//
//
//                    projects.add(q);
//
//                }
//                ProjectsUpdate(projects);
//            }
//
//        }




    return projects;
}


    }
}
