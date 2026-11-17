package Entrega1;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GestorCSVTest {
    private static final String CABECERA =
            "tipo;nombrePoblacion;responsable;dias;codigo;fechaNacimiento;peso;sexo;temperatura;"
                    + "observaciones;mutacionX1;mutacionX2;mutacionY";
    private static final String POBLACION = "POBLACION;Poblacion 1;Elena;69;;;;;;;;;";
    private static final String RATON_OK = "RATON;;;;R1;2020-01-01;11;MACHO;34.0;;false;;false";

    private Path dir;

    @BeforeEach
    public void crearDirectorio() throws IOException {
        dir = Files.createTempDirectory("animalario-test");
    }

    @AfterEach
    public void borrarDirectorio() throws IOException {
        try (Stream<Path> s = Files.walk(dir)) {
            s.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        }
    }

    // ------------------------------------------------------------ guardar y cargar

    @Test
    public void testGuardarYCargarPoblacionVacia() throws IOException {
        Poblaciones original = new Poblaciones("Vacía", "Ana", 10);
        String ruta = ruta("vacia.csv");

        GestorCSV.guardar(original, ruta);
        Poblaciones leida = GestorCSV.cargar(ruta);

        assertEquals("Vacía", leida.getNombre());
        assertEquals("Ana", leida.getResponsable());
        assertEquals(10, leida.getDiasPermanencia());
        assertTrue(leida.getRatones().isEmpty());
    }

    @Test
    public void testGuardarYCargarPoblacionConRatones() throws IOException {
        Poblaciones original = new Poblaciones("P", "Elena", 269);
        original.agregarRaton(raton("M1", Ratones.Sexo.MACHO, 30, 37.5, "obs", true, false, true));
        original.agregarRaton(raton("H1", Ratones.Sexo.HEMBRA, 25, 30.0, "", true, true, false));
        original.agregarRaton(raton("H2", Ratones.Sexo.HEMBRA, 26, 45.0, "x", false, true, false));
        String ruta = ruta("p.csv");

        GestorCSV.guardar(original, ruta);
        Poblaciones leida = GestorCSV.cargar(ruta);

        assertEquals(3, leida.getRatones().size());
        assertEquals("M1", leida.getRatones().get(0).getCodigoReferencia());
        Ratones m1 = leida.buscarRaton("M1");
        assertEquals(LocalDate.of(2020, 1, 1), m1.getFechaNacimiento());
        assertEquals(30, m1.getPesoGramos());
        assertEquals(37.5, m1.getTemperaturaCorporal());
        assertEquals("obs", m1.getObservaciones());
        assertTrue(m1.esEsteril());
        assertTrue(m1.esPoligamo());

        Ratones h1 = leida.buscarRaton("H1");
        assertEquals(Ratones.Sexo.HEMBRA, h1.getSexo());
        assertTrue(h1.esEsteril());
        assertFalse(h1.esPoligamo());
        assertEquals(30.0, h1.getTemperaturaCorporal());

        assertFalse(leida.buscarRaton("H2").esEsteril());
        assertEquals(45.0, leida.buscarRaton("H2").getTemperaturaCorporal());
    }

    @Test
    public void testTextosConPuntoYComaComillasYSaltosDeLinea() throws IOException {
        String raro = "uno; \"dos\"\ny tres\r\ncuatro";
        Poblaciones original = new Poblaciones("Nombre; con \"comillas\"", "Resp;onsable", 5);
        original.agregarRaton(raton("COD;1\"", Ratones.Sexo.MACHO, 30, 37.5, raro, false, false, false));
        String ruta = ruta("raro.csv");

        GestorCSV.guardar(original, ruta);
        Poblaciones leida = GestorCSV.cargar(ruta);

        assertEquals("Nombre; con \"comillas\"", leida.getNombre());
        assertEquals("Resp;onsable", leida.getResponsable());
        assertEquals(raro, leida.buscarRaton("COD;1\"").getObservaciones());
    }

    @Test
    public void testElArchivoGuardadoTieneElFormatoPedido() throws IOException {
        Poblaciones p = new Poblaciones("P", "E", 69);
        p.agregarRaton(raton("A", Ratones.Sexo.MACHO, 11, 34.0, "", false, false, true));
        p.agregarRaton(raton("B", Ratones.Sexo.HEMBRA, 11, 34.0, "", true, false, false));
        String ruta = ruta("formato.csv");

        GestorCSV.guardar(p, ruta);

        String esperado = CABECERA + "\r\n"
                + "POBLACION;P;E;69;;;;;;;;;\r\n"
                + "RATON;;;;A;2020-01-01;11;MACHO;34.0;;false;;true\r\n"
                + "RATON;;;;B;2020-01-01;11;HEMBRA;34.0;;true;false;\r\n";
        assertEquals(esperado, Files.readString(Path.of(ruta), StandardCharsets.UTF_8));
    }

    @Test
    public void testSobrescribirNoDejaArchivosTemporales() throws IOException {
        String ruta = ruta("a.csv");
        GestorCSV.guardar(new Poblaciones("P", "E", 1), ruta);
        GestorCSV.guardar(new Poblaciones("Q", "E", 2), ruta);

        assertEquals("Q", GestorCSV.cargar(ruta).getNombre());
        try (Stream<Path> s = Files.list(dir)) {
            assertEquals(1L, s.count());
        }
    }

    @Test
    public void testGuardarEnDirectorioInexistenteFalla() {
        String ruta = dir.resolve("no_existe").resolve("a.csv").toString();
        assertThrows(IOException.class, () -> GestorCSV.guardar(new Poblaciones("P", "E", 1), ruta));
    }

    // ------------------------------------------------------------ archivo de ejemplo


    @Test
    public void testSeAceptaFinDeLineaUnixYSinSaltoFinal() throws IOException {
        String contenido = CABECERA + "\n" + POBLACION + "\n" + RATON_OK;
        Poblaciones p = GestorCSV.cargar(escribir("unix.csv", contenido));
        assertEquals(1, p.getRatones().size());
    }

    // ------------------------------------------------------------ archivos incorrectos

    @Test
    public void testRechazaArchivoInexistente() {
        assertThrows(IOException.class, () -> GestorCSV.cargar(ruta("no_existe.csv")));
    }


    @Test
    public void testRechazaArchivoVacioOSoloCabecera() throws IOException {
        assertThrows(IOException.class, () -> GestorCSV.cargar(escribir("v.csv", "")));
        assertThrows(IOException.class, () -> GestorCSV.cargar(escribir("c.csv", CABECERA + "\r\n")));
    }

    @Test
    public void testRechazaCabeceraIncorrecta() throws IOException {
        String ruta = escribir("cab.csv", CABECERA.replace("peso", "kilos") + "\r\n" + POBLACION + "\r\n");
        assertThrows(IOException.class, () -> GestorCSV.cargar(ruta));
    }

    @Test
    public void testRechazaSiLaPrimeraFilaNoEsPoblacion() throws IOException {
        assertInvalido(RATON_OK);
    }

    @Test
    public void testRechazaDosFilasPoblacion() throws IOException {
        assertInvalido(POBLACION, POBLACION);
    }

    @Test
    public void testRechazaDatosGeneralesInvalidos() throws IOException {
        assertInvalido("POBLACION;;Elena;69;;;;;;;;;");
        assertInvalido("POBLACION;P;;69;;;;;;;;;");
        assertInvalido("POBLACION;P;E;0;;;;;;;;;");
        assertInvalido("POBLACION;P;E;270;;;;;;;;;");
        assertInvalido("POBLACION;P;E;abc;;;;;;;;;");
        assertInvalido("POBLACION;P;E;-5;;;;;;;;;");
    }

    @Test
    public void testRechazaCamposQueDebenEstarVaciosEnPoblacion() throws IOException {
        assertInvalido("POBLACION;P;E;69;X;;;;;;;;");
    }

    @Test
    public void testRechazaReferenciasDuplicadas() throws IOException {
        assertInvalido(POBLACION, RATON_OK, RATON_OK);
    }

    @Test
    public void testRechazaFilasConColumnasDeMasOdeMenos() throws IOException {
        assertInvalido(POBLACION, "RATON;;;;R1;2020-01-01;11;MACHO;34.0;;false;");
        assertInvalido(POBLACION, RATON_OK + ";extra");
    }

    @Test
    public void testRechazaTipoDeFilaDesconocido() throws IOException {
        assertInvalido(POBLACION, RATON_OK.replace("RATON", "GATO"));
    }


    @Test
    public void testRechazaConfiguracionesCromosomicasImposibles() throws IOException {
        assertInvalido(POBLACION, "RATON;;;;R1;2020-01-01;11;MACHO;34.0;;false;false;false"); // macho con X2
        assertInvalido(POBLACION, "RATON;;;;R1;2020-01-01;11;MACHO;34.0;;false;;");           // macho sin Y
        assertInvalido(POBLACION, "RATON;;;;R1;2020-01-01;11;HEMBRA;34.0;;false;false;true"); // hembra con Y
        assertInvalido(POBLACION, "RATON;;;;R1;2020-01-01;11;HEMBRA;34.0;;false;;");          // hembra sin X2
        assertInvalido(POBLACION, "RATON;;;;R1;2020-01-01;11;HEMBRA;34.0;;;false;");          // sin X1
        assertInvalido(POBLACION, "RATON;;;;R1;2020-01-01;11;HEMBRA;34.0;;TRUE;false;");      // booleano con mayúsculas
    }

    @Test
    public void testRechazaComillasMalFormadas() throws IOException {
        assertInvalido(POBLACION, "RATON;;;;R1;2020-01-01;11;MACHO;34.0;\"sin cerrar;false;;false");
        assertInvalido(POBLACION, "RATON;;;;R1;2020-01-01;11;MACHO;34.0;\"a\"b;false;;false");
        assertInvalido(POBLACION, "RATON;;;;R1;2020-01-01;11;MACHO;34.0;a\"b;false;;false");
    }

    @Test
    public void testRechazaArchivoQueNoEstaEnUtf8() throws IOException {
        Path p = dir.resolve("latin1.csv");
        Files.write(p, (CABECERA + "\r\nPOBLACION;Población;Elena;69;;;;;;;;;\r\n")
                .getBytes(StandardCharsets.ISO_8859_1));
        assertThrows(IOException.class, () -> GestorCSV.cargar(p.toString()));
    }

    // ------------------------------------------------------------ auxiliares

    private String ruta(String nombre) {
        return dir.resolve(nombre).toString();
    }

    private String escribir(String nombre, String contenido) throws IOException {
        Path p = dir.resolve(nombre);
        Files.writeString(p, contenido, StandardCharsets.UTF_8);
        return p.toString();
    }

    private void assertInvalido(String... filas) throws IOException {
        String contenido = CABECERA + "\r\n" + String.join("\r\n", filas) + "\r\n";
        String ruta = escribir("invalido.csv", contenido);
        assertThrows(IOException.class, () -> GestorCSV.cargar(ruta), "Debería rechazarse: " + contenido);
    }

    private Path copiarRecurso(String recurso, String destino) throws IOException {
        Path p = dir.resolve(destino);
        try (InputStream in = GestorCSVTest.class.getResourceAsStream(recurso)) {
            assertNotNull(in);
            Files.copy(in, p);
        }
        return p;
    }

    private static Ratones raton(String codigo, Ratones.Sexo sexo, int peso, double temp, String obs,
                                 boolean x1, boolean x2, boolean y) {
        boolean macho = sexo == Ratones.Sexo.MACHO;
        return new Ratones(codigo, LocalDate.of(2020, 1, 1), peso, sexo, temp, obs,
                new CromosomasSexualesYMutaciones(sexo, x1, macho ? false : x2, macho ? y : false));
    }

}