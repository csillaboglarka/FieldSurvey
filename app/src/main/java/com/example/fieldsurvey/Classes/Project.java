package com.example.fieldsurvey.Classes;


import java.util.ArrayList;

public class Project {
  private   String projectName;
  private String userId;
  private ArrayList<Item> itemsList;



    public Project(String projectName, String userId) {
        this.projectName = projectName;
        this.userId=userId;
    }

    public Project(String projectName) {
        this.projectName = projectName;

    }

    public void setItemsList(ArrayList<Item> itemsList) {
        this.itemsList = itemsList;
    }

    public ArrayList<Item> getItemsList() {
        return itemsList;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
