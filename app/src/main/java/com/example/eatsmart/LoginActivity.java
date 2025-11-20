package com.example.eatsmart; // להתאים לשם החבילה שלך

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmailLogin, etPasswordLogin;
    private Button btnLogin;
    private TextView tvGoToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // מחבר ל־XML

        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToSignUp = findViewById(R.id.tvGoToSignUp);

        // לחיצה על כפתור התחברות
        btnLogin.setOnClickListener(v -> {
            String email = etEmailLogin.getText().toString().trim();
            String password = etPasswordLogin.getText().toString().trim();

            if (!isValidLogin(email, password)) {
                return;
            }

            // כאן בעתיד תעשי בדיקה מול שרת / DB
            Toast.makeText(this, "Login success (בינתיים דמו)", Toast.LENGTH_SHORT).show();

            // מעבר למסך הבית (בינתיים אפשר ליצור Activity ריק שנקרא HomeActivity)
            // Intent intent = new Intent(this, HomeActivity.class);
            // startActivity(intent);
        });

        // מעבר למסך Sign Up
        tvGoToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private boolean isValidLogin(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            etEmailLogin.setError("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailLogin.setError("Invalid email");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPasswordLogin.setError("Password is required");
            return false;
        }
        if (password.length() < 6) {
            etPasswordLogin.setError("At least 6 characters");
            return false;
        }
        return true;
    }
}
