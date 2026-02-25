package com.example.eatsmart;

import java.util.List;

public class RecipeResponse {
    // השם 'results' חייב להיות תואם בדיוק לשדה ב-JSON שמחזיר ה-API
    private List<Recipe> results;

    public List<Recipe> getResults() {
        return results;
    }

    public void setResults(List<Recipe> results) {
        this.results = results;
    }
}