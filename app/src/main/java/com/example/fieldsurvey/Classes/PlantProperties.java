package com.example.fieldsurvey.Classes;

public class PlantProperties extends Plant {

    private String img;

    public PlantProperties( String plantSpecies, String hungarianName, String latinName,String img) {
        super(plantSpecies, hungarianName, latinName);
        this.img=img;
    }
}
