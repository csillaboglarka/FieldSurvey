package com.example.fieldsurvey.DataBase;


import androidx.annotation.NonNull;

import com.example.fieldsurvey.Classes.Furniture;
import com.example.fieldsurvey.Classes.Plant;
import com.example.fieldsurvey.Classes.Project;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseDataHelper {
    public static ArrayList<Project> projects= new ArrayList<>();
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
        public static void ProjectsUpdate(ArrayList<Project> q) {
            projects.addAll(q);

        }

        public static ArrayList<Project> GetProjects(final String Uid) {

            surveyReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        String txt = item.child("userId").getValue().toString();

                        if (txt.equals(Uid)) {
                            String name = item.child("projectName").getValue().toString();

                            Project p = new Project(name);
                            projects.add(p);
                            ProjectsUpdate(projects);
                        }
                        // QuestionsUpdate(questions);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return  projects;
        }
    }
}
