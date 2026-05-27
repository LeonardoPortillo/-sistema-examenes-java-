package model;

//REPREENTAMOS UNA PREGUNTA EN ESTE CASO DE TIPO TEST, CON LAS 4 POSIBLES RESPUESTAS Y 
//CUAL DE ELLAS SERIA LA CORRECTA

public class Pregunta {
    private int id;
    private String texto;
    private String opcionA;
    private String opcionB;
    private String opcionC;
    private String opcionD;
    private String respuestaCorrecta;
    private int examenId;
    private int profesorId;
    
    public Pregunta() {}
    
    public Pregunta(String texto, String opcionA, String opcionB, String opcionC, String opcionD, 
                    String respuestaCorrecta, int examenId) {
        this.texto = texto;
        this.opcionA = opcionA;
        this.opcionB = opcionB;
        this.opcionC = opcionC;
        this.opcionD = opcionD;
        this.respuestaCorrecta = respuestaCorrecta;
        this.examenId = examenId;
    }
    
    public Pregunta(String texto, String opcionA, String opcionB, String opcionC, String opcionD, 
                    String respuestaCorrecta, int examenId, int profesorId) {
        this.texto = texto;
        this.opcionA = opcionA;
        this.opcionB = opcionB;
        this.opcionC = opcionC;
        this.opcionD = opcionD;
        this.respuestaCorrecta = respuestaCorrecta;
        this.examenId = examenId;
        this.profesorId = profesorId;
    }
    
 //GETTERS Y SETTER PARA QUE LAS DEMAS CLASES PUEDAN ACCEDER A SU CONTENIDO


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getOpcionA() { return opcionA; }
    public void setOpcionA(String opcionA) { this.opcionA = opcionA; }
    public String getOpcionB() { return opcionB; }
    public void setOpcionB(String opcionB) { this.opcionB = opcionB; }
    public String getOpcionC() { return opcionC; }
    public void setOpcionC(String opcionC) { this.opcionC = opcionC; }
    public String getOpcionD() { return opcionD; }
    public void setOpcionD(String opcionD) { this.opcionD = opcionD; }
    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }
    public int getExamenId() { return examenId; }
    public void setExamenId(int examenId) { this.examenId = examenId; }
    public int getProfesorId() { return profesorId; }
    public void setProfesorId(int profesorId) { this.profesorId = profesorId; }
}