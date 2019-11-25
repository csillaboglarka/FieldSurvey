package com.example.fieldsurvey.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fieldsurvey.Adapters.MyAdapter;
import com.example.fieldsurvey.Adapters.itemAdapter;
import com.example.fieldsurvey.Classes.Furniture;
import com.example.fieldsurvey.Classes.Item;
import com.example.fieldsurvey.Classes.Plant;
import com.example.fieldsurvey.Classes.Project;
import com.example.fieldsurvey.DataBase.FirebaseDataHelper;
import com.example.fieldsurvey.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProjectActivity extends AppCompatActivity {
    Button addItem;
    RecyclerView recyclerView;
    ArrayList<Item> itemList;
    String projectName;
    String currentUser;
    RecyclerView.LayoutManager layoutManager;
    itemAdapter myAdapter;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference surveyReference = database.getReference().child("Survey");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Intent i = getIntent();
        projectName=i.getStringExtra("Name");
        currentUser= FirebaseDataHelper.Instance.getCurentUser();
        itemList= new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerview_projects);

        final String currentUser=FirebaseDataHelper.Instance.getCurentUser();
        surveyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String user = item.child("userId").getValue().toString();
                    //itt az volt a gond hogy nem csak projektek voltak a databesben ezert itt errort adott a txt-nel
                    String name = item.child("projectName").getValue().toString();
                    if (user.equals(currentUser) && name.equals(projectName) ) {

                        if(item.child("Items").child("Plants").exists()) {
                            for(DataSnapshot dupla : item.child("Items").child("Plants").getChildren()) {
                                    String spec = dupla.child("plantSpecies").getValue().toString();
                                    String huName = dupla.child("hungarianName").getValue().toString();
                                    String laName = dupla.child("latinName").getValue().toString();
                                    Log.i("dddd",huName + laName);
                                    Plant p = new Plant(spec,huName,laName);
                                    Item it1 = new Item(p);
                                    //itemList.add(it1);
                            }
                        }
                        if(item.child("Items").child("Furniture").exists()) {
                            for(DataSnapshot dupla : item.child("Items").child("Furniture").getChildren()) {
                                String mat = dupla.child("material").getValue().toString();
                                String typ = dupla.child("type").getValue().toString();
                                Furniture f = new Furniture(mat,typ);
                                Item it = new Item(f);
                                itemList.add(it);
                            }

                        }


                    }


                }
                myAdapter = new itemAdapter(itemList);
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
    public void addMyItem(View view){

        final String[] items = {"Plant", "Furniture"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectActivity.this);
        builder.setTitle("Choose what item to Add");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                                Intent intent = new Intent(ProjectActivity.this, AddPlantActivity.class);
                                intent.putExtra("Name",projectName);
                                intent.putExtra("user",currentUser);
                                startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(ProjectActivity.this, AddFurnitureActivity.class);
                        intent.putExtra("Name",projectName);
                        intent.putExtra("user",currentUser);
                        startActivity(intent);
                    }

            }
        });
        builder.show();
    }


}
