package com.example.fieldsurvey.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
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
    RelativeLayout relativeLayout;
    ProgressDialog pDialog;
    boolean boolean_permission;
    boolean boolean_save;
    public static int REQUEST_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Intent i = getIntent();
        projectName=i.getStringExtra("Name");
        currentUser= FirebaseDataHelper.Instance.getCurrentUser();
        mStorage= FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        InitializeUI();
        txtProjectName.setText(projectName);
        ProgressBarInit();
        MenuInit();
        GetObjects();

    }

//dialog amelyik elojon amig keszul a pdf
    private void createMultiPagePDF() {

        pDialog = new ProgressDialog(ProjectActivity.this,R.style.MyDialogTheme);
        pDialog.setMessage("Creating PDF");
        pDialog.setCancelable(false);
        pDialog.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (boolean_save) {
//
                } else {
                    if (boolean_permission) {
                        pDialog = new ProgressDialog(ProjectActivity.this);
                        pDialog.setMessage("Please wait");
                            bitmap = loadBitmapFromView(recyclerView, recyclerView.getWidth(), recyclerView.getHeight());
                        createMultiplePDF();
                    }
                    createMultiplePDF();
                }
            }
        }, 4000);
    }

//view-bol bitmap-et keszit
    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

//a kulso memoria irasahoz ellenorzi a hozzaferest
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;

            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }

//pdf fajl letrehozasa nev,cim, es a recyclerview elemek hozzadasa a dokumentumhoz
    public void createMultiplePDF() {
        File filePath = new File(Environment.getExternalStorageDirectory()+File.separator+"FieldSurvey_");
        filePath.mkdirs();

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("_yyyyMMdd_HHmmss").format(date);

        final File myFile = new File(filePath + projectName +timeStamp + ".pdf");

        Document document = new Document(PageSize.A4, 1, 1, 0, 0);

        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(myFile));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();


        try {
            int i;
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 35.0f, Font.BOLD, BaseColor.BLACK);
            Paragraph Title = new Paragraph(projectName,font);
            Title.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(Title);
            for(i = 0; i<itemList.size(); ++i) {
                recyclerView.setDrawingCacheEnabled(true);
                Bitmap processedBitmap;
                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);

                if(holder == null){
                    holder = myAdapter.holderHashMap.get(i);
                }

                processedBitmap = getBitmapFromView(holder.itemView);
                Log.i("hsvp", processedBitmap.toString());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                processedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                Image myImg = Image.getInstance(stream.toByteArray());
                Log.i("hsvi", myImg.getId().toString());
                myImg.scaleToFit(PageSize.A4);
                myImg.setAlignment(Image.ALIGN_CENTER);
                document.add(myImg);
                writer.setPageEmpty(false);
            }

        }catch (DocumentException ex){
                ex.printStackTrace();
            } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();

            pDialog.dismiss();

    }
//bitmap-et keszit a view-ekbol + hatteret feheret rak ha nem volt
   private Bitmap getBitmapFromView(View view) {


        int totalHeight = view.getHeight();
        int totalWidth =   view.getWidth();
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
//egy projekt osszes objektumait jelenitik meg recyclerview-ba
    private void GetObjects() {
        itemList= new ArrayList<>();
        final String currentUser=FirebaseDataHelper.Instance.getCurrentUser();
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
                    String name = item.child("projectName").getValue().toString();
                    // user es projektnev talal azon belul keresi az osszes novenyt is es butorokat
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
//menut inicializalja kijelentkezes ,uj noveny/butor hozzadas ,profile-oldalra lepes es a
// pdf-be valo mentes gomb inicializalasa
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
                    case R.id.navigation_save:  createMultiPagePDF();
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
//megkerdi a hozzadas gomb kattintas utan hogy milyen elemet szeretnenk hozzadni es aszerint
    //visz minket a megfelelo oldalra
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
    //frissiti az adaptert uj elem beszurasa eseten
    private void applyItems(Item item) {
        itemList.add(item);
        myAdapter.notifyDataSetChanged();


    }



}
