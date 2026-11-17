import org.junit.jupiter.api.Test;

import aed.Main;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testSumaSoloPositivos() {
        try {
            // Test case 1: Both numbers are positive
            int result1 = Main.sumaSoloPositivos(5, 10);
            assertEquals(15, result1);

            // Test case 2: One number is positive and the other is negative
            assertThrows(Exception.class, () -> Main.sumaSoloPositivos(-5, 10));

            // Test case 3: Both numbers are negative
            assertThrows(Exception.class, () -> Main.sumaSoloPositivos(-5, -10));

            // Test case 4: One number is zero and the other is positive
            assertThrows(Exception.class, () -> Main.sumaSoloPositivos(0, 10));

            // Test case 5: One number is positive and the other is zero
            assertThrows(Exception.class, () -> Main.sumaSoloPositivos(5, 0));

        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
}