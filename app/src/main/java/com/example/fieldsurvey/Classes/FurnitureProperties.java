package com.example.fieldsurvey.Classes;

public class FurnitureProperties extends Furniture {

    private String img;

    public FurnitureProperties(String type, String material, String img) {
        super( type, material);
        this.img=img;
    }
}
