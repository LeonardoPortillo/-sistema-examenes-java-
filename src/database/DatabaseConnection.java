package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import model.*;
 //CREAMOS LA CONEXION A LA BASE DE DATOS PONIENDO EL USUARIO Y CONTRASEÑA EN ESTE CASO ROOT 1234
public class DatabaseConnection { 
    
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_examenes?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "1102";
    
    private static Connection connection = null; // declara una variable que más tarde PODRÁ contener una conexión
    
    private DatabaseConnection() {}
    //COMPROBAMOS SI NOS PODEMOS CONECTAR A LA BBDD
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("✅ Conectado a la base de datos");
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver no encontrado");
            } catch (SQLException e) {
                System.err.println("❌ Error: " + e.getMessage());
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //CIFRAMOS LA CONTRASEÑA
    private static String cifrarPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }
    
    
    // USUARIO
    
    
    //REGISTRAR AL ALUMNO
    public static boolean registrarAlumno(Usuario usuario) {
        String hashedPassword = cifrarPassword(usuario.getPassword());
        String sql = "INSERT INTO usuarios (nombre, email, password, rol) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, "alumno");
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //REGISTRAR AL PROFESOR
    public static boolean registrarProfesor(Usuario usuario) {
        String hashedPassword = cifrarPassword(usuario.getPassword());
        String sql = "INSERT INTO usuarios (nombre, email, password, rol) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, usuario.getRol());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //VERIFICAMOS LA CREDENCIALES PARA PODER LOGEARSE
    public static Usuario login(String email, String password) {
        String hashedPassword = cifrarPassword(password);
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, hashedPassword);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRol(rs.getString("rol"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //LISTAR TODOS LOS USUARIOS
    public static List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRol(rs.getString("rol"));
                usuarios.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }
    
    
    // EXAMEN
    
    

    //CREAR EL EXAMEN
    public static boolean crearExamen(Examen examen) {
        String sql = "INSERT INTO examenes (titulo, fecha_creacion, usuario_id, tipo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, examen.getTitulo());
            pstmt.setString(2, examen.getFechaCreacion());
            pstmt.setInt(3, examen.getUsuarioId());
            pstmt.setString(4, examen.getTipo());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                examen.setId(rs.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //VER LOS EXAMENES DE UN PROFESOR
    public static List<Examen> listarExamenesPorProfesor(int usuarioId) {
        List<Examen> lista = new ArrayList<>();
        String sql = "SELECT * FROM examenes WHERE usuario_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Examen e = new Examen();
                e.setId(rs.getInt("id"));
                e.setTitulo(rs.getString("titulo"));
                e.setFechaCreacion(rs.getString("fecha_creacion"));
                e.setUsuarioId(rs.getInt("usuario_id"));
                e.setTipo(rs.getString("tipo"));
                lista.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    //LISTAMOS TODOS LOS EXAMENES INDEPENDIENTEMENTE DEL PROFESOR
    public static List<Examen> listarTodosExamenes() {
        List<Examen> examenes = new ArrayList<>();
        String sql = "SELECT * FROM examenes";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Examen e = new Examen();
                e.setId(rs.getInt("id"));
                e.setTitulo(rs.getString("titulo"));
                e.setFechaCreacion(rs.getString("fecha_creacion"));
                e.setUsuarioId(rs.getInt("usuario_id"));
                e.setTipo(rs.getString("tipo"));
                examenes.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return examenes;
    }
    //PARA BUSCAR UN EXAMEN PO REL ID
    public static Examen getExamenById(int id) {
        String sql = "SELECT * FROM examenes WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Examen e = new Examen();
                e.setId(rs.getInt("id"));
                e.setTitulo(rs.getString("titulo"));
                e.setFechaCreacion(rs.getString("fecha_creacion"));
                e.setUsuarioId(rs.getInt("usuario_id"));
                e.setTipo(rs.getString("tipo"));
                return e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //PARA BORRAR UN EXAMEN
    public static boolean eliminarExamen(int examenId) {
        String sql = "DELETE FROM examenes WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, examenId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    // PREGUNTA TEST
    
    

    //GUARADAR UNA PREGUNTA TEST EN LA BBDD
    public static boolean agregarPregunta(Pregunta pregunta) {
        String sql = "INSERT INTO preguntas (texto, opcion_a, opcion_b, opcion_c, opcion_d, respuesta_correcta, examen_id, profesor_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, pregunta.getTexto());
            pstmt.setString(2, pregunta.getOpcionA());
            pstmt.setString(3, pregunta.getOpcionB());
            pstmt.setString(4, pregunta.getOpcionC());
            pstmt.setString(5, pregunta.getOpcionD());
            pstmt.setString(6, pregunta.getRespuestaCorrecta());
            
            // Si examenId es 0, guardamos NULL
            if (pregunta.getExamenId() == 0) {
                pstmt.setNull(7, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(7, pregunta.getExamenId());
            }
            
            pstmt.setInt(8, pregunta.getProfesorId());
            
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                pregunta.setId(rs.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //LISTAMOS LAS PREGUNTAS DE UN EXAMEN
    public static List<Pregunta> listarPreguntasPorExamen(int examenId) {
        List<Pregunta> lista = new ArrayList<>();
        String sql = "SELECT * FROM preguntas WHERE examen_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, examenId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Pregunta p = new Pregunta();
                p.setId(rs.getInt("id"));
                p.setTexto(rs.getString("texto"));
                p.setOpcionA(rs.getString("opcion_a"));
                p.setOpcionB(rs.getString("opcion_b"));
                p.setOpcionC(rs.getString("opcion_c"));
                p.setOpcionD(rs.getString("opcion_d"));
                p.setRespuestaCorrecta(rs.getString("respuesta_correcta"));
                p.setExamenId(rs.getInt("examen_id"));
                p.setProfesorId(rs.getInt("profesor_id"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    //LISTAMOS TODAS LAS PREGUNTAS
    public static List<Pregunta> listarTodasLasPreguntas() {
        List<Pregunta> lista = new ArrayList<>();
        String sql = "SELECT * FROM preguntas";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pregunta p = new Pregunta();
                p.setId(rs.getInt("id"));
                p.setTexto(rs.getString("texto"));
                p.setOpcionA(rs.getString("opcion_a"));
                p.setOpcionB(rs.getString("opcion_b"));
                p.setOpcionC(rs.getString("opcion_c"));
                p.setOpcionD(rs.getString("opcion_d"));
                p.setRespuestaCorrecta(rs.getString("respuesta_correcta"));
                p.setExamenId(rs.getInt("examen_id"));
                p.setProfesorId(rs.getInt("profesor_id"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    //BORRAMOS UNA PREGUNTA POR EL ID

    public static boolean eliminarPregunta(int preguntaId) {
        String sql = "DELETE FROM preguntas WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, preguntaId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    // PREGUNTA DESARROLLO
   
    
    //GUARDAMOS LA PREGUNTA DE DESARROLLO
    public static boolean agregarPreguntaAbierta(PreguntaAbierta pregunta) {
        String sql = "INSERT INTO preguntas_abiertas (texto, respuesta_esperada, puntuacion_maxima, examen_id, profesor_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, pregunta.getTexto());
            pstmt.setString(2, pregunta.getRespuestaEsperada());
            pstmt.setInt(3, pregunta.getPuntuacionMaxima());
            
            if (pregunta.getExamenId() == 0) {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(4, pregunta.getExamenId());
            }
            
            pstmt.setInt(5, pregunta.getProfesorId());
            
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                pregunta.setId(rs.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //LIASTAR PREGUNTA POR EXAMEN
    public static List<PreguntaAbierta> listarPreguntasAbiertasPorExamen(int examenId) {
        List<PreguntaAbierta> lista = new ArrayList<>();
        String sql = "SELECT * FROM preguntas_abiertas WHERE examen_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, examenId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PreguntaAbierta p = new PreguntaAbierta();
                p.setId(rs.getInt("id"));
                p.setTexto(rs.getString("texto"));
                p.setRespuestaEsperada(rs.getString("respuesta_esperada"));
                p.setPuntuacionMaxima(rs.getInt("puntuacion_maxima"));
                p.setExamenId(rs.getInt("examen_id"));
                p.setProfesorId(rs.getInt("profesor_id"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    //TODAS LAS PREGUNTAS DE DESARROLLO
    public static List<PreguntaAbierta> listarTodasLasPreguntasAbiertas() {
        List<PreguntaAbierta> lista = new ArrayList<>();
        String sql = "SELECT * FROM preguntas_abiertas";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PreguntaAbierta p = new PreguntaAbierta();
                p.setId(rs.getInt("id"));
                p.setTexto(rs.getString("texto"));
                p.setRespuestaEsperada(rs.getString("respuesta_esperada"));
                p.setPuntuacionMaxima(rs.getInt("puntuacion_maxima"));
                p.setExamenId(rs.getInt("examen_id"));
                p.setProfesorId(rs.getInt("profesor_id"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    //ELIMINAR PREGUNTA DE DESARROLLO
    public static boolean eliminarPreguntaAbierta(int preguntaId) {
        String sql = "DELETE FROM preguntas_abiertas WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, preguntaId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    // RESPUESTAS
   
    //GUARDAMOS LA RESPUESTA DE TIPO TEST
    public static boolean guardarRespuesta(Respuesta respuesta) {
        String sql = "INSERT INTO respuestas (alumno_id, pregunta_id, respuesta_alumno, es_correcta) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, respuesta.getAlumnoId());
            pstmt.setInt(2, respuesta.getPreguntaId());
            pstmt.setString(3, respuesta.getRespuestaAlumno());
            pstmt.setBoolean(4, respuesta.isEsCorrecta());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //GUARDAMOS LA RESPUESTA DE DESARROLLO
    public static boolean guardarRespuestaAbierta(RespuestaAbierta respuesta) {
        String sql = "INSERT INTO respuestas_abiertas (alumno_id, pregunta_id, texto_respuesta, puntuacion_obtenida) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, respuesta.getAlumnoId());
            pstmt.setInt(2, respuesta.getPreguntaId());
            pstmt.setString(3, respuesta.getTextoRespuesta());
            pstmt.setInt(4, 0);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    // RESULTADOS
    
    //GUARDAMOS EL RESULTADO
    public static boolean guardarResultado(Resultado resultado) {
        String sql = "INSERT INTO resultados (alumno_id, examen_id, nota, fecha_realizacion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, resultado.getAlumnoId());
            pstmt.setInt(2, resultado.getExamenId());
            pstmt.setInt(3, resultado.getNota());
            pstmt.setString(4, resultado.getFechaRealizacion());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //MOSTRAMOS LOS RESULTADOS DE UN ALUMNOI
    public static List<Resultado> listarResultadosPorAlumno(int alumnoId) {
        List<Resultado> lista = new ArrayList<>();
        String sql = "SELECT * FROM resultados WHERE alumno_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, alumnoId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Resultado r = new Resultado();
                r.setId(rs.getInt("id"));
                r.setAlumnoId(rs.getInt("alumno_id"));
                r.setExamenId(rs.getInt("examen_id"));
                r.setNota(rs.getInt("nota"));
                r.setFechaRealizacion(rs.getString("fecha_realizacion"));
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    //LISTAMOS LAS NOTAS DE UN EXAMEN
    public static List<Resultado> listarResultadosPorExamen(int examenId) {
        List<Resultado> lista = new ArrayList<>();
        String sql = "SELECT * FROM resultados WHERE examen_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, examenId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Resultado r = new Resultado();
                r.setId(rs.getInt("id"));
                r.setAlumnoId(rs.getInt("alumno_id"));
                r.setExamenId(rs.getInt("examen_id"));
                r.setNota(rs.getInt("nota"));
                r.setFechaRealizacion(rs.getString("fecha_realizacion"));
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    //HACE QUE UN ALUMNO NO PUEDA REPETIR EL EXAMEN
    public static boolean alumnoYaRealizoExamen(int alumnoId, int examenId) {
        String sql = "SELECT COUNT(*) FROM resultados WHERE alumno_id = ? AND examen_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, alumnoId);
            pstmt.setInt(2, examenId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    // AUDITORIA
   
    
    public static void registrarAuditoria(int usuarioId, String operacion, String detalle) {
        String sql = "INSERT INTO auditoria (usuario_id, operacion, detalle) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setString(2, operacion);
            pstmt.setString(3, detalle);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //PARA VER TODO EL HISTORIAL
    
    public static List<String> getHistorialAuditoria() {
        List<String> historial = new ArrayList<>();
        String sql = "SELECT a.*, u.nombre FROM auditoria a " +
                     "JOIN usuarios u ON a.usuario_id = u.id " +
                     "ORDER BY a.fecha_hora DESC LIMIT 50";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String registro = String.format("[%s] %s - %s: %s",
                    rs.getTimestamp("fecha_hora"),
                    rs.getString("nombre"),
                    rs.getString("operacion"),
                    rs.getString("detalle"));
                historial.add(registro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historial;
    }
}