# Implementación de Contactos Favoritos

Este documento describe la implementación de la funcionalidad de contactos favoritos o destacados en la aplicación.

## Características Implementadas

1. **Campo de favorito en contactos**: Se ha agregado un campo booleano `isFavorite` a la entidad Contact para marcar contactos como favoritos.
2. **Marcar/desmarcar favoritos**: Se ha implementado un endpoint para alternar fácilmente el estado de favorito de un contacto.
3. **Filtrado de favoritos**: Se ha agregado un endpoint para obtener solo los contactos marcados como favoritos.

## Cambios Realizados

### Modelo de Datos

- Se agregó el campo `isFavorite` (boolean) a la clase `Contact` con un valor predeterminado de `false`.
- Se actualizaron los constructores para incluir este nuevo campo.
- Se agregaron los métodos getter y setter para el campo `isFavorite`.

### Clases de Solicitud y Respuesta

- Se agregó el campo `isFavorite` a `ContactRequest` para permitir establecer el estado de favorito al crear o actualizar contactos.
- Se agregó el campo `isFavorite` a `ContactResponse` para devolver el estado de favorito en las respuestas de la API.

### Controlador de Contactos

- Se modificó el método `createContact` para establecer el estado de favorito desde la solicitud.
- Se modificó el método `updateContact` para actualizar el estado de favorito.
- Se agregó un nuevo endpoint `toggle-favorite` para alternar el estado de favorito de un contacto.
- Se agregó un nuevo endpoint `favorites` para obtener solo los contactos marcados como favoritos.

### Repositorio de Contactos

- Se agregó un método `findByUserIdAndIsFavoriteTrue` para encontrar contactos favoritos por ID de usuario.

## Cómo Usar la Funcionalidad

### Crear un Contacto como Favorito

```json
POST /api/contacts
{
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "phone": "123456789",
  "notes": "Contacto importante",
  "favorite": true
}
```

### Actualizar el Estado de Favorito

```json
PUT /api/contacts/{id}
{
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "phone": "123456789",
  "notes": "Contacto importante",
  "favorite": true
}
```

### Alternar el Estado de Favorito

```
PUT /api/contacts/{id}/toggle-favorite
```

Este endpoint no requiere cuerpo en la solicitud. Simplemente alterna el estado actual: si era favorito, lo desmarca; si no era favorito, lo marca.

### Obtener Solo Contactos Favoritos

```
GET /api/contacts/favorites
```

## Integración con Frontend

Para integrar esta funcionalidad en el frontend, se recomienda:

1. **Mostrar indicador de favorito**: Agregar un icono de estrella o corazón junto a cada contacto que se pueda hacer clic para marcar/desmarcar como favorito.

2. **Sección de favoritos**: Crear una sección separada o una pestaña para mostrar solo los contactos favoritos.

3. **Ordenación**: Mostrar los contactos favoritos primero en la lista general de contactos.

### Ejemplo de Componente React para Mostrar Favoritos

```jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import authHeader from './auth-header';

function FavoriteContacts() {
  const [favorites, setFavorites] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchFavorites();
  }, []);

  const fetchFavorites = async () => {
    try {
      setLoading(true);
      const response = await axios.get('http://localhost:8080/api/contacts/favorites', {
        headers: authHeader()
      });
      setFavorites(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching favorite contacts:', error);
      setLoading(false);
    }
  };

  const toggleFavorite = async (id) => {
    try {
      await axios.put(`http://localhost:8080/api/contacts/${id}/toggle-favorite`, {}, {
        headers: authHeader()
      });
      // Actualizar la lista después de cambiar el estado
      fetchFavorites();
    } catch (error) {
      console.error('Error toggling favorite status:', error);
    }
  };

  if (loading) return <div>Cargando contactos favoritos...</div>;

  return (
    <div className="favorites-container">
      <h2>Contactos Destacados</h2>
      {favorites.length === 0 ? (
        <p>No tienes contactos destacados. Marca algunos como favoritos para verlos aquí.</p>
      ) : (
        <ul className="favorites-list">
          {favorites.map(contact => (
            <li key={contact.id} className="favorite-item">
              <div className="contact-info">
                <h3>{contact.name}</h3>
                <p>{contact.email}</p>
                <p>{contact.phone}</p>
              </div>
              <button 
                onClick={() => toggleFavorite(contact.id)}
                className="unfavorite-button"
              >
                ★ Quitar de favoritos
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default FavoriteContacts;
```

### Ejemplo de CSS para Contactos Favoritos

```css
.contact-item {
  position: relative;
  padding: 15px;
  border: 1px solid #ddd;
  margin-bottom: 10px;
  border-radius: 4px;
}

.contact-item.favorite {
  border-left: 4px solid gold;
  background-color: #fffdf0;
}

.favorite-toggle {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #ccc;
}

.favorite-toggle.active {
  color: gold;
}

.favorites-section {
  margin-bottom: 30px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.favorites-section h2 {
  margin-top: 0;
  color: #333;
  border-bottom: 2px solid gold;
  padding-bottom: 10px;
}
```

## Conclusión

La implementación de contactos favoritos mejora la experiencia del usuario al permitirle destacar y acceder rápidamente a sus contactos más importantes. Esta funcionalidad es especialmente útil cuando el usuario tiene muchos contactos y necesita encontrar rápidamente los más relevantes.