package com.edgelordzeta.zetainventorymanager;

public class user {

    private  String userName;
    private  String password;
    //for later release, all users for the beta will be administrators
    private String accountType;


    public user(String[] users) {
    }

    public user(String text, String text2) {
        userName = text;
        password = text2;
    }

    public  String getUser() {
        return userName;
    }

    public void setText(String text) {
        userName = text;
    }
    public  String getPassword() {
        return password;
    }

}