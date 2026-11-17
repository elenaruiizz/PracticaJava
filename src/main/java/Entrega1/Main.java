package Entrega1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();

        BufferedReader consola =
                new BufferedReader(new InputStreamReader(System.in));

        boolean continuar = true;

        while (continuar) {
            menu.mostrarMenu();

            try {
                String entrada = consola.readLine();

                if (entrada == null) {
                    System.out.println("Fin de entrada");
                    break;
                }

                int opcion = Integer.parseInt(entrada.trim());

                continuar = menu.ejecutarOpcion(opcion, consola);

            } catch (NumberFormatException ex) {
                System.out.println("Introduce un número entero");

            } catch (IOException ex) {
                System.out.println("Error de lectura: " + ex.getMessage());
                break;
            }

        }

        System.out.println("Programa finalizado");
    }
}
