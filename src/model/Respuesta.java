package model;
// CLASE QUE GUARDA LA RESPUESTA DE UN ALUMNO A UNA PREGUNTA TEST
public class Respuesta {
    private int id;
    private int alumnoId;
    private int preguntaId;
    private String respuestaAlumno;
    private boolean esCorrecta;
    
    public Respuesta() {}
    
    public Respuesta(int alumnoId, int preguntaId, String respuestaAlumno, boolean esCorrecta) {
        this.alumnoId = alumnoId;
        this.preguntaId = preguntaId;
        this.respuestaAlumno = respuestaAlumno;
        this.esCorrecta = esCorrecta;
    }
    //GETTERS Y SETTER PARA QUE LAS DEMAS CLASES PUEDAN ACCEDER A SU CONTENIDO
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAlumnoId() { return alumnoId; }
    public void setAlumnoId(int alumnoId) { this.alumnoId = alumnoId; }
    public int getPreguntaId() { return preguntaId; }
    public void setPreguntaId(int preguntaId) { this.preguntaId = preguntaId; }
    public String getRespuestaAlumno() { return respuestaAlumno; }
    public void setRespuestaAlumno(String respuestaAlumno) { this.respuestaAlumno = respuestaAlumno; }
    public boolean isEsCorrecta() { return esCorrecta; }
    public void setEsCorrecta(boolean esCorrecta) { this.esCorrecta = esCorrecta; }
}