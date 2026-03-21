package com.example.eatsmart;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * מחלקה זו אחראית על ניהול הנתונים של המחשבון.
 * ה-ViewModel שומר על הנתונים בזיכרון גם אם המסך מסתובב או משתנה.
 */
public class CalculatorViewModel extends ViewModel {

    // יצירת משתנה מסוג LiveData שיכול להשתנות (Mutable)
    // המשתנה הזה מחזיק את סכום הקלוריות ומתחיל ב-0
    private MutableLiveData<Integer> totalCalories = new MutableLiveData<>(0);

    // פונקציה שמאפשרת לפרגמנט "לצפות" בשינויים של הקלוריות
    public MutableLiveData<Integer> getTotalCalories() {
        return totalCalories;
    }

    /**
     * פונקציה להוספת קלוריות לסכום הקיים.
     * מקבלת מספר (amount) ומעדכנת את ה-LiveData.
     */
    public void addCalories(int amount) {
        // לקיחת הערך הנוכחי שיש בתוך ה-LiveData
        Integer current = totalCalories.getValue();

        // בדיקה שהערך לא ריק (null) כדי למנוע שגיאות
        if (current != null) {
            // עדכון ה-LiveData עם הסכום החדש
            // הפעולה הזו אוטומטית "תקפיץ" עדכון לכל מי שצופה במשתנה הזה
            totalCalories.setValue(current + amount);
        }
    }
}