package com.example.eatsmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import java.util.HashSet;
import java.util.Set;

/**
 * מסך בחירת תוכנית תזונה.
 * המטרה: לאפשר למשתמש לבחור העדפות תזונה (ללא גלוטן, טבעוני וכו') ולשמור אותן במכשיר.
 */
public class ChoosePlanActivity extends AppCompatActivity {

    // שימוש ב-Set כדי לשמור רשימה של בחירות בלי כפילויות
    private Set<String> selectedPlans = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plan);

        /*
         * קריאה לפונקציית העזר setupPlanButton עבור כל כפתור.
         * לכל כפתור נשלח השם שלו, צבע רגיל וצבע למצב לחיצה.
         */
        setupPlanButton(findViewById(R.id.btnGlutenFree), "Gluten Free", "#D1C4E9", "#9575CD");
        setupPlanButton(findViewById(R.id.btnNoSugar), "No Sugar", "#F8BBD0", "#F06292");
        setupPlanButton(findViewById(R.id.btnLowCarb), "Low Carb", "#FFE0B2", "#FFB74D");
        setupPlanButton(findViewById(R.id.btnVegan), "Vegan", "#C8E6C9", "#81C784");
        setupPlanButton(findViewById(R.id.btnBalanced), "Balanced", "#F48FB1", "#AD1457");

        Button btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {
            // בדיקה שהמשתמש בחר לפחות תוכנית אחת לפני שממשיכים
            if (selectedPlans.isEmpty()) {
                Toast.makeText(this, "Please select at least one plan", Toast.LENGTH_SHORT).show();
            } else {
                /*
                 * הפיכת רשימת הבחירות (Set) למחרוזת טקסט אחת רגילה.
                 * אנחנו מנקים את הסוגריים המרובעים [ ] כדי שהטקסט יהיה נקי.
                 */
                String plansString = selectedPlans.toString()
                        .replace("[", "")
                        .replace("]", "");

                /*
                 * שימוש ב-SharedPreferences לשמירה קבועה על זיכרון המכשיר.
                 * המידע יישמר גם אם נסגור את האפליקציה ונפתח אותה שוב.
                 */
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // שמירת הבחירות תחת מפתחות (Keys) כדי שנוכל לשלוף אותן במסכים אחרים
                editor.putString("chosen_plan", plansString);
                editor.putString("chosen_plans", plansString);
                editor.apply(); // ביצוע השמירה בפועל

                // מעבר למסך הבית ושליחת הוראה לפתוח את טאב המתכונים
                Intent intent = new Intent(ChoosePlanActivity.this, HomeActivity.class);
                intent.putExtra("OPEN_FRAGMENT", "recipes");
                startActivity(intent);
                finish(); // סגירת המסך הנוכחי כדי שלא יוכלו לחזור אליו ב"אחורה"
            }
        });
    }

    /**
     * פונקציה שמנהלת את הלוגיקה והעיצוב של כפתורי הבחירה.
     * הפונקציה בודקת אם התוכנית כבר נבחרה - אם כן היא מסירה אותה, ואם לא היא מוסיפה.
     */
    private void setupPlanButton(Button btn, String planName, String normalColor, String selectedColor) {
        btn.setOnClickListener(v -> {
            if (selectedPlans.contains(planName)) {
                // המשתמש ביטל בחירה קיימת - מחזירים לצבע רגיל
                selectedPlans.remove(planName);
                ViewCompat.setBackgroundTintList(btn, ColorStateList.valueOf(Color.parseColor(normalColor)));
                btn.setTextColor(Color.BLACK);
            } else {
                // המשתמש בחר בתוכנית - משנים לצבע המודגש (נבחר)
                selectedPlans.add(planName);
                ViewCompat.setBackgroundTintList(btn, ColorStateList.valueOf(Color.parseColor(selectedColor)));
                btn.setTextColor(Color.WHITE);
            }
        });
    }
}