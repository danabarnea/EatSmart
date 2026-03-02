package com.example.eatsmart.data.model;

public class Ingredient {
    private int id;
    private String name;
    private String original; // זה השדה שאת רוצה להציג למשתמש
    private String image;

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getOriginal() { return original; }
    public String getImage() { return image; }
}
