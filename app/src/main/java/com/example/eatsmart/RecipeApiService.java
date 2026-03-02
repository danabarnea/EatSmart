package com.example.eatsmart;

import com.example.eatsmart.data.model.RecipeDetailResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeApiService {
    @GET("recipes/complexSearch")
    Call<RecipeResponse> searchRecipes(
            @Query("apiKey") String apiKey,
            @Query("diet") String diet,
            @Query("number") int number
    );

    @GET("recipes/{id}/information")
    Call<RecipeDetailResponse> getRecipeInfo(
            @Path("id") Integer recipeId,
            @Query("apiKey") String apiKey

    );
}