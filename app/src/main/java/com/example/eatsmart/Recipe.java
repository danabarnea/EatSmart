package com.example.eatsmart;

public class Recipe {
    private int id;
    private String title;
    private String image;
    private int readyInMinutes;

    // Constructor מעודכן
    public Recipe(int id, String title, String image, int readyInMinutes) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.readyInMinutes = readyInMinutes;
    }

    // ה-Getter שחסר לך וגרם לשגיאה האדומה:
    public int getId() {
        return id;
    }

    public String getTitle() { return title; }
    public String getImage() { return image; }
    public int getReadyInMinutes() { return readyInMinutes; }
}