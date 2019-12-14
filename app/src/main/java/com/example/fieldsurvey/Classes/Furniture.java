package com.example.fieldsurvey.Classes;

import android.graphics.Bitmap;

public class Furniture {
   // private int furnitureId;
    private String type;
    private String material;
    private String furnitureImage;
    private Bitmap bitmap;
    private String locationNumber;

//    public Furniture(String type, String  material,) {
//       // this.furnitureId=furnitureId;
//        this.type=type;
//        this.material=material;
//        this.locationNumber=locationNumber;
//    }

    public Furniture(String type, String material, String furnitureImage, String locationNumber) {
        this.type = type;
        this.material = material;
        this.furnitureImage = furnitureImage;
        this.locationNumber=locationNumber;
    }

    public String getFurnitureImage() {
        return furnitureImage;
    }

    public void setFurnitureImage(String furnitureImage) {
        this.furnitureImage = furnitureImage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Furniture(String type, String material, Bitmap bitmap, String locationNumber) {
        this.type = type;
        this.material = material;
        this.bitmap = bitmap;
        this.locationNumber=locationNumber;
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

    public String getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
