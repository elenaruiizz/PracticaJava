package Entrega1;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RatonesTest {
    @Test
    public void testConstructor() {
        CromosomasSexualesYMutaciones cromosomas =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.MACHO,
                        false,
                        false,
                        false
                );
        Ratones r = new Ratones(
                "R001",
                LocalDate.of(2025, 5, 20),
                30,
                Ratones.Sexo.MACHO,
                37.5,
                "Sin observaciones",
                cromosomas
        );

        assertEquals("R001", r.getCodigoReferencia());
        assertEquals(LocalDate.of(2025, 5, 20),
                r.getFechaNacimiento());
        assertEquals(30, r.getPesoGramos());
        assertEquals(Ratones.Sexo.MACHO, r.getSexo());
        assertEquals(37.5, r.getTemperaturaCorporal());
        assertEquals("Sin observaciones", r.getObservaciones());
    }

    @Test
    public void testCodigoReferenciaInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            crearRaton("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            crearRaton("   ");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            crearRaton(null);
        });
    }

    @Test
    public void testFechaNacimientoInvalida() {
        Ratones r = crearRaton("R001");

        assertThrows(IllegalArgumentException.class, () -> {
            r.setFechaNacimiento(null);
        });
    }

    @Test
    public void testPesoInvalido() {
        Ratones r = crearRaton("R001");

        assertThrows(IllegalArgumentException.class, () -> {
            r.setPeso(-5);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            r.setPeso(0);
        });
    }

    @Test
    public void testSexoInvalido() {
        Ratones r = crearRaton("R001");

        assertThrows(IllegalArgumentException.class, () -> {
            r.setSexo(null);
        });
    }

    @Test
    public void testTemperaturaLimites() {
        Ratones r = crearRaton("R001");

        r.setTemperaturaCorporal(30.0);
        assertEquals(30.0, r.getTemperaturaCorporal());

        r.setTemperaturaCorporal(45.0);
        assertEquals(45.0, r.getTemperaturaCorporal());
    }

    @Test
    public void testModificarPeso() {
        Ratones r = crearRaton("R001");

        r.setPeso(40);

        assertEquals(40, r.getPesoGramos());
    }

    @Test
    public void testObservacionesVacias() {
        Ratones r = crearRaton("R001");

        r.setObservaciones("");

        assertEquals("", r.getObservaciones());
    }

    @Test
    public void testNoModificarPesoInvalido() {
        Ratones r = crearRaton("R001");

        assertThrows(IllegalArgumentException.class, () -> {
            r.setPeso(-10);
        });

        assertEquals(30, r.getPesoGramos());
    }

    // Método auxiliar para crear ratones en los test
    private Ratones crearRaton(String codigo) {
        CromosomasSexualesYMutaciones cromosomas =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.MACHO,
                        false,
                        false,
                        false
                );
        return new Ratones(
                codigo,
                LocalDate.of(2025, 5, 20),
                30,
                Ratones.Sexo.MACHO,
                37.5,
                "",
                cromosomas
        );
    }


    @Test
    public void testTemperaturaInvalida() {
        Ratones r = crearRaton("R001");

        assertThrows(IllegalArgumentException.class, () -> {
            r.setTemperaturaCorporal(29.9);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            r.setTemperaturaCorporal(45.1);
        });

        assertEquals(37.5, r.getTemperaturaCorporal());
    }

    @Test
    public void testModificarCodigoReferencia() {
        Ratones r = crearRaton("R001");

        r.setCodigoReferencia("R002");

        assertEquals("R002", r.getCodigoReferencia());
    }

    @Test
    public void testModificarFechaNacimiento() {
        Ratones r = crearRaton("R001");

        LocalDate nuevaFecha = LocalDate.of(2026, 1, 15);
        r.setFechaNacimiento(nuevaFecha);

        assertEquals(nuevaFecha, r.getFechaNacimiento());
    }

    @Test
    public void testCromosomasNulos() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Ratones(
                    "R001",
                    LocalDate.of(2025, 5, 20),
                    30,
                    Ratones.Sexo.MACHO,
                    37.5,
                    "",
                    null);
        });
    }

    @Test
    public void testSexoIncompatibleConCromosomas() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.HEMBRA,
                        false, false, false);

        assertThrows(IllegalArgumentException.class, () -> {
            new Ratones(
                    "R001",
                    LocalDate.of(2025, 5, 20),
                    30,
                    Ratones.Sexo.MACHO,
                    37.5,
                    "",
                    c);
        });
    }

    @Test
    public void testNoCambiarSexoIncompatible() {
        Ratones r = crearRaton("R001");

        assertThrows(IllegalArgumentException.class, () -> {
            r.setSexo(Ratones.Sexo.HEMBRA);
        });

        assertEquals(Ratones.Sexo.MACHO, r.getSexo());
    }

    @Test
    public void testRatonEsteril() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.MACHO,
                        true, false, false);

        Ratones r = new Ratones(
                "R001",
                LocalDate.of(2025, 5, 20),
                30,
                Ratones.Sexo.MACHO,
                37.5,
                "",
                c);

        assertTrue(r.esEsteril());
        assertFalse(r.esPoligamo());
    }

    @Test
    public void testRatonPoligamo() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.MACHO,
                        false, false, true);

        Ratones r = new Ratones(
                "R001",
                LocalDate.of(2025, 5, 20),
                30,
                Ratones.Sexo.MACHO,
                37.5,
                "",
                c);

        assertFalse(r.esEsteril());
        assertTrue(r.esPoligamo());
    }


}