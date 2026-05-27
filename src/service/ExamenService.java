package service;

import model.*;
import repository.SistemaRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


// CLASE QUE CONTIENE TODA LA LÓGICA DE NEGOCIO 
// RELACIONADA CON EXÁMENES Y PREGUNTAS
public class ExamenService {
    
    private SistemaRepository repository;
    
    public ExamenService() {
        this.repository = new SistemaRepository();
    }
    
    // MÉTODO AUXILIAR PARA REPETIR STRINGS 
   
    
    private String repetir(String str, int veces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < veces; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
  
    // CREAR EXAMEN
    
    
    public String crearExamen(String titulo, int profesorId, String tipo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return "El título es obligatorio";
        }
        if (!tipo.equals("test") && !tipo.equals("desarrollo") && !tipo.equals("ambos")) {
            return "El tipo debe ser 'test', 'desarrollo' o 'ambos'";
        }
        
        String fecha = LocalDate.now().toString();
        Examen examen = new Examen(titulo, fecha, profesorId, tipo);
        boolean creado = repository.crearExamen(examen);
        
        if (creado) {
            return "SUCCESS";
        } else {
            return "Error al crear el examen";
        }
    }
    
  
    // AGREGAR PREGUNTA TEST
  
    
    public String agregarPreguntaTest(String texto, String opcionA, String opcionB, 
                                       String opcionC, String opcionD, 
                                       String respuestaCorrecta, int examenId, int profesorId) {
        if (texto == null || texto.trim().isEmpty()) {
            return "El texto de la pregunta es obligatorio";
        }
        if (opcionA == null || opcionA.trim().isEmpty() ||
            opcionB == null || opcionB.trim().isEmpty() ||
            opcionC == null || opcionC.trim().isEmpty() ||
            opcionD == null || opcionD.trim().isEmpty()) {
            return "Todas las opciones son obligatorias";
        }
        if (!respuestaCorrecta.equals("A") && !respuestaCorrecta.equals("B") && 
            !respuestaCorrecta.equals("C") && !respuestaCorrecta.equals("D")) {
            return "La respuesta correcta debe ser A, B, C o D";
        }
        
        Pregunta pregunta = new Pregunta(texto, opcionA, opcionB, opcionC, opcionD, 
                                          respuestaCorrecta, examenId, profesorId);
        boolean guardado = repository.agregarPregunta(pregunta);
        
        if (guardado) {
            return "SUCCESS";
        } else {
            return "Error al agregar la pregunta";
        }
    }
    
    // AGREGAR PREGUNTA DESARROLLO
  
    
    public String agregarPreguntaDesarrollo(String texto, String respuestaEsperada, 
                                             int puntuacionMaxima, int examenId, int profesorId) {
        if (texto == null || texto.trim().isEmpty()) {
            return "El texto de la pregunta es obligatorio";
        }
        if (respuestaEsperada == null || respuestaEsperada.trim().isEmpty()) {
            return "La respuesta esperada es obligatoria";
        }
        if (puntuacionMaxima <= 0) {
            return "La puntuación máxima debe ser mayor que 0";
        }
        
        PreguntaAbierta pregunta = new PreguntaAbierta(texto, respuestaEsperada, 
                                                        puntuacionMaxima, examenId, profesorId);
        boolean guardado = repository.agregarPreguntaAbierta(pregunta);
        
        if (guardado) {
            return "SUCCESS";
        } else {
            return "Error al agregar la pregunta";
        }
    }
    
    
    // LISTAR EXÁMENES
   
    //TE MUESTRA LOS EXAMENES QUE HA CREADO UN PROFESOR
    public List<Examen> listarExamenesPorProfesor(int profesorId) {
        return repository.listarExamenesPorProfesor(profesorId);
    }
    //MUETRA TODOS LOS EXAMENES
    public List<Examen> listarTodosExamenes() {
        return repository.listarTodosExamenes();
    }
    //BUSCA UN EXAMEN POR SU ID
    public Examen buscarExamenPorId(int id) {
        return repository.getExamenById(id);
    }
    
   
    // OBTENER PREGUNTAS
   
