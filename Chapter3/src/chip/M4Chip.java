package chip;

import storage.Storage;

// Specific Chips
public class M4Chip extends Chip {
    public M4Chip() {
        super("M4", 200);
    }

    @Override
    public void performOperation(String operation, double a, double b, Storage storage) {
        if (isInCooldown()) {
            System.out.println("M4 is throttled. Cannot perform operation.");
            return;
        }
        incrementOperationCount();
        if (operation.equalsIgnoreCase("addition")) {
            double result = a + b;
            System.out.printf("M4 performing addition: %.2f + %.2f = %.2f%n", a, b, result);
            recordOperation(storage, String.format("Addition: %.2f + %.2f = %.2f", a, b, result));
        } else {
            System.out.println("M4 only supports addition.");
        }
    }
}