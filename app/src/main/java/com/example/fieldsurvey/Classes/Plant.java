package com.example.fieldsurvey.Classes;

public class Plant {
    //private int plantId;
    private String plantSpecies;
    private String hungarianName;
    private String latinName;


    public Plant(String plantSpecies, String hungarianName, String latinName){
        //this.plantId=plantId;
        this.plantSpecies=plantSpecies;
        this.hungarianName=hungarianName;
        this.latinName=latinName;
    }

//    public int getPlantId() {
//        return plantId;
//    }
//
//    public void setPlantId(int plantId) {
//        this.plantId = plantId;
//    }

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
}
