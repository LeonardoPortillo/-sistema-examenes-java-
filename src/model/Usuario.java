package model;
// CLASE QUE REPRESENTA UN USUARIO DEL SISTEMA (PROFESOR O ALUMNO)
public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String password;
    private String rol;
    
    public Usuario() {}
    
    public Usuario(String nombre, String email, String password, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }
      //GETTERS Y SETTER PARA QUE LAS DEMAS CLASES PUEDAN ACCEDER A SU CONTENIDO
  
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}