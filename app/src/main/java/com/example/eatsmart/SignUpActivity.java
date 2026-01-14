package com.example.eatsmart; // להתאים לשם החבילה

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
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private EditText etEmail, etPhone, etWeight, etHeight, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        btnSignUp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String weight = etWeight.getText().toString().trim();
            String height = etHeight.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (!isValidSignUp(email, phone, weight, height, password, confirmPassword)) {
                return;
            }
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task ->
                    {
                        if(task.isSuccessful()) {
                            Toast.makeText(this, "Login success (בינתיים דמו)", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e("Dana", task.getException().getMessage());
                            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

            // כאן בעתיד תשמרי משתמש ב-DB / Firebase וכו'
            Toast.makeText(this, "Sign Up success (דמו)", Toast.LENGTH_SHORT).show();

            // אחרי הרשמה אפשר לעבור למסך בחירת תוכנית או למסך הבית
            // Intent intent = new Intent(this, ChoosePlanActivity.class);
            // startActivity(intent);
        });

        tvGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean isValidSignUp(String email, String phone, String weight,
                                  String height, String password, String confirmPassword) {

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email");
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone is required");
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

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
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
