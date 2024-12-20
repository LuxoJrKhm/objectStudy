package chip;

import storage.Storage;

public class M4ProChip extends Chip {
    public M4ProChip() {
        super("M4Pro", 400);
    }

    @Override
    public void performOperation(String operation, double a, double b, Storage storage) {
        if (isInCooldown()) {
            System.out.println("M4Pro is throttled. Cannot perform operation.");
            return;
        }
        incrementOperationCount();
        double result;
        switch (operation.toLowerCase()) {
            case "addition":
                result = a + b;
                System.out.printf("M4Pro performing addition: %.2f + %.2f = %.2f%n", a, b, result);
                recordOperation(storage, String.format("Addition: %.2f + %.2f = %.2f", a, b, result));
                break;
            case "subtraction":
                result = a - b;
                System.out.printf("M4Pro performing subtraction: %.2f - %.2f = %.2f%n", a, b, result);
                recordOperation(storage, String.format("Subtraction: %.2f - %.2f = %.2f", a, b, result));
                break;
            case "multiplication":
                result = a * b;
                System.out.printf("M4Pro performing multiplication: %.2f * %.2f = %.2f%n", a, b, result);
                recordOperation(storage, String.format("Multiplication: %.2f * %.2f = %.2f", a, b, result));
                break;
            case "division":
                if (b != 0) {
                    result = a / b;
                    System.out.printf("M4Pro performing division: %.2f / %.2f = %.2f%n", a, b, result);
                    recordOperation(storage, String.format("Division: %.2f / %.2f = %.2f", a, b, result));
                } else {
                    System.out.println("Division by zero is not allowed.");
                }
                break;
            default:
                System.out.println("Operation not supported by M4Pro.");
        }
    }
}