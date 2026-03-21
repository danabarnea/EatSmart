package com.example.eatsmart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * מסך ההתחברות של האפליקציה.
 * המטרה: לאמת את פרטי המשתמש מול מסד הנתונים של Firebase ולאפשר כניסה.
 */
public class LoginActivity extends AppCompatActivity {

    // הגדרת משתנים לרכיבי המסך (תיבות טקסט וכפתורים)
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvGoToSignUp;

    // משתנה לניהול האימות מול Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // אתחול האובייקט של Firebase Auth - יוצר קשר עם שירות הענן
        mAuth = FirebaseAuth.getInstance();

        // קישור המשתנים בקוד לרכיבים הגרפיים בקובץ ה-XML לפי ה-ID שלהם
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToSignUp = findViewById(R.id.tvGoToSignUp);

        // הגדרת פעולה בלחיצה על כפתור ההתחברות
        btnLogin.setOnClickListener(v -> {
            // לקיחת הטקסט שהמשתמש הזין וניקוי רווחים מיותרים (trim)
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // בדיקת תקינות בסיסית: מוודאים שהשדות לא ריקים לפני שפונים לענן
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            /*
             * שליחת פרטי המשתמש (אימייל וסיסמה) ל-Firebase לבדיקה.
             * הפעולה מתבצעת ברקע כדי לא לתקוע את האפליקציה.
             */
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        // אם האימות הצליח - עוברים למסך הבית
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish(); // סגירת מסך הלוגין כדי שלא יהיה אפשר לחזור אליו ב"אחורה"
                        } else {
                            // אם האימות נכשל - מציגים למשתמש הודעת שגיאה ברורה מהשרת
                            Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // מעבר למסך ההרשמה (SignUp) במידה ולמשתמש אין עדיין חשבון
        tvGoToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }
}