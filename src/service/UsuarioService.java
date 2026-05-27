package service;

import model.Usuario;
import repository.SistemaRepository;

// CLASE QUE GESTIONA TODO LO RELACIONADO CON USUARIOS 
// (REGISTRO Y LOGIN)

public class UsuarioService {
    
    private SistemaRepository repository;
    
    public UsuarioService() {
        this.repository = new SistemaRepository();
    }

     // REGISTRO DE ALUMNO (solo alumnos pueden registrarse)

    public String registrarAlumno(String nombre, String email, String password) {
        
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre es obligatorio";
        }
        if (email == null || email.trim().isEmpty()) {
            return "El email es obligatorio";
        }
        if (password == null || password.trim().isEmpty()) {
            return "La contraseña es obligatoria";
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            return "El email no es válido";
        }
        
        //LO GUARDA EN EL REPOSITORY

        Usuario usuario = new Usuario(nombre, email, password, "alumno");
        boolean guardado = repository.registrarAlumno(usuario);
        
        if (guardado) {
            return "SUCCESS";
        } else {
            return "Error al registrar. El email puede estar duplicado";
        }
    }
    
     // LOGIN (tanto alumnos como profesores)

    public Usuario login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            return null;
        }
        return repository.login(email, password);
    }

    // VERIFICAR ROLES
    
    public boolean esProfesor(Usuario usuario) {
        return usuario != null && usuario.getRol().equals("profesor");
    }
    
    public boolean esAlumno(Usuario usuario) {
        return usuario != null && usuario.getRol().equals("alumno");
    }
}