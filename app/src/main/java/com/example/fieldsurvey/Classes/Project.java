package com.example.fieldsurvey.Classes;


public class Project {
    String projectName;
    String userId;



    public Project(){
    }

    public Project(String projectName, String userId) {
        this.projectName = projectName;
        this.userId=userId;
    }

    public Project(String projectName) {
        this.projectName = projectName;

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
