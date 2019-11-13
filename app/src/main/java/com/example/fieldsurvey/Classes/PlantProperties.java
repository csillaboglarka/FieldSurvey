package com.example.fieldsurvey.Classes;

public class PlantProperties extends Plant {

    private String novenyKep;

    public PlantProperties(int plantId, String plantSpecies, String hungarianName, String latinName,String novenyKep) {
        super(plantId, plantSpecies, hungarianName, latinName);
        this.novenyKep=novenyKep;
    }
}
