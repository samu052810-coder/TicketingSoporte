package controlador;

import modelo.*;
import vista.VentanaPrincipal;
import javax.swing.*;
import java.util.List;

public class ControladorPrincipal {
    private VentanaPrincipal vista;
    private SistemaTicketing modelo;

    public ControladorPrincipal(VentanaPrincipal vista, SistemaTicketing modelo) {
        this.vista = vista;
        this.modelo = modelo;
        inicializarEventos();
        actualizarVistaGrafo();
    }

    private void inicializarEventos() {
        // Evento Agregar Especialista
        vista.btnAgregarEspecialista.addActionListener(e -> {
            try {
                String id = vista.txtIdEspec.getText();
                String nombre = vista.txtNombreEspec.getText();
                String especialidad = vista.txtEspEspec.getText();
                int nivel = Integer.parseInt(vista.txtNivelEspec.getText());

                if (id.isEmpty() || nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "ID y Nombre son obligatorios");
                    return;
                }

                Especialista espec = new Especialista(id, nombre, especialidad, nivel);
                modelo.getGrafo().agregarEspecialista(espec);
                
                vista.modeloListaEspecialistas.addElement(espec);
                vista.actualizarComboEspecialistas();
                actualizarVistaGrafo();
                
                vista.txtIdEspec.setText("");
                vista.txtNombreEspec.setText("");
                vista.txtEspEspec.setText("");
                vista.txtNivelEspec.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Nivel debe ser numérico");
            }
        });

        // Evento Agregar Ruta
        vista.btnAgregarRuta.addActionListener(e -> {
            Especialista origen = (Especialista) vista.cbOrigen.getSelectedItem();
            Especialista destino = (Especialista) vista.cbDestino.getSelectedItem();
            
            if (origen == null || destino == null) {
                JOptionPane.showMessageDialog(vista, "Seleccione origen y destino");
                return;
            }
            if (origen.equals(destino)) {
                JOptionPane.showMessageDialog(vista, "El origen y destino no pueden ser el mismo");
                return;
            }

            try {
                int costo = Integer.parseInt(vista.txtCosto.getText());
                modelo.getGrafo().agregarRutaEscalamiento(origen, destino, costo);
                actualizarVistaGrafo();
                vista.txtCosto.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Costo debe ser numérico");
            }
        });

        // Evento Crear y Escalar Ticket
        vista.btnCrearYEscalarTicket.addActionListener(e -> {
            try {
                String id = vista.txtIdTicket.getText();
                String desc = vista.txtDescTicket.getText();
                int complejidad = Integer.parseInt(vista.txtComplejidadTicket.getText());
                Especialista origen = (Especialista) vista.cbEspecialistaInicial.getSelectedItem();

                if (id.isEmpty() || origen == null) {
                    JOptionPane.showMessageDialog(vista, "ID y Registrador son obligatorios");
                    return;
                }

                Ticket ticket = new Ticket(id, desc, complejidad);
                modelo.agregarTicket(ticket);

                List<Especialista> ruta = modelo.escalarTicket(ticket, origen);

                StringBuilder sb = new StringBuilder();
                sb.append("=== REPORTE DEL SISTEMA ===\n");
                sb.append("ESTADO: ").append(ticket.toString()).append("\n\n");
                
                if (ruta == null) {
                    sb.append("[!] ERROR CRÍTICO: No existe protocolo de transferencia para escalar a nivel ").append(complejidad);
                } else {
                    sb.append(">> INICIANDO PROTOCOLO DE TRANSFERENCIA DE TICKETS <<\n");
                    int tiempoTotal = 0;
                    for (int i = 0; i < ruta.size(); i++) {
                        sb.append("   - Paso ").append(i + 1).append(": ").append(ruta.get(i).getNombre());
                        if (i == 0) sb.append(" (Recepción Inicial)");
                        sb.append("\n");
                        
                        if (i < ruta.size() - 1) {
                            Especialista actual = ruta.get(i);
                            Especialista sig = ruta.get(i+1);
                            int costo = 0;
                            for (Conexion c : modelo.getGrafo().getAdyacencia().get(actual)) {
                                if (c.getDestino().equals(sig)) { costo = c.getCostoEscalamiento(); break; }
                            }
                            tiempoTotal += costo;
                            sb.append("       | Transferencia (Tiempo est: ").append(costo).append(" min)\n       V\n");
                        }
                    }
                    sb.append("\n[*] RESOLUCIÓN ESTIMADA EN: ").append(tiempoTotal).append(" minutos.");
                }
                vista.txtAreaResultadoTicket.setText(sb.toString());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Complejidad debe ser numérica");
            }
        });

        // Evento Eliminar Especialista
        vista.btnEliminarEspecialista.addActionListener(e -> {
            Especialista seleccionado = vista.listaEspecialistas.getSelectedValue();
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(vista, "Seleccione un especialista de la lista para eliminar");
                return;
            }
            modelo.getGrafo().eliminarEspecialista(seleccionado);
            vista.modeloListaEspecialistas.removeElement(seleccionado);
            vista.actualizarComboEspecialistas();
            actualizarVistaGrafo();
            JOptionPane.showMessageDialog(vista, "Especialista eliminado correctamente.");
        });

        // Eventos de Persistencia
        vista.btnGuardar.addActionListener(e -> {
            try {
                modelo.guardarDatos("ticketing_datos.dat");
                JOptionPane.showMessageDialog(vista, "Datos guardados correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al guardar: " + ex.getMessage());
            }
        });

        vista.btnCargar.addActionListener(e -> {
            try {
                SistemaTicketing cargado = SistemaTicketing.cargarDatos("ticketing_datos.dat");
                this.modelo = cargado;
                actualizarVistaCompletaDesdeModelo();
                JOptionPane.showMessageDialog(vista, "Datos cargados correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al cargar: " + ex.getMessage());
            }
        });
    }

    private void actualizarVistaGrafo() {
        StringBuilder sb = new StringBuilder();
        var adyacencia = modelo.getGrafo().getAdyacencia();
        for (Especialista e : adyacencia.keySet()) {
            sb.append(e.getNombre()).append(" -> ");
            List<Conexion> conexiones = adyacencia.get(e);
            if (conexiones.isEmpty()) {
                sb.append(" Ninguno");
            } else {
                for (Conexion c : conexiones) {
                    sb.append("[").append(c.getDestino().getNombre()).append(", Costo:").append(c.getCostoEscalamiento()).append("] ");
                }
            }
            sb.append("\n");
        }
        vista.txtAreaRutas.setText(sb.toString());
    }

    private void actualizarVistaCompletaDesdeModelo() {
        vista.modeloListaEspecialistas.clear();
        for (Especialista e : modelo.getGrafo().obtenerEspecialistas()) {
            vista.modeloListaEspecialistas.addElement(e);
        }
        
        vista.actualizarComboEspecialistas();
        actualizarVistaGrafo();
        vista.txtAreaResultadoTicket.setText("");
    }
}
