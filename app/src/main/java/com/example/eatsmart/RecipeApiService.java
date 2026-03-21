package com.example.eatsmart;

import com.example.eatsmart.data.model.RecipeDetailResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * ממשק (Interface) המגדיר את הקריאות לשרת המתכונים (API).
 * אנחנו משתמשים בספריית Retrofit כדי להפוך את הקישורים מהאינטרנט לפונקציות בקוד.
 */
public interface RecipeApiService {

    /*
     * פונקציה לחיפוש מתכונים לפי דיאטה ספציפית.
     * ה-Annotation מסוג @GET מציין את הנתיב בשרת שאליו פונים.
     */
    @GET("recipes/complexSearch")
    Call<RecipeResponse> searchRecipes(
            // ה-API Key הוא "המפתח" האישי שלנו שמאפשר גישה לשרת
            @Query("apiKey") String apiKey,

            // כאן אנחנו שולחים את סוג הדיאטה (למשל: Vegan או Gluten Free)
            @Query("diet") String diet,

            // מספר המתכונים שאנחנו רוצים לקבל בכל פעם
            @Query("number") int number
    );

    /*
     * פונקציה לשליפת מידע מפורט על מתכון ספציפי לפי ה-ID שלו.
     * ה-@Path משמש להזרקת ה-ID ישירות לתוך הקישור (URL).
     */
    @GET("recipes/{id}/information")
    Call<RecipeDetailResponse> getRecipeInfo(
            // המזהה הייחודי של המתכון בשרת
            @Path("id") Integer recipeId,

            // מפתח הגישה ל-API
            @Query("apiKey") String apiKey
    );
}