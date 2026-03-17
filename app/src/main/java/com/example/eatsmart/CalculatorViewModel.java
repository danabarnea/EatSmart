package com.example.eatsmart;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalculatorViewModel extends ViewModel {
    private MutableLiveData<Integer> totalCalories = new MutableLiveData<>(0);

    public MutableLiveData<Integer> getTotalCalories() {
        return totalCalories;
    }

    public void addCalories(int amount) {
        Integer current = totalCalories.getValue();
        if (current != null) {
            totalCalories.setValue(current + amount);
        }
    }
}