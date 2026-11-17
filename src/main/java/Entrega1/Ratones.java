package Entrega1;

import java.time.LocalDate;

public class Ratones {
    public enum Sexo{
        MACHO,
        HEMBRA
    }
    private String codigoReferencia;
    private LocalDate fechaNacimiento;
    private int pesoGramos;
    private Sexo sexo;
    private double temperaturaCorporal;
    private String observaciones;
    private CromosomasSexualesYMutaciones mutacionesCromosomicas;

    /**
     * @param codigoReferencia No puede ser un texto vacio
     * @param fechaNacimiento lo generamos con LocalDate
     * @param pesoGramos un numero entero positivo
     * @param sexo solo puede ser Macho o hembra
     * @param temperaturaCorporal valor entre 30.0 y 45.0ºC
     * @param observaciones puede ser vacio
     * @param mutacionesCromosomicas
     */
    public Ratones(String codigoReferencia, LocalDate fechaNacimiento, int pesoGramos, Sexo sexo,
                   double temperaturaCorporal, String observaciones, CromosomasSexualesYMutaciones mutacionesCromosomicas ){
        setCodigoReferencia(codigoReferencia);
        setFechaNacimiento(fechaNacimiento);
        setPeso(pesoGramos);
        setSexo(sexo);
        setTemperaturaCorporal(temperaturaCorporal);
        setObservaciones(observaciones);
        setMutacionesCromosomicas(mutacionesCromosomicas);
    }
    public String getCodigoReferencia(){
        return codigoReferencia;
    }
    public void setCodigoReferencia(String codigoReferencia){
        if(codigoReferencia==null||codigoReferencia.trim().isEmpty()){
            throw new IllegalArgumentException("El código no puede estar vacío");
        }
        this.codigoReferencia=codigoReferencia;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if(fechaNacimiento==null){
            throw new IllegalArgumentException("La fecha es invalida");
        }
        this.fechaNacimiento=fechaNacimiento;
    }

    public int getPesoGramos() {
        return pesoGramos;
    }

    public void setPeso(int pesoGramos) {
        if(pesoGramos<=0){
            throw new IllegalArgumentException("Peso invalido");
        }
        this.pesoGramos=pesoGramos;
    }
    public Sexo getSexo(){
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        if (sexo == null) {
            throw new IllegalArgumentException("El sexo debe ser MACHO o HEMBRA");
        }
        if (mutacionesCromosomicas != null && mutacionesCromosomicas.getSexo() != sexo) {
            throw new IllegalArgumentException("El sexo no coincide con los cromosomas");
        }
        this.sexo = sexo;
    }

    public double getTemperaturaCorporal() {
        return temperaturaCorporal;
    }

    public void setTemperaturaCorporal(double temperaturaCorporal) {
        if(!Double.isFinite(temperaturaCorporal)||temperaturaCorporal<30.0||temperaturaCorporal>45.0){
            throw new IllegalArgumentException("Temperatura incorrecta");
        }
        this.temperaturaCorporal=temperaturaCorporal;
    }
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public CromosomasSexualesYMutaciones getMutacionesCromosomicas() {
        return mutacionesCromosomicas;
    }
    public void setMutacionesCromosomicas(CromosomasSexualesYMutaciones mutaciones) {
        if (mutaciones == null) {
            throw new IllegalArgumentException("Faltan las mutaciones cromosómicas");
        }
        this.mutacionesCromosomicas = mutaciones;
    }
    /**
     * @return la infromacion que tenemos en la clase cromosomas y mutaciones
     * De esta forma, solo tenemos que escribir:
     * ratones.esEsteril()
     */
    public boolean esEsteril() {
        return mutacionesCromosomicas.esEsteril();
    }

    /**
     * @return a infromacion que tenemos en la clase cromosomas y mutaciones
     * De esta forma, solo tenemos que escribir:
     * ratones.esPoligamo()
     */
    public boolean esPoligamo() {
        return mutacionesCromosomicas.esPoligamo();
    }
}
