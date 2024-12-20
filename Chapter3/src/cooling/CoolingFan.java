package cooling;

import chip.Chip;

// CoolingFan class
public class CoolingFan {
    private String type;
    private double price;

    public CoolingFan(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public void coolDown(Chip chip) {
        chip.resetOperationCount();
    }

    @Override
    public String toString() {
        return type;
    }
}
