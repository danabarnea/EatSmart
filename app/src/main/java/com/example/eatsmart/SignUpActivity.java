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

public class SignUpActivity extends AppCompatActivity {
    // אתחול Firebase Auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private EditText etEmail, etWeight, etHeight, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // קישור רכיבי ה-UI
        etEmail = findViewById(R.id.etEmail);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        btnSignUp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String weight = etWeight.getText().toString().trim();
            String height = etHeight.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // בדיקת תקינות הקלט
            if (!isValidSignUp(email, weight, height, password, confirmPassword)) {
                return;
            }

            // יצירת המשתמש ב-Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // המעבר למסך בחירת תוכנית מתבצע כאן [cite: 12]
                            Toast.makeText(this, "Registration Success!", Toast.LENGTH_SHORT).show();

                            // יצירת Intent למעבר למסך ChoosePlanActivity
                            Intent intent = new Intent(SignUpActivity.this, ChoosePlanActivity.class);
                            startActivity(intent);

                            // סגירת מסך ההרשמה כדי שלא יהיה ניתן לחזור אליו בלחיצה על 'אחורה'
                            finish();
                        } else {
                            // הודעת שגיאה במקרה של כישלון [cite: 8, 12]
                            Log.e("Dana", task.getException().getMessage());
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // מעבר למסך התחברות
        tvGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean isValidSignUp(String email, String weight, String height, String password, String confirmPassword) {
        // אימייל חייב להיות בפורמט תקין [cite: 10]
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

        // הסיסמה חייבת להכיל לפחות 6 תווים [cite: 10]
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