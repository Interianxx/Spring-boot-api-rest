# Proyecto de Gestión de Contactos y Categorías

Este proyecto es una aplicación web desarrollada con **Spring Boot** en el backend y **React** en el frontend. Su objetivo es gestionar contactos y categorías, con autenticación basada en tokens JWT.

## Características

- **Autenticación y Autorización**:
  - Inicio de sesión y registro de usuarios.
  - Protección de rutas en el frontend.
  - Uso de tokens JWT para la autenticación.

- **Gestión de Contactos**:
  - Crear, leer, actualizar y eliminar contactos.
  - Listar contactos asociados a un usuario autenticado.

- **Gestión de Categorías**:
  - Crear y listar categorías por usuario.
  - Relación entre categorías y contactos.

- **Interfaz de Usuario**:
  - Navegación protegida con React Router.
  - Manejo de errores y mensajes de estado.

## Tecnologías Utilizadas

### Backend
- **Java** y **Spring Boot**:
  - Spring Security para autenticación y autorización.
  - Spring Data JPA para acceso a la base de datos.
  - Base de datos relacional (por ejemplo, MySQL o PostgreSQL).

### Frontend
- **React**:
  - React Router para la navegación.
  - Axios para las peticiones HTTP.

### Otros
- **Maven** para la gestión de dependencias.
- **JWT** para la autenticación.
- **Axios** para la comunicación entre el frontend y el backend.

## Estructura del Proyecto

### Backend
- `src/main/java/com/bezkoder/springjwt/models`: Contiene las entidades del modelo de datos.
- `src/main/java/com/bezkoder/springjwt/repository`: Repositorios JPA para interactuar con la base de datos.
- `src/main/java/com/bezkoder/springjwt/controllers`: Controladores REST para manejar las solicitudes HTTP.
- `src/main/java/com/bezkoder/springjwt/security`: Configuración de seguridad y manejo de JWT.

### Frontend
- `src/services`: Servicios para interactuar con la API (por ejemplo, `AuthService` y `ContactService`).
- `src/pages`: Componentes principales de las páginas (por ejemplo, `ContactsPage`).
- `src/components`: Componentes reutilizables (por ejemplo, `ProtectedRoute`).

## Instalación y Configuración

### Backend
1. Clona el repositorio.
2. Configura la base de datos en el archivo `application.properties` o `application.yml`.
3. Ejecuta el proyecto con Maven:
   ```bash
   mvn spring-boot:run

## Esta aplicacion fue basada en un proyecto de SpringBoot con Autenticacion de JWT, sin embargo, el calendario, las notificaciones, el crud de contactos y categorias fue por nuestra cuenta, a continuación enviare las paginas en donde partimos para el proyecto

- https://www.bezkoder.com/spring-boot-react-jwt-auth/
- https://www.bezkoder.com/spring-boot-react-jwt-auth/

## Autores 

- **Nombre**: [Jose Alejandro Interian Pech, Carla Escalante, Jose Ramirez]
- **Email**: [al070687@uacam.mx, al064216@uacam.mx, al064255@uacam.mx]]
