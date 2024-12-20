package memory;

// Abstract Memory class
public abstract class Memory {
    private String size;
    private double price;

    public Memory(String size, double price) {
        this.size = size;
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return size;
    }
}

