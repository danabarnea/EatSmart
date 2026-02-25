package com.example.eatsmart;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApiService {
    @GET("recipes/complexSearch")
    Call<RecipeResponse> searchRecipes(
            @Query("apiKey") String apiKey,
            @Query("diet") String diet,
            @Query("number") int number
    );
}