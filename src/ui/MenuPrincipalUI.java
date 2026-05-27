package ui;

import model.*;
import service.ExamenService;
import service.UsuarioService;
import repository.SistemaRepository;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


// CLASE PRINCIPAL DE LA INTERFAZ 

public class MenuPrincipalUI extends JFrame {
    
    private Usuario usuarioActual;
    private ExamenService examenService;
    private UsuarioService usuarioService; 
    private SistemaRepository sistemaRepository;
    private JPanel panelCards;
    private CardLayout cardLayout;
    
    private JTable tablaExamenes;
    private DefaultTableModel modelExamenes;
//CONSTRUCTOR   
    public MenuPrincipalUI(Usuario usuario) {
        this.usuarioActual = usuario;
        this.examenService = new ExamenService();
        this.usuarioService = new UsuarioService();
        this.sistemaRepository = new SistemaRepository();
    //CONFIGURAMOS LA VENTANA    
        setTitle("Sistema de Exámenes - " + usuario.getNombre());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //CREAMOS LA INTERFAZ
        initComponents();
        //REGISTRAMOS EL ACCESO EN AUDITORIA (BBDD)
        sistemaRepository.registrarAuditoria(usuario.getId(), "MENU_PRINCIPAL", "Acceso al menú principal");
    }

  // CONSTRUIR LA INTERFAZ  

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Barra superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(0, 102, 204));
        panelSuperior.setPreferredSize(new Dimension(900, 50));
        
        JLabel lblUsuario = new JLabel("  Usuario: " + usuarioActual.getNombre() + " (" + usuarioActual.getRol() + ")");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        panelSuperior.add(lblUsuario, BorderLayout.WEST);
        
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(255, 102, 0));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> logout());
        panelSuperior.add(btnLogout, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Menú lateral
        JPanel panelMenu = new JPanel();
        panelMenu.setBackground(new Color(240, 240, 240));
        panelMenu.setPreferredSize(new Dimension(200, 600));
        panelMenu.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        String[] botones;
        if (usuarioService.esProfesor(usuarioActual)) {
            botones = new String[]{"Inicio", "Crear Examen", "Mis Exámenes", "Administrar Preguntas", "Generar Examen", "Ver Resultados Alumnos"};
        } else {
            botones = new String[]{"Inicio", "Ver Exámenes", "Realizar Examen", "Ver Resultados"};
        }
        
        gbc.gridy = 0;
        for (String texto : botones) {
            JButton btn = new JButton(texto);
            btn.setPreferredSize(new Dimension(180, 40));
            btn.addActionListener(e -> cambiarPanel(texto));
            panelMenu.add(btn, gbc);
            gbc.gridy++;
        }
        
        add(panelMenu, BorderLayout.WEST);
        
        // Panel central
        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);
        
        panelCards.add(crearPanelInicio(), "Inicio");
        
        if (usuarioService.esProfesor(usuarioActual)) {
            panelCards.add(crearPanelCrearExamen(), "Crear Examen");
            panelCards.add(crearPanelMisExamenes(), "Mis Exámenes");
            panelCards.add(crearPanelResultadosAlumnos(), "Ver Resultados Alumnos");
        } else {
            panelCards.add(crearPanelVerExamenes(), "Ver Exámenes");
            panelCards.add(crearPanelRealizarExamen(), "Realizar Examen");
            panelCards.add(crearPanelResultados(), "Ver Resultados");
        }
        
        add(panelCards, BorderLayout.CENTER);
        
        cardLayout.show(panelCards, "Inicio");
    }
