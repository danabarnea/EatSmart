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

public class ChoosePlanActivity extends AppCompatActivity {

    private Set<String> selectedPlans = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plan);

        // הגדרת כפתורים
        setupPlanButton(findViewById(R.id.btnGlutenFree), "Gluten Free", "#D1C4E9", "#9575CD");
        setupPlanButton(findViewById(R.id.btnNoSugar), "No Sugar", "#F8BBD0", "#F06292");
        setupPlanButton(findViewById(R.id.btnLowCarb), "Low Carb", "#FFE0B2", "#FFB74D");
        setupPlanButton(findViewById(R.id.btnVegan), "Vegan", "#C8E6C9", "#81C784");
        setupPlanButton(findViewById(R.id.btnBalanced), "Balanced", "#F48FB1", "#AD1457");

        Button btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {
            if (selectedPlans.isEmpty()) {
                Toast.makeText(this, "Please select at least one plan", Toast.LENGTH_SHORT).show();
            } else {
                // הפיכת ה-Set למחרוזת נקייה ללא סוגריים: "Vegan, Low Carb"
                String plansString = selectedPlans.toString()
                        .replace("[", "")
                        .replace("]", "");

                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // אנחנו שומרים תחת שני המפתחות כדי שכל הפרגמנטים יזהו
                editor.putString("chosen_plan", plansString);
                editor.putString("chosen_plans", plansString);
                editor.apply();

                Intent intent = new Intent(ChoosePlanActivity.this, HomeActivity.class);
                intent.putExtra("OPEN_FRAGMENT", "recipes");
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupPlanButton(Button btn, String planName, String normalColor, String selectedColor) {
        btn.setOnClickListener(v -> {
            if (selectedPlans.contains(planName)) {
                selectedPlans.remove(planName);
                ViewCompat.setBackgroundTintList(btn, ColorStateList.valueOf(Color.parseColor(normalColor)));
                btn.setTextColor(Color.BLACK);
            } else {
                selectedPlans.add(planName);
                ViewCompat.setBackgroundTintList(btn, ColorStateList.valueOf(Color.parseColor(selectedColor)));
                btn.setTextColor(Color.WHITE);
            }
        });
    }
}