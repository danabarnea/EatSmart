package com.example.eatsmart;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private static final String API_KEY = "d2f08716e363455face126efc89ff328";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // אתחול ה-RecyclerView לפי ה-ID שנתנו ב-XML
        recyclerView = findViewById(R.id.recipesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // קבלת סוג הדיאטה מהמסך הקודם (Home או ChoosePlan)
        String selectedDiet = getIntent().getStringExtra("SELECTED_DIET");
        if (selectedDiet == null) {
            selectedDiet = "balanced";
        }

        fetchRecipes(selectedDiet);
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
                if (response.isSuccessful() && response.body() != null) {
                    List<Recipe> recipes = response.body().getResults();

                    if (recipes != null && !recipes.isEmpty()) {
                        // חיבור הנתונים ל-Adapter והצגתם במסך
                        adapter = new RecipeAdapter(RecipesActivity.this, recipes);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(RecipesActivity.this, "לא נמצאו מתכונים", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_ERROR", "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(RecipesActivity.this, "שגיאה בחיבור לאינטרנט", Toast.LENGTH_SHORT).show();
            }
        });
    }
}