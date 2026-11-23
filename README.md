# Servicios del huerto escolar

Servicios independientes con Java 17, Spring Boot 3.3.5, Spring MVC, Spring Security, Spring Data JPA y una base H2 de archivo por proceso. **Inventario** mantiene materiales disponibles. **Agenda** mantiene jornadas del taller. Las cuentas y evidencias de las tareas escolares siguen en la base Room de cada instalación.

## Iniciar ambos procesos

Usa Maven 3.9 y Java 17. En dos terminales desde esta carpeta establece la misma contraseña académica, por ejemplo con `read -rs HUERTO_API_CLAVE; export HUERTO_API_CLAVE`. En la primera ejecuta `mvn -f inventario/pom.xml spring-boot:run` y en la segunda `mvn -f agenda/pom.xml spring-boot:run`. Usuario de servicio: docente. Los archivos H2 se crean bajo datos en el directorio de ejecución. Reutiliza el mismo directorio al reiniciar para comprobar persistencia.

| Servicio | Colección | Cuerpo JSON |
|---|---|---|
| Inventario, puerto 8081 | /api/insumos | {"nombre":"Semillas","cantidad":12} |
| Agenda, puerto 8082 | /api/jornadas | {"nombre":"Taller de riego","fecha":"2026-11-25"} |

Ambos admiten GET de colección, GET por /{id}, POST en colección, PUT y DELETE por /{id}. POST devuelve 201 y DELETE 204. El servidor exige autenticación HTTP Basic y valida el nombre y el valor. El ID lo genera la base. Mantén estos servicios de demostración restringidos al equipo del laboratorio.

## Conectar Android

Con el dispositivo conectado por USB o emulador activo, ejecutar `adb reverse tcp:8081 tcp:8081` y `adb reverse tcp:8082 tcp:8082`. La aplicación abre 127.0.0.1 mediante esos túneles. Iniciar sesión local como docente, abrir uno de los módulos remotos e ingresar la contraseña que configuraste en el servidor. Crear un registro, consultar, tocar la tarjeta para modificarlo y eliminarlo con confirmación. Reiniciar Spring entre creación y consulta permite verificar la base de archivo.

## Estructura

Cada módulo tiene su propio pom, Application (inicio y seguridad), entidad JPA y controlador. El controlador mapea HTTP, valida Entrada y usa JpaRepository para insertar, actualizar o consultar. La API meteorológica se consume directamente desde Android. El proyecto usa una ubicación pública fija para San Bernardo y no solicita GPS.
