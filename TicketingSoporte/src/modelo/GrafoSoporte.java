package modelo;

import java.io.Serializable;
import java.util.*;

public class GrafoSoporte implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Lista de Adyacencia: Vértice -> Lista de Arcos
    private Map<Especialista, List<Conexion>> adyacencia;

    public GrafoSoporte() {
        adyacencia = new HashMap<>();
    }

    public void agregarEspecialista(Especialista e) {
        adyacencia.putIfAbsent(e, new ArrayList<>());
    }

    public void eliminarEspecialista(Especialista e) {
        adyacencia.remove(e);
        for (List<Conexion> conexiones : adyacencia.values()) {
            conexiones.removeIf(c -> c.getDestino().equals(e));
        }
    }

    // Grafo dirigido: La arista va de origen a destino
    public void agregarRutaEscalamiento(Especialista origen, Especialista destino, int costo) {
        agregarEspecialista(origen);
        agregarEspecialista(destino);
        adyacencia.get(origen).add(new Conexion(destino, costo));
    }

    public Map<Especialista, List<Conexion>> getAdyacencia() {
        return adyacencia;
    }
    
    public List<Especialista> obtenerEspecialistas() {
        return new ArrayList<>(adyacencia.keySet());
    }

    /**
     * Implementación del Algoritmo de Dijkstra para encontrar la ruta más barata
     * hacia un especialista que tenga el nivel de complejidad requerido para el ticket.
     */
    public List<Especialista> encontrarMejorRutaEscalamiento(Especialista origen, int complejidadRequerida) {
        Map<Especialista, Integer> distancias = new HashMap<>();
        Map<Especialista, Especialista> predecesores = new HashMap<>();
        
        // Inicializamos las distancias al infinito
        for (Especialista e : adyacencia.keySet()) {
            distancias.put(e, Integer.MAX_VALUE);
        }
        
        // La distancia al origen es 0 si el origen existe
        if (!adyacencia.containsKey(origen)) return null;
        distancias.put(origen, 0);
        
        // Cola de prioridad para seleccionar siempre el vértice con menor distancia
        PriorityQueue<Especialista> pq = new PriorityQueue<>(Comparator.comparingInt(distancias::get));
        pq.add(origen);

        List<Especialista> posiblesDestinos = new ArrayList<>();

        while (!pq.isEmpty()) {
            Especialista actual = pq.poll();

            // Si este especialista tiene el nivel necesario, lo guardamos como posible destino final
            if (actual.getNivel() >= complejidadRequerida) {
                posiblesDestinos.add(actual);
            }

            for (Conexion conexion : adyacencia.getOrDefault(actual, Collections.emptyList())) {
                Especialista vecino = conexion.getDestino();
                int nuevoCosto = distancias.get(actual) + conexion.getCostoEscalamiento();

                if (nuevoCosto < distancias.get(vecino)) {
                    distancias.put(vecino, nuevoCosto);
                    predecesores.put(vecino, actual);
                    // Actualizar en cola de prioridad
                    pq.remove(vecino);
                    pq.add(vecino);
                }
            }
        }

        // Si no encontramos a nadie calificado
        if (posiblesDestinos.isEmpty()) {
            return null; 
        }

        // Seleccionamos el destino calificado con menor distancia desde el origen
        Especialista mejorDestino = posiblesDestinos.stream()
                .min(Comparator.comparingInt(distancias::get))
                .orElse(null);

        if (mejorDestino == null || distancias.get(mejorDestino) == Integer.MAX_VALUE) {
            return null;
        }

        // Reconstruimos la ruta desde el destino hacia atrás usando los predecesores
        List<Especialista> ruta = new ArrayList<>();
        Especialista paso = mejorDestino;
        while (paso != null) {
            ruta.add(paso);
            paso = predecesores.get(paso);
        }
        Collections.reverse(ruta); // Para que vaya de origen a destino
        return ruta;
    }
}
