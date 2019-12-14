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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

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
    Button btn_createPDF;
    RelativeLayout relativeLayout;

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

        btn_createPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePDF();
            }
        });



    }

    private Bitmap getBitmapFromView(View view) {
        RecyclerView hsv = findViewById(R.id.recyclerview_projects);
        int totalHeight = hsv.getChildAt(0).getHeight();
        int totalWidth = hsv.getChildAt(0).getWidth();
        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        }   else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);

        return returnedBitmap;
    }


    private static void addImage(Document document,byte[] byteArray)
    {
        Image image = null;
        try
        {
            image = Image.getInstance(byteArray);
        }
        catch (BadElementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try
        {
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void CreatePDF()
    {

        File folder = new File(Environment.getExternalStorageDirectory()+File.separator+"PDF Folder");
        folder.mkdirs();

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        final File myFile = new File(folder + timeStamp + ".pdf");
        try {
            OutputStream output  = new FileOutputStream(myFile);
            Document document = new Document(PageSize.A4);
            try{
                PdfWriter.getInstance(document, output);
                document.open();
                LinearLayout view2 = (LinearLayout)findViewById(R.id.view2);

                view2.setDrawingCacheEnabled(true);
                Bitmap screen2= getBitmapFromView(view2);
                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                screen2.compress(Bitmap.CompressFormat.JPEG,100, stream2);
                byte[] byteArray2 = stream2.toByteArray();
                addImage(document,byteArray2);

                document.close();
                AlertDialog.Builder builder =  new AlertDialog.Builder(ProjectActivity.this,R.style.MyDialogTheme);
                builder.setTitle("Success")
                        .setMessage("enter code herePDF File Generated Successfully.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            }

                        }).show();

                //document.add(new Paragraph(mBodyEditText.getText().toString()));
            }catch (DocumentException e)
            {
                //loading.dismiss();
                e.printStackTrace();
            }

        }catch (FileNotFoundException e)
        {
            // loading.dismiss();
            e.printStackTrace();
        }


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
        btn_createPDF=findViewById(R.id.btnpdf);
        relativeLayout=findViewById(R.id.relativlayout);
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
