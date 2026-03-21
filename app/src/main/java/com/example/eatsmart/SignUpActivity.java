package com.example.eatsmart;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore; // חובה להוסיף

import java.util.HashMap;
import java.util.Map;

/**
 * מסך ההרשמה של האפליקציה.
 * המטרה: ליצור חשבון משתמש חדש ב-Firebase ולשמור את הנתונים הפיזיים שלו בענן.
 */
public class SignUpActivity extends AppCompatActivity {

    // אתחול כלי העבודה של Firebase לאימות ושמירת נתונים
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // אתחול Firestore

    private EditText etEmail, etWeight, etHeight, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // קישור הרכיבים מה-XML למשתנים בקוד
        etEmail = findViewById(R.id.etEmail);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        // הגדרת לחיצה על כפתור ההרשמה
        btnSignUp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String weight = etWeight.getText().toString().trim();
            String height = etHeight.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // בדיקת תקינות הקלט לפני שפונים לשרת
            if (!isValidSignUp(email, weight, height, password, confirmPassword)) {
                return;
            }

            /*
             * יצירת משתמש חדש ב-Firebase Auth בעזרת אימייל וסיסמה.
             */
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // 1. אם יצירת החשבון הצליחה, עוברים לשמירת הנתונים הנוספים ב-Firestore
                            saveUserData(weight, height);
                        } else {
                            // הצגת הודעת שגיאה במקרה של כישלון (למשל אימייל שכבר קיים)
                            Log.e("Dana", task.getException().getMessage());
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // מעבר למסך ההתחברות למשתמשים שכבר רשומים
        tvGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * פונקציה חדשה לשמירת הנתונים האישיים (גובה ומשקל) בתוך מסד הנתונים Firestore.
     */
    private void saveUserData(String weight, String height) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // יצירת אובייקט מסוג Map כדי לארגן את הנתונים לפני השמירה
            Map<String, Object> userData = new HashMap<>();
            userData.put("weight", weight);
            userData.put("height", height);

            /*
             * שמירה ב-Firestore תחת האוסף "users".
             * אנחנו משתמשים ב-UID של המשתמש כשם המסמך כדי שנוכל למצוא אותו בקלות.
             */
            db.collection("users").document(userId)
                    .set(userData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Registration Success!", Toast.LENGTH_SHORT).show();

                        // רק אחרי שהשמירה הצליחה, עוברים למסך בחירת תוכנית התזונה
                        Intent intent = new Intent(SignUpActivity.this, ChoosePlanActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Dana", "Error saving data: " + e.getMessage());
                        Toast.makeText(this, "Data save failed, but user created.", Toast.LENGTH_SHORT).show();

                        // בכל זאת עוברים למסך הבא כדי לא לתקוע את המשתמש
                        startActivity(new Intent(SignUpActivity.this, ChoosePlanActivity.class));
                        finish();
                    });
        }
    }

    /**
     * פונקציית עזר לבדיקת תקינות השדות.
     * בודקת פורמט אימייל, אורך סיסמה והתאמה בין סיסמאות.
     */
    private boolean isValidSignUp(String email, String weight, String height, String password, String confirmPassword) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            return false;
        }
        if (TextUtils.isEmpty(weight)) {
            etWeight.setError("Weight is required");
            return false;
        }
        if (TextUtils.isEmpty(height)) {
            etHeight.setError("Height is required");
            return false;
        }
        if (password.length() < 6) {
            etPassword.setError("At least 6 characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }
}