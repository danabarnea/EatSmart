package com.example.eatsmart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

/**
 * מחלקה זו אחראית על חיבור הנתונים (רשימת המתכונים) לתצוגה הגרפית (ה-RecyclerView).
 * היא "מייצרת" כל שורה ברשימה וממלאת אותה בתוכן המתאים.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private OnRecipeClickListener listener;

    /**
     * ממשק (Interface) המאפשר להעביר אירועי לחיצה מהאדפטר בחזרה לפרגמנט.
     * זה מאפשר הפרדה בין התצוגה לבין הלוגיקה של האפליקציה.
     */
    public interface OnRecipeClickListener {
        void onSpeakClick(Recipe recipe); // לחיצה להקראת שם המתכון
        void onRecipeClick(Recipe recipe); // לחיצה למעבר למסך פירוט המתכון
    }

    public RecipeAdapter(List<Recipe> recipeList, OnRecipeClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    /**
     * יצירת ה-ViewHolder - זהו השלב שבו האדפטר מנפח (inflate) את עיצוב השורה הבודדת (item_recipe).
     */
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    /**
     * חיבור הנתונים לשורה ספציפית ברשימה לפי המיקום שלה (position).
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        // הצגת כותרת המתכון בתיבת הטקסט
        holder.titleTextView.setText(recipe.getTitle());

        /*
         * שימוש בספריית Glide לטעינת תמונה מכתובת URL באינטרנט.
         * הספרייה מטפלת בזיכרון, בהורדה ובהצגה של התמונה בצורה יעילה.
         */
        Glide.with(holder.itemView.getContext())
                .load(recipe.getImage())
                .placeholder(android.R.drawable.ic_menu_gallery) // תמונה זמנית עד שההורדה מסתיימת
                .into(holder.recipeImageView);

        // הגדרת מאזין ללחיצה על אייקון הרמקול (הקראה)
        holder.btnSpeak.setOnClickListener(v -> listener.onSpeakClick(recipe));

        // הגדרת מאזין ללחיצה על השורה כולה (מעבר למסך פירוט)
        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
    }

    /**
     * החזרת כמות הפריטים ברשימה.
     */
    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    /**
     * מחלקה פנימית שמחזיקה את הרכיבים הגרפיים של שורה אחת.
     * זה מונע מהמערכת לחפש את ה-ID בכל פעם מחדש ומשפר ביצועים.
     */
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImageView, btnSpeak;
        TextView titleTextView, infoTextView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.ivRecipeImage);
            titleTextView = itemView.findViewById(R.id.tvRecipeTitle);
            infoTextView = itemView.findViewById(R.id.tvRecipeInfo);
            btnSpeak = itemView.findViewById(R.id.btnSpeakIcon);
        }
    }
}