package com.example.eatsmart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * מחלקה זו מנהלת את מסך מחשבון הקלוריות.
 * המטרה: לקבל קלט מהמשתמש, לעדכן את סך הקלוריות היומי ולהציג אותו.
 */
public class CalculatorFragment extends Fragment {

    // משתנה שיחזיק את ה-ViewModel (מחסן הנתונים של הפרגמנט)
    private CalculatorViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // קישור הקוד לקובץ העיצוב (XML) של הפרגמנט
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        /*
         * יצירת חיבור ל-ViewModel.
         * השתמשתי ב-requireActivity() כדי שהנתונים יישמרו גם אם עוברים בין טאבים בתוך ה-HomeActivity.
         */
        viewModel = new ViewModelProvider(requireActivity()).get(CalculatorViewModel.class);

        // מציאת הרכיבים הגרפיים לפי ה-ID שלהם בקובץ ה-XML
        EditText etCalories = view.findViewById(R.id.etCaloriesAmount);
        Button btnCalc = view.findViewById(R.id.btnCalc);
        TextView tvResult = view.findViewById(R.id.tvTotalResult);

        /*
         * הגדרת "צופה" (Observer).
         * ברגע שהערך של הקלוריות ב-ViewModel משתנה, השורה הזו מזהה את השינוי
         * ומעדכנת אוטומטית את הטקסט שמופיע למשתמש על המסך.
         */
        viewModel.getTotalCalories().observe(getViewLifecycleOwner(), total -> {
            tvResult.setText("Total today: " + total + " kcal");
        });

        // הגדרת פעולה שקורה ברגע לחיצה על כפתור החישוב
        btnCalc.setOnClickListener(v -> {
            String input = etCalories.getText().toString();

            // בדיקה שהמשתמש אכן הקליד משהו כדי למנוע קריסה של האפליקציה
            if (!input.isEmpty()) {
                // הפיכת הטקסט שהוקלד למספר שלם
                int amount = Integer.parseInt(input);

                // שליחת המספר ל-ViewModel כדי שיבצע את פעולת החיבור בזיכרון
                viewModel.addCalories(amount);

                // ניקוי תיבת הטקסט לאחר הלחיצה כדי להכין אותה לקלט הבא
                etCalories.setText("");
            }
        });

        return view;
    }
}