# Mini Twitter - TP Individual

## Autor
**Agustín Fernández Gómez**

---

## Consigna

**Universidad Nacional de Río Negro**  
Taller de Tecnologías y Producción de Software  
Lic. En Sistemas

### Trabajo Práctico General

Vamos a construir una versión muy simplificada de Twitter con una arquitectura monolítica de back-end. El back-end y el front-end son aplicaciones separadas que se comunican vía servicios web.

### Requerimientos a modelar

1. Cada usuario conoce todos los Tweets que hizo.
2. Un tweet puede ser re-tweet de otro, y este tweet debe conocer a su tweet de origen.
3. Un re-tweet no tiene texto adicional.
4. Los tweets de un usuario se deben eliminar cuando el usuario es eliminado. No existen tweets no referenciados por un usuario.
5. No se pueden agregar dos usuarios con el mismo userName.
6. userName no puede ser menor a 5 caracteres ni mayor a 25.
7. Los tweets deben tener un texto de 1 carácter como mínimo y 280 caracteres como máximo.
8. No se debe permitir crear un re-tweet de un tweet creado por el mismo usuario que re-twittea.

### Se pide

1. Modelar en objetos.
2. Escribir test unitarios.
3. Mapear para persistir y generar los servicios de backend.
4. Escribir test de integración usando una BD en memoria.
5. Exponer los servicios de backend como Servicios Web.
6. Escribir test de integración de la capa Web.
7. Lograr cobertura alta (>= 90%).
8. Implementar el front-end cuya navegabilidad se describe más adelante.

---

## Front-end

La home page debe tener la siguiente estructura:

- **Header Menú**:
  - Nombre del sistema
  - Link a la home page
  - Link para crear un nuevo tweet

- **Panel principal**:
  - Se muestran los tweets (no incluir ReTweets) de cualquier usuario, paginados de a 10 por página.
  - Navegación de paginación (adelante/atrás, botones deshabilitados si no hay más).

- **Panel izquierdo**:
  - Lista de usuarios del sistema (solo userNames).
  - Al clickear en un usuario, se muestran sus últimos 15 tweets en el panel principal.
  - Si es re-tweet, mostrar la fecha de retweet, el nombre del usuario que retwitteó y los datos originales del tweet.
  - Botón "Mostrar más" para ver los siguientes 15 tweets, hasta que no haya más (cambia a "No hay más..." si se termina).

- **Crear nuevo tweet**:
  - Formulario con:
    - Input para el userid del creador
    - Input para el texto del tweet
    - Botón para crear
  - Indicar éxito o falla en la creación.

---

## Tecnologías usadas

- <img src="https://img.shields.io/badge/Java-17%2B-blue?logo=java" alt="Java" height="20"/> Java 17+
- <img src="https://img.shields.io/badge/JUnit-5-green?logo=junit5" alt="JUnit 5" height="20"/> JUnit 5 para tests unitarios e integración
- <img src="https://img.shields.io/badge/Maven-Build%20Tool-blue?logo=apachemaven" alt="Maven" height="20"/> Maven para gestión de dependencias y build
- <img src="https://img.shields.io/badge/H2-Database-lightgrey?logo=h2" alt="H2 Database" height="20"/> H2 Database (o similar) para tests de integración con base de datos en memoria
- <img src="https://img.shields.io/badge/Hibernate-Persistencia-59666C?logo=hibernate" alt="Hibernate" height="20"/> Hibernate para persistencia de datos
- <img src="https://img.shields.io/badge/Spring%20Boot-Web%20Backend-6DB33F?logo=springboot" alt="Spring Boot" height="20"/> Spring Boot para servicios web REST
- <img src="https://img.shields.io/badge/REST-API-orange?logo=rest" alt="REST API" height="20"/> REST API para comunicación entre front-end y back-end
- <img src="https://img.shields.io/badge/React-Front--end-61DAFB?logo=react" alt="React" height="20"/> React para el front-end
- <img src="https://img.shields.io/badge/HTML%2FCSS%2FJS-Frontend-yellow?logo=html5" alt="HTML/CSS/JavaScript" height="20"/> HTML/CSS/JavaScript para el front-end

---

> Trabajo Práctico General - Mini Twitter  
> Universidad Nacional de Río Negro - Lic. en Sistemas  
> Autor: Agustín Fernández Gómez
