package notebook;

import chip.M4Chip;
import display.Display;
import memory.Memory;
import storage.Storage;

// Subclass representing notebook. MacbookAir
public class MacbookAir extends Macbook {

    public MacbookAir(Memory memory, Storage storage) {
        super(new M4Chip(), memory, storage, new Display("RetinaDisplay", 300));
    }

    @Override
    public void performTask(String task, double a, double b) {
        getChip().performOperation(task, 10, 5, getStorage());
    }
}

