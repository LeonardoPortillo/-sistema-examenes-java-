package model;
// CLASE QUE GUARDA LA NOTA FINAL DE UN ALUMNO EN UN EXAMEN
public class Resultado {
    private int id;
    private int alumnoId;
    private int examenId;
    private int nota;
    private String fechaRealizacion;
    
    public Resultado() {}
    
    public Resultado(int alumnoId, int examenId, int nota, String fechaRealizacion) {
        this.alumnoId = alumnoId;
        this.examenId = examenId;
        this.nota = nota;
        this.fechaRealizacion = fechaRealizacion;
    }
      //GETTERS Y SETTER PARA QUE LAS DEMAS CLASES PUEDAN ACCEDER A SU CONTENIDO
  
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAlumnoId() { return alumnoId; }
    public void setAlumnoId(int alumnoId) { this.alumnoId = alumnoId; }
    public int getExamenId() { return examenId; }
    public void setExamenId(int examenId) { this.examenId = examenId; }
    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }
    public String getFechaRealizacion() { return fechaRealizacion; }
    public void setFechaRealizacion(String fechaRealizacion) { this.fechaRealizacion = fechaRealizacion; }
}