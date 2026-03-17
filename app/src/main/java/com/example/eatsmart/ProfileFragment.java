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
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvName = view.findViewById(R.id.tvProfileName);
        TextView tvPlans = view.findViewById(R.id.tvProfilePlans);
        TextView tvWeight = view.findViewById(R.id.tvProfileWeight);
        TextView tvHeight = view.findViewById(R.id.tvProfileHeight);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user != null) {
            String name = user.getEmail().split("@")[0];
            tvName.setText(name);

            // שליפת גובה ומשקל מ-Firebase
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            tvWeight.setText(documentSnapshot.getString("weight") + " kg");
                            tvHeight.setText(documentSnapshot.getString("height") + " cm");
                        }
                    });
        }

        // שליפת התוכנית מה-SharedPreferences
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        // השתמשנו במפתח chosen_plan שעדכנו ב-Activity הקודם
        String plans = sharedPrefs.getString("chosen_plan", "No plans selected");
        tvPlans.setText(plans);

        return view;
    }
}