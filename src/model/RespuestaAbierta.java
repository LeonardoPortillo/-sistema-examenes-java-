package model;

// CLASE QUE GUARDA LA RESPUESTA DE UN ALUMNO A UNA PREGUNTA DE DESARROLLO
public class RespuestaAbierta {
    private int id;
    private int alumnoId;
    private int preguntaId;
    private String textoRespuesta;
    private int puntuacionObtenida;
    
    public RespuestaAbierta() {}
    
    public RespuestaAbierta(int alumnoId, int preguntaId, String textoRespuesta) {
        this.alumnoId = alumnoId;
        this.preguntaId = preguntaId;
        this.textoRespuesta = textoRespuesta;
        this.puntuacionObtenida = 0;
    }
     //GETTERS Y SETTER PARA QUE LAS DEMAS CLASES PUEDAN ACCEDER A SU CONTENIDO
   
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAlumnoId() { return alumnoId; }
    public void setAlumnoId(int alumnoId) { this.alumnoId = alumnoId; }
    public int getPreguntaId() { return preguntaId; }
    public void setPreguntaId(int preguntaId) { this.preguntaId = preguntaId; }
    public String getTextoRespuesta() { return textoRespuesta; }
    public void setTextoRespuesta(String textoRespuesta) { this.textoRespuesta = textoRespuesta; }
    public int getPuntuacionObtenida() { return puntuacionObtenida; }
    public void setPuntuacionObtenida(int puntuacionObtenida) { this.puntuacionObtenida = puntuacionObtenida; }
}