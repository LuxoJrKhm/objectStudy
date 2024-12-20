package chip;

import storage.Storage;

// Abstract Chip class
public abstract class Chip {
    private String type;
    private double price;
    private int operationCount;
    private static final int MAX_OPERATIONS = 8;
    private static final int COOLDOWN_OPERATIONS = 2;

    public Chip(String type, double price) {
        this.type = type;
        this.price = price;
        this.operationCount = 0;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public boolean canPerformOperation() {
        return operationCount < MAX_OPERATIONS + COOLDOWN_OPERATIONS;
    }

    public boolean isInCooldown() {
        return operationCount >= MAX_OPERATIONS;
    }

    public void incrementOperationCount() {
        if (canPerformOperation()) {
            operationCount++;
        }
    }

    public void resetOperationCount() {
        operationCount = 0;
    }

    public void recordOperation(Storage storage, String operation) {
        int capacity = Integer.parseInt(storage.getCapacity().replaceAll("[^0-9]", ""));
        if (storage.getOperationHistory().size() < capacity) {
            storage.addOperationRecord(operation);
        } else {
            System.out.println("Storage capacity reached. Cannot record more operations.");
        }
    }

    public abstract void performOperation(String operation, double a, double b, Storage storage);

    @Override
    public String toString() {
        return type;
    }
}
