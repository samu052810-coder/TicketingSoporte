package main;

import controlador.ControladorPrincipal;
import modelo.SistemaTicketing;
import vista.VentanaPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Establecer Look and Feel del sistema para mejor apariencia
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("No se pudo cargar el Look and Feel del sistema.");
        }

        SwingUtilities.invokeLater(() -> {
            SistemaTicketing modelo = new SistemaTicketing();
            VentanaPrincipal vista = new VentanaPrincipal();
            new ControladorPrincipal(vista, modelo);
            
            vista.setVisible(true);
        });
    }
}
