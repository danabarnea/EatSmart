package com.example.eatsmart;

import java.util.List;

/**
 * מחלקה זו משמשת כ"מעטפת" (Wrapper) לתשובה שמגיעה מה-API.
 * מכיוון השרת של Spoonacular לא מחזיר רשימה 'נקייה'. הוא מחזיר אובייקט
 * JSON
 */
public class RecipeResponse {

    // השם 'results' חייב להיות תואם בדיוק לשדה ב-JSON שמחזיר ה-API
    // המשתנה מחזיק רשימה של אובייקטים מסוג Recipe
    private List<Recipe> results;

    // פונקציה לקבלת הרשימה
    public List<Recipe> getResults() {
        return results;
    }

    // פונקציה לעדכון הרשימה
    public void setResults(List<Recipe> results) {
        this.results = results;
    }
}