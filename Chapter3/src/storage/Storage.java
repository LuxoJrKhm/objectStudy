package storage;

import java.util.ArrayList;

// storage.Storage class
public class Storage {
    private String capacity;
    private double price;
    private ArrayList<String> operationHistory;

    public Storage(String capacity, double price) {
        this.capacity = capacity;
        this.price = price;
        this.operationHistory = new ArrayList<>();
    }

    public String getCapacity() {
        return capacity;
    }

    public void addOperationRecord(String record) {
        operationHistory.add(record);
    }

    public ArrayList<String> getOperationHistory() {
        return new ArrayList<>(operationHistory);
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return capacity;
    }
}

