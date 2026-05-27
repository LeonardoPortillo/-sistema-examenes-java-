package repository;

import database.DatabaseConnection;
import model.*;
import java.util.List;

// CLASE PUENTE ENTRE LOS SERVICIOS Y LA BASE DE DATOS 
//SU FUNCION ES PASAR LA INFORMACION A LA DATABASECONNECTION

public class SistemaRepository {
    
    // USUARIO
   
    //REGISTRAMOS UN ALUMNO
    public boolean registrarAlumno(Usuario usuario) {
        return DatabaseConnection.registrarAlumno(usuario);
    }
    //A UN PROFE
    public boolean registrarProfesor(Usuario usuario) {
        return DatabaseConnection.registrarProfesor(usuario);
    }
    //VERIFICAMOS EL LOGIN
    public Usuario login(String email, String password) {
        return DatabaseConnection.login(email, password);
    }
    //LISTAMOS TODOS LOS USUARIOS
    public List<Usuario> listarUsuarios() {
        return DatabaseConnection.listarUsuarios();
    }
    
    
    // EXAMEN
  
    
    public boolean crearExamen(Examen examen) {
        return DatabaseConnection.crearExamen(examen);
    }
    
    public List<Examen> listarExamenesPorProfesor(int profesorId) {
        return DatabaseConnection.listarExamenesPorProfesor(profesorId);
    }
    
    public List<Examen> listarTodosExamenes() {
        return DatabaseConnection.listarTodosExamenes();
    }
    
    public Examen getExamenById(int id) {
        return DatabaseConnection.getExamenById(id);
    }
    
    public boolean eliminarExamen(int examenId) {
        return DatabaseConnection.eliminarExamen(examenId);
    }
    
   
    // PREGUNTA TEST
   
    
    public boolean agregarPregunta(Pregunta pregunta) {
        return DatabaseConnection.agregarPregunta(pregunta);
    }
    
    public List<Pregunta> listarPreguntasPorExamen(int examenId) {
        return DatabaseConnection.listarPreguntasPorExamen(examenId);
    }
    
    public List<Pregunta> listarTodasLasPreguntas() {
        return DatabaseConnection.listarTodasLasPreguntas();
    }
    
    public boolean eliminarPregunta(int preguntaId) {
        return DatabaseConnection.eliminarPregunta(preguntaId);
    }
    
    // PREGUNTA DESARROLLO
    
    
    public boolean agregarPreguntaAbierta(PreguntaAbierta pregunta) {
        return DatabaseConnection.agregarPreguntaAbierta(pregunta);
    }
    
    public List<PreguntaAbierta> listarPreguntasAbiertasPorExamen(int examenId) {
        return DatabaseConnection.listarPreguntasAbiertasPorExamen(examenId);
    }
    
    public List<PreguntaAbierta> listarTodasLasPreguntasAbiertas() {
        return DatabaseConnection.listarTodasLasPreguntasAbiertas();
    }
    
    public boolean eliminarPreguntaAbierta(int preguntaId) {
        return DatabaseConnection.eliminarPreguntaAbierta(preguntaId);
    }
    
   
    // RESPUESTAS
  
    public boolean guardarRespuesta(Respuesta respuesta) {
        return DatabaseConnection.guardarRespuesta(respuesta);
    }
    
    public boolean guardarRespuestaAbierta(RespuestaAbierta respuesta) {
        return DatabaseConnection.guardarRespuestaAbierta(respuesta);
    }
    
    
    // RESULTADOS
    
    
    public boolean guardarResultado(Resultado resultado) {
        return DatabaseConnection.guardarResultado(resultado);
    }
    
    public List<Resultado> listarResultadosPorAlumno(int alumnoId) {
        return DatabaseConnection.listarResultadosPorAlumno(alumnoId);
    }
    
    public List<Resultado> listarResultadosPorExamen(int examenId) {
        return DatabaseConnection.listarResultadosPorExamen(examenId);
    }
    
    public boolean alumnoYaRealizoExamen(int alumnoId, int examenId) {
        return DatabaseConnection.alumnoYaRealizoExamen(alumnoId, examenId);
    }
    
   
    // AUDITORIA
    
    
    public void registrarAuditoria(int usuarioId, String operacion, String detalle) {
        DatabaseConnection.registrarAuditoria(usuarioId, operacion, detalle);
    }
}