package com.example.eatsmart;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Locale;

/**
 * מסך הבית הראשי של האפליקציה.
 * המטרה: לנהל את הניווט בין הטאבים (Fragments) ולהפעיל את מנוע הדיבור.
 */
public class HomeActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    // משתנה עבור מנוע הדיבור (Text To Speech)
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // אתחול מנוע הדיבור - הפקודה הזו מכינה את הטלפון לדבר
        tts = new TextToSpeech(this, this);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        // --- הגדרת עיצוב פסטל לתפריט הניווט ---
        int selectedColor = Color.parseColor("#AD1457");
        int unselectedColor = Color.parseColor("#9575CD");

        /*
         * יצירת רשימת מצבים לצבעים:
         * צבע אחד למצב שנבחר (Checked) וצבע שני למצב שלא נבחר.
         */
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[]{selectedColor, unselectedColor}
        );

        // החלת הצבעים על האייקונים והטקסט של התפריט
        bottomNav.setItemIconTintList(colorStateList);
        bottomNav.setItemTextColor(colorStateList);

        // --- ניהול המעבר בין הפרגמנטים בלחיצה על התפריט ---
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            // זיהוי איזה כפתור נלחץ והחלפה לפרגמנט המתאים
            if (itemId == R.id.nav_home) {
                selectedFragment = new CalculatorFragment();
            } else if (itemId == R.id.nav_recipes) {
                selectedFragment = new RecipesFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            // פקודה שמבצעת את ההחלפה הפיזית של המסך בתוך ה-Container
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // --- ברירת מחדל: טעינת עמוד המתכונים כשהאפליקציה נפתחת ---
        if (savedInstanceState == null) { // בדיקה האם האקטיביטי נוצרה עכשיו בפעם הראשונה (ולא עקב סיבוב מסך למשל)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RecipesFragment())
                    .commit();
            // עדכון ויזואלי של התפריט התחתון כך שהאייקון של המתכונים יראה כ"נבחר"
            bottomNav.setSelectedItemId(R.id.nav_recipes);
        }
    }

    /**
     * פונקציה שמאפשרת לפרגמנטים אחרים לבקש מהאפליקציה "להקריא" טקסט.
     */
    public void speakRecipeName(String text) {
        if (tts != null) {
            // הפקודה שמבצעת את ההקראה בפועל
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    /**
     * פונקציית אתחול של מנוע הדיבור.
     * כאן אנחנו מגדירים שהשפה תהיה אנגלית.
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US);
        }
    }

    /**
     * שחרור זיכרון כשהמסך נסגר.
     * חשוב מאוד כדי שמנוע הדיבור לא ימשיך לעבוד ברקע ויבזבז סוללה.
     */
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}