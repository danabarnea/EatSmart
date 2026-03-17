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

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onSpeakClick(Recipe recipe);
        void onRecipeClick(Recipe recipe); // פונקציה חדשה למעבר מסך
    }

    public RecipeAdapter(List<Recipe> recipeList, OnRecipeClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.titleTextView.setText(recipe.getTitle());

        Glide.with(holder.itemView.getContext())
                .load(recipe.getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.recipeImageView);

        // לחיצה על המיקרופון
        holder.btnSpeak.setOnClickListener(v -> listener.onSpeakClick(recipe));

        // לחיצה על כל השורה למעבר למסך פירוט
        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

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