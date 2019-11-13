package com.example.fieldsurvey.Classes;

public class FurnitureProperties extends Furniture {

    private String butorKep;

    public FurnitureProperties(int furnitureId, String type, String material, String butorKep) {
        super(furnitureId, type, material);
        this.butorKep=butorKep;
    }
}
