package Entrega1;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorCSV {
    private static final String CABECERA =
            "tipo;nombrePoblacion;responsable;dias;codigo;" +
                    "fechaNacimiento;peso;sexo;temperatura;observaciones;" +
                    "mutacionX1;mutacionX2;mutacionY";

    public static Poblaciones cargar(String ruta) throws IOException {
        if (ruta == null || ruta.trim().isEmpty()) {
            throw new IOException("La ruta no puede estar vacía");
        }
        String contenido = Files.readString(
                Path.of(ruta), StandardCharsets.UTF_8);

        List<List<String>> filas = leerCSV(contenido);

        if (filas.size() < 2) {
            throw new IOException("Falta la cabecera o la población");
        }

        if (!filas.get(0).equals(List.of(CABECERA.split(";")))) {
            throw new IOException("Cabecera CSV incorrecta");
        }

        List<String> primera = filas.get(1);
        comprobarColumnas(primera);

        if (!primera.get(0).equals("POBLACION")) {
            throw new IOException("La primera fila debe ser POBLACION");
        }

        try {
            Poblaciones poblacion = new Poblaciones(
                    primera.get(1),
                    primera.get(2),
                    Integer.parseInt(primera.get(3)));

            for (int j = 4; j < 13; j++) {
                exigirVacio(primera.get(j));
            }

            for (int i = 2; i < filas.size(); i++) {
                List<String> f = filas.get(i);
                comprobarColumnas(f);

                if (!f.get(0).equals("RATON")) {
                    throw new IllegalArgumentException(
                            "Se esperaba una fila RATON");
                }

                for (int j = 1; j <= 3; j++) {
                    exigirVacio(f.get(j));
                }

                Ratones.Sexo sexo = Ratones.Sexo.valueOf(f.get(7));

                boolean x1 = leerBooleano(f.get(10));
                boolean x2;
                boolean y;

                if (sexo == Ratones.Sexo.MACHO) {
                    exigirVacio(f.get(11));
                    x2 = false;
                    y = leerBooleano(f.get(12));
                } else {
                    x2 = leerBooleano(f.get(11));
                    exigirVacio(f.get(12));
                    y = false;
                }

                CromosomasSexualesYMutaciones c =
                        new CromosomasSexualesYMutaciones(
                                sexo, x1, x2, y);

                Ratones raton = new Ratones(
                        f.get(4),
                        LocalDate.parse(f.get(5)),
                        Integer.parseInt(f.get(6)),
                        sexo,
                        leerTemperatura(f.get(8)),
                        f.get(9),
                        c);

                poblacion.agregarRaton(raton);
            }

            return poblacion;

        } catch (RuntimeException ex) {
            throw new IOException(
                    "Contenido CSV inválido: " + ex.getMessage(), ex);
        }
    }
    private static double leerTemperatura(String texto) {

        if (!texto.matches("[+-]?[0-9]+(\\.[0-9]+)?")) {
            throw new IllegalArgumentException(
                    "Formato de temperatura incorrecto");
        }

        return Double.parseDouble(texto);
    }

    public static void guardar(Poblaciones poblacion, String ruta)
            throws IOException {

        if (poblacion == null) {
            throw new IllegalArgumentException("No hay población");
        }

        StringBuilder csv = new StringBuilder();
        csv.append(CABECERA).append("\r\n");

        agregarFila(csv,
                "POBLACION",
                poblacion.getNombre(),
                poblacion.getResponsable(),
                String.valueOf(poblacion.getDiasPermanencia()),
                "", "", "", "", "", "", "", "", "");

        for (Ratones r : poblacion.getRatones()) {
            CromosomasSexualesYMutaciones c =
                    r.getMutacionesCromosomicas();

            boolean macho = r.getSexo() == Ratones.Sexo.MACHO;

            agregarFila(csv,
                    "RATON", "", "", "",
                    r.getCodigoReferencia(),
                    r.getFechaNacimiento().toString(),
                    String.valueOf(r.getPesoGramos()),
                    r.getSexo().name(),
                    String.valueOf(r.getTemperaturaCorporal()),
                    r.getObservaciones(),
                    String.valueOf(c.isMutacionX1()),
                    macho ? "" : String.valueOf(c.isMutacionX2()),
                    macho ? String.valueOf(c.isMutacionY()) : "");
        }

        Path destino = Path.of(ruta).toAbsolutePath();
        Path directorio = destino.getParent();

        // Escribimos primero en un archivo temporal para evitar
        // dejar un CSV incompleto si falla la escritura.
        Path temporal = Files.createTempFile(
                directorio, "animalario-", ".tmp");

        try {
            Files.writeString(
                    temporal, csv.toString(), StandardCharsets.UTF_8);

            try {
                Files.move(temporal, destino,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temporal, destino,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporal);
        }
    }

    private static void agregarFila(StringBuilder sb, String... campos) {
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) sb.append(';');
            sb.append(escapar(campos[i]));
        }
        sb.append("\r\n");
    }

    private static String escapar(String texto) {
        if (texto == null) texto = "";

        if (texto.contains(";") || texto.contains("\"")
                || texto.contains("\n") || texto.contains("\r")) {
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
    }

    private static void comprobarColumnas(List<String> fila) {
        if (fila.size() != 13) {
            throw new IllegalArgumentException(
                    "Una fila no tiene 13 columnas");
        }
    }

    private static void exigirVacio(String valor) {
        if (!valor.isEmpty()) {
            throw new IllegalArgumentException(
                    "Campo que debería estar vacío");
        }
    }

    private static boolean leerBooleano(String valor) {
        if (valor.equals("true")) return true;
        if (valor.equals("false")) return false;
        throw new IllegalArgumentException(
                "Valor booleano incorrecto: " + valor);
    }

    private static List<List<String>> leerCSV(String texto)
            throws IOException {

        List<List<String>> filas = new ArrayList<>();
        List<String> fila = new ArrayList<>();
        StringBuilder campo = new StringBuilder();

        boolean entreComillas = false;
        boolean cerrado = false;

        for (int i = 0; i < texto.length(); i++) {
            char ch = texto.charAt(i);

            if (entreComillas) {
                if (ch == '"') {
                    if (i + 1 < texto.length()
                            && texto.charAt(i + 1) == '"') {
                        campo.append('"');
                        i++;
                    } else {
                        entreComillas = false;
                        cerrado = true;
                    }
                } else {
                    campo.append(ch);
                }
            } else if (ch == '"') {
                if (campo.length() != 0 || cerrado) {
                    throw new IOException("Comillas CSV incorrectas");
                }
                entreComillas = true;
            } else if (ch == ';' || ch == '\n' || ch == '\r') {
                fila.add(campo.toString());
                campo.setLength(0);
                cerrado = false;

                if (ch != ';') {
                    filas.add(fila);
                    fila = new ArrayList<>();

                    if (ch == '\r' && i + 1 < texto.length()
                            && texto.charAt(i + 1) == '\n') {
                        i++;
                    }
                }
            } else {
                if (cerrado) {
                    throw new IOException(
                            "Caracteres después de cerrar comillas");
                }
                campo.append(ch);
            }
        }

        if (entreComillas) {
            throw new IOException("Comillas sin cerrar");
        }

        if (!fila.isEmpty() || campo.length() > 0 || cerrado) {
            fila.add(campo.toString());
            filas.add(fila);
        }

        return filas;
    }
}
