package aed;

public class Main {
    // Here they go all the functions and methods
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    /**
     * @param a
     * @param b
     * @return
     * @throws Exception
     */
    public static int sumaSoloPositivos(int a, int b) throws Exception {
        if (a > 0 && b > 0) {
            return a+b;
        } else {
            throw new Exception("Los números deben ser positivos");
        }
        //HOLAAAAAA
    }
}