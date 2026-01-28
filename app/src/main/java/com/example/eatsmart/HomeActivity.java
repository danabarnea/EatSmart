package com.example.eatsmart;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // אתחול כפתורי הניווט
        ImageButton btnNavHome = findViewById(R.id.btnNavHome);
//        ImageButton btnNavRecipes = findViewById(R.id.btnNavRecipes);
        ImageButton btnNavShopping = findViewById(R.id.btnNavShopping);
        ImageButton btnNavLocation = findViewById(R.id.btnNavLocation);
        ImageButton btnNavProfile = findViewById(R.id.btnNavProfile);

        // הגדרת פעולות ללחיצה (כרגע מציג הודעות טוסט עד שתיצרי את המסכים הבאים)
        btnNavHome.setOnClickListener(v ->
                Toast.makeText(this, "You are already Home", Toast.LENGTH_SHORT).show());

//        btnNavRecipes.setOnClickListener(v ->
//                Toast.makeText(this, "Opening Recipes...", Toast.LENGTH_SHORT).show());

        btnNavShopping.setOnClickListener(v ->
                Toast.makeText(this, "Opening Shopping List...", Toast.LENGTH_SHORT).show());

        btnNavLocation.setOnClickListener(v ->
                Toast.makeText(this, "Opening Store Locator...", Toast.LENGTH_SHORT).show());

        btnNavProfile.setOnClickListener(v ->
                Toast.makeText(this, "Opening Your Profile...", Toast.LENGTH_SHORT).show());
    }
}