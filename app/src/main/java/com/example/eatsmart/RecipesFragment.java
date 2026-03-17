package com.example.eatsmart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private static final String API_KEY = "d2f08716e363455face126efc89ff328";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recipes, container, false);
        recyclerView = view.findViewById(R.id.recipesRecyclerView);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String selectedDiet = sharedPrefs.getString("chosen_plan", "balanced");

        fetchRecipes(selectedDiet);
        return view;
    }

    private void fetchRecipes(String diet) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeApiService service = retrofit.create(RecipeApiService.class);

        service.searchRecipes(API_KEY, diet, 15).enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    List<Recipe> recipes = response.body().getResults();

                    if (recipes != null && !recipes.isEmpty()) {
                        // חיבור האדפטר עם המאזין ללחיצות
                        adapter = new RecipeAdapter(recipes, new RecipeAdapter.OnRecipeClickListener() {
                            @Override
                            public void onSpeakClick(Recipe recipe) {
                                if (getActivity() instanceof HomeActivity) {
                                    ((HomeActivity) getActivity()).speakRecipeName(recipe.getTitle());
                                }
                            }

                            @Override
                            public void onRecipeClick(Recipe recipe) {
                                // מעבר למסך הפירוט
                                Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                                intent.putExtra("RECIPE_ID", recipe.getId());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.e("DEBUG_API", "Failure: " + t.getMessage());
            }
        });
    }
}