package modelo;

import java.io.Serializable;

public class Conexion implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Especialista destino;
    private int costoEscalamiento; // Representa el tiempo o costo de transferir a este especialista

    public Conexion(Especialista destino, int costoEscalamiento) {
        this.destino = destino;
        this.costoEscalamiento = costoEscalamiento;
    }

    public Especialista getDestino() {
        return destino;
    }

    public int getCostoEscalamiento() {
        return costoEscalamiento;
    }
}
