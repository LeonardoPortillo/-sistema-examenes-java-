package ui;

import service.UsuarioService;
import javax.swing.*;
import java.awt.*;

//PANTALAL DE REGISTRO DE NUEVOS ALUMNOS
public class RegistroUI extends JFrame {
    // COMPONENTES DE LA INTERFAZ 
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private UsuarioService usuarioService;
    // CONSTRUCTOR: configura la ventana
    public RegistroUI() {
        this.usuarioService = new UsuarioService();
        setTitle("Registro de Alumno");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }
  
     // CREAR LA INTERFAZ GRÁFICA
    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel lblTitulo = new JLabel("REGISTRO DE ALUMNO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Contraseña:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("Confirmar Contraseña:"), gbc);
        txtConfirmPassword = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(txtConfirmPassword, gbc);
        
        JLabel lblInfo = new JLabel("⚠️ Solo alumnos pueden registrarse. Los profesores son creados por administrador.");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 10));
        lblInfo.setForeground(Color.RED);
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(lblInfo, gbc);
        
        JButton btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setBackground(new Color(0, 102, 204));
        btnRegistrar.setForeground(Color.WHITE);
        gbc.gridy = 6;
        panel.add(btnRegistrar, gbc);
        
        JButton btnVolver = new JButton("Volver al Login");
        gbc.gridy = 7;
        panel.add(btnVolver, gbc);
        
        add(panel);
        
        btnRegistrar.addActionListener(e -> registrar());
        btnVolver.addActionListener(e -> {
            dispose();
            new LoginUI().setVisible(true);
        });
    }
    // ACCIÓN DEL BOTÓN "REGISTRARSE"
     
    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
        
        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String resultado = usuarioService.registrarAlumno(nombre, email, password);
        
        if (resultado.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "✅ Alumno registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginUI().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}