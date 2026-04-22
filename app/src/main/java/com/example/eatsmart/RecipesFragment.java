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

/**
 * פרגמנט המציג את רשימת המתכונים.
 * המטרה: לשלוף מתכונים מה-API בהתאם לדיאטה שנבחרה ולהציג אותם ברשימה נגללת.
 */
public class RecipesFragment extends Fragment {
    // הצהרה על רכיב הרשימה והמתאם (Adapter) שמקשר בין הנתונים לעיצוב
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    // מפתח אישי לגישה ל-API של המתכונים (חובה לזיהוי מול השרת)
    private static final String API_KEY = "d2f08716e363455face126efc89ff328";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // ניפוח עיצוב הפרגמנט (XML)
        View view = inflater.inflate(R.layout.activity_recipes, container, false);
        recyclerView = view.findViewById(R.id.recipesRecyclerView);

        // הגדרת מנהל הפריסה (LayoutManager) - רשימה אנכית רגילה
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        /*
         * שליפת הדיאטה הנבחרת מה-SharedPreferences.
         * אם המשתמש לא בחר כלום, ברירת המחדל תהיה "balanced".
         */
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String selectedDiet = sharedPrefs.getString("chosen_plan", "balanced");

        // קריאה לפונקציה שמושכת את המתכונים מהשרת
        fetchRecipes(selectedDiet);
        return view;
    }

    /**
     * פונקציה המבצעת את הקריאה ל-API בעזרת Retrofit.
     */
    private void fetchRecipes(String diet) {
        Retrofit retrofit = new Retrofit.Builder() // Retrofit היא ספרייה שהופכת כתובת אינטרנט (API) לאובייקטים של Java.
                .baseUrl("https://api.spoonacular.com/") // הכתובת הראשית של השרת (Spoonacular).
                .addConverterFactory(GsonConverterFactory.create()) // הוא רכיב שמבצע המרה אוטומטית בין טקסט בפורמט
                .build();
// יצירת ממשק השירות לפי ההגדרות שכתבנו ב-RecipeApiService
        RecipeApiService service = retrofit.create(RecipeApiService.class);

        // שליחת הבקשה לחיפוש מתכונים (15 תוצאות)
        service.searchRecipes(API_KEY, diet, 15).enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                /*
                 * בדיקה שהפרגמנט עדיין "חי" (isAdded) ושהתשובה תקינה.
                 * זה מונע קריסות אם המשתמש יצא מהמסך לפני שהנתונים חזרו.
                 */
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    List<Recipe> recipes = response.body().getResults();

                    if (recipes != null && !recipes.isEmpty()) {
                        /*
                         * יצירת האדפטר וחיבור המאזין (Listener) לאירועים.
                         * כאן אנחנו מגדירים מה יקרה בלחיצה על המיקרופון או על השורה.
                         */
                        // אתחול האדפטר עם רשימת המתכונים שהגיעו מהשרת
                        adapter = new RecipeAdapter(recipes, new RecipeAdapter.OnRecipeClickListener() {
                            @Override
                            public void onSpeakClick(Recipe recipe) {
                                // קריאה למנוע הדיבור שנמצא ב-HomeActivity
                                if (getActivity() instanceof HomeActivity) {
                                    ((HomeActivity) getActivity()).speakRecipeName(recipe.getTitle());
                                }
                            }

                            @Override
                            public void onRecipeClick(Recipe recipe) {
                                // מעבר למסך פירוט המתכון עם ה-ID המתאים
                                Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                                intent.putExtra("RECIPE_ID", recipe.getId());
                                startActivity(intent);
                            }
                        });
                        // הצמדת האדפטר ל-RecyclerView
                        // עדיף כי הוא משתמש בתבנית עיצוב שנקראת "ViewHolder", שמאפשרת לו למחזר (Recycle) את רכיבי התצוגה של שורות שיצאו מהמסך עבור נתונים חדשים שנכנסים, ובכך הוא חוסך זיכרון ומונע "תקיעות" בזמן הגלילה.
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                // הדפסת שגיאה בלוג למקרה של תקלה בתקשורת
                Log.e("DEBUG_API", "Failure: " + t.getMessage());
            }
        });
    }
}