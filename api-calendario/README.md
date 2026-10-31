# API Calendario

API REST desarrollada con **Java, Spring Boot y PostgreSQL** para generar y consultar el calendario laboral de un año.

Este proyecto forma parte del Taller 1 de Arquitectura de Software II del ITM y consume la API Festivos que ya está implementada en este repositorio.

## Funcionalidades

- Consultar los festivos de un año en API Festivos.
- Generar todos los días del año, incluyendo el 29 de febrero en años bisiestos.
- Clasificar las fechas como día laboral, fin de semana o día festivo.
- Guardar el calendario completo en PostgreSQL.
- Consultar los días almacenados de un año en orden de fecha.
- Validar los años recibidos y manejar errores de conexión o calendarios repetidos.

## Arquitectura

La aplicación sigue **nuestro diagrama de arquitectura Onion** y toma como referencia la organización del proyecto de monedas del profesor.

El proyecto tiene un POM padre y cinco módulos Maven:

- **Dominio:** entidades `Calendario` y `Tipo`, DTOs y excepciones. Son clases Java sin dependencias de Spring ni JPA.
- **Core:** interfaces de los servicios, repositorios y consulta a API Festivos.
- **Aplicación:** `CalendarioServicio`, donde se genera, clasifica y consulta el calendario.
- **Infraestructura:** entidades JPA, repositorios, mapeadores y cliente HTTP de API Festivos.
- **Presentación:** controlador REST, clase principal, configuración de Swagger y manejo de errores.

Las entidades del dominio y las entidades JPA están separadas. Los mapeadores convierten los datos entre ambas, como en el ejemplo del docente. Aplicación trabaja con las interfaces de core; infraestructura implementa el acceso a PostgreSQL y la comunicación con Festivos.

**[Ver nuestro diagrama de arquitectura](../diagramas/diagrama-arquitectura-apicalendario-onion.md)**

## Base de datos

La API utiliza la base **`calendariolaboral`** existente en PostgreSQL, con las tablas `tipo` y `calendario`.

Las categorías que utiliza son:

| Id | Tipo |
|---|---|
| 1 | Día laboral |
| 2 | Fin de Semana |
| 3 | Día festivo |

La aplicación valida la estructura al iniciar. No crea la base, no crea tablas ni carga categorías. Los espacios al principio o al final de los nombres se ignoran al leerlos, sin cambiar los datos guardados.

**[Ver el modelo relacional](../diagramas/diagrama-relacional-calendariolaboral.md)**

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/calendario/generar/{anio}` | Generar y guardar el calendario del año |
| GET | `/api/calendario/listar/{anio}` | Consultar los días almacenados del año |

Se conservan las rutas y el método GET que aparecen en el ejemplo del enunciado.

La generación devuelve `true` cuando termina correctamente. La consulta devuelve una lista con `id`, `fecha`, `tipo` y `descripcion`. El tipo incluye su identificador y nombre; la descripción corresponde al día de la semana.

## Ejecución local

Se requiere **Java 21, Maven, PostgreSQL** y la base de datos ya preparada. Las versiones Java 21 y Spring Boot 3.3.5 corresponden al ejemplo del profesor.

Desde la carpeta `api-calendario`, compilar y generar el ejecutable:

```powershell
mvn package
```

La terminal debe estar configurada para usar Java 21. Puedes revisar las versiones con `java -version` y `mvn -version`.

Para iniciar la API, configurar la conexión en esa terminal:

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/calendariolaboral'
$env:DB_USUARIO = 'postgres'
$claveCalendario = Read-Host 'Contraseña de PostgreSQL' -AsSecureString
$env:DB_CLAVE = [System.Net.NetworkCredential]::new('', $claveCalendario).Password
$env:FESTIVOS_URL = 'http://localhost:3030/api/festivos'

java -jar .\presentacion\target\presentacion-0.0.1-SNAPSHOT.jar
```

La contraseña se recibe por una variable de entorno y no se guarda en el repositorio. La conexión también puede configurarse mediante las variables de entorno del IDE.

La API estará disponible en:

http://localhost:8080

Para generar calendarios, API Festivos debe estar encendida. Desde su carpeta, en otra terminal:

```powershell
cd ..\api-festivos
npm.cmd start
```

MongoDB también debe estar en ejecución. Los calendarios que ya están guardados se pueden consultar aunque API Festivos esté apagada.

## Funcionamiento y validaciones

- Se permiten años entre **1900 y 9999**, igual que en API Festivos.
- Se consulta una sola vez `http://localhost:3030/api/festivos/{anio}`, usando sus campos actuales: `nombre`, `fecha` y `tipo`.
- Un festivo tiene prioridad sobre sábado o domingo. Si dos festivos coinciden, se guarda un solo día.
- El tipo de cálculo de API Festivos, de 1 a 4, es independiente de las tres categorías del calendario.
- Si el año ya tiene registros, se responde **409** y se conserva lo que está guardado.
- Los días se guardan en una transacción: si ocurre un error, el lote completo se revierte.
- Una consulta sin registros devuelve una lista vacía: `[]`.

| Estado | Situación |
|---|---|
| 400 | Año inválido |
| 404 / 405 | Ruta inexistente o método HTTP no permitido |
| 409 | Año existente o conflicto al guardar |
| 502 / 504 | Error de API Festivos o tiempo de espera agotado |
| 500 | Error interno o categorías mal configuradas |

El puerto se puede cambiar con `PORT`. La URL de Festivos usa `FESTIVOS_URL`; los tiempos de conexión y lectura se configuran con `FESTIVOS_TIMEOUT_CONEXION` y `FESTIVOS_TIMEOUT_LECTURA`, cuyos valores por defecto son `3s` y `10s`.

## Documentación Swagger

Los endpoints se pueden consultar y probar desde:

http://localhost:8080/api-docs

La descripción OpenAPI está disponible en:

http://localhost:8080/v3/api-docs

## Estado

La API Calendario está implementada y organizada según nuestro diagrama Onion.

Se comprobó la integración con API Festivos y PostgreSQL, la generación de calendarios de 365 y 366 días y el manejo de solicitudes del mismo año. La conexión con la base existente del proyecto también fue verificada.

API Festivos conserva su implementación original.
