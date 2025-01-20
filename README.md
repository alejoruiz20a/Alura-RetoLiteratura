# LiterAlura - Aplicación de Consola para Libros

## Descripción

**LiterAlura** es una aplicación de consola en Java que permite buscar libros a través de la API de Gutendex y almacenarlos en una base de datos PostgreSQL. Además, ofrece diversas funciones para consultar y gestionar la información de libros y autores guardados.

La aplicación está diseñada con una arquitectura modular, implementando patrones de diseño como **Repositorio** y **Servicio** para mejorar la organización, escalabilidad y mantenimiento del código.

## Funcionalidades

- **Buscar libro por título**: Consulta la API de Gutendex y guarda el libro en la base de datos si no está registrado.
- **Listar libros almacenados**: Muestra todos los libros guardados en la base de datos.
- **Listar autores registrados**: Presenta un listado de los autores disponibles en la base de datos.
- **Buscar autores vivos en un año específico**: Filtra y muestra los autores que estaban vivos en un determinado año.
- **Listar libros por idioma**: Permite consultar libros según su idioma.

## Requisitos

Para ejecutar la aplicación, necesitas:

- **Java 8 o superior**
- **PostgreSQL**
- **Clave de API de Gutendex**

## Configuración

Antes de iniciar la aplicación, configura los siguientes valores en `application.properties`:

```properties
spring.application.name=literalura

spring.datasource.url=jdbc:postgresql://${DB_HOST}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
hibernate.dialect=org.hibernate.dialect.HSQLDialect

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true
spring.jpa.format-sql=true
```

Sustituye `${DB_HOST}`, `${DB_USER}`, `${DB_NAME}` y `${DB_PASSWORD}` con las credenciales correspondientes a tu entorno.

## Instalación y Ejecución

1. Clona el repositorio en tu equipo.
2. Configura las credenciales en `application.properties`.
3. Ejecuta la aplicación desde tu IDE o con el siguiente comando:

   ```sh
   mvn spring-boot:run
   ```

## Uso

Al ejecutar la aplicación, sigue las instrucciones en pantalla para navegar por las distintas opciones. Selecciona el número correspondiente a la acción deseada y sigue las indicaciones para interactuar con la aplicación.

## Autor

- **Juan José Jaramillo Vera**  
- **Contacto**: [juanjojaramillovera@gmail.com]

