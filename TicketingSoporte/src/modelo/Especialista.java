package modelo;

import java.io.Serializable;
import java.util.Objects;

public class Especialista implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String nombre;
    private String especialidad;
    private int nivel; // 1 = Soporte Nivel 1, >1 = Especialistas

    public Especialista(String id, String nombre, String especialidad, int nivel) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.nivel = nivel;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public int getNivel() {
        return nivel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Especialista that = (Especialista) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nombre + " (Nivel " + nivel + " - " + especialidad + ")";
    }
}
