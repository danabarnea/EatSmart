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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * פרגמנט הפרופיל של המשתמש.
 * המטרה: להציג את נתוני המשתמש שנשאבו מהענן (Firestore) ומהזיכרון המקומי (SharedPreferences).
 */
public class ProfileFragment extends Fragment {

    // משתנה לגישה למסד הנתונים בענן (Firestore)
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // חיבור הקוד לקובץ העיצוב XML של הפרופיל
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // קישור רכיבי הטקסט מה-XML למשתנים בקוד
        TextView tvName = view.findViewById(R.id.tvProfileName);
        TextView tvPlans = view.findViewById(R.id.tvProfilePlans);
        TextView tvWeight = view.findViewById(R.id.tvProfileWeight);
        TextView tvHeight = view.findViewById(R.id.tvProfileHeight);

        // קבלת המשתמש המחובר כרגע מ-Firebase Auth
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // אתחול הגישה ל-Firestore
        db = FirebaseFirestore.getInstance();

        if (user != null) {
            /*
             * חיתוך האימייל לפני ה-@ כדי להשתמש בו כשם משתמש זמני.
             * למשל: dana@gmail.com יהפוך ל-"dana".
             */
            String name = user.getEmail().split("@")[0];
            tvName.setText(name);

            /*
             * שליפת נתוני גובה ומשקל מתוך ה-Firestore.
             * אנחנו ניגשים לאוסף "users" ולמסמך הספציפי של המשתמש לפי ה-UID הייחודי שלו.
             */
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        // בדיקה אם המסמך קיים בענן לפני שמנסים לקרוא ממנו
                        if (documentSnapshot.exists()) {
                            // הצגת המשקל והגובה בצירוף היחידות (kg/cm)
                            tvWeight.setText(documentSnapshot.getString("weight") + " kg");
                            tvHeight.setText(documentSnapshot.getString("height") + " cm");
                        }
                    });
        }

        /*
         * שליפת תוכנית התזונה שנשמרה ב-SharedPreferences (זיכרון מקומי).
         * השתמשנו במפתח "chosen_plan" שהגדרנו במסך הבחירה.
         */
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // אם לא נמצאה תוכנית, יוצג טקסט ברירת מחדל
        String plans = sharedPrefs.getString("chosen_plan", "No plans selected");
        tvPlans.setText(plans);

        return view;
    }
}