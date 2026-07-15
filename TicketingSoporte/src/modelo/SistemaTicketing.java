package modelo;

import utilidades.GestorPersistencia;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SistemaTicketing implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private GrafoSoporte grafo;
    private List<Ticket> tickets;

    public SistemaTicketing() {
        this.grafo = new GrafoSoporte();
        this.tickets = new ArrayList<>();
    }

    public GrafoSoporte getGrafo() {
        return grafo;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void agregarTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void eliminarTicket(Ticket ticket) {
        tickets.remove(ticket);
    }

    /**
     * Intenta escalar un ticket.
     * @param ticket El ticket a escalar
     * @param origen Desde qué especialista se está escalando (puede ser el que creó el ticket)
     * @return La ruta sugerida o null si no hay especialista capaz conectado
     */
    public List<Especialista> escalarTicket(Ticket ticket, Especialista origen) {
        List<Especialista> ruta = grafo.encontrarMejorRutaEscalamiento(origen, ticket.getNivelComplejidad());
        if (ruta != null && !ruta.isEmpty()) {
            Especialista destinoFinal = ruta.get(ruta.size() - 1);
            ticket.setEspecialistaAsignado(destinoFinal);
            ticket.setEstado("En Progreso - Escalado");
        }
        return ruta;
    }

    // --- Persistencia del Sistema Completo ---
    public void guardarDatos(String archivo) throws Exception {
        GestorPersistencia.guardarObjeto(this, archivo);
    }

    public static SistemaTicketing cargarDatos(String archivo) throws Exception {
        return (SistemaTicketing) GestorPersistencia.cargarObjeto(archivo);
    }
}
