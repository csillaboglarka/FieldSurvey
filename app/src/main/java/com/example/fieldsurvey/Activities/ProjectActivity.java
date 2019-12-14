package com.example.fieldsurvey.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.fieldsurvey.Adapters.itemAdapter;
import com.example.fieldsurvey.Classes.Furniture;
import com.example.fieldsurvey.Classes.Item;
import com.example.fieldsurvey.Classes.Plant;
import com.example.fieldsurvey.DataBase.FirebaseDataHelper;
import com.example.fieldsurvey.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProjectActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Item> itemList;
    String projectName;
    String currentUser;
    RecyclerView.LayoutManager layoutManager;
    itemAdapter myAdapter;
    TextView txtProjectName;
    static final String TAG = "EmailPassword";
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference surveyReference = database.getReference().child("Survey");
    StorageReference mStorage;
    Bitmap bitmap;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    int counter=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Intent i = getIntent();
        projectName=i.getStringExtra("Name");
        currentUser= FirebaseDataHelper.Instance.getCurentUser();
        mStorage= FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        InitializeUI();
        txtProjectName.setText(projectName);
        ProgressBarInit();
        MenuInit();
        GetObjects();




    }

    private void GetObjects() {
        itemList= new ArrayList<>();
        final String currentUser=FirebaseDataHelper.Instance.getCurentUser();
        myAdapter = new itemAdapter(itemList);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setHasFixedSize(true);

        surveyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String user = item.child("userId").getValue().toString();
                    //itt az volt a gond hogy nem csak projektek voltak a databesben ezert itt errort adott a txt-nel
                    String name = item.child("projectName").getValue().toString();
                    if (user.equals(currentUser) && name.equals(projectName) ) {

                        if(item.child("Items").child("Plants").exists()) {
                            for(DataSnapshot objects : item.child("Items").child("Plants").getChildren()) {
                                final String spec = objects.child("plantSpecies").getValue().toString();
                                final String huName = objects.child("hungarianName").getValue().toString();
                                final String laName = objects.child("latinName").getValue().toString();
                                final String photoName = objects.child("plantImage").getValue().toString();
                                final String loc = objects.child("locationNumber").getValue().toString();
                                final long ONE_MEGABYTE = 1024 * 1024;
                                counter+=counter;

                                mStorage.child(photoName).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        Plant plant = new Plant(spec,huName,laName,bitmap,loc);
                                        Item plantItem = new Item(plant);
                                        applyItems(plantItem);
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {



                                    }
                                });


                            }
                        }
                        if(item.child("Items").child("Furniture").exists()) {
                            for(DataSnapshot objects : item.child("Items").child("Furniture").getChildren()) {
                                final String mat = objects.child("material").getValue().toString();
                                final String typ = objects.child("type").getValue().toString();
                                final String photoName = objects.child("furnitureImage").getValue().toString();
                                final String locNumb = objects.child("locationNumber").getValue().toString();
                                final long ONE_MEGABYTE = 1024 * 1024;
                                counter+=counter;
                                mStorage.child(photoName).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        Furniture furniture = new Furniture(mat,typ,bitmap,locNumb);
                                        Item furnitureItem = new Item(furniture);
                                        applyItems(furnitureItem);
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {



                                    }
                                });

                            }

                        }

                    }


                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void MenuInit() {
        bottomNavigationView.getMenu().removeItem(R.id.navigation_addProject);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_logout: SignOut();
                        return true;
                    case R.id.navigation_addItem: addMyItem();
                        return true;
                }
                return false;
            }
        });
    }

    private void ProgressBarInit() {
        progressBar.setMax(100);
        progressBar.setProgress(20);
    }

    private void InitializeUI() {
        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView=findViewById(R.id.navigationView);
        txtProjectName=findViewById(R.id.projectName);
        recyclerView=findViewById(R.id.recyclerview_projects);
    }

    public void SignOut() {
        final Intent intent = new Intent(ProjectActivity.this, MainActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectActivity.this, R.style.MyDialogTheme);
        builder.setTitle("You want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Log.d(TAG, "onAuthStateChanged:signed_out");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }

    public void addMyItem(){

        final String[] items = {"Plant", "Furniture"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectActivity.this,R.style.MyDialogTheme);
        builder.setTitle("Choose what item to Add");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                                Intent intent = new Intent(ProjectActivity.this, AddPlantActivity.class);
                                intent.putExtra("Name",projectName);
                                intent.putExtra("user",currentUser);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(ProjectActivity.this, AddFurnitureActivity.class);
                        intent.putExtra("Name",projectName);
                        intent.putExtra("user",currentUser);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

            }
        });
        builder.show();
    }
    private void applyItems(Item item) {
        itemList.add(item);
        myAdapter.notifyDataSetChanged();


    }



}
