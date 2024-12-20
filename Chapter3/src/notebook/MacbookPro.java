package notebook;

import chip.Chip;
import cooling.CoolingFan;
import display.Display;
import memory.Memory;
import storage.Storage;

// Subclass representing notebook.Macbook Pro
public class MacbookPro extends Macbook {
    private CoolingFan coolingFan;

    public MacbookPro(Chip chip, Memory memory, Storage storage) {
        super(chip, memory, storage, new Display("RetinaXDRDisplay", 500));
        this.coolingFan = new CoolingFan("Active Cooling", 50);
    }

    public CoolingFan getCoolingFan() {
        return coolingFan;
    }

    @Override
    public void performTask(String task, double a, double b) {
        Chip chip = getChip();
        if (chip.isInCooldown()) {
            coolingFan.coolDown(chip);
            System.out.println("Cooling fan activated. Chip operation count reset.");
        }
        chip.performOperation(task, a, b, getStorage());
    }

    @Override
    public String toString() {
        return super.toString() + ", Cooling Fan: " + coolingFan;
    }
}
