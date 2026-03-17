package com.example.eatsmart;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // אתחול הדיבור
        tts = new TextToSpeech(this, this);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        // --- עיצוב פסטל לאייקונים של אנדרואיד ---
        // ורוד כהה לטאב שנבחר, סגול עדין לטאבים האחרים
        int selectedColor = Color.parseColor("#AD1457");
        int unselectedColor = Color.parseColor("#9575CD");

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[]{selectedColor, unselectedColor}
        );

        bottomNav.setItemIconTintList(colorStateList);
        bottomNav.setItemTextColor(colorStateList);

        // --- ניהול המעבר בין הפרגמנטים ---
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new CalculatorFragment();
            } else if (itemId == R.id.nav_recipes) {
                selectedFragment = new RecipesFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // --- ברירת מחדל: טעינת עמוד המתכונים וסימון האייקון הנכון ---
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RecipesFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_recipes);
        }
    }

    public void speakRecipeName(String text) {
        if (tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US);
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}