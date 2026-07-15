package modelo;

import java.io.Serializable;

public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String descripcion;
    private int nivelComplejidad; // 1 = Básico, >1 = Complejo
    private Especialista especialistaAsignado;
    private String estado; // "Abierto", "En Progreso", "Cerrado"

    public Ticket(String id, String descripcion, int nivelComplejidad) {
        this.id = id;
        this.descripcion = descripcion;
        this.nivelComplejidad = nivelComplejidad;
        this.estado = "Abierto";
    }

    public String getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public int getNivelComplejidad() { return nivelComplejidad; }
    public Especialista getEspecialistaAsignado() { return especialistaAsignado; }
    
    public void setEspecialistaAsignado(Especialista especialistaAsignado) { 
        this.especialistaAsignado = especialistaAsignado; 
    }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        String asig = (especialistaAsignado != null) ? especialistaAsignado.getNombre() : "Sin asignar";
        return "Ticket #" + id + " - Nivel req: " + nivelComplejidad + " [" + estado + "] -> Asignado a: " + asig;
    }
}
