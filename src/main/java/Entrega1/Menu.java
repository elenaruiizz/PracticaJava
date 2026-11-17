package Entrega1;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;

public class Menu {
    private Poblaciones poblacionActiva;
    private String rutaArchivo;
    private boolean cambiosPendientes;

    public void mostrarMenu() {
        System.out.println("\n------ ANIMALARIO ------");
        System.out.println("0. Salir");
        System.out.println("1. Abrir población");
        System.out.println("2. Crear población");
        System.out.println("3. Añadir ratón");
        System.out.println("4. Listar ratones");
        System.out.println("5. Eliminar ratón");
        System.out.println("6. Modificar ratón");
        System.out.println("7. Consultar ratón");
        System.out.println("8. Guardar");
        System.out.println("9. Guardar como");
        System.out.print("Seleccione una opción: ");
    }

    public boolean ejecutarOpcion(int opcion, BufferedReader consola)
            throws IOException {

        if (opcion >= 3 && opcion <= 9 && poblacionActiva == null) {
            System.out.println("Primero debes abrir o crear una población");
            return true;
        }

        try {
            switch (opcion) {
                case 0:
                    return !confirmarSalida(consola);

                case 1:
                    abrirPoblacion(consola);
                    break;

                case 2:
                    crearPoblacion(consola);
                    break;

                case 3:
                    agregarRaton(consola);
                    break;

                case 4:
                    listarRatones();
                    break;

                case 5:
                    eliminarRaton(consola);
                    break;

                case 6:
                    modificarRaton(consola);
                    break;

                case 7:
                    consultarRaton(consola);
                    break;

                case 8:
                    guardar(consola, false);
                    break;

                case 9:
                    guardar(consola, true);
                    break;

                default:
                    System.out.println("Opción no válida");
            }

        } catch (RuntimeException ex) {
            System.out.println("Datos inválidos: " + ex.getMessage());

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return true;
    }

    private String leer(BufferedReader consola, String mensaje)
            throws IOException {
        System.out.print(mensaje);
        String valor = consola.readLine();
        if (valor == null) {
            throw new IOException("Fin de la entrada");
        }
        return valor;
    }

    private int leerEntero(BufferedReader consola, String mensaje)
            throws IOException {
        return Integer.parseInt(leer(consola, mensaje).trim());
    }

    private boolean leerBooleano(BufferedReader consola, String mensaje)
            throws IOException {
        String valor = leer(consola, mensaje + " (true/false): ").trim();

        if (valor.equalsIgnoreCase("true")) return true;
        if (valor.equalsIgnoreCase("false")) return false;

        throw new IllegalArgumentException("Debes escribir true o false");
    }

    private boolean confirmar(BufferedReader consola, String mensaje)
            throws IOException {
        String respuesta = leer(consola, mensaje + " (s/n): ").trim();
        return respuesta.equalsIgnoreCase("s");
    }

    private boolean confirmarSustitucion(BufferedReader consola)
            throws IOException {

        if (poblacionActiva != null && cambiosPendientes) {
            return confirmar(consola,
                    "Hay cambios sin guardar. ¿Quieres descartarlos?");
        }
        return true;
    }

    private boolean confirmarSalida(BufferedReader consola)
            throws IOException {

        if (cambiosPendientes) {
            return confirmar(consola,
                    "Hay cambios sin guardar. ¿Quieres salir igualmente?");
        }
        return true;
    }

    private void crearPoblacion(BufferedReader consola)
            throws IOException {

        if (!confirmarSustitucion(consola)) return;

        String nombre = leer(consola, "Nombre de población: ");
        String responsable = leer(consola, "Responsable: ");
        int dias = leerEntero(consola, "Días de permanencia: ");

        Poblaciones nueva = new Poblaciones(nombre, responsable, dias);

        poblacionActiva = nueva;
        rutaArchivo = null;
        cambiosPendientes = true;

        System.out.println("Población creada correctamente");
    }

    private void abrirPoblacion(BufferedReader consola)
            throws IOException {

        if (!confirmarSustitucion(consola)) return;

        String ruta = leer(consola, "Ruta del CSV: ");

        // La población activa solo cambia si la carga termina bien.
        Poblaciones nueva = GestorCSV.cargar(ruta);

        poblacionActiva = nueva;
        rutaArchivo = ruta;
        cambiosPendientes = false;

        System.out.println("Población cargada correctamente");
    }

    private Ratones leerRaton(BufferedReader consola)
            throws IOException {

        String codigo = leer(consola, "Código de referencia: ");

        LocalDate fecha = LocalDate.parse(
                leer(consola, "Fecha de nacimiento (AAAA-MM-DD): "));

        int peso = leerEntero(consola, "Peso en gramos: ");

        Ratones.Sexo sexo = Ratones.Sexo.valueOf(
                leer(consola, "Sexo (MACHO/HEMBRA): ")
                        .trim().toUpperCase());

        double temperatura = Double.parseDouble(
                leer(consola, "Temperatura corporal: ").trim());

        String observaciones = leer(consola, "Observaciones: ");

        boolean x1 = leerBooleano(consola, "Mutación X1");
        boolean x2 = false;
        boolean y = false;

        if (sexo == Ratones.Sexo.MACHO) {
            y = leerBooleano(consola, "Mutación Y");
        } else {
            x2 = leerBooleano(consola, "Mutación X2");
        }

        CromosomasSexualesYMutaciones cromosomas =
                new CromosomasSexualesYMutaciones(sexo, x1, x2, y);

        return new Ratones(
                codigo, fecha, peso, sexo,
                temperatura, observaciones, cromosomas);
    }

    private void agregarRaton(BufferedReader consola)
            throws IOException {

        Ratones raton = leerRaton(consola);
        poblacionActiva.agregarRaton(raton);

        cambiosPendientes = true;
        System.out.println("Ratón añadido correctamente");
    }

    private void listarRatones() {
        if (poblacionActiva.getRatones().isEmpty()) {
            System.out.println("La población no tiene ratones");
            return;
        }

        System.out.println("Códigos de referencia:");

        for (Ratones r : poblacionActiva.getRatones()) {
            System.out.println(r.getCodigoReferencia());
        }
    }

    private void eliminarRaton(BufferedReader consola)
            throws IOException {

        String codigo = leer(consola, "Código del ratón a eliminar: ");

        if (poblacionActiva.eliminarRaton(codigo)) {
            cambiosPendientes = true;
            System.out.println("Ratón eliminado");
        } else {
            System.out.println("No existe un ratón con ese código");
        }
    }

    private void modificarRaton(BufferedReader consola)
            throws IOException {

        String codigo = leer(consola, "Código del ratón a modificar: ");

        Ratones anterior = poblacionActiva.buscarRaton(codigo);

        if (anterior == null) {
            System.out.println("El ratón no existe");
            return;
        }

        System.out.println("Introduce todos los datos nuevos del ratón:");

        // Se valida el ratón completo antes de sustituir el original.
        Ratones nuevo = leerRaton(consola);

        poblacionActiva.modificarRaton(codigo, nuevo);

        cambiosPendientes = true;
        System.out.println("Ratón modificado correctamente");
    }

    private void consultarRaton(BufferedReader consola)
            throws IOException {

        String codigo = leer(consola, "Código del ratón: ");
        Ratones r = poblacionActiva.buscarRaton(codigo);

        if (r == null) {
            System.out.println("El ratón no existe");
            return;
        }

        CromosomasSexualesYMutaciones c =
                r.getMutacionesCromosomicas();

        System.out.println("\n--- DATOS DEL RATÓN ---");
        System.out.println("Código: " + r.getCodigoReferencia());
        System.out.println("Nacimiento: " + r.getFechaNacimiento());
        System.out.println("Peso: " + r.getPesoGramos() + " g");
        System.out.println("Sexo: " + r.getSexo());
        System.out.println("Temperatura: "
                + r.getTemperaturaCorporal());
        System.out.println("Observaciones: " + r.getObservaciones());
        System.out.println("Mutación X1: " + c.isMutacionX1());

        if (r.getSexo() == Ratones.Sexo.MACHO) {
            System.out.println("Mutación Y: " + c.isMutacionY());
        } else {
            System.out.println("Mutación X2: " + c.isMutacionX2());
        }

        System.out.println("Estéril: " + r.esEsteril());
        System.out.println("Polígamo: " + r.esPoligamo());
    }

    private void guardar(BufferedReader consola, boolean guardarComo)
            throws IOException {

        String destino = rutaArchivo;

        if (guardarComo || destino == null) {
            destino = leer(consola, "Ruta de destino del CSV: ");

            if (destino.isBlank()) {
                throw new IllegalArgumentException("Ruta vacía");
            }
        }

        GestorCSV.guardar(poblacionActiva, destino);

        // Actualizamos el estado solo después de guardar bien.
        rutaArchivo = destino;
        cambiosPendientes = false;

        System.out.println("Población guardada correctamente");
    }
}
