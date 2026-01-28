package com.example.eatsmart;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmailLogin, etPasswordLogin;
    private Button btnLogin;
    private TextView tvGoToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // אתחול Firebase
        mAuth = FirebaseAuth.getInstance();

        // קישור רכיבי ה-XML
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToSignUp = findViewById(R.id.tvGoToSignUp);

        // לחיצה על כפתור התחברות
        btnLogin.setOnClickListener(v -> {
            String email = etEmailLogin.getText().toString().trim();
            String password = etPasswordLogin.getText().toString().trim();

            // בדיקת תקינות שדות
            if (!isValidLogin(email, password)) {
                return;
            }

            // ניסיון התחברות ב-Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // התחברות הצליחה!
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                            // מעבר למסך בחירת תוכנית
                            Intent intent = new Intent(LoginActivity.this, ChoosePlanActivity.class);
                            startActivity(intent);

                            // סגירת מסך הלוגין
                            finish();
                        } else {
                            // התחברות נכשלה
                            String error = task.getException() != null ? task.getException().getMessage() : "Authentication failed";
                            Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // מעבר למסך Sign Up
        tvGoToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    // הפונקציה שהייתה חסרה או לא סגורה אצלך
    private boolean isValidLogin(String email, String password) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailLogin.setError("Please enter a valid email");
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPasswordLogin.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }
}