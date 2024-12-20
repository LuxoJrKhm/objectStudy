package MainTest;

import chip.M4ProChip;
import memory.Memory16GB;
import memory.Memory32GB;
import notebook.Macbook;
import notebook.MacbookAir;
import notebook.MacbookPro;
import storage.Storage;

public class Main {
    public static void main(String[] args) {
        // Test notebook.MacbookAir
        System.out.println("Testing notebook.Macbook Air:");
        Macbook macbookAir = new MacbookAir(new Memory16GB(), new Storage("512GB", 200));
        System.out.println(macbookAir);
        macbookAir.performTask("addition", 1, 4);
        macbookAir.performTask("subtraction", 23, 46); // Unsupported task for M4Chip
        System.out.println();

        // Test notebook.MacbookPro with M4ProChip
        System.out.println("Testing notebook.Macbook Pro with M4ProChip:");
        Macbook macbookPro = new MacbookPro(new M4ProChip(), new Memory32GB(), new Storage("1024GB", 400));
        System.out.println(macbookPro);
        macbookPro.performTask("addition", 285, 13);
        macbookPro.performTask("subtraction", 153, 456);
        macbookPro.performTask("multiplication", 4567, 1564);
        macbookPro.performTask("division", 1562, 0);
        macbookPro.performTask("division", 1513, 151);
        System.out.println();

        // Test chip cooling with notebook.MacbookPro
        System.out.println("Testing Chip Throttling and Cooling on notebook.Macbook Pro:");
        for (int i = 1; i <= 10; i++) {
            macbookPro.performTask("addition", 5, 4);
        }
        System.out.println(macbookPro);
        System.out.println();
    }
}
