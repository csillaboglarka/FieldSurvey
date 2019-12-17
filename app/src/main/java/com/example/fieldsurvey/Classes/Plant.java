package com.example.fieldsurvey.Classes;

import android.graphics.Bitmap;

public class Plant {
    //private int plantId;
    private String plantSpecies;
    private String hungarianName;
    private String latinName;
    private String plantImage;
    private Bitmap bitmap;
    private String locationNumber;



    public Plant(String plantSpecies, String hungarianName, String latinName,String plantImage,String locationNumber){
        this.plantSpecies=plantSpecies;
        this.hungarianName=hungarianName;
        this.latinName=latinName;
        this.plantImage=plantImage;
        this.locationNumber=locationNumber;

    }
    public Plant(String plantSpecies, String hungarianName, String latinName,Bitmap bitmap,String locationNumber){
        this.plantSpecies=plantSpecies;
        this.hungarianName=hungarianName;
        this.latinName=latinName;
        this.bitmap=bitmap;
        this.locationNumber=locationNumber;

    }

    public String getPlantImage() {
        return plantImage;
    }

    public void setPlantImage(String plantImage) {
        this.plantImage = plantImage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public String getPlantSpecies() {
        return plantSpecies;
    }

    public void setPlantSpecies(String plantSpecies) {
        this.plantSpecies = plantSpecies;
    }

    public String getHungarianName() {
        return hungarianName;
    }

    public void setHungarianName(String hungarianName) {
        this.hungarianName = hungarianName;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }
}
