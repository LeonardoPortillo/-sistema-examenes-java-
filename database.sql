-- =====================================================
-- BASE DE DATOS: SISTEMA DE EXÁMENES
-- =====================================================

-- 1. CREAR LA BASE DE DATOS
-- =====================================================
CREATE DATABASE IF NOT EXISTS sistema_examenes;
USE sistema_examenes;

-- =====================================================
-- 2. TABLA: USUARIOS (profesores y alumnos)
-- =====================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol ENUM('profesor', 'alumno') DEFAULT 'alumno',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- 3. TABLA: EXAMENES
-- =====================================================
CREATE TABLE IF NOT EXISTS examenes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    fecha_creacion DATE NOT NULL,
    usuario_id INT NOT NULL,
    tipo ENUM('test', 'desarrollo', 'ambos') DEFAULT 'test',
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- =====================================================
-- 4. TABLA: PREGUNTAS (TEST - opción múltiple)
-- =====================================================
CREATE TABLE IF NOT EXISTS preguntas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    texto TEXT NOT NULL,
    opcion_a TEXT NOT NULL,
    opcion_b TEXT NOT NULL,
    opcion_c TEXT NOT NULL,
    opcion_d TEXT NOT NULL,
    respuesta_correcta ENUM('A', 'B', 'C', 'D') NOT NULL,
    examen_id INT NULL,
    profesor_id INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (examen_id) REFERENCES examenes(id) ON DELETE SET NULL,
    FOREIGN KEY (profesor_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- =====================================================
-- 5. TABLA: PREGUNTAS_ABIERTAS (DESARROLLO)
-- =====================================================
CREATE TABLE IF NOT EXISTS preguntas_abiertas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    texto TEXT NOT NULL,
    respuesta_esperada TEXT NOT NULL,
    puntuacion_maxima INT NOT NULL DEFAULT 10,
    examen_id INT NULL,
    profesor_id INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (examen_id) REFERENCES examenes(id) ON DELETE SET NULL,
    FOREIGN KEY (profesor_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- =====================================================
-- 6. TABLA: RESPUESTAS (para preguntas TEST)
-- =====================================================
CREATE TABLE IF NOT EXISTS respuestas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    alumno_id INT NOT NULL,
    pregunta_id INT NOT NULL,
    respuesta_alumno ENUM('A', 'B', 'C', 'D') NOT NULL,
    es_correcta BOOLEAN NOT NULL,
    fecha_respuesta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (alumno_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (pregunta_id) REFERENCES preguntas(id) ON DELETE CASCADE
);

-- =====================================================
-- 7. TABLA: RESPUESTAS_ABIERTAS (para preguntas DESARROLLO)
-- =====================================================
CREATE TABLE IF NOT EXISTS respuestas_abiertas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    alumno_id INT NOT NULL,
    pregunta_id INT NOT NULL,
    texto_respuesta TEXT NOT NULL,
    puntuacion_obtenida INT DEFAULT 0,
    corregida BOOLEAN DEFAULT FALSE,
    fecha_respuesta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (alumno_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (pregunta_id) REFERENCES preguntas_abiertas(id) ON DELETE CASCADE
);

-- =====================================================
-- 8. TABLA: RESULTADOS
-- =====================================================
CREATE TABLE IF NOT EXISTS resultados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    alumno_id INT NOT NULL,
    examen_id INT NOT NULL,
    nota INT NOT NULL,
    fecha_realizacion DATE NOT NULL,
    FOREIGN KEY (alumno_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (examen_id) REFERENCES examenes(id) ON DELETE CASCADE
);

-- =====================================================
-- 9. TABLA: AUDITORIA
-- =====================================================
CREATE TABLE IF NOT EXISTS auditoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    operacion VARCHAR(100) NOT NULL,
    detalle TEXT,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);
