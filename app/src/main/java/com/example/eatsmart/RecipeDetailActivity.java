package com.example.eatsmart;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull; // הוספתי לייבוא
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.eatsmart.data.model.Ingredient;
import com.example.eatsmart.data.model.RecipeDetailResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeDetailActivity extends AppCompatActivity {

    private ImageView recipeImage;
    private TextView titleText, timeText, ingredientsText, instructionsText;
    private static final String API_KEY = "d2f08716e363455face126efc89ff328";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // הפעלת החץ בראש המסך - חייב להיות אחרי setContentView
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("פרטי המתכון");
        }

        recipeImage = findViewById(R.id.detailRecipeImage);
        titleText = findViewById(R.id.detailRecipeTitle);
        timeText = findViewById(R.id.detailReadyInMinutes);
        ingredientsText = findViewById(R.id.detailIngredients);
        instructionsText = findViewById(R.id.detailInstructions);

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId != -1) {
            fetchRecipeDetails(recipeId);
        }
    }

    // הפונקציה שגורמת לחץ לעבוד ולחזור אחורה
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void fetchRecipeDetails(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeApiService service = retrofit.create(RecipeApiService.class);

        service.getRecipeInfo(id, API_KEY).enqueue(new Callback<RecipeDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeDetailResponse> call, @NonNull Response<RecipeDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeDetailResponse> call, @NonNull Throwable t) {
                Toast.makeText(RecipeDetailActivity.this, "שגיאה בחיבור", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(RecipeDetailResponse detail) {
        titleText.setText(detail.getTitle());
        timeText.setText("זמן הכנה: " + detail.getReadyInMinutes() + " דקות");
        Glide.with(this).load(detail.getImage()).into(recipeImage);

        StringBuilder sb = new StringBuilder();
        if (detail.getExtendedIngredients() != null) {
            for (Ingredient ing : detail.getExtendedIngredients()) {
                sb.append("• ").append(ing.getOriginal()).append("\n");
            }
        }
        ingredientsText.setText(sb.toString());

        if (detail.getInstructions() != null) {
            instructionsText.setText(Html.fromHtml(detail.getInstructions(), Html.FROM_HTML_MODE_COMPACT));
        }
    }
}