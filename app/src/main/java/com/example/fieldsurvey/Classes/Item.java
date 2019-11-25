package com.example.fieldsurvey.Classes;

import java.util.ArrayList;

public class Item {
    public Plant plant;
    public Furniture furniture;
    public Item() {

    }
    public Item(Plant plant) {
        this.plant=plant;

    }
    public Item(Furniture furniture) {
        this.furniture=furniture;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Furniture getFurniture() {
        return furniture;
    }

    public void setFurniture(Furniture furniture) {
        this.furniture = furniture;
    }
}
