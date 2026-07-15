package vista;

import modelo.Especialista;
import modelo.Ticket;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    // --- Componentes Especialistas ---
    public JTextField txtIdEspec = new JTextField(10);
    public JTextField txtNombreEspec = new JTextField(15);
    public JTextField txtEspEspec = new JTextField(15);
    public JTextField txtNivelEspec = new JTextField(5);
    public JButton btnAgregarEspecialista = new JButton("Agregar Especialista");
    public JButton btnEliminarEspecialista = new JButton("Eliminar Especialista");
    public DefaultListModel<Especialista> modeloListaEspecialistas = new DefaultListModel<>();
    public JList<Especialista> listaEspecialistas = new JList<>(modeloListaEspecialistas);

    // --- Componentes Conexiones (Aristas) ---
    public JComboBox<Especialista> cbOrigen = new JComboBox<>();
    public JComboBox<Especialista> cbDestino = new JComboBox<>();
    public JTextField txtCosto = new JTextField(5);
    public JButton btnAgregarRuta = new JButton("Agregar Ruta de Escalamiento");
    public JTextArea txtAreaRutas = new JTextArea();

    // --- Componentes Tickets ---
    public JTextField txtIdTicket = new JTextField(10);
    public JTextField txtDescTicket = new JTextField(20);
    public JTextField txtComplejidadTicket = new JTextField(5);
    public JComboBox<Especialista> cbEspecialistaInicial = new JComboBox<>();
    public JButton btnCrearYEscalarTicket = new JButton("Crear y Escalar Ticket");
    public JTextArea txtAreaResultadoTicket = new JTextArea();

    // --- Persistencia ---
    public JButton btnGuardar = new JButton("Guardar Datos");
    public JButton btnCargar = new JButton("Cargar Datos");

    public VentanaPrincipal() {
        setTitle("Gestor de Equipos de Soporte Técnico");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Directorio de Empleados", crearPanelEspecialistas());
        tabbedPane.addTab("Protocolos de Transferencia", crearPanelRutas());
        tabbedPane.addTab("Recepción de Tickets", crearPanelTickets());

        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSur.add(btnGuardar);
        pnlSur.add(btnCargar);

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(pnlSur, BorderLayout.SOUTH);
    }

    private JPanel crearPanelEspecialistas() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(new TitledBorder("Registrar Empleado"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx = 0; gbc.gridy = 0; pnlForm.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; pnlForm.add(txtIdEspec, gbc);
        gbc.gridx = 0; gbc.gridy = 1; pnlForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; pnlForm.add(txtNombreEspec, gbc);
        gbc.gridx = 0; gbc.gridy = 2; pnlForm.add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1; pnlForm.add(txtEspEspec, gbc);
        gbc.gridx = 0; gbc.gridy = 3; pnlForm.add(new JLabel("Nivel (1,2,3...):"), gbc);
        gbc.gridx = 1; pnlForm.add(txtNivelEspec, gbc);
        gbc.gridx = 1; gbc.gridy = 4; pnlForm.add(btnAgregarEspecialista, gbc);

        panel.add(pnlForm, BorderLayout.WEST);
        
        JScrollPane scrollLista = new JScrollPane(listaEspecialistas);
        scrollLista.setBorder(new TitledBorder("Directorio Activo"));
        
        JPanel pnlLista = new JPanel(new BorderLayout());
        pnlLista.add(scrollLista, BorderLayout.CENTER);
        pnlLista.add(btnEliminarEspecialista, BorderLayout.SOUTH);
        
        panel.add(pnlLista, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel crearPanelRutas() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel pnlForm = new JPanel(new FlowLayout());
        pnlForm.setBorder(new TitledBorder("Definir Protocolo"));
        pnlForm.add(new JLabel("Origen:"));
        pnlForm.add(cbOrigen);
        pnlForm.add(new JLabel("Destino:"));
        pnlForm.add(cbDestino);
        pnlForm.add(new JLabel("Tiempo de Transf. (min):"));
        pnlForm.add(txtCosto);
        pnlForm.add(btnAgregarRuta);

        panel.add(pnlForm, BorderLayout.NORTH);

        txtAreaRutas.setEditable(false);
        JScrollPane scrollArea = new JScrollPane(txtAreaRutas);
        scrollArea.setBorder(new TitledBorder("Mapa Organizacional de Soporte"));
        panel.add(scrollArea, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelTickets() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(new TitledBorder("Nuevo Ticket"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0; pnlForm.add(new JLabel("ID Ticket:"), gbc);
        gbc.gridx = 1; pnlForm.add(txtIdTicket, gbc);
        gbc.gridx = 0; gbc.gridy = 1; pnlForm.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; pnlForm.add(txtDescTicket, gbc);
        gbc.gridx = 0; gbc.gridy = 2; pnlForm.add(new JLabel("Nivel Complejidad Req:"), gbc);
        gbc.gridx = 1; pnlForm.add(txtComplejidadTicket, gbc);
        gbc.gridx = 0; gbc.gridy = 3; pnlForm.add(new JLabel("Registrado por (Origen):"), gbc);
        gbc.gridx = 1; pnlForm.add(cbEspecialistaInicial, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4; pnlForm.add(btnCrearYEscalarTicket, gbc);

        panel.add(pnlForm, BorderLayout.NORTH);

        txtAreaResultadoTicket.setEditable(false);
        txtAreaResultadoTicket.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollArea = new JScrollPane(txtAreaResultadoTicket);
        scrollArea.setBorder(new TitledBorder("Protocolo de Resolución del Ticket"));
        panel.add(scrollArea, BorderLayout.CENTER);

        return panel;
    }

    // Método de utilidad para actualizar ComboBoxes
    public void actualizarComboEspecialistas() {
        cbOrigen.removeAllItems();
        cbDestino.removeAllItems();
        cbEspecialistaInicial.removeAllItems();
        for (int i = 0; i < modeloListaEspecialistas.size(); i++) {
            Especialista e = modeloListaEspecialistas.get(i);
            cbOrigen.addItem(e);
            cbDestino.addItem(e);
            cbEspecialistaInicial.addItem(e);
        }
    }
}
