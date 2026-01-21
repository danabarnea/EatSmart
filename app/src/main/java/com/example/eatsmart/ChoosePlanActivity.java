package com.example.eatsmart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChoosePlanActivity extends AppCompatActivity {

    private String selectedPlan = ""; // משתנה לשמירת הבחירה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plan);

        // מציאת הכפתורים
        Button btnGlutenFree = findViewById(R.id.btnGlutenFree);
        Button btnNoSugar = findViewById(R.id.btnNoSugar);
        Button btnLowCarb = findViewById(R.id.btnLowCarb);
        Button btnVegan = findViewById(R.id.btnVegan);
        Button btnBalanced = findViewById(R.id.btnBalanced);
        Button btnContinue = findViewById(R.id.btnContinue);

        // הגדרת פעולה לכל כפתור בחירה
        btnGlutenFree.setOnClickListener(v -> selectPlan("Gluten Free"));
        btnNoSugar.setOnClickListener(v -> selectPlan("No Sugar"));
        btnLowCarb.setOnClickListener(v -> selectPlan("Low Carb"));
        btnVegan.setOnClickListener(v -> selectPlan("Vegan"));
        btnBalanced.setOnClickListener(v -> selectPlan("Balanced"));

        // כפתור המשך למסך הבית
        btnContinue.setOnClickListener(v -> {
            if (selectedPlan.isEmpty()) {
                // אם לא נבחר דבר - מוצגת אזהרה [cite: 23]
                Toast.makeText(this, "Please select a plan first", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Selected: " + selectedPlan, Toast.LENGTH_SHORT).show();
                // מעבר למסך הבית [cite: 22]
                Intent intent = new Intent(this, HomeActivity.class); // ודאי שיצרת HomeActivity
                startActivity(intent);
            }
        });
    }

    private void selectPlan(String planName) {
        selectedPlan = planName;
        Toast.makeText(this, "You chose: " + planName, Toast.LENGTH_SHORT).show();
        // כאן אפשר להוסיף ויזואליות שהכפתור נבחר (למשל שינוי צבע)
    }
}