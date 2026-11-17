package aed;

public class OtherClass {
    
    public static double[] solveEquation2Grade(int a, int b, int c) {
        double[] result = new double[2];
        double x1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        double x2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        result[0] = x1;
        result[1] = x2;
        return result;
    }
}
