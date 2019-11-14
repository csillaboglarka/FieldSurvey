package com.example.fieldsurvey.Classes;

public class Furniture {
   // private int furnitureId;
    private String type;
    private String material;

    public Furniture(String type, String  material) {
       // this.furnitureId=furnitureId;
        this.type=type;
        this.material=material;
    }

  /*  public int getFurnitureId() {
        return furnitureId;
    }

    public void setFurnitureId(int furnitureId) {
        this.furnitureId = furnitureId;
    }
*/
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
