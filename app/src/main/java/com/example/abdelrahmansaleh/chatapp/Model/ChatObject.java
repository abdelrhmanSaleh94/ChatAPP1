package com.example.abdelrahmansaleh.chatapp.Model;

import java.util.ArrayList;

public class ChatObject {
    private String massage;
    private Boolean CurrentUser;
    private ArrayList<String> ImageMassage;


    public ChatObject(String massage, Boolean currentUser,ArrayList<String> ImageMassage) {
        this.massage = massage;
        this.CurrentUser = currentUser;
        this.ImageMassage=ImageMassage;

    }

    public ChatObject(String massage) {
        this.massage = massage;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public Boolean getCurrentUser() {
        return CurrentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        CurrentUser = currentUser;
    }

    public ArrayList<String> getImageMassage() {
        return ImageMassage;
    }
}
