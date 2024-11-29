package com.example.smartwastesegregator;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class WasteData {
    private final IntegerProperty id;
    private final IntegerProperty amount;

    // Constructor that accepts both id and amount
    public WasteData(int id, int amount) {
        this.id = new SimpleIntegerProperty(id);
        this.amount = new SimpleIntegerProperty(amount);
    }

    // Getters and Setters for id and amount
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getAmount() {
        return amount.get();
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }
}
