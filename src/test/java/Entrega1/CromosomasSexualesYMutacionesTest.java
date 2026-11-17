package Entrega1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CromosomasSexualesYMutacionesTest {
    @Test
    public void testMachoFertil() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.MACHO,
                        false, false, false);

        assertFalse(c.esEsteril());
    }

    @Test
    public void testMachoEsteril() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.MACHO,
                        true, false, false);

        assertTrue(c.esEsteril());
    }

    @Test
    public void testHembraFertil() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.HEMBRA,
                        false, false, false);

        assertFalse(c.esEsteril());
    }

    @Test
    public void testHembraConUnXMutado() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.HEMBRA,
                        true, false, false);

        assertFalse(c.esEsteril());
    }

    @Test
    public void testHembraConX2Mutado() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.HEMBRA,
                        false, true, false);

        assertFalse(c.esEsteril());
    }

    @Test
    public void testHembraEsteril() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.HEMBRA,
                        true, true, false);

        assertTrue(c.esEsteril());
    }

    @Test
    public void testMachoPoligamo() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.MACHO,
                        false, false, true);

        assertTrue(c.esPoligamo());
    }

    @Test
    public void testMachoNoPoligamo() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.MACHO,
                        false, false, false);

        assertFalse(c.esPoligamo());
    }

    @Test
    public void testHembraNoPoligama() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.HEMBRA,
                        true, true, false);

        assertFalse(c.esPoligamo());
    }

    @Test
    public void testMachoConDosX() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CromosomasSexualesYMutaciones(
                    Ratones.Sexo.MACHO,
                    false, true, false);
        });
    }

    @Test
    public void testHembraConY() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CromosomasSexualesYMutaciones(
                    Ratones.Sexo.HEMBRA,
                    false, false, true);
        });
    }

    @Test
    public void testSexoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CromosomasSexualesYMutaciones(
                    null, false, false, false);
        });
    }

    @Test
    public void testGetSexo() {
        CromosomasSexualesYMutaciones c =
                new CromosomasSexualesYMutaciones(
                        Ratones.Sexo.MACHO,
                        false, false, false);

        assertEquals(Ratones.Sexo.MACHO, c.getSexo());
    }


}