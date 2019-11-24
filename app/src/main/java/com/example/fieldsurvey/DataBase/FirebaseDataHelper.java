package com.example.fieldsurvey.DataBase;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
