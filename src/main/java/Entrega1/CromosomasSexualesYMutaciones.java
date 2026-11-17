package Entrega1;

public class CromosomasSexualesYMutaciones {
    private final Ratones.Sexo sexo;
    private final boolean mutacionX1;
    private final boolean mutacionX2;
    private final boolean mutacionY;
    public CromosomasSexualesYMutaciones(Ratones.Sexo sexo, boolean mutacionX1, boolean mutacionX2, boolean mutacionY){
        if(sexo ==null){
            throw new IllegalArgumentException("Sexo invalido, debe ser MACHO o HEMBRA");
        }
        if(sexo==Ratones.Sexo.MACHO && mutacionX2){
            throw new IllegalArgumentException("Un macho no puede tener dos cromosomas X");
        }
        if(sexo==Ratones.Sexo.HEMBRA && mutacionY){
            throw new IllegalArgumentException("Una hembra no puede tener cromosoma Y");
        }
        this.sexo=sexo;
        this.mutacionX1=mutacionX1;
        this.mutacionX2=mutacionX2;
        this.mutacionY=mutacionY;
    }

    /**
     * @return Macho: estéril si su único X tiene la mutación.
     * Hembra: solo si sus dos X la tienen
     */
    public boolean esEsteril(){
        if(sexo==Ratones.Sexo.MACHO){
            return mutacionX1;
        }else{
            return mutacionX1&&mutacionX2;
        }
    }

    /**
     * @return La poligamia solo existe en machos
     * con la mutación en el cromosoma Y
     */
    public boolean esPoligamo(){
        return sexo==Ratones.Sexo.MACHO&&mutacionY;
    }
    public Ratones.Sexo getSexo() {
        return sexo;
    }

    public boolean isMutacionX1() {
        return mutacionX1;
    }

    public boolean isMutacionX2() {
        return mutacionX2;
    }

    public boolean isMutacionY() {
        return mutacionY;
    }


}
