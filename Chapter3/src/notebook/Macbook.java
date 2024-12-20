package notebook;

import chip.Chip;
import display.Display;
import memory.Memory;
import storage.Storage;

public abstract class Macbook {
    private Chip chip;
    private Memory memory;
    private Storage storage;
    private Display display;

    public Macbook(Chip chip, Memory memory, Storage storage, Display display) {
        this.chip = chip;
        this.memory = memory;
        this.storage = storage;
        this.display = display;
    }

    public Chip getChip() {
        return chip;
    }

    public Memory getMemory() {
        return memory;
    }

    public Storage getStorage() {
        return storage;
    }

    public Display getDisplay() {
        return display;
    }

    public abstract void performTask(String task, double a, double b);

    public double calculatePrice() {
        return chip.getPrice() + memory.getPrice() + storage.getPrice() + display.getPrice();
    }

    @Override
    public String toString() {
        return String.format("Chip: %s, Memory: %s, storage.Storage: %s, display.Display: %s, Total Price: $%.2f",
                chip, memory, storage, display, calculatePrice());
    }
}



