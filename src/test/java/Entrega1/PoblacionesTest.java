package Entrega1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PoblacionesTest {
    @Test
    public void testConstructor() {
        Poblaciones p = new Poblaciones("Poblacion A", "Elena", 120);

        assertEquals("Poblacion A", p.getNombre());
        assertEquals("Elena", p.getResponsable());
        assertEquals(120, p.getDiasPermanencia());
    }

    @Test
    public void testNombreVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Poblaciones("", "Elena", 120);
        });
    }

    @Test
    public void testNombreNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Poblaciones(null, "Elena", 120);
        });
    }

    @Test
    public void testResponsableVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Poblaciones("Poblacion A", "", 120);
        });
    }

    @Test
    public void testDiasInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Poblaciones("Poblacion A", "Elena", 300);
        });
    }

    @Test
    public void testLimitesDias() {
        Poblaciones p1 = new Poblaciones("A", "Elena", 1);
        Poblaciones p2 = new Poblaciones("B", "Ana", 269);

        assertEquals(1, p1.getDiasPermanencia());
        assertEquals(269, p2.getDiasPermanencia());
    }

    @Test
    public void testModificarNombre() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);

        p.setNombre("B");

        assertEquals("B", p.getNombre());
    }

    @Test
    public void testModificarDiasInvalidos() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);

        assertThrows(IllegalArgumentException.class, () -> {
            p.setDiasPermanencia(0);
        });

        assertEquals(120, p.getDiasPermanencia());
    }

    @Test
    public void testPoblacionInicialmenteVacia() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);

        assertTrue(p.getRatones().isEmpty());
    }

    @Test
    public void testAgregarRaton() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);
        Ratones r = crearRaton("R001");

        p.agregarRaton(r);

        assertEquals(1, p.getRatones().size());
        assertSame(r, p.buscarRaton("R001"));
    }

    @Test
    public void testAgregarVariosRatones() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);

        p.agregarRaton(crearRaton("R001"));
        p.agregarRaton(crearRaton("R002"));
        p.agregarRaton(crearRaton("R003"));

        assertEquals(3, p.getRatones().size());
    }

    @Test
    public void testAgregarRatonNull() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);

        assertThrows(IllegalArgumentException.class, () -> {
            p.agregarRaton(null);
        });

        assertTrue(p.getRatones().isEmpty());
    }

    @Test
    public void testCodigoRepetido() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);

        p.agregarRaton(crearRaton("R001"));

        assertThrows(IllegalArgumentException.class, () -> {
            p.agregarRaton(crearRaton("R001"));
        });

        assertEquals(1, p.getRatones().size());
    }

    @Test
    public void testMismoCodigoEnDistintasPoblaciones() {
        Poblaciones p1 = new Poblaciones("A", "Elena", 120);
        Poblaciones p2 = new Poblaciones("B", "Ana", 120);

        p1.agregarRaton(crearRaton("R001"));
        p2.agregarRaton(crearRaton("R001"));

        assertEquals(1, p1.getRatones().size());
        assertEquals(1, p2.getRatones().size());
    }

    @Test
    public void testBuscarRatonExistente() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);
        Ratones r = crearRaton("R001");

        p.agregarRaton(r);

        assertSame(r, p.buscarRaton("R001"));
    }

    @Test
    public void testBuscarRatonInexistente() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);

        assertNull(p.buscarRaton("R999"));
    }

    @Test
    public void testEliminarRatonExistente() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);

        p.agregarRaton(crearRaton("R001"));

        assertTrue(p.eliminarRaton("R001"));
        assertEquals(0, p.getRatones().size());
        assertNull(p.buscarRaton("R001"));
    }

    @Test
    public void testEliminarRatonInexistente() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);

        assertFalse(p.eliminarRaton("R999"));
    }

    @Test
    public void testEliminarSoloRatonSeleccionado() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);

        p.agregarRaton(crearRaton("R001"));
        p.agregarRaton(crearRaton("R002"));

        assertTrue(p.eliminarRaton("R001"));

        assertNull(p.buscarRaton("R001"));
        assertNotNull(p.buscarRaton("R002"));
        assertEquals(1, p.getRatones().size());
    }

    @Test
    public void testGetRatonesDevuelveCopia() {
        Poblaciones p = new Poblaciones("A", "Elena", 120);

        p.agregarRaton(crearRaton("R001"));

        p.getRatones().clear();

        assertEquals(1, p.getRatones().size());
        assertNotNull(p.buscarRaton("R001"));
    }

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
                java.time.LocalDate.of(2025, 5, 20),
                30,
                Ratones.Sexo.MACHO,
                37.5,
                "",
                cromosomas
        );
    }


}