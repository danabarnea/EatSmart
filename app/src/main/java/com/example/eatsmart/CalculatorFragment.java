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

public class CalculatorFragment extends Fragment {

    private CalculatorViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        // שימוש ב-requireActivity() מבטיח שהזיכרון יישמר כל עוד ה-HomeActivity פועל
        viewModel = new ViewModelProvider(requireActivity()).get(CalculatorViewModel.class);

        EditText etCalories = view.findViewById(R.id.etCaloriesAmount);
        Button btnCalc = view.findViewById(R.id.btnCalc);
        TextView tvResult = view.findViewById(R.id.tvTotalResult);

        // צפייה בשינויים (Observer) - מעדכן את המסך אוטומטית כשהערך בזיכרון משתנה
        viewModel.getTotalCalories().observe(getViewLifecycleOwner(), total -> {
            tvResult.setText("Total today: " + total + " kcal");
        });

        btnCalc.setOnClickListener(v -> {
            String input = etCalories.getText().toString();
            if (!input.isEmpty()) {
                int amount = Integer.parseInt(input);
                viewModel.addCalories(amount);
                etCalories.setText("");
            }
        });

        return view;
    }
}