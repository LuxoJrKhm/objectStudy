package display;

// Display class
public class Display {
    private String type;
    private double price;

    public Display(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return type;
    }
}