// PANTALLAS 
    //CUANDO INICIAS SESION
    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel lblBienvenida = new JLabel("Bienvenido al Sistema de Exámenes");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 0;
        panel.add(lblBienvenida, gbc);
        
        JLabel lblInfo = new JLabel("Selecciona una opción del menú lateral");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        panel.add(lblInfo, gbc);
        
        return panel;
    }
    //PARA CREAR EXAMENES Y PREGUNTAS
    private JPanel crearPanelCrearExamen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField txtTitulo = new JTextField(20);
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"test", "desarrollo", "ambos"});
        
        gbc.gridy = 0;
        gbc.gridx = 0;
        panelForm.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtTitulo, gbc);
        
        gbc.gridy = 1;
        gbc.gridx = 0;
        panelForm.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panelForm.add(cbTipo, gbc);
        
        JButton btnCrear = new JButton("Crear Examen");
        btnCrear.setBackground(new Color(0, 102, 204));
        btnCrear.setForeground(Color.WHITE);
        gbc.gridy = 2;
        gbc.gridx = 1;
        panelForm.add(btnCrear, gbc);
        
        panel.add(panelForm, BorderLayout.NORTH);
        
        JTextArea txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setBackground(new Color(240, 240, 240));
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setPreferredSize(new Dimension(600, 200));
        panel.add(scrollLog, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));
        
        JButton btnAddTest = new JButton("➕ Agregar Pregunta Test");
        JButton btnAddDesarrollo = new JButton("📝 Agregar Pregunta Desarrollo");
        
        
        btnAddTest.addActionListener(e -> {
    agregarPreguntaTestInterfaz(0, txtLog);  // 0 = sin examen, se guarda en biblioteca
});
        
        btnAddDesarrollo.addActionListener(e -> {
        
                    agregarPreguntaDesarrolloInterfaz(0, txtLog);
        });
        
        
        
        panelBotones.add(btnAddTest);
        panelBotones.add(btnAddDesarrollo);
        
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        btnCrear.addActionListener(e -> {
            String titulo = txtTitulo.getText().trim();
            String tipo = cbTipo.getSelectedItem().toString();
            
            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "El título es obligatorio");
                return;
            }
            
            String resultado = examenService.crearExamen(titulo, usuarioActual.getId(), tipo);
            
            if (resultado.equals("SUCCESS")) {
                List<Examen> examenes = examenService.listarExamenesPorProfesor(usuarioActual.getId());
                for (Examen ex : examenes) {
                    if (ex.getTitulo().equals(titulo)) {
                        txtLog.append("Examen creado: " + ex.getTitulo() + " (ID: " + ex.getId() + ")\n");
                        sistemaRepository.registrarAuditoria(usuarioActual.getId(), "CREAR_EXAMEN", 
                            "Creado examen: " + ex.getTitulo());
                        break;
                    }
                }
                txtTitulo.setText("");
            } else {
                JOptionPane.showMessageDialog(panel, resultado, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return panel;
    }
    
    private void agregarPreguntaTestInterfaz(int examenId, JTextArea txtLog) {
    JTextField txtTexto = new JTextField();
    JTextField txtA = new JTextField();
    JTextField txtB = new JTextField();
    JTextField txtC = new JTextField();
    JTextField txtD = new JTextField();
    JComboBox<String> cbCorrecta = new JComboBox<>(new String[]{"A", "B", "C", "D"});
    
    Object[] inputs = {
        "Texto de la pregunta:", txtTexto,
        "Opción A:", txtA,
        "Opción B:", txtB,
        "Opción C:", txtC,
        "Opción D:", txtD,
        "Respuesta correcta:", cbCorrecta
    };
    
    int result = JOptionPane.showConfirmDialog(this, inputs, "Crear Pregunta Test", JOptionPane.OK_CANCEL_OPTION);
    
    if (result == JOptionPane.OK_OPTION) {
        String texto = txtTexto.getText().trim();
        String opA = txtA.getText().trim();
        String opB = txtB.getText().trim();
        String opC = txtC.getText().trim();
        String opD = txtD.getText().trim();
        String correcta = (String) cbCorrecta.getSelectedItem();
        
        if (texto.isEmpty() || opA.isEmpty() || opB.isEmpty() || opC.isEmpty() || opD.isEmpty()) {
            txtLog.append("❌ Error: Todos los campos son obligatorios\n");
            return;
        }
        
        // examenId = 0 significa pregunta independiente (sin examen)
        String resultado = examenService.agregarPreguntaTest(texto, opA, opB, opC, opD, correcta, examenId, usuarioActual.getId());
        
        if (resultado.equals("SUCCESS")) {
            if (examenId == 0) {
                txtLog.append("✅ Pregunta TEST creada en la biblioteca \n");
            } else {
                txtLog.append("✅ Pregunta TEST agregada al examen ID: " + examenId + "\n");
            }
        } else {
            txtLog.append("❌ Error: " + resultado + "\n");
        }
    }
}
    
    private void agregarPreguntaDesarrolloInterfaz(int examenId, JTextArea txtLog) {
    JTextField txtTexto = new JTextField();
    JTextField txtRespuesta = new JTextField();
    JTextField txtPuntuacion = new JTextField("10");
    
    Object[] inputs = {
        "Texto de la pregunta:", txtTexto,
        "Respuesta esperada:", txtRespuesta,
        "Puntuación máxima:", txtPuntuacion
    };
    
    int result = JOptionPane.showConfirmDialog(this, inputs, "Crear Pregunta Desarrollo", JOptionPane.OK_CANCEL_OPTION);
    
    if (result == JOptionPane.OK_OPTION) {
        String texto = txtTexto.getText().trim();
        String respuesta = txtRespuesta.getText().trim();
        int puntuacion;
        try {
            puntuacion = Integer.parseInt(txtPuntuacion.getText().trim());
        } catch (NumberFormatException e) {
            puntuacion = 10;
        }
        
        if (texto.isEmpty() || respuesta.isEmpty()) {
            txtLog.append("❌ Error: Texto y respuesta esperada son obligatorios\n");
            return;
        }
        
        // examenId = 0 significa pregunta independiente (sin examen)
        String resultado = examenService.agregarPreguntaDesarrollo(texto, respuesta, puntuacion, examenId, usuarioActual.getId());
        
        if (resultado.equals("SUCCESS")) {
            if (examenId == 0) {
                txtLog.append("✅ Pregunta DESARROLLO creada en la biblioteca\n");
            } else {
                txtLog.append("✅ Pregunta DESARROLLO agregada al examen ID: " + examenId + "\n");
            }
        } else {
            txtLog.append("❌ Error: " + resultado + "\n");
        }
    }
}
    
 
    // ADMINISTRAR PREGUNTAS

    
    private void administrarPreguntas() {
        List<Pregunta> preguntasTest = examenService.obtenerTodasPreguntasTestDelProfesor(usuarioActual.getId());
        List<PreguntaAbierta> preguntasDesarrollo = examenService.obtenerTodasPreguntasDesarrolloDelProfesor(usuarioActual.getId());
        
        if (preguntasTest.isEmpty() && preguntasDesarrollo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay preguntas en la biblioteca.\nCrea algunas preguntas primero.");
            return;
        }
        
        List<String> opcionesList = new ArrayList<>();
        List<Integer> idsList = new ArrayList<>();
        List<String> tiposList = new ArrayList<>();
        
        for (Pregunta p : preguntasTest) {
            String texto = p.getTexto();
            if (texto.length() > 60) texto = texto.substring(0, 57) + "...";
            opcionesList.add("[TEST] ID: " + p.getId() + " - " + texto);
            idsList.add(p.getId());
            tiposList.add("TEST");
        }
        
        for (PreguntaAbierta p : preguntasDesarrollo) {
            String texto = p.getTexto();
            if (texto.length() > 60) texto = texto.substring(0, 57) + "...";
            opcionesList.add("[DESARROLLO] ID: " + p.getId() + " - " + texto);
            idsList.add(p.getId());
            tiposList.add("DESARROLLO");
        }
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField txtBuscarTexto = new JTextField(20);
        JTextField txtBuscarId = new JTextField(10);
        
        panelBusqueda.add(new JLabel("Buscar por texto (enunciado):"));
        panelBusqueda.add(txtBuscarTexto);
        panelBusqueda.add(new JLabel("Buscar por ID:"));
        panelBusqueda.add(txtBuscarId);
        
        int busquedaOption = JOptionPane.showConfirmDialog(this, panelBusqueda, "Buscar Pregunta", JOptionPane.OK_CANCEL_OPTION);
        if (busquedaOption != JOptionPane.OK_OPTION) return;
        
        String textoBusqueda = txtBuscarTexto.getText().trim().toLowerCase();
        String idBusqueda = txtBuscarId.getText().trim();
        
        List<String> opcionesFiltradas = new ArrayList<>();
        List<Integer> idsFiltrados = new ArrayList<>();
        List<String> tiposFiltrados = new ArrayList<>();
        
        for (int i = 0; i < opcionesList.size(); i++) {
            String opcion = opcionesList.get(i);
            int id = idsList.get(i);
            
            if (!idBusqueda.isEmpty()) {
                if (id == Integer.parseInt(idBusqueda)) {
                    opcionesFiltradas.add(opcion);
                    idsFiltrados.add(id);
                    tiposFiltrados.add(tiposList.get(i));
                    break;
                }
                continue;
            }
            
            if (!textoBusqueda.isEmpty()) {
                if (opcion.toLowerCase().contains(textoBusqueda)) {
                    opcionesFiltradas.add(opcion);
                    idsFiltrados.add(id);
                    tiposFiltrados.add(tiposList.get(i));
                }
            } else {
                opcionesFiltradas.add(opcion);
                idsFiltrados.add(id);
                tiposFiltrados.add(tiposList.get(i));
            }
        }
        
        if (opcionesFiltradas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron preguntas con esos criterios.");
            return;
        }
        
        String[] opcionesArray = opcionesFiltradas.toArray(new String[0]);
        String seleccion = (String) JOptionPane.showInputDialog(this,
            "Selecciona una pregunta:",
            "Administrar Preguntas",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesArray,
            opcionesArray[0]);
        
        if (seleccion == null) return;
        
        int indice = -1;
        for (int i = 0; i < opcionesFiltradas.size(); i++) {
            if (opcionesFiltradas.get(i).equals(seleccion)) {
                indice = i;
                break;
            }
        }
        
        int preguntaId = idsFiltrados.get(indice);
        String tipoPregunta = tiposFiltrados.get(indice);
        
        String[] acciones = {"Ver pregunta completa", "Añadir a un examen", "Borrar pregunta", "Cancelar"};
        int accion = JOptionPane.showOptionDialog(this,
            "¿Qué quieres hacer con la pregunta seleccionada?",
            "Pregunta " + tipoPregunta + " - ID: " + preguntaId,
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            acciones,
            acciones[3]);
        
        if (accion == 0) {
            // VER PREGUNTA COMPLETA
            if (tipoPregunta.equals("TEST")) {
                for (Pregunta p : preguntasTest) {
                    if (p.getId() == preguntaId) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("=== DETALLES DE LA PREGUNTA ===\n\n");
                        sb.append("ID: ").append(p.getId()).append("\n");
                        sb.append("Tipo: TEST\n\n");
                        sb.append("Texto: ").append(p.getTexto()).append("\n\n");
                        sb.append("Opciones:\n");
                        sb.append("  A) ").append(p.getOpcionA()).append("\n");
                        sb.append("  B) ").append(p.getOpcionB()).append("\n");
                        sb.append("  C) ").append(p.getOpcionC()).append("\n");
                        sb.append("  D) ").append(p.getOpcionD()).append("\n\n");
                        sb.append("Respuesta correcta: ").append(p.getRespuestaCorrecta()).append("\n");
                        
                        JTextArea textArea = new JTextArea(sb.toString());
                        textArea.setEditable(false);
                        JScrollPane scroll = new JScrollPane(textArea);
                        scroll.setPreferredSize(new Dimension(500, 400));
                        JOptionPane.showMessageDialog(this, scroll, "Pregunta Completa", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } else {
                for (PreguntaAbierta p : preguntasDesarrollo) {
                    if (p.getId() == preguntaId) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("=== DETALLES DE LA PREGUNTA ===\n\n");
                        sb.append("ID: ").append(p.getId()).append("\n");
                        sb.append("Tipo: DESARROLLO\n\n");
                        sb.append("Texto: ").append(p.getTexto()).append("\n\n");
                        sb.append("Respuesta esperada: ").append(p.getRespuestaEsperada()).append("\n");
                        sb.append("Puntuación máxima: ").append(p.getPuntuacionMaxima()).append("\n");
                        
                        JTextArea textArea = new JTextArea(sb.toString());
                        textArea.setEditable(false);
                        JScrollPane scroll = new JScrollPane(textArea);
                        scroll.setPreferredSize(new Dimension(500, 400));
                        JOptionPane.showMessageDialog(this, scroll, "Pregunta Completa", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            }
            
            int añadir = JOptionPane.showConfirmDialog(this, "¿Quieres añadir esta pregunta a un examen?", "Añadir", JOptionPane.YES_NO_OPTION);
            if (añadir == JOptionPane.YES_OPTION) {
                accion = 1;
            } else {
                return;
            }
        }
        
        if (accion == 1) {
            // AÑADIR A EXAMEN
            String examenIdStr = JOptionPane.showInputDialog(this, "ID del examen destino (ver en 'Mis Exámenes'):");
            if (examenIdStr == null || examenIdStr.trim().isEmpty()) return;
            
            try {
                int examenDestinoId = Integer.parseInt(examenIdStr);
                Examen examenDestino = examenService.buscarExamenPorId(examenDestinoId);
                
                if (examenDestino == null) {
                    JOptionPane.showMessageDialog(this, "No existe el examen con ID: " + examenDestinoId);
                    return;
                }
                
                String resultado = "";
                if (tipoPregunta.equals("TEST")) {
                    resultado = examenService.copiarPreguntaTest(preguntaId, examenDestinoId, usuarioActual.getId());
                } else {
                    resultado = examenService.copiarPreguntaDesarrollo(preguntaId, examenDestinoId, usuarioActual.getId());
                }
                
                if (resultado.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(this, "✅ Pregunta añadida al examen: " + examenDestino.getTitulo());
                    sistemaRepository.registrarAuditoria(usuarioActual.getId(), "AÑADIR_PREGUNTA_A_EXAMEN",
                        "Añadida pregunta " + tipoPregunta + " " + preguntaId + " al examen " + examenDestinoId);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error: " + resultado);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido");
            }
        }
        
        if (accion == 2) {
            // BORRAR PREGUNTA
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de borrar esta pregunta?\nNo se podrá recuperar.", "Confirmar borrado", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean borrado = false;
                if (tipoPregunta.equals("TEST")) {
                    borrado = sistemaRepository.eliminarPregunta(preguntaId);
                } else {
                    borrado = sistemaRepository.eliminarPreguntaAbierta(preguntaId);
                }
                
                if (borrado) {
                    JOptionPane.showMessageDialog(this, "✅ Pregunta borrada correctamente");
                    sistemaRepository.registrarAuditoria(usuarioActual.getId(), "BORRAR_PREGUNTA",
                        "Borrada pregunta " + tipoPregunta + " ID: " + preguntaId);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al borrar la pregunta");
                }
            }
        }
    }
    //LISTA DE EXAMENES DEL PROFESOR


    private JPanel crearPanelMisExamenes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        modelExamenes = new DefaultTableModel(new String[]{"ID", "Título", "Fecha", "Tipo"}, 0);
        tablaExamenes = new JTable(modelExamenes);
        JScrollPane scroll = new JScrollPane(tablaExamenes);
        panel.add(scroll, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnVerPreguntas = new JButton("Ver Preguntas");
        JButton btnExportar = new JButton("Exportar Examen");
        JButton btnBorrarExamen = new JButton("🗑️ Borrar Examen");
        
        btnRefrescar.addActionListener(e -> cargarMisExamenes());
        btnVerPreguntas.addActionListener(e -> {
            int row = tablaExamenes.getSelectedRow();
            if (row >= 0) {
                int examenId = (int) modelExamenes.getValueAt(row, 0);
                verPreguntasExamen(examenId);
            } else {
                JOptionPane.showMessageDialog(panel, "Selecciona un examen");
            }
        });
        
        btnExportar.addActionListener(e -> {
            int row = tablaExamenes.getSelectedRow();
            if (row >= 0) {
                int examenId = (int) modelExamenes.getValueAt(row, 0);
                String nombreArchivo = JOptionPane.showInputDialog(panel, "Nombre del archivo:", "examen_" + examenId);
                if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
                    boolean guardado = examenService.guardarExamenImprimible(examenId, nombreArchivo + ".txt");
                    JOptionPane.showMessageDialog(panel, guardado ? "✅ Examen guardado" : "❌ Error");
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Selecciona un examen");
            }
        });
        
        btnBorrarExamen.addActionListener(e -> {
            int row = tablaExamenes.getSelectedRow();
            if (row >= 0) {
                int examenId = (int) modelExamenes.getValueAt(row, 0);
                String titulo = (String) modelExamenes.getValueAt(row, 1);
                
                int confirm = JOptionPane.showConfirmDialog(panel, "¿Borrar el examen '" + titulo + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean borrado = sistemaRepository.eliminarExamen(examenId);
                    if (borrado) {
                        JOptionPane.showMessageDialog(panel, "✅ Examen borrado");
                        cargarMisExamenes();
                        sistemaRepository.registrarAuditoria(usuarioActual.getId(), "BORRAR_EXAMEN", "Borrado examen: " + titulo);
                    } else {
                        JOptionPane.showMessageDialog(panel, "❌ Error al borrar");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Selecciona un examen");
            }
        });
        
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnVerPreguntas);
        panelBotones.add(btnExportar);
        panelBotones.add(btnBorrarExamen);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        cargarMisExamenes();
        return panel;
    }
    
    private void cargarMisExamenes() {
        modelExamenes.setRowCount(0);
        List<Examen> examenes = examenService.listarExamenesPorProfesor(usuarioActual.getId());
        for (Examen e : examenes) {
            modelExamenes.addRow(new Object[]{e.getId(), e.getTitulo(), e.getFechaCreacion(), e.getTipo()});
        }
    }
    
    private void verPreguntasExamen(int examenId) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PREGUNTAS DEL EXAMEN ===\n\n");
        
        List<Pregunta> preguntas = examenService.obtenerPreguntasTest(examenId);
        sb.append("--- PREGUNTAS TEST ---\n");
        for (Pregunta p : preguntas) {
            sb.append("ID: ").append(p.getId()).append("\n");
            sb.append("Texto: ").append(p.getTexto()).append("\n");
            sb.append("A) ").append(p.getOpcionA()).append("\n");
            sb.append("B) ").append(p.getOpcionB()).append("\n");
            sb.append("C) ").append(p.getOpcionC()).append("\n");
            sb.append("D) ").append(p.getOpcionD()).append("\n");
            sb.append("Correcta: ").append(p.getRespuestaCorrecta()).append("\n\n");
        }
        
        List<PreguntaAbierta> abiertas = examenService.obtenerPreguntasDesarrollo(examenId);
        sb.append("--- PREGUNTAS DESARROLLO ---\n");
        for (PreguntaAbierta p : abiertas) {
            sb.append("ID: ").append(p.getId()).append("\n");
            sb.append("Texto: ").append(p.getTexto()).append("\n");
            sb.append("Respuesta esperada: ").append(p.getRespuestaEsperada()).append("\n");
            sb.append("Puntuación: ").append(p.getPuntuacionMaxima()).append("\n\n");
        }
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(this, scroll, "Preguntas del Examen", JOptionPane.INFORMATION_MESSAGE);
    }
    //CREAMOS UN JPANEL PARA LAS OTAS DE LOS ALUMNOS (PROFESOR)
    private JPanel crearPanelResultadosAlumnos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel panelTop = new JPanel(new FlowLayout());
        JComboBox<Examen> cbExamenes = new JComboBox<>();
        panelTop.add(new JLabel("Seleccionar examen:"));
        panelTop.add(cbExamenes);
        
        JButton btnCargar = new JButton("Cargar Resultados");
        panelTop.add(btnCargar);
        panel.add(panelTop, BorderLayout.NORTH);
        
        DefaultTableModel modelResultados = new DefaultTableModel(new String[]{"Alumno ID", "Nota", "Fecha"}, 0);
        JTable tablaResultados = new JTable(modelResultados);
        JScrollPane scroll = new JScrollPane(tablaResultados);
        panel.add(scroll, BorderLayout.CENTER);
        
        List<Examen> examenes = examenService.listarExamenesPorProfesor(usuarioActual.getId());
        for (Examen e : examenes) {
            cbExamenes.addItem(e);
        }
        
        btnCargar.addActionListener(e -> {
            Examen examen = (Examen) cbExamenes.getSelectedItem();
            if (examen != null) {
                modelResultados.setRowCount(0);
                List<Resultado> resultados = examenService.verResultadosPorExamen(examen.getId());
                for (Resultado r : resultados) {
                    modelResultados.addRow(new Object[]{r.getAlumnoId(), r.getNota(), r.getFechaRealizacion()});
                }
                if (resultados.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "No hay resultados para este examen");
                }
            }
        });
        
        return panel;
    }
    //VER LOS EXAMENES QUE ESTAN DISPONIBLES (ALUMNOS)
    private JPanel crearPanelVerExamenes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Título", "Profesor", "Tipo"}, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);
        
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> {
            model.setRowCount(0);
            List<Examen> examenes = examenService.listarTodosExamenes();
            for (Examen ex : examenes) {
                model.addRow(new Object[]{ex.getId(), ex.getTitulo(), ex.getUsuarioId(), ex.getTipo()});
            }
        });
        
        panel.add(btnRefrescar, BorderLayout.SOUTH);
        btnRefrescar.doClick();
        return panel;
    }
    //JPANEL PARA REALIZAR UN EXAMEN (ALUMNO)
    private JPanel crearPanelRealizarExamen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JComboBox<Examen> cbExamenes = new JComboBox<>();
        JButton btnRealizar = new JButton("Realizar Examen");
        btnRealizar.setBackground(new Color(0, 102, 204));
        btnRealizar.setForeground(Color.WHITE);
        
        JPanel panelTop = new JPanel(new FlowLayout());
        panelTop.add(new JLabel("Seleccionar examen:"));
        panelTop.add(cbExamenes);
        panelTop.add(btnRealizar);
        panel.add(panelTop, BorderLayout.NORTH);
        
        cargarExamenesDisponibles(cbExamenes);
        
        btnRealizar.addActionListener(e -> {
            Examen examen = (Examen) cbExamenes.getSelectedItem();
            if (examen == null) return;
            
            if (examenService.alumnoYaRealizoExamen(usuarioActual.getId(), examen.getId())) {
                JOptionPane.showMessageDialog(panel, "Ya realizaste este examen");
                return;
            }
            
            if (examen.getTipo().equals("test")) {
                realizarExamenTest(examen);
            } else {
                realizarExamenDesarrollo(examen);
            }
            
            cargarExamenesDisponibles(cbExamenes);
        });
        
        return panel;
    }
    
    private void cargarExamenesDisponibles(JComboBox<Examen> cbExamenes) {
        cbExamenes.removeAllItems();
        List<Examen> examenes = examenService.listarTodosExamenes();
        for (Examen e : examenes) {
            if (!examenService.alumnoYaRealizoExamen(usuarioActual.getId(), e.getId())) {
                cbExamenes.addItem(e);
            }
        }
    }
    
    private void realizarExamenTest(Examen examen) {
        List<Pregunta> preguntas = examenService.obtenerPreguntasTest(examen.getId());
        if (preguntas == null || preguntas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este examen no tiene preguntas");
            return;
        }
        
        List<String> respuestas = new ArrayList<>();
        
        for (Pregunta p : preguntas) {
            String respuesta = (String) JOptionPane.showInputDialog(this,
                p.getTexto() + "\n\nA) " + p.getOpcionA() + "\nB) " + p.getOpcionB() + 
                "\nC) " + p.getOpcionC() + "\nD) " + p.getOpcionD(),
                "Pregunta",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"A", "B", "C", "D"},
                "A");
            if (respuesta == null) return;
            respuestas.add(respuesta);
        }
        
        int aciertos = examenService.corregirExamenTest(usuarioActual.getId(), examen.getId(), respuestas);
        int nota = (aciertos * 10) / preguntas.size();
        
        String resultado = examenService.guardarResultado(usuarioActual.getId(), examen.getId(), nota);
        
        if (resultado.equals("SUCCESS")) {
            sistemaRepository.registrarAuditoria(usuarioActual.getId(), "REALIZAR_EXAMEN_TEST",
                "Realizado examen: " + examen.getTitulo() + " - Nota: " + nota);
            JOptionPane.showMessageDialog(this, "Examen completado!\nAciertos: " + aciertos + "/" + preguntas.size() + "\nNota: " + nota);
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el resultado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void realizarExamenDesarrollo(Examen examen) {
        List<PreguntaAbierta> preguntas = examenService.obtenerPreguntasDesarrollo(examen.getId());
        
        if (preguntas == null || preguntas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este examen no tiene preguntas");
            return;
        }
        
        for (PreguntaAbierta p : preguntas) {
            String respuesta = JOptionPane.showInputDialog(this, p.getTexto());
            if (respuesta != null && !respuesta.trim().isEmpty()) {
                examenService.guardarRespuestaDesarrollo(usuarioActual.getId(), p.getId(), respuesta);
            }
        }
        
        String resultado = examenService.guardarResultado(usuarioActual.getId(), examen.getId(), -1);
        
        if (resultado.equals("SUCCESS")) {
            sistemaRepository.registrarAuditoria(usuarioActual.getId(), "REALIZAR_EXAMEN_DESARROLLO",
                "Realizado examen (desarrollo): " + examen.getTitulo());
            JOptionPane.showMessageDialog(this, "Respuestas guardadas. El profesor corregirá tu examen.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //PARA VER LAS NOTAS DESDE (ALUMNO)
    private JPanel crearPanelResultados() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        DefaultTableModel model = new DefaultTableModel(new String[]{"Examen", "Nota", "Fecha"}, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);
        
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> {
            model.setRowCount(0);
            List<Resultado> resultados = examenService.verResultadosPorAlumno(usuarioActual.getId());
            for (Resultado r : resultados) {
                Examen ex = examenService.buscarExamenPorId(r.getExamenId());
                String titulo = ex != null ? ex.getTitulo() : "Examen " + r.getExamenId();
                String nota = r.getNota() == -1 ? "Pendiente" : String.valueOf(r.getNota());
                model.addRow(new Object[]{titulo, nota, r.getFechaRealizacion()});
            }
        });
        
        panel.add(btnRefrescar, BorderLayout.SOUTH);
        btnRefrescar.doClick();
        return panel;
    }
    //MOSTRAMOS UN EXAMEN USANDO SU TITULOS, EL NUMERO DE PREGUNTAS DE TIPO TEST
    //Y DESARROLLO ES PARA CREAR UNE XAMEN ALEATORIO
    private void mostrarDialogoGenerarExamen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridy = 0;
        gbc.gridx = 0;
        panel.add(new JLabel("Título del examen:"), gbc);
        JTextField txtTitulo = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtTitulo, gbc);
        
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Número de preguntas TEST:"), gbc);
        JSpinner spinnerTest = new JSpinner(new SpinnerNumberModel(0, 0, 50, 1));
        gbc.gridx = 1;
        panel.add(spinnerTest, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Número de preguntas DESARROLLO:"), gbc);
        JSpinner spinnerDesarrollo = new JSpinner(new SpinnerNumberModel(0, 0, 50, 1));
        gbc.gridx = 1;
        panel.add(spinnerDesarrollo, gbc);
        
        int option = JOptionPane.showConfirmDialog(this, panel, "Generar Examen Aleatorio", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;
        
        String titulo = txtTitulo.getText().trim();
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título es obligatorio");
            return;
        }
        
        int numTest = (int) spinnerTest.getValue();
        int numDesarrollo = (int) spinnerDesarrollo.getValue();
        
        if (numTest == 0 && numDesarrollo == 0) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar al menos una pregunta");
            return;
        }
        
        try {
            String resultado = examenService.crearExamen(titulo, usuarioActual.getId(), "ambos");
            if (!resultado.equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Error al crear el examen: " + resultado);
                return;
            }
            
            Examen examen = null;
            List<Examen> examenes = examenService.listarExamenesPorProfesor(usuarioActual.getId());
            for (Examen e : examenes) {
                if (e.getTitulo().equals(titulo)) {
                    examen = e;
                    break;
                }
            }
            
            if (examen == null) {
                JOptionPane.showMessageDialog(this, "Error: No se encontró el examen creado");
                return;
            }
            
            int testAgregadas = 0;
            int desarrolloAgregadas = 0;
            
            if (numTest > 0) {
                List<Pregunta> preguntasTest = examenService.obtenerTodasPreguntasTestDelProfesor(usuarioActual.getId());
                if (!preguntasTest.isEmpty()) {
                    Collections.shuffle(preguntasTest);
                    int aCopiar = Math.min(numTest, preguntasTest.size());
                    for (int i = 0; i < aCopiar; i++) {
                        Pregunta original = preguntasTest.get(i);
                        String res = examenService.agregarPreguntaTest(
                            original.getTexto(), original.getOpcionA(), original.getOpcionB(),
                            original.getOpcionC(), original.getOpcionD(), original.getRespuestaCorrecta(),
                            examen.getId(), usuarioActual.getId()
                        );
                        if (res.equals("SUCCESS")) testAgregadas++;
                    }
                    if (testAgregadas < numTest) {
                        JOptionPane.showMessageDialog(this, "Solo hay " + preguntasTest.size() + " preguntas TEST. Se agregaron " + testAgregadas);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No hay preguntas TEST disponibles");
                }
            }
            
            if (numDesarrollo > 0) {
                List<PreguntaAbierta> preguntasDesarrollo = examenService.obtenerTodasPreguntasDesarrolloDelProfesor(usuarioActual.getId());
                if (!preguntasDesarrollo.isEmpty()) {
                    Collections.shuffle(preguntasDesarrollo);
                    int aCopiar = Math.min(numDesarrollo, preguntasDesarrollo.size());
                    for (int i = 0; i < aCopiar; i++) {
                        PreguntaAbierta original = preguntasDesarrollo.get(i);
                        String res = examenService.agregarPreguntaDesarrollo(
                            original.getTexto(), original.getRespuestaEsperada(), original.getPuntuacionMaxima(),
                            examen.getId(), usuarioActual.getId()
                        );
                        if (res.equals("SUCCESS")) desarrolloAgregadas++;
                    }
                    if (desarrolloAgregadas < numDesarrollo) {
                        JOptionPane.showMessageDialog(this, "Solo hay " + preguntasDesarrollo.size() + " preguntas DESARROLLO. Se agregaron " + desarrolloAgregadas);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No hay preguntas DESARROLLO disponibles");
                }
            }
            
            JOptionPane.showMessageDialog(this, "✅ Examen generado correctamente\n" +
                "ID: " + examen.getId() + "\n" +
                "TEST: " + testAgregadas + "\n" +
                "DESARROLLO: " + desarrolloAgregadas);
            
            sistemaRepository.registrarAuditoria(usuarioActual.getId(), "GENERAR_EXAMEN_ALEATORIO",
                "Generado examen: " + examen.getTitulo());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //CAMBIA LA PANTALLA VISIBLE
    private void cambiarPanel(String nombre) {
        switch (nombre) {
            case "Inicio":
                cardLayout.show(panelCards, "Inicio");
                break;
            case "Crear Examen":
                cardLayout.show(panelCards, "Crear Examen");
                break;
            case "Mis Exámenes":
                cardLayout.show(panelCards, "Mis Exámenes");
                cargarMisExamenes();
                break;
            case "Administrar Preguntas":
                administrarPreguntas();
                break;
            case "Generar Examen":
                mostrarDialogoGenerarExamen();
                break;
            case "Ver Exámenes":
                cardLayout.show(panelCards, "Ver Exámenes");
                break;
            case "Realizar Examen":
                cardLayout.show(panelCards, "Realizar Examen");
                break;
            case "Ver Resultados":
                cardLayout.show(panelCards, "Ver Resultados");
                break;
            case "Ver Resultados Alumnos":
                cardLayout.show(panelCards, "Ver Resultados Alumnos");
                break;
        }
    }
    //CIERRA SESION Y TE MANDA AL LOGIN
    private void logout() {
        sistemaRepository.registrarAuditoria(usuarioActual.getId(), "LOGOUT", "Cierre de sesión");
        int confirm = JOptionPane.showConfirmDialog(this, "¿Cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginUI().setVisible(true);
        }
    }
    
    
}