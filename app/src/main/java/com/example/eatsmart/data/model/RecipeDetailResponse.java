package com.example.eatsmart.data.model;

import java.util.List;

public class RecipeDetailResponse {
    private int id;
    private String title;
    private String image;
    private int readyInMinutes;
    private String instructions; // הוראות ההכנה כטקסט
    private String summary;      // סיכום קצר (מכיל HTML)
    private List<Ingredient> extendedIngredients; // רשימת המצרכים

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getImage() { return image; }
    public int getReadyInMinutes() { return readyInMinutes; }
    public String getInstructions() { return instructions; }
    public String getSummary() { return summary; }
    public List<Ingredient> getExtendedIngredients() { return extendedIngredients; }

    // Setters (אופציונלי, תלוי אם את משתמשת ב-GSON)
}
