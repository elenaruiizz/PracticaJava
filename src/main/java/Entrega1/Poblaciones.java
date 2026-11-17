package Entrega1;

import java.util.ArrayList;

public class Poblaciones {
    private String nombre;
    private String responsable;
    private int diasPermanencia;
    private ArrayList<Ratones> ratones;

    /**
     * @param nombre de la poblacion
     * @param responsable Persona Responsable
     * @param diasPermanencia Dias entre 1 y 269
     * @throws IllegalArgumentException si los datos son inválidos
     */
    public Poblaciones(String nombre, String responsable, int diasPermanencia){
        setNombre(nombre);
        setResponsable(responsable);
        setDiasPermanencia(diasPermanencia);
        this.ratones=new ArrayList<>();

    }

    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre
     * Si no se cumple la excepcion el nombre introducido va a ser el nombre de nuestro atributo
     */
    public void setNombre(String nombre) {
        if(nombre==null||nombre.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre no puede estar valido");
        }
        this.nombre=nombre;
    }

    public String getResponsable(){
        return responsable;
    }

    /**
     * @param responsable
     * Si no se cumple la excepcion el nombre introducido va a ser el nombre de nuestro atributo
     */
    public void setResponsable(String responsable) {
        if(responsable==null||responsable.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre no puede estar valido");
        }
        this.responsable=responsable;
    }

    public int getDiasPermanencia() {
        return diasPermanencia;
    }

    /**
     * @param diasPermanencia
     * Si no se cumple la excepcion el nombre introducido va a ser el nombre de nuestro atributo
     */
    public void setDiasPermanencia(int diasPermanencia) {
        if(diasPermanencia<1||diasPermanencia>269){
            throw new IllegalArgumentException("No se cumplen los criterios de permanencia");
        }
        this.diasPermanencia=diasPermanencia;
    }

    /**
     * @return los ratones que contiene la poblacion
     */
    public ArrayList<Ratones> getRatones() {
        return new ArrayList<>(ratones);
    }

    /**
     * @param codigo
     * @return localizamos un raton a partir de su codigo de referencia
     */
    public Ratones buscarRaton(String codigo) {
        for (Ratones r : ratones) {
            if (r.getCodigoReferencia().equals(codigo)) {
                return r;
            }
        }
        return null;
    }

    /**
     * @param codigo
     * @return a partir del codigo, podemos eliminar un raton
     */
    public boolean eliminarRaton(String codigo) {
        Ratones raton = buscarRaton(codigo);

        if (raton == null) {
            return false;
        }

        ratones.remove(raton);
        return true;
    }
    /**
     * @param raton por el cual con su codigo de referencia vamos a comprobar
     *              que no coincida con ningun raton existene.
     *              Si su codigo de referencia coincide con el codigo de referencia con algun otro raton,
     *              lanzamos una excepcion
     */
    public void agregarRaton(Ratones raton) {

        if (raton == null) {
            throw new IllegalArgumentException("El ratón no puede ser null");
        }

        for (Ratones r : ratones) {
            if (r.getCodigoReferencia().equals(raton.getCodigoReferencia())) {
                throw new IllegalArgumentException("Código repetido");
            }
        }

        ratones.add(raton);
    }

    public void modificarRaton(String codigoAnterior, Ratones nuevo) {
        if (nuevo == null) {
            throw new IllegalArgumentException("Ratón nulo");
        }

        Ratones anterior = buscarRaton(codigoAnterior);

        if (anterior == null) {
            throw new IllegalArgumentException("El ratón no existe");
        }

        Ratones repetido = buscarRaton(nuevo.getCodigoReferencia());

        if (repetido != null && repetido != anterior) {
            throw new IllegalArgumentException("Código repetido");
        }

        int posicion = ratones.indexOf(anterior);
        ratones.set(posicion, nuevo);
    }

}