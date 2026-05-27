package model;

//REPRESENTAMOS UN EXAMEN EN EL SISTEMA POR ID TITULO,
//  FECHA, ID DEL PROFE Y SI ES TEST O DESARROLLO

public class Examen {
    private int id;
    private String titulo;
    private String fechaCreacion;
    private int usuarioId;
    private String tipo;
    
    public Examen() {}
    
    public Examen(String titulo, String fechaCreacion, int usuarioId, String tipo) {
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
    }
    
    //GETTERS Y SETTER PARA QUE LAS DEMAS CLASES PUEDAN ACCEDER A SU CONTENIDO

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    @Override
    public String toString() {
        return titulo;
    }
}