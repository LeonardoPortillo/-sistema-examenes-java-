package model;

//REPRESEWNTAMOS UNA PREGUNTA DE DESARROLLO POR ID, SU TEXTO, LA RESPUESTA ESPERADA
//Y LA PUNTUACION DE LA PREGUNTA

public class PreguntaAbierta {
    private int id;
    private String texto;
    private String respuestaEsperada;
    private int puntuacionMaxima;
    private int examenId;
    private int profesorId;
    
    public PreguntaAbierta() {}
    
    public PreguntaAbierta(String texto, String respuestaEsperada, int puntuacionMaxima, int examenId) {
        this.texto = texto;
        this.respuestaEsperada = respuestaEsperada;
        this.puntuacionMaxima = puntuacionMaxima;
        this.examenId = examenId;
    }
    
    public PreguntaAbierta(String texto, String respuestaEsperada, int puntuacionMaxima, int examenId, int profesorId) {
        this.texto = texto;
        this.respuestaEsperada = respuestaEsperada;
        this.puntuacionMaxima = puntuacionMaxima;
        this.examenId = examenId;
        this.profesorId = profesorId;
    }

//GETTERS Y SETTER PARA QUE LAS DEMAS CLASES PUEDAN ACCEDER A SU CONTENIDO

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getRespuestaEsperada() { return respuestaEsperada; }
    public void setRespuestaEsperada(String respuestaEsperada) { this.respuestaEsperada = respuestaEsperada; }
    public int getPuntuacionMaxima() { return puntuacionMaxima; }
    public void setPuntuacionMaxima(int puntuacionMaxima) { this.puntuacionMaxima = puntuacionMaxima; }
    public int getExamenId() { return examenId; }
    public void setExamenId(int examenId) { this.examenId = examenId; }
    public int getProfesorId() { return profesorId; }
    public void setProfesorId(int profesorId) { this.profesorId = profesorId; }
}