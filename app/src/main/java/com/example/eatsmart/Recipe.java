package com.example.eatsmart;

/**
 * מחלקת המודל של מתכון.
 * המטרה: להגדיר את המבנה של אובייקט "מתכון" באפליקציה.
 * כל מתכון מכיל מזהה, כותרת, תמונה וזמן הכנה.
 */
public class Recipe {
    // משתנים פרטיים ששומרים את נתוני המתכון
    private int id;
    private String title;
    private String image;
    private int readyInMinutes;

    /*
     * הבנאי (Constructor).
     * תפקידו ליצור אובייקט חדש של מתכון עם הנתונים שקיבלנו מה-API.
     */
    public Recipe(int id, String title, String image, int readyInMinutes) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.readyInMinutes = readyInMinutes;
    }

    /*
     * פונקציות Getter.
     * מכיוון שהמשתנים הם private (כדי לשמור על בטיחות הנתונים),
     * אנחנו משתמשים בפונקציות האלו כדי לאפשר למחלקות אחרות (כמו ה-Adapter)
     * לקרוא את הערכים ולהציג אותם על המסך.
     */

    // החזרת ה-ID הייחודי של המתכון
    public int getId() {
        return id;
    }

    // החזרת שם המתכון
    public String getTitle() {
        return title;
    }

    // החזרת הקישור (URL) לתמונת המתכון
    public String getImage() {
        return image;
    }

    // החזרת זמן ההכנה בדקות
    public int getReadyInMinutes() {
        return readyInMinutes;
    }
}