    //OBTIENE TODAS LA PREGUNTAS TIPO TEST
    public List<Pregunta> obtenerPreguntasTest(int examenId) {
        return repository.listarPreguntasPorExamen(examenId);
    }
    //OBTIENE TODAS LAS PREGUNTAS DE DESARROLLO
    public List<PreguntaAbierta> obtenerPreguntasDesarrollo(int examenId) {
        return repository.listarPreguntasAbiertasPorExamen(examenId);
    }
    
    
    // OBTENER TODAS LAS PREGUNTAS DEL PROFESOR

    //DE UN PROFESOR NOS DA LAS PREGUNTAS DE TIPO TEST
    public List<Pregunta> obtenerTodasPreguntasTestDelProfesor(int profesorId) {
        List<Pregunta> todas = repository.listarTodasLasPreguntas();
        List<Pregunta> filtradas = new ArrayList<>();
        for (Pregunta p : todas) {
            if (p.getProfesorId() == profesorId) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }
    //OBTIENE LA PREGUNTAS DE DESARROLLO DE UN PROFESOR
    public List<PreguntaAbierta> obtenerTodasPreguntasDesarrolloDelProfesor(int profesorId) {
        List<PreguntaAbierta> todas = repository.listarTodasLasPreguntasAbiertas();
        List<PreguntaAbierta> filtradas = new ArrayList<>();
        for (PreguntaAbierta p : todas) {
            if (p.getProfesorId() == profesorId) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }
    
    
    // COPIAR PREGUNTA TEST
    
    //SIRVE PARA REUTILIZAR LAS PREGUNTAS DE TIPO TEST
    public String copiarPreguntaTest(int preguntaId, int examenDestinoId, int profesorId) {
        List<Pregunta> todas = obtenerTodasPreguntasTestDelProfesor(profesorId);
        Pregunta original = null;
        
        for (Pregunta p : todas) {
            if (p.getId() == preguntaId) {
                original = p;
                break;
            }
        }
        
        if (original == null) {
            return "No se encontró la pregunta";
        }
        
        Pregunta nueva = new Pregunta(
            original.getTexto(),
            original.getOpcionA(),
            original.getOpcionB(),
            original.getOpcionC(),
            original.getOpcionD(),
            original.getRespuestaCorrecta(),
            examenDestinoId,
            profesorId
        );
        
        boolean guardado = repository.agregarPregunta(nueva);
        return guardado ? "SUCCESS" : "Error al copiar la pregunta";
    }
    
   
    // COPIAR PREGUNTA DESARROLLO
    

    //SIRVE PARA REUTILIZAR LAS PREGUNTAS DE DESARROLLO
    
    public String copiarPreguntaDesarrollo(int preguntaId, int examenDestinoId, int profesorId) {
        List<PreguntaAbierta> todas = obtenerTodasPreguntasDesarrolloDelProfesor(profesorId);
        PreguntaAbierta original = null;
        
        for (PreguntaAbierta p : todas) {
            if (p.getId() == preguntaId) {
                original = p;
                break;
            }
        }
        
        if (original == null) {
            return "No se encontró la pregunta";
        }
        
        PreguntaAbierta nueva = new PreguntaAbierta(
            original.getTexto(),
            original.getRespuestaEsperada(),
            original.getPuntuacionMaxima(),
            examenDestinoId,
            profesorId
        );
        
        boolean guardado = repository.agregarPreguntaAbierta(nueva);
        return guardado ? "SUCCESS" : "Error al copiar la pregunta";
    }
    

    // GENERAR EXAMEN ALEATORIO
    

    //CREAMOS UN EXAMEN ELIGIENDO EL NUMERO DE PREGUNTAS TANTO DE TEST Y DE DESARROLLO
    //Y LAS ESCOJE DE FORMA ALEATORIA PARA CREAR EL EXMAEN
    
    public Examen generarExamenAleatorio(String titulo, int profesorId, int numPreguntas) {
        String fecha = LocalDate.now().toString();
        Examen examen = new Examen(titulo, fecha, profesorId, "test");
        boolean creado = repository.crearExamen(examen);
        
        if (!creado) {
            return null;
        }
        
        List<Pregunta> candidatas = obtenerTodasPreguntasTestDelProfesor(profesorId);
        
        if (candidatas.isEmpty()) {
            return null;
        }
        
        Collections.shuffle(candidatas);
        int aCopiar = Math.min(numPreguntas, candidatas.size());
        
        for (int i = 0; i < aCopiar; i++) {
            Pregunta original = candidatas.get(i);
            Pregunta nueva = new Pregunta(
                original.getTexto(),
                original.getOpcionA(),
                original.getOpcionB(),
                original.getOpcionC(),
                original.getOpcionD(),
                original.getRespuestaCorrecta(),
                examen.getId(),
                profesorId
            );
            repository.agregarPregunta(nueva);
        }
        
        return examen;
    }
    
    
    // GENERAR EXAMEN CON PREGUNTAS SELECCIONADAS
   

    //CREAMOS UN EXAMEN PERO EN VEZ DE SER ALEATORIO BUSCAMOS LAS PREGUNTAS POR SU ID
    
    public Examen generarExamenConPreguntas(String titulo, int profesorId, 
                                            String tipo, List<Integer> preguntasIds) {
        String fecha = LocalDate.now().toString();
        Examen examen = new Examen(titulo, fecha, profesorId, tipo);
        boolean creado = repository.crearExamen(examen);
        
        if (!creado) {
            return null;
        }
        
        List<Pregunta> todas = obtenerTodasPreguntasTestDelProfesor(profesorId);
        
        for (int preguntaId : preguntasIds) {
            for (Pregunta p : todas) {
                if (p.getId() == preguntaId) {
                    Pregunta nueva = new Pregunta(
                        p.getTexto(), p.getOpcionA(), p.getOpcionB(),
                        p.getOpcionC(), p.getOpcionD(), p.getRespuestaCorrecta(),
                        examen.getId(), profesorId
                    );
                    repository.agregarPregunta(nueva);
                    break;
                }
            }
        }
        
        return examen;
    }
    
  
    // GENERAR EXAMEN MIXTO
    

    //CREARMOS UN EXAMEN TANTO DE PREGUNTAS MANUALES Y ALEATORIAS
    
    public Examen generarExamenMixto(String titulo, int profesorId,
                                      List<Integer> preguntasObligatoriasIds,
                                      int numAleatorias) {
        String fecha = LocalDate.now().toString();
        Examen examen = new Examen(titulo, fecha, profesorId, "test");
        boolean creado = repository.crearExamen(examen);
        
        if (!creado) {
            return null;
        }
        
        List<Pregunta> todas = obtenerTodasPreguntasTestDelProfesor(profesorId);
        
        for (int preguntaId : preguntasObligatoriasIds) {
            for (Pregunta p : todas) {
                if (p.getId() == preguntaId) {
                    Pregunta nueva = new Pregunta(
                        p.getTexto(), p.getOpcionA(), p.getOpcionB(),
                        p.getOpcionC(), p.getOpcionD(), p.getRespuestaCorrecta(),
                        examen.getId(), profesorId
                    );
                    repository.agregarPregunta(nueva);
                    break;
                }
            }
        }
        
        List<Pregunta> candidatas = new ArrayList<>();
        for (Pregunta p : todas) {
            boolean esObligatoria = false;
            for (int id : preguntasObligatoriasIds) {
                if (p.getId() == id) {
                    esObligatoria = true;
                    break;
                }
            }
            if (!esObligatoria) {
                candidatas.add(p);
            }
        }
        
        Collections.shuffle(candidatas);
        int aCopiar = Math.min(numAleatorias, candidatas.size());
        
        for (int i = 0; i < aCopiar; i++) {
            Pregunta original = candidatas.get(i);
            Pregunta nueva = new Pregunta(
                original.getTexto(),
                original.getOpcionA(),
                original.getOpcionB(),
                original.getOpcionC(),
                original.getOpcionD(),
                original.getRespuestaCorrecta(),
                examen.getId(),
                profesorId
            );
            repository.agregarPregunta(nueva);
        }
        
        return examen;
    }
    
    
    // EXPORTAR EXAMEN 
  
    //GENERA UN TEXTO 
    public String exportarExamenImprimible(int examenId) {
        Examen examen = buscarExamenPorId(examenId);
        if (examen == null) {
            return "No se encontró el examen";
        }
        
        List<Pregunta> preguntasTest = repository.listarPreguntasPorExamen(examenId);
        List<PreguntaAbierta> preguntasDesarrollo = repository.listarPreguntasAbiertasPorExamen(examenId);
        
        StringBuilder sb = new StringBuilder();
        sb.append(repetir("=", 80)).append("\n");
        sb.append("EXAMEN: ").append(examen.getTitulo().toUpperCase()).append("\n");
        sb.append("Fecha: ").append(examen.getFechaCreacion()).append("\n");
        sb.append(repetir("=", 80)).append("\n\n");
        
        int numero = 1;
        
        for (Pregunta p : preguntasTest) {
            sb.append(numero++).append(". ").append(p.getTexto()).append("\n");
            sb.append("  a) ").append(p.getOpcionA()).append("\n");
            sb.append("  b) ").append(p.getOpcionB()).append("\n");
            sb.append("  c) ").append(p.getOpcionC()).append("\n");
            sb.append("  d) ").append(p.getOpcionD()).append("\n");
            sb.append("\n");
        }
        
        for (PreguntaAbierta p : preguntasDesarrollo) {
            sb.append(numero++).append(". ").append(p.getTexto()).append("\n");
            sb.append("  [Pregunta de desarrollo - Espacio para respuesta]\n");
            sb.append("  ").append(repetir("-", 60)).append("\n");
            sb.append("\n");
        }
        
        if (preguntasTest.isEmpty() && preguntasDesarrollo.isEmpty()) {
            sb.append("No hay preguntas en este examen.\n");
        }
        
        sb.append("\n").append(repetir("=", 80)).append("\n");
        sb.append("FIN DEL EXAMEN\n");
        
        return sb.toString();
    }
    
    //GUARDA ESE TEXTO EN UN .TXT

    public boolean guardarExamenImprimible(int examenId, String rutaArchivo) {
        String contenido = exportarExamenImprimible(examenId);
        if (contenido.startsWith("No se encontró")) {
            return false;
        }
        
        try {
            java.io.FileWriter fw = new java.io.FileWriter(rutaArchivo);
            fw.write(contenido);
            fw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
   
    // CORREGIR EXAMEN TEST
    
    
    public int corregirExamenTest(int alumnoId, int examenId, List<String> respuestasAlumno) {
        List<Pregunta> preguntas = repository.listarPreguntasPorExamen(examenId);
        int aciertos = 0;
        
        for (int i = 0; i < preguntas.size() && i < respuestasAlumno.size(); i++) {
            Pregunta pregunta = preguntas.get(i);
            boolean esCorrecta = pregunta.getRespuestaCorrecta().equals(respuestasAlumno.get(i));
            if (esCorrecta) aciertos++;
            
            Respuesta respuesta = new Respuesta(alumnoId, pregunta.getId(), respuestasAlumno.get(i), esCorrecta);
            repository.guardarRespuesta(respuesta);
        }
        return aciertos;
    }
    
   
    // GUARDAR RESPUESTA DESARROLLO
   
    
    public String guardarRespuestaDesarrollo(int alumnoId, int preguntaId, String respuestaTexto) {
        RespuestaAbierta respuesta = new RespuestaAbierta(alumnoId, preguntaId, respuestaTexto);
        boolean guardado = repository.guardarRespuestaAbierta(respuesta);
        return guardado ? "SUCCESS" : "Error al guardar la respuesta";
    }
    
    
    // VERIFICAR SI ALUMNO YA REALIZÓ EXAMEN
   
    
    public boolean alumnoYaRealizoExamen(int alumnoId, int examenId) {
        return repository.alumnoYaRealizoExamen(alumnoId, examenId);
    }
    
    // GUARDAR RESULTADO
    
    
    public String guardarResultado(int alumnoId, int examenId, int nota) {
        if (repository.alumnoYaRealizoExamen(alumnoId, examenId)) {
            return "El alumno ya ha realizado este examen";
        }
        
        String fecha = LocalDate.now().toString();
        Resultado resultado = new Resultado(alumnoId, examenId, nota, fecha);
        boolean guardado = repository.guardarResultado(resultado);
        return guardado ? "SUCCESS" : "Error al guardar el resultado";
    }
    
    
    // VER RESULTADOS
    
    public List<Resultado> verResultadosPorAlumno(int alumnoId) {
        return repository.listarResultadosPorAlumno(alumnoId);
    }
    
    public List<Resultado> verResultadosPorExamen(int examenId) {
        return repository.listarResultadosPorExamen(examenId);
    }
}