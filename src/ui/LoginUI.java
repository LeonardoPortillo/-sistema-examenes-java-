package ui;

import model.Usuario;
import service.UsuarioService;
import repository.SistemaRepository;
import javax.swing.*;
import java.awt.*;

// PANTALLA DE INICIO DE SESIÓN (VENTANA PRINCIPAL)


public class LoginUI extends JFrame {

   // COMPONENTES DE LA INTERFAZ

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private UsuarioService usuarioService;
    private SistemaRepository sistemaRepository;

    // CONSTRUCTOR: configura la ventana

    public LoginUI() {
        this.usuarioService = new UsuarioService();
        this.sistemaRepository = new SistemaRepository();
        setTitle("Sistema de Exámenes - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    // CREAR LA INTERFAZ GRÁFICA

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel lblTitulo = new JLabel("SISTEMA DE EXÁMENES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Contraseña:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);
        
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);
        
        JButton btnRegistro = new JButton("Registrarse");
        gbc.gridy = 4;
        panel.add(btnRegistro, gbc);
        
        add(panel);
        
        btnLogin.addActionListener(e -> login());
        btnRegistro.addActionListener(e -> {
            dispose();
            new RegistroUI().setVisible(true);
        });
    }

 // ACCIÓN DEL BOTÓN "INICIAR SESIÓN"

    private void login() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email y contraseña son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario usuario = usuarioService.login(email, password);
        
        if (usuario != null) {
            sistemaRepository.registrarAuditoria(usuario.getId(), "LOGIN", "Inicio de sesión exitoso");
            
            String rol = usuario.getRol().equals("profesor") ? "Profesor" : "Alumno";
            JOptionPane.showMessageDialog(this, "Bienvenido " + rol + ", " + usuario.getNombre(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            new MenuPrincipalUI(usuario).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Email o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
//LO MUESTRA POR PANTALLA
    public static void main(String[] args) {
        new LoginUI().setVisible(true);
    }
}