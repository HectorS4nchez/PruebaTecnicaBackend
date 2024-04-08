# Prueba Técnica Backend

Este proyecto es una API RESTful desarrollada con Spring Boot, que demuestra operaciones CRUD para la gestión de propiedades en un contexto inmobiliario. Incorpora una arquitectura hexagonal, separando las capas de dominio, aplicación e infraestructura para garantizar una base de código limpia y mantenible.

## Configuración e Instalación

### Prerrequisitos

Java JDK 11 o posterior - Maven - MySQL

### Configuración de la Base de Datos

Usa el archivo Docker Compose proporcionado para configurar una base de datos MySQL y ejecuta el siguiente comando para iniciar la base de datos:

**docker-compose up -d**

## Ejecutando la Aplicación

Para ejecutar la aplicación, utiliza el siguiente comando de Maven:

**mvn spring-boot:run**

## Pruebas

Para ejecutar las pruebas unitarias e integración:

**mvn test**

## Puntos de Extremo de la API

La API proporciona los siguientes puntos de extremo para la gestión de propiedades:

* **POST** /api/properties - Crear una nueva propiedad

* **GET** /api/properties - Recuperar todas las propiedades

* **GET** /api/properties/{id} - Recuperar una propiedad por su ID

* **PUT** /api/properties/{id} - Actualizar una propiedad

* **DELETE** /api/properties/{id} - Eliminar una propiedad (borrado suave)

* **PATCH** /api/properties/{id}/rent - Marcar una propiedad como alquilada

Consulta las clases controladoras para obtener más información detallada sobre los puntos de extremo y los formatos de solicitud/respuesta esperados.

