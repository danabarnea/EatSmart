package com.example.eatsmart;

// מחלקה זו מייצגת מתכון בודד כפי שהוא מגיע מה-API
public class Recipe {
    private int id;
    private String title;
    private String image;

    // Getters - חשוב שהשמות יהיו מדויקים עבור ה-Adapter
    public String getTitle() { return title; }
    public String getImage() { return image; }
    public int getId() { return id; }
}