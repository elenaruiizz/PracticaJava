import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import aed.OtherClass;

public class OtherClassTest {

    @Test
    public void testSolveEquation2Grade() {
        // Test case 1: a = 1, b = -3, c = 2
        double[] result1 = OtherClass.solveEquation2Grade(1, -3, 2);
        assertArrayEquals(new double[]{2.0, 1.0}, result1, 0.0001);

        // Test case 2: a = 2, b = 5, c = 2
        double[] result2 = OtherClass.solveEquation2Grade(2, 5, 2);
        assertArrayEquals(new double[]{-0.5, -2.0}, result2, 0.0001);

        // Test case 3: a = 1, b = 0, c = -4
        double[] result3 = OtherClass.solveEquation2Grade(1, 0, -4);
        assertArrayEquals(new double[]{2.0, -2.0}, result3, 0.0001);

        // Test case 4: a = 4, b = 4, c = 1
        double[] result4 = OtherClass.solveEquation2Grade(4, 4, 1);
        assertArrayEquals(new double[]{-0.5, -0.5}, result4, 0.0001);

        // Test case 5: a = 1, b = -6, c = 9
        double[] result5 = OtherClass.solveEquation2Grade(1, -6, 9);
        assertArrayEquals(new double[]{3.0, 3.0}, result5, 0.0001);
    }
}