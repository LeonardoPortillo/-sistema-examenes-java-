# 📚 Sistema de Gestión de Exámenes

## 📋 Descripción
Aplicación de escritorio desarrollada en **Java** para la gestión de exámenes educativos. Permite a profesores crear preguntas (tipo test y desarrollo), generar exámenes de forma aleatoria y a los alumnos realizarlos desde el ordenador.

## 🚀 Tecnologías utilizadas
| Tecnología | Uso |
|------------|-----|
| Java 8/11/17 | Lenguaje principal |
| Swing | Interfaz gráfica |
| MySQL | Base de datos |
| JDBC | Conexión a base de datos |
| Git & GitHub | Control de versiones |

## 📋 Funcionalidades principales

### Para profesores
- ✅ Login seguro con contraseñas cifradas (SHA-256)
- ✅ Crear, editar y eliminar preguntas (test y desarrollo)
- ✅ Biblioteca de preguntas reutilizables
- ✅ Generar exámenes (aleatorio, manual o mixto)
- ✅ Exportar exámenes a formato imprimible (.txt)
- ✅ Ver resultados de los alumnos
- ✅ Auditoría completa de todas las acciones

### Para alumnos
- ✅ Registro de nuevos alumnos
- ✅ Realizar exámenes tipo test (corrección automática)
- ✅ Realizar exámenes de desarrollo
- ✅ Ver historial de resultados

## 🗄️ Estructura de la base de datos
```sql
-- 8 tablas principales:
-- usuarios, examenes, preguntas, preguntas_abiertas,
-- respuestas, respuestas_abiertas, resultados, auditoria
