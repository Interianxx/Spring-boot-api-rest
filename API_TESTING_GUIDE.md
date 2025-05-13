# Guía para probar los endpoints de Contactos

Esta guía te ayudará a probar los endpoints de la API de contactos que has implementado en tu aplicación Spring Boot con autenticación JWT.

## Herramientas necesarias

- [Postman](https://www.postman.com/downloads/) (o cualquier cliente HTTP similar)
- Tu aplicación Spring Boot ejecutándose localmente

## Paso 1: Iniciar la aplicación

Asegúrate de que tu aplicación Spring Boot esté en ejecución. Puedes iniciarla desde tu IDE o usando el comando:

```bash
mvn spring-boot:run
```

## Paso 2: Registrar un usuario (si aún no tienes uno)

1. Abre Postman
2. Crea una nueva solicitud POST a `http://localhost:8080/api/auth/signup`
3. En la pestaña "Body", selecciona "raw" y "JSON"
4. Ingresa los datos del usuario:

```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "role": ["user"]
}
```

5. Envía la solicitud y deberías recibir una respuesta exitosa

## Paso 3: Iniciar sesión para obtener el token JWT

1. Crea una nueva solicitud POST a `http://localhost:8080/api/auth/signin`
2. En la pestaña "Body", selecciona "raw" y "JSON"
3. Ingresa las credenciales:

```json
{
  "username": "testuser",
  "password": "password123"
}
```

4. Envía la solicitud
5. En la respuesta, copia el valor del token JWT (campo "accessToken")

## Paso 4: Usar el token para acceder a los endpoints de contactos

Para cada solicitud a los endpoints de contactos, debes incluir el token JWT en el encabezado de autorización:

1. En la pestaña "Headers" de tu solicitud en Postman
2. Agrega un nuevo encabezado:
   - Key: `Authorization`
   - Value: `Bearer tu_token_jwt` (reemplaza "tu_token_jwt" con el token que obtuviste)

## Paso 5: Probar los endpoints CRUD de contactos

### Crear un contacto (POST)

1. Crea una nueva solicitud POST a `http://localhost:8080/api/contacts`
2. Agrega el encabezado de autorización como se explicó anteriormente
3. En el cuerpo de la solicitud (Body), selecciona "raw" y "JSON"
4. Ingresa los datos del contacto:

```json
{
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "phone": "123456789",
  "notes": "Contacto de trabajo"
}
```

5. Envía la solicitud
6. Deberías recibir una respuesta con código 201 (Created) y los datos del contacto creado, incluyendo su ID

### Obtener todos los contactos (GET)

1. Crea una nueva solicitud GET a `http://localhost:8080/api/contacts`
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Deberías recibir una lista de todos tus contactos

### Obtener un contacto específico (GET)

1. Crea una nueva solicitud GET a `http://localhost:8080/api/contacts/{id}` (reemplaza {id} con el ID del contacto)
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Deberías recibir los datos del contacto solicitado

### Actualizar un contacto (PUT)

1. Crea una nueva solicitud PUT a `http://localhost:8080/api/contacts/{id}` (reemplaza {id} con el ID del contacto)
2. Agrega el encabezado de autorización
3. En el cuerpo de la solicitud, ingresa los datos actualizados:

```json
{
  "name": "Juan Pérez Actualizado",
  "email": "juan.nuevo@example.com",
  "phone": "987654321",
  "notes": "Contacto de trabajo actualizado"
}
```

4. Envía la solicitud
5. Deberías recibir los datos actualizados del contacto

### Eliminar un contacto (DELETE)

1. Crea una nueva solicitud DELETE a `http://localhost:8080/api/contacts/{id}` (reemplaza {id} con el ID del contacto)
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Deberías recibir un mensaje confirmando que el contacto fue eliminado

## Paso 6: Probar los endpoints CRUD de categorías

### Crear una categoría (POST)

1. Crea una nueva solicitud POST a `http://localhost:8080/api/categories`
2. Agrega el encabezado de autorización como se explicó anteriormente
3. En el cuerpo de la solicitud (Body), selecciona "raw" y "JSON"
4. Ingresa los datos de la categoría:

```json
{
  "name": "Trabajo",
  "description": "Contactos relacionados con el trabajo"
}
```

5. Envía la solicitud
6. Deberías recibir una respuesta con código 201 (Created) y los datos de la categoría creada, incluyendo su ID

### Obtener todas las categorías (GET)

1. Crea una nueva solicitud GET a `http://localhost:8080/api/categories`
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Deberías recibir una lista de todas tus categorías

### Obtener una categoría específica (GET)

1. Crea una nueva solicitud GET a `http://localhost:8080/api/categories/{id}` (reemplaza {id} con el ID de la categoría)
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Deberías recibir los datos de la categoría solicitada

### Actualizar una categoría (PUT)

1. Crea una nueva solicitud PUT a `http://localhost:8080/api/categories/{id}` (reemplaza {id} con el ID de la categoría)
2. Agrega el encabezado de autorización
3. En el cuerpo de la solicitud, ingresa los datos actualizados:

```json
{
  "name": "Trabajo Actualizado",
  "description": "Contactos profesionales y laborales"
}
```

4. Envía la solicitud
5. Deberías recibir los datos actualizados de la categoría

### Eliminar una categoría (DELETE)

1. Crea una nueva solicitud DELETE a `http://localhost:8080/api/categories/{id}` (reemplaza {id} con el ID de la categoría)
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Deberías recibir un mensaje confirmando que la categoría fue eliminada

## Paso 7: Asociar categorías con contactos

Ahora puedes asociar categorías con contactos al crear o actualizar un contacto. Para ello, debes incluir el campo `categoryIds` en el cuerpo de la solicitud:

### Crear un contacto con categorías

```json
{
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "phone": "123456789",
  "notes": "Contacto de trabajo",
  "categoryIds": [1, 2]
}
```

### Actualizar un contacto con categorías

```json
{
  "name": "Juan Pérez Actualizado",
  "email": "juan.nuevo@example.com",
  "phone": "987654321",
  "notes": "Contacto de trabajo actualizado",
  "categoryIds": [2, 3]
}
```

Al obtener un contacto o la lista de contactos, ahora recibirás también la información de las categorías asociadas:

```json
{
  "id": 1,
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "phone": "123456789",
  "notes": "Contacto de trabajo",
  "categories": [
    {
      "id": 1,
      "name": "Trabajo",
      "description": "Contactos relacionados con el trabajo"
    },
    {
      "id": 2,
      "name": "Amigos",
      "description": "Contactos personales"
    }
  ]
}
```

## Implementación del dropdown de categorías en el frontend

Si estás experimentando problemas con el dropdown de categorías en tu frontend, donde siempre se queda en "sin categoría" aunque selecciones una categoría, aquí hay una guía para implementarlo correctamente:

### 1. Obtener las categorías disponibles

Primero, necesitas cargar todas las categorías disponibles para el usuario:

```javascript
import axios from 'axios';

const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/categories', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error al cargar categorías:', error);
    return [];
  }
};
```

### 2. Crear el componente de dropdown

```jsx
import React, { useState, useEffect } from 'react';

function CategoryDropdown({ selectedCategories, onChange }) {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadCategories = async () => {
      setLoading(true);
      const data = await fetchCategories();
      setCategories(data);
      setLoading(false);
    };

    loadCategories();
  }, []);

  const handleChange = (event) => {
    // Convertir los valores seleccionados a números (IDs)
    const selectedIds = Array.from(event.target.selectedOptions, option => Number(option.value));
    onChange(selectedIds);
  };

  if (loading) return <p>Cargando categorías...</p>;

  return (
    <select 
      multiple 
      className="form-control" 
      onChange={handleChange}
      value={selectedCategories.map(id => id.toString())}
    >
      <option value="">Sin categoría</option>
      {categories.map(category => (
        <option key={category.id} value={category.id}>
          {category.name}
        </option>
      ))}
    </select>
  );
}
```

### 3. Usar el dropdown en el formulario de contacto

```jsx
import React, { useState } from 'react';
import CategoryDropdown from './CategoryDropdown';

function ContactForm({ onSubmit, initialData = {} }) {
  const [formData, setFormData] = useState({
    name: initialData.name || '',
    email: initialData.email || '',
    phone: initialData.phone || '',
    notes: initialData.notes || '',
    categoryIds: initialData.categoryIds || []
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleCategoryChange = (selectedCategoryIds) => {
    setFormData({
      ...formData,
      categoryIds: selectedCategoryIds
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="form-group">
        <label>Nombre:</label>
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleInputChange}
          className="form-control"
          required
        />
      </div>

      <div className="form-group">
        <label>Email:</label>
        <input
          type="email"
          name="email"
          value={formData.email}
          onChange={handleInputChange}
          className="form-control"
          required
        />
      </div>

      <div className="form-group">
        <label>Teléfono:</label>
        <input
          type="text"
          name="phone"
          value={formData.phone}
          onChange={handleInputChange}
          className="form-control"
        />
      </div>

      <div className="form-group">
        <label>Notas:</label>
        <textarea
          name="notes"
          value={formData.notes}
          onChange={handleInputChange}
          className="form-control"
        />
      </div>

      <div className="form-group">
        <label>Categorías:</label>
        <CategoryDropdown
          selectedCategories={formData.categoryIds}
          onChange={handleCategoryChange}
        />
        <small className="form-text text-muted">
          Puedes seleccionar múltiples categorías manteniendo presionada la tecla Ctrl (o Cmd en Mac).
        </small>
      </div>

      <button type="submit" className="btn btn-primary">
        {initialData.id ? 'Actualizar' : 'Crear'} Contacto
      </button>
    </form>
  );
}
```

### 4. Enviar los datos correctamente al backend

Al crear o actualizar un contacto, asegúrate de que estás enviando el campo `categoryIds` como un array de IDs:

```javascript
const createContact = async (contactData) => {
  try {
    const response = await axios.post('http://localhost:8080/api/contacts', contactData, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error al crear contacto:', error);
    throw error;
  }
};
```

### 5. Puntos importantes a verificar

- Asegúrate de que `categoryIds` sea un array de números, no de strings.
- Verifica en la consola del navegador que estás enviando correctamente el objeto JSON con el campo `categoryIds`.
- Si estás usando un framework como React, asegúrate de que el estado del componente se actualiza correctamente cuando seleccionas categorías.
- Verifica que las categorías se están cargando correctamente desde el backend.

## Paso 8: Probar los endpoints CRUD de eventos

La aplicación ahora incluye funcionalidad para gestionar eventos y recordatorios con visualización en calendario.

### Crear un evento (POST)

1. Crea una nueva solicitud POST a `http://localhost:8080/api/events`
2. Agrega el encabezado de autorización como se explicó anteriormente
3. En el cuerpo de la solicitud (Body), selecciona "raw" y "JSON"
4. Ingresa los datos del evento:

```json
{
  "title": "Reunión de trabajo",
  "description": "Discutir el nuevo proyecto",
  "startDateTime": "2023-06-15T10:00:00",
  "endDateTime": "2023-06-15T11:30:00"
}
```

5. Envía la solicitud
6. Deberías recibir una respuesta con código 201 (Created) y los datos del evento creado, incluyendo su ID

### Obtener todos los eventos (GET)

1. Crea una nueva solicitud GET a `http://localhost:8080/api/events`
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Deberías recibir una lista de todos tus eventos

### Obtener eventos por rango de fechas (GET)

1. Crea una nueva solicitud GET a `http://localhost:8080/api/events/range?start=2023-06-01T00:00:00&end=2023-06-30T23:59:59`
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Deberías recibir una lista de eventos dentro del rango de fechas especificado

### Obtener un evento específico (GET)

1. Crea una nueva solicitud GET a `http://localhost:8080/api/events/{id}` (reemplaza {id} con el ID del evento)
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Deberías recibir los datos del evento solicitado

### Actualizar un evento (PUT)

1. Crea una nueva solicitud PUT a `http://localhost:8080/api/events/{id}` (reemplaza {id} con el ID del evento)
2. Agrega el encabezado de autorización
3. En el cuerpo de la solicitud, ingresa los datos actualizados:

```json
{
  "title": "Reunión de trabajo actualizada",
  "description": "Discutir el progreso del proyecto",
  "startDateTime": "2023-06-15T11:00:00",
  "endDateTime": "2023-06-15T12:30:00"
}
```

4. Envía la solicitud
5. Deberías recibir los datos actualizados del evento

### Eliminar un evento (DELETE)

1. Crea una nueva solicitud DELETE a `http://localhost:8080/api/events/{id}` (reemplaza {id} con el ID del evento)
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Deberías recibir un mensaje confirmando que el evento fue eliminado

## Paso 9: Integración con FullCalendar en React

La API incluye un endpoint especial para obtener eventos en un formato compatible con FullCalendar, una popular biblioteca de JavaScript para mostrar calendarios interactivos.

### Obtener eventos para FullCalendar (GET)

1. Crea una nueva solicitud GET a `http://localhost:8080/api/events/calendar`
2. Opcionalmente, puedes agregar parámetros de rango de fechas: `http://localhost:8080/api/events/calendar?start=2023-06-01T00:00:00&end=2023-06-30T23:59:59`
3. Agrega el encabezado de autorización
4. Envía la solicitud
5. Deberías recibir una lista de eventos en formato compatible con FullCalendar

### Implementación en React

A continuación se muestra un ejemplo de cómo implementar FullCalendar en una aplicación React:

1. Instala las dependencias necesarias:

```bash
npm install @fullcalendar/react @fullcalendar/core @fullcalendar/daygrid @fullcalendar/timegrid @fullcalendar/interaction axios
```

2. Crea un componente de calendario:

```jsx
import React, { useState, useEffect } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import axios from 'axios';

function Calendar() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        setLoading(true);
        const response = await axios.get('http://localhost:8080/api/events/calendar', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        setEvents(response.data);
        setLoading(false);
      } catch (err) {
        setError('Error al cargar eventos: ' + err.message);
        setLoading(false);
      }
    };

    fetchEvents();
  }, []);

  const handleDateClick = (info) => {
    // Aquí puedes abrir un modal para crear un nuevo evento
    console.log('Fecha seleccionada:', info.dateStr);
  };

  const handleEventClick = (info) => {
    // Aquí puedes abrir un modal para ver/editar/eliminar el evento
    console.log('Evento seleccionado:', info.event.title);
  };

  if (loading) return <div>Cargando eventos...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="calendar-container">
      <FullCalendar
        plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
        initialView="dayGridMonth"
        headerToolbar={{
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay'
        }}
        events={events}
        dateClick={handleDateClick}
        eventClick={handleEventClick}
        height="auto"
        locale="es"
      />
    </div>
  );
}

export default Calendar;
```

3. Implementa un formulario para crear/editar eventos:

```jsx
import React, { useState } from 'react';
import axios from 'axios';

function EventForm({ onSubmit, initialData = {}, onCancel }) {
  const [formData, setFormData] = useState({
    title: initialData.title || '',
    description: initialData.description || '',
    startDateTime: initialData.start || '',
    endDateTime: initialData.end || ''
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      let response;

      if (initialData.id) {
        // Actualizar evento existente
        response = await axios.put(`http://localhost:8080/api/events/${initialData.id}`, formData, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
      } else {
        // Crear nuevo evento
        response = await axios.post('http://localhost:8080/api/events', formData, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
      }

      onSubmit(response.data);
    } catch (error) {
      console.error('Error al guardar evento:', error);
      alert('Error al guardar el evento. Por favor, inténtalo de nuevo.');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="form-group">
        <label>Título:</label>
        <input
          type="text"
          name="title"
          value={formData.title}
          onChange={handleInputChange}
          className="form-control"
          required
        />
      </div>

      <div className="form-group">
        <label>Descripción:</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleInputChange}
          className="form-control"
        />
      </div>

      <div className="form-group">
        <label>Fecha y hora de inicio:</label>
        <input
          type="datetime-local"
          name="startDateTime"
          value={formData.startDateTime}
          onChange={handleInputChange}
          className="form-control"
          required
        />
      </div>

      <div className="form-group">
        <label>Fecha y hora de fin:</label>
        <input
          type="datetime-local"
          name="endDateTime"
          value={formData.endDateTime}
          onChange={handleInputChange}
          className="form-control"
        />
      </div>

      <div className="form-group buttons">
        <button type="submit" className="btn btn-primary">
          {initialData.id ? 'Actualizar' : 'Crear'} Evento
        </button>
        <button type="button" className="btn btn-secondary" onClick={onCancel}>
          Cancelar
        </button>
      </div>
    </form>
  );
}

export default EventForm;
```

4. Integra estos componentes en tu aplicación React y asegúrate de que el usuario esté autenticado antes de acceder a la página de calendario.

## Paso 10: Alertas y Notificaciones

La aplicación ahora incluye funcionalidad para enviar recordatorios automáticos por correo electrónico y mostrar notificaciones dentro de la plataforma al iniciar sesión.

### Configuración de Correo Electrónico

Para habilitar el envío de correos electrónicos, debes configurar las propiedades de correo en el archivo `application.properties`:

```properties
# Email Configuration (Gmail example - change according to your email provider)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=tu-email@gmail.com
spring.mail.password=tu-contraseña-de-aplicación
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Nota importante para Gmail**: Si estás usando Gmail, debes generar una "contraseña de aplicación" en lugar de usar tu contraseña normal. Puedes hacerlo en la configuración de seguridad de tu cuenta de Google.

### Recordatorios Automáticos por Correo

La aplicación enviará automáticamente recordatorios por correo electrónico para eventos próximos. Puedes configurar cuántos días antes del evento se envía el recordatorio:

```properties
# Reminder Configuration
app.reminder.days-before=1
app.reminder.enabled=true
```

### Notificaciones en la Plataforma

Cuando inicias sesión, la respuesta incluirá el número de notificaciones no leídas:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "roles": ["ROLE_USER"],
  "unreadNotifications": 3
}
```

### Endpoints de Notificaciones

#### Obtener todas las notificaciones

1. Crea una nueva solicitud GET a `http://localhost:8080/api/notifications`
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Recibirás una lista de todas tus notificaciones

#### Obtener notificaciones no leídas

1. Crea una nueva solicitud GET a `http://localhost:8080/api/notifications/unread`
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Recibirás una lista de tus notificaciones no leídas

#### Obtener el conteo de notificaciones no leídas

1. Crea una nueva solicitud GET a `http://localhost:8080/api/notifications/count`
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Recibirás un objeto con el conteo de notificaciones no leídas:

```json
{
  "count": 3
}
```

#### Marcar una notificación como leída

1. Crea una nueva solicitud PUT a `http://localhost:8080/api/notifications/{id}/read` (reemplaza {id} con el ID de la notificación)
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Recibirás un mensaje confirmando que la notificación fue marcada como leída

#### Marcar todas las notificaciones como leídas

1. Crea una nueva solicitud PUT a `http://localhost:8080/api/notifications/read-all`
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Recibirás un mensaje confirmando cuántas notificaciones fueron marcadas como leídas

#### Eliminar una notificación

1. Crea una nueva solicitud DELETE a `http://localhost:8080/api/notifications/{id}` (reemplaza {id} con el ID de la notificación)
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Recibirás un mensaje confirmando que la notificación fue eliminada

#### Enviar un correo electrónico de prueba

Este endpoint te permite enviar un correo electrónico de prueba para verificar que la configuración de correo electrónico funciona correctamente:

1. Crea una nueva solicitud POST a `http://localhost:8080/api/notifications/send-test-email`
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Recibirás un mensaje confirmando que el correo electrónico de prueba fue enviado a tu dirección de correo

```json
{
  "message": "Test email sent to tu-email@example.com"
}
```

#### Enviar un recordatorio por correo electrónico para un evento específico

Este endpoint te permite enviar manualmente un recordatorio por correo electrónico para un evento específico:

1. Crea una nueva solicitud POST a `http://localhost:8080/api/notifications/send-event-reminder/{eventId}` (reemplaza {eventId} con el ID del evento)
2. Agrega el encabezado de autorización
3. Envía la solicitud
4. Recibirás un mensaje confirmando que el recordatorio fue enviado

```json
{
  "message": "Event reminder sent for: Reunión de trabajo"
}
```

### Implementación en React

A continuación se muestra un ejemplo de cómo implementar un centro de notificaciones en React:

```jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './NotificationCenter.css';

function NotificationCenter() {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showDropdown, setShowDropdown] = useState(false);
  const [sendingEmail, setSendingEmail] = useState(false);

  useEffect(() => {
    fetchNotifications();
  }, []);

  const fetchNotifications = async () => {
    try {
      setLoading(true);
      const response = await axios.get('http://localhost:8080/api/notifications/unread', {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      setNotifications(response.data);
      setLoading(false);
    } catch (err) {
      setError('Error al cargar notificaciones');
      setLoading(false);
    }
  };

  const markAsRead = async (id) => {
    try {
      await axios.put(`http://localhost:8080/api/notifications/${id}/read`, {}, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      // Actualizar la lista de notificaciones
      setNotifications(notifications.filter(notification => notification.id !== id));
    } catch (err) {
      console.error('Error al marcar notificación como leída:', err);
    }
  };

  const markAllAsRead = async () => {
    try {
      await axios.put('http://localhost:8080/api/notifications/read-all', {}, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      // Limpiar la lista de notificaciones
      setNotifications([]);
    } catch (err) {
      console.error('Error al marcar todas las notificaciones como leídas:', err);
    }
  };

  const sendTestEmail = async () => {
    try {
      setSendingEmail(true);
      const response = await axios.post('http://localhost:8080/api/notifications/send-test-email', {}, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      alert(response.data.message);
      setSendingEmail(false);
    } catch (err) {
      console.error('Error al enviar correo de prueba:', err);
      alert('Error al enviar correo de prueba: ' + (err.response?.data?.message || err.message));
      setSendingEmail(false);
    }
  };

  const sendEventReminder = async (eventId) => {
    try {
      setSendingEmail(true);
      const response = await axios.post(`http://localhost:8080/api/notifications/send-event-reminder/${eventId}`, {}, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      alert(response.data.message);
      setSendingEmail(false);
      // Actualizar notificaciones después de enviar el recordatorio
      fetchNotifications();
    } catch (err) {
      console.error('Error al enviar recordatorio:', err);
      alert('Error al enviar recordatorio: ' + (err.response?.data?.message || err.message));
      setSendingEmail(false);
    }
  };

  const toggleDropdown = () => {
    setShowDropdown(!showDropdown);
  };

  return (
    <div className="notification-center">
      <div className="notification-icon" onClick={toggleDropdown}>
        <i className="fa fa-bell"></i>
        {notifications.length > 0 && (
          <span className="notification-badge">{notifications.length}</span>
        )}
      </div>

      {showDropdown && (
        <div className="notification-dropdown">
          <div className="notification-header">
            <h3>Notificaciones</h3>
            <div className="notification-actions">
              {notifications.length > 0 && (
                <button onClick={markAllAsRead} className="mark-all-read">
                  Marcar todas como leídas
                </button>
              )}
              <button 
                onClick={sendTestEmail} 
                className="send-test-email"
                disabled={sendingEmail}
              >
                {sendingEmail ? 'Enviando...' : 'Enviar correo de prueba'}
              </button>
            </div>
          </div>

          <div className="notification-list">
            {loading ? (
              <div className="notification-loading">Cargando...</div>
            ) : error ? (
              <div className="notification-error">{error}</div>
            ) : notifications.length === 0 ? (
              <div className="no-notifications">No tienes notificaciones nuevas</div>
            ) : (
              notifications.map(notification => (
                <div key={notification.id} className="notification-item">
                  <div className="notification-content">
                    <div className="notification-message">{notification.message}</div>
                    <div className="notification-details">{notification.details}</div>
                    <div className="notification-time">
                      {new Date(notification.createdAt).toLocaleString()}
                    </div>
                  </div>
                  <div className="notification-buttons">
                    <button 
                      onClick={() => markAsRead(notification.id)} 
                      className="mark-read-button"
                    >
                      Marcar como leída
                    </button>
                    {notification.eventId && (
                      <button 
                        onClick={() => sendEventReminder(notification.eventId)} 
                        className="send-reminder-button"
                        disabled={sendingEmail}
                      >
                        Enviar recordatorio
                      </button>
                    )}
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default NotificationCenter;
```

Y el CSS correspondiente:

```css
.notification-center {
  position: relative;
  display: inline-block;
}

.notification-icon {
  cursor: pointer;
  padding: 10px;
  font-size: 20px;
  position: relative;
}

.notification-badge {
  position: absolute;
  top: 0;
  right: 0;
  background-color: red;
  color: white;
  border-radius: 50%;
  width: 18px;
  height: 18px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.notification-dropdown {
  position: absolute;
  right: 0;
  top: 40px;
  width: 350px;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.notification-header {
  display: flex;
  flex-direction: column;
  padding: 10px 15px;
  border-bottom: 1px solid #eee;
}

.notification-header h3 {
  margin: 0 0 10px 0;
  font-size: 16px;
}

.notification-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.mark-all-read, .send-test-email {
  background: none;
  border: 1px solid #ddd;
  border-radius: 4px;
  color: #007bff;
  cursor: pointer;
  font-size: 12px;
  padding: 5px 10px;
  margin-right: 5px;
}

.send-test-email {
  background-color: #f8f9fa;
}

.send-test-email:disabled {
  color: #999;
  cursor: not-allowed;
}

.notification-list {
  max-height: 300px;
  overflow-y: auto;
}

.notification-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
  display: flex;
  flex-direction: column;
}

.notification-content {
  flex: 1;
  margin-bottom: 10px;
}

.notification-message {
  font-weight: bold;
  margin-bottom: 5px;
}

.notification-details {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.notification-time {
  font-size: 12px;
  color: #999;
}

.notification-buttons {
  display: flex;
  justify-content: flex-start;
}

.mark-read-button, .send-reminder-button {
  background: none;
  border: 1px solid #ddd;
  border-radius: 4px;
  color: #007bff;
  cursor: pointer;
  font-size: 12px;
  padding: 5px 10px;
  margin-right: 5px;
  white-space: nowrap;
}

.send-reminder-button {
  background-color: #f8f9fa;
}

.send-reminder-button:disabled {
  color: #999;
  cursor: not-allowed;
}

.no-notifications, .notification-loading, .notification-error {
  padding: 20px;
  text-align: center;
  color: #666;
}
```

## Solución de problemas

- **Error 401 (Unauthorized)**: Verifica que estás incluyendo correctamente el token JWT en el encabezado de autorización.
- **Error 403 (Forbidden)**: Verifica que el usuario tiene los permisos necesarios.
- **Error 404 (Not Found)**: Verifica que el ID del contacto, categoría o evento existe.
- **Error al enviar correos electrónicos**: Verifica la configuración de correo en application.properties. Si estás usando Gmail, asegúrate de usar una "contraseña de aplicación" y no tu contraseña normal.

### Solución de problemas con notificaciones por correo

Si las notificaciones por correo no funcionan, puedes seguir estos pasos para diagnosticar el problema:

1. **Verifica la configuración de correo**: Asegúrate de que las propiedades de correo en `application.properties` son correctas.
2. **Envía un correo de prueba**: Usa el endpoint `POST /api/notifications/send-test-email` para enviar un correo de prueba y verificar si la configuración funciona.
3. **Revisa los logs del servidor**: Busca mensajes de error relacionados con el envío de correos.
4. **Verifica la dirección de correo del usuario**: Asegúrate de que el usuario tiene una dirección de correo válida en su perfil.
5. **Comprueba la carpeta de spam**: A veces, los correos automáticos pueden ser marcados como spam.

Si sigues teniendo problemas, puedes implementar el componente NotificationCenter con el botón "Enviar correo de prueba" para diagnosticar el problema desde el frontend.
- **Error 400 (Bad Request)**: Verifica que los datos enviados son válidos según las restricciones definidas.
- **Problemas de CORS**: Si estás accediendo a la API desde una aplicación frontend (como React) y recibes errores de CORS, asegúrate de que la configuración CORS en el backend esté correctamente configurada. La aplicación ahora tiene habilitada la configuración CORS para permitir solicitudes desde cualquier origen.
- **Formato de fecha y hora**: Asegúrate de que estás enviando las fechas y horas en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS).

## Ejemplo de flujo completo

1. Registrar un usuario (si es necesario)
2. Iniciar sesión para obtener el token JWT
3. Crear un nuevo contacto
4. Obtener la lista de contactos para verificar que se creó correctamente
5. Obtener el contacto específico por su ID
6. Actualizar el contacto
7. Verificar que se actualizó correctamente
8. Eliminar el contacto
9. Verificar que se eliminó correctamente

## Uso desde una aplicación React

Si estás consumiendo esta API desde una aplicación React, sigue estas recomendaciones para evitar problemas de sesión:

### 1. Configuración correcta de Axios

Es crucial configurar Axios correctamente para que incluya el token JWT en todas las peticiones:

```javascript
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/';

const axiosInstance = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para añadir el token JWT a TODAS las peticiones
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para manejar errores de respuesta (401, 403)
axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      // Token expirado o inválido, redirigir al login
      console.log('Sesión expirada o no autorizada');
      // Opcional: redirigir al login
      // window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
```

### 2. Manejo adecuado de la autenticación

Es importante guardar correctamente el token y verificar su existencia antes de navegar a rutas protegidas:

```javascript
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/';

class AuthService {
  login(username, password) {
    return axios
      .post(API_URL + 'signin', {
        username,
        password
      })
      .then(response => {
        if (response.data.accessToken) {
          // Guardar el token y la información del usuario
          localStorage.setItem('user', JSON.stringify(response.data));
          localStorage.setItem('token', response.data.accessToken);

          // Importante: establecer el token en el header por defecto para todas las peticiones futuras
          axios.defaults.headers.common['Authorization'] = 'Bearer ' + response.data.accessToken;
        }
        return response.data;
      });
  }

  logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    // Eliminar el header de autorización
    delete axios.defaults.headers.common['Authorization'];
  }

  register(username, email, password) {
    return axios.post(API_URL + 'signup', {
      username,
      email,
      password
    });
  }

  getCurrentUser() {
    return JSON.parse(localStorage.getItem('user'));
  }

  // Método para verificar si el usuario está autenticado
  isAuthenticated() {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    return token && user;
  }
}

export default new AuthService();
```

### 3. Componente de protección de rutas

Crea un componente para proteger las rutas que requieren autenticación:

```javascript
import React from 'react';
import { Navigate } from 'react-router-dom';
import AuthService from './AuthService';

// Componente para proteger rutas que requieren autenticación
const ProtectedRoute = ({ children }) => {
  const isAuthenticated = AuthService.isAuthenticated();

  if (!isAuthenticated) {
    // Redirigir al login si no está autenticado
    return <Navigate to="/login" replace />;
  }

  return children;
};

// Uso en tu Router:
// <Route path="/contacts" element={<ProtectedRoute><ContactsPage /></ProtectedRoute>} />
```

### 4. Servicio para contactos

Asegúrate de usar la instancia de Axios configurada con los interceptores:

```javascript
import api from './api'; // Importa la instancia de Axios configurada

class ContactService {
  getAll() {
    return api.get('contacts');
  }

  get(id) {
    return api.get(`contacts/${id}`);
  }

  create(data) {
    return api.post('contacts', data);
  }

  update(id, data) {
    return api.put(`contacts/${id}`, data);
  }

  delete(id) {
    return api.delete(`contacts/${id}`);
  }
}

export default new ContactService();
```

### 5. Verificación de autenticación al iniciar la aplicación

En tu componente principal (App.js), verifica y configura la autenticación al cargar:

```javascript
import React, { useEffect } from 'react';
import axios from 'axios';
import AuthService from './services/AuthService';

function App() {
  useEffect(() => {
    // Verificar si hay un token guardado y configurarlo en Axios
    const token = localStorage.getItem('token');
    if (token) {
      axios.defaults.headers.common['Authorization'] = 'Bearer ' + token;
    }
  }, []);

  return (
    <div className="App">
      {/* Contenido de tu aplicación */}
    </div>
  );
}
```

### 6. Solución de problemas comunes de sesión

Si sigues experimentando problemas donde la sesión se cierra al navegar a la página de contactos:

1. **Verifica la consola del navegador**: Busca errores 401 o 403 que indiquen problemas de autenticación.

2. **Comprueba el almacenamiento local**: Asegúrate de que el token se guarda correctamente en localStorage y no se elimina al navegar.

3. **Usa herramientas de desarrollo**: En Chrome, ve a la pestaña "Application" > "Storage" > "Local Storage" para verificar que el token existe.

4. **Verifica las peticiones HTTP**: En la pestaña "Network", asegúrate de que el token se envía en el encabezado de autorización.

5. **Evita recargar la página**: Si recargas la página manualmente (F5), asegúrate de que el token se recupera y configura correctamente.

6. **Implementa un sistema de refresco de token**: Si el token expira, implementa un sistema para obtener un nuevo token sin requerir que el usuario inicie sesión nuevamente.

Con esta configuración mejorada, tu aplicación React debería mantener la sesión correctamente al navegar entre páginas, incluyendo la página de contactos.

## Solución específica para el problema de cierre de sesión al navegar a contactos

Si específicamente estás experimentando el problema donde "cuando inicio sesion y cambio a contactos me cierra la sesion y me dice que no tengo permisos", esto puede deberse a varias causas:

1. **El token no se está guardando correctamente**: Asegúrate de que después de iniciar sesión, el token se guarda en localStorage y se configura en los headers de Axios.

2. **El token no se está enviando en las peticiones**: Verifica que todas las peticiones a `/api/contacts` incluyan el header de autorización.

3. **Problemas con la navegación en React**: Si estás usando React Router, asegúrate de que la navegación entre páginas no está causando que se pierda el estado de la aplicación.

4. **Recarga manual de la página**: Si estás recargando la página manualmente (F5) al navegar a contactos, el token debe recuperarse de localStorage y configurarse nuevamente.

### Ejemplo de implementación para la página de contactos

```javascript
import React, { useState, useEffect } from 'react';
import ContactService from '../services/ContactService';
import AuthService from '../services/AuthService';
import { useNavigate } from 'react-router-dom';

function ContactsPage() {
  const [contacts, setContacts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    // Verificar autenticación antes de cargar contactos
    if (!AuthService.isAuthenticated()) {
      navigate('/login');
      return;
    }

    // Cargar contactos
    setLoading(true);
    ContactService.getAll()
      .then(response => {
        setContacts(response.data);
        setLoading(false);
      })
      .catch(err => {
        console.error('Error cargando contactos:', err);
        setError('Error al cargar los contactos. Por favor, intenta de nuevo.');
        setLoading(false);

        // Si hay un error de autenticación, redirigir al login
        if (err.response && (err.response.status === 401 || err.response.status === 403)) {
          AuthService.logout(); // Limpiar datos de sesión
          navigate('/login');
        }
      });
  }, [navigate]);

  if (loading) return <div>Cargando contactos...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="contacts-page">
      <h1>Mis Contactos</h1>
      {contacts.length === 0 ? (
        <p>No tienes contactos guardados.</p>
      ) : (
        <ul className="contacts-list">
          {contacts.map(contact => (
            <li key={contact.id} className="contact-item">
              <h3>{contact.name}</h3>
              <p>Email: {contact.email}</p>
              <p>Teléfono: {contact.phone}</p>
              <p>Notas: {contact.notes}</p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default ContactsPage;
```

Con esta implementación, deberías poder navegar a la página de contactos sin perder la sesión, siempre que el token JWT sea válido y se esté enviando correctamente en las peticiones.
