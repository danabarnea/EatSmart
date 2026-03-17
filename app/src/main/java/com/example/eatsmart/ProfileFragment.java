package com.example.eatsmart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore; // הוספנו את זה

public class ProfileFragment extends Fragment {

    private FirebaseFirestore db;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvName = view.findViewById(R.id.tvProfileName);
        TextView tvPlans = view.findViewById(R.id.tvProfilePlans);
        TextView tvWeight = view.findViewById(R.id.tvProfileWeight);
        TextView tvHeight = view.findViewById(R.id.tvProfileHeight);

        // 1. זיהוי המשתמש
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user != null) {
            userId = user.getUid(); // ה-ID הייחודי של המשתמש ב-Firebase

            // הצגת שם המשתמש מהאימייל
            String name = user.getEmail().split("@")[0];
            tvName.setText(name);

            // 2. שליפת נתונים מ-Firestore (משקל וגובה)
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String weight = documentSnapshot.getString("weight");
                            String height = documentSnapshot.getString("height");

                            if (weight != null) tvWeight.setText(weight + " kg");
                            if (height != null) tvHeight.setText(height + " cm");
                        }
                    });
        }

        // 3. שליפת התוכנית מהזיכרון המקומי (SharedPreferences)
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        // וודאי שהמפתח כאן הוא "chosen_plan" כפי שהגדרת במסכים הקודמים
        String plan = sharedPrefs.getString("chosen_plan", "No plan selected");
        tvPlans.setText(plan);

        return view;
    }
}