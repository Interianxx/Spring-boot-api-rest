# Implementación de Notificaciones por Correo Electrónico

## Problema Resuelto

Se ha solucionado el problema donde las notificaciones por correo electrónico no funcionaban correctamente desde el frontend. El sistema ya tenía implementada la funcionalidad para enviar correos automáticamente a través del programador de tareas (scheduler), pero no había una forma para que el frontend pudiera desencadenar estos envíos manualmente.

## Cambios Realizados

### 1. Nuevos Endpoints en NotificationController

Se han agregado dos nuevos endpoints al controlador de notificaciones:

#### Enviar un correo electrónico de prueba

```java
@PostMapping("/send-test-email")
public ResponseEntity<?> sendTestEmail() {
    try {
        UserDetailsImpl userDetails = getCurrentUser();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String to = user.getEmail();
        if (to == null || to.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not have an email address"));
        }
        
        String subject = "Test Email from Your Agenda App";
        String text = "Hello " + user.getUsername() + ",\n\n" +
                "This is a test email from your Agenda App to verify that email notifications are working correctly.\n\n" +
                "If you received this email, it means that the email notification system is configured correctly.\n\n" +
                "Regards,\nYour Agenda App Team";
        
        emailService.sendSimpleEmail(to, subject, text);
        logger.info("Test email sent to {}", to);
        
        return ResponseEntity.ok(new MessageResponse("Test email sent to " + to));
    } catch (Exception e) {
        logger.error("Failed to send test email: {}", e.getMessage());
        return ResponseEntity.badRequest().body(new MessageResponse("Failed to send test email: " + e.getMessage()));
    }
}
```

#### Enviar un recordatorio por correo electrónico para un evento específico

```java
@PostMapping("/send-event-reminder/{eventId}")
public ResponseEntity<?> sendEventReminder(@PathVariable Long eventId) {
    try {
        UserDetailsImpl userDetails = getCurrentUser();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        // Check if the event belongs to the current user
        if (!event.getUser().getId().equals(userDetails.getId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You don't have permission to send a reminder for this event"));
        }
        
        // Send email reminder
        emailService.sendEventReminderEmail(user, event);
        logger.info("Event reminder email sent to {} for event: {}", user.getEmail(), event.getTitle());
        
        // Create notification
        String message = "Reminder: " + event.getTitle();
        String details = "You requested a reminder for this event.";
        notificationService.createEventNotification(user, message, details, event);
        
        return ResponseEntity.ok(new MessageResponse("Event reminder sent for: " + event.getTitle()));
    } catch (Exception e) {
        logger.error("Failed to send event reminder: {}", e.getMessage());
        return ResponseEntity.badRequest().body(new MessageResponse("Failed to send event reminder: " + e.getMessage()));
    }
}
```

### 2. Actualización de la Documentación

Se ha actualizado el archivo `API_TESTING_GUIDE.md` con:

1. Documentación detallada de los nuevos endpoints
2. Ejemplos de cómo usar estos endpoints desde el frontend
3. Un componente React actualizado que incluye botones para enviar correos de prueba y recordatorios
4. Estilos CSS actualizados para los nuevos botones
5. Una sección de solución de problemas específica para notificaciones por correo

## Cómo Funciona

### Envío Automático de Correos (Existente)

El sistema ya tenía implementada la funcionalidad para enviar correos automáticamente:

1. El `ReminderScheduler` se ejecuta diariamente a las 8:00 AM
2. Llama a `notificationService.createEventReminderNotifications()`
3. Este método busca eventos próximos y envía recordatorios por correo

### Envío Manual de Correos (Nuevo)

Ahora, el frontend puede desencadenar el envío de correos manualmente:

1. **Correo de prueba**: Permite al usuario verificar que la configuración de correo funciona correctamente
2. **Recordatorio de evento**: Permite al usuario enviar un recordatorio para un evento específico en cualquier momento

## Implementación en el Frontend

Se ha proporcionado un componente React actualizado que incluye:

1. Un botón "Enviar correo de prueba" en el centro de notificaciones
2. Un botón "Enviar recordatorio" para cada notificación relacionada con un evento
3. Manejo adecuado de estados de carga y errores

## Solución de Problemas

Si las notificaciones por correo no funcionan, se pueden seguir estos pasos:

1. **Verificar la configuración de correo**: Asegurarse de que las propiedades en `application.properties` son correctas
2. **Enviar un correo de prueba**: Usar el nuevo endpoint para verificar la configuración
3. **Revisar los logs del servidor**: Buscar mensajes de error relacionados con el envío de correos
4. **Verificar la dirección de correo del usuario**: Asegurarse de que el usuario tiene una dirección de correo válida
5. **Comprobar la carpeta de spam**: A veces, los correos automáticos pueden ser marcados como spam

## Mantenimiento Futuro

Si en el futuro se necesitan agregar más tipos de notificaciones por correo, se puede seguir el mismo patrón:

1. Agregar un nuevo método en `EmailService` si es necesario
2. Agregar un nuevo endpoint en `NotificationController`
3. Actualizar la documentación y el componente React