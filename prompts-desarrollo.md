# Prompts de desarrollo

## Prompt 1: Buscando la manera más eficiente de manejar las excepciones en los controllers y entender que hace Validated

### Prompt utilizado: "Estoy haciendo una API REST y tengo este controller de ejemplo: [Codigo del controller de empleado] y tengo 2 dudas:¿Cuáles serían las mejores formas de tratar las excepciones que van a mandar los servicios? y ¿Qué hace el validated? La segunda tambien implica saber por qué me da error en la linea 33"

### Respuesta Recibida: "✅ 1. ¿Qué hace @Validated en el controlador?
La anotación @Validated (de org.springframework.validation.annotation.Validated) habilita la validación de parámetros en métodos del controlador, ya sea:

Parámetros del path (@PathVariable),

Parámetros de la query (@RequestParam),

O directamente los objetos del body que tienen anotaciones como @Valid.

Es complementaria a @Valid, pero se enfoca en la validación a nivel de clase, no solo en el @RequestBody.

⚠️ ¿Por qué te da error en la línea 33?
java
Copiar
Editar
public Empleado crear(@Valid @RequestBody Empleado empleado)
El error más probable está en el uso de @Valid, que requiere que el objeto Empleado tenga anotaciones de validación en sus atributos. Por ejemplo:

java
Copiar
Editar
public class Empleado {

    @NotBlank
    private String nombre;

    @NotNull
    @Positive
    private Double salario;

    // getters y setters...
}
Si no tenés las dependencias de Bean Validation (jakarta.validation, Hibernate Validator, etc.) o si el modelo Empleado no tiene ninguna validación, puede explotar con errores tipo:

yaml
Copiar
Editar
javax.validation.ConstraintDeclarationException: No validator could be found for constraint ...
✅ 2. ¿Cómo manejar las excepciones correctamente?
Hay tres formas principales de hacerlo en Spring Boot:

🧱 A. Usar @ControllerAdvice (Recomendado)
Creás una clase para manejar errores globales:

java
Copiar
Editar
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(NoSuchElementException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return errors;
    }

    // Podés agregar más tipos de excepciones según lo necesites
}
🧱 B. Validaciones con @Valid o @Validated + excepciones personalizadas
Tu EmpleadoService puede tirar excepciones como:

java
Copiar
Editar
public Empleado buscarPorId(Long id) {
    return empleadoRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Empleado no encontrado"));
}
Y esas excepciones se manejan en el @ControllerAdvice.

🧱 C. Devolver ResponseEntity con errores manualmente
No tan limpio como el ControllerAdvice, pero posible:

java
Copiar
Editar
@GetMapping("/{id}")
public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
    try {
        Empleado emp = empleadoService.buscarPorId(id);
        return ResponseEntity.ok(emp);
    } catch (NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(Map.of("error", e.getMessage()));
    }
}
🧾 Pro tip: Validación de parámetros simples
Si querés validar que min y max en @RequestParam sean mayores a cero, necesitás activar validación con anotaciones en los argumentos del método:

java
Copiar
Editar
@GetMapping("/salario")
public List<Empleado> obtenerPorRangoSalario(
        @RequestParam @DecimalMin("0.0") Double min,
        @RequestParam @DecimalMin("0.0") Double max) {
    return empleadoService.buscarPorRangoSalario(min, max);
}
Y asegurate de que @Validated esté en el controlador para que esto funcione.

✅ Conclusión
Tema	Resumen
@Validated	Habilita la validación a nivel de clase para argumentos como @RequestParam y @PathVariable. Se usa junto con @Valid.
Error en línea 33	Seguramente porque el modelo Empleado no tiene anotaciones de validación o faltan dependencias de jakarta.validation.
Manejo de excepciones	Lo ideal es usar @ControllerAdvice y definir manejadores globales para errores comunes (no encontrado, validación, etc.).

¿Querés que te ayude a armar un Empleado con validaciones y un @ControllerAdvice base para empezar?"

### Modificaciones Realizadas:
- [Eliminar el Valid]

### Explicación del prompt: La idea principal es saber cuál sería la forma más eficiente de manejar las excepciones en los controller, pero además saber porque me daba error en la linea 33

### Aprendizajes Obtenidos:
- [Validated como decorador para validar datos]
- [Aprender distintas formas de manejar las excepciones en una API]

## Prompt 2: Aprender a como usar el ControllerAdvice

### Prompt utilizado: "Supongamos que quiero usar el ControllerAdvice ¿Cómo funciona y cómo podría utilizarlo?"

### Respuesta Recibida: "Perfecto. Usar @ControllerAdvice en una API REST de Spring Boot es la forma más limpia y escalable de manejar errores globalmente. Te permite capturar excepciones lanzadas desde cualquier controlador y devolver respuestas bien estructuradas.

🧱 ¿Qué es @ControllerAdvice?
Es una anotación que marca una clase como manejador global de excepciones y validaciones. Puede:

Manejar excepciones con @ExceptionHandler

Procesar respuestas con @ResponseBody o ResponseEntity

Personalizar errores de validación (@Valid, @Validated)

Ser usado en conjunto con @RestControllerAdvice (que implica @ControllerAdvice + @ResponseBody)

✅ Ejemplo completo: GlobalExceptionHandler
Creamos una clase aparte, como GlobalExceptionHandler.java:

java
Copiar
Editar
package project.TP5.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Error de validación de campos (@Valid falló)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });
        return errores;
    }

    // Recurso no encontrado
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchElement(NoSuchElementException ex) {
        return Map.of("error", ex.getMessage());
    }

    // Error genérico
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericException(Exception ex) {
        return Map.of("error", "Ocurrió un error inesperado");
    }
}
✅ Cómo se conecta con tu controlador
Tu controlador o servicio puede lanzar, por ejemplo:

java
Copiar
Editar
public Empleado buscarPorId(Long id) {
    return empleadoRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Empleado con ID " + id + " no encontrado"));
}
Y automáticamente, sin hacer nada más, Spring:

Detecta el NoSuchElementException

Llama al handleNoSuchElement del GlobalExceptionHandler

Devuelve un JSON como:

json
Copiar
Editar
{
  "error": "Empleado con ID 5 no encontrado"
}
✅ Validaciones con @Valid
Si tu modelo Empleado tiene validaciones como:

java
Copiar
Editar
import jakarta.validation.constraints.*;

public class Empleado {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotNull(message = "El salario es obligatorio")
    @Min(value = 0, message = "El salario no puede ser negativo")
    private Double salario;

    // getters y setters...
}
Y hacés un POST /api/empleados con un JSON inválido, MethodArgumentNotValidException será capturada por tu handler y se devolverá algo como:

json
Copiar
Editar
{
  "nombre": "El nombre no puede estar vacío",
  "salario": "El salario es obligatorio"
}
📌 Consejos útiles
Si usás @RestControllerAdvice no hace falta poner @ResponseBody en cada método.

Los métodos de @ExceptionHandler pueden devolver ResponseEntity<...> si querés controlar el status y headers más finamente.

Podés crear tus propias excepciones personalizadas (EmpleadoNoEncontradoException) y atraparlas también.

¿Querés que te genere un ejemplo con una excepción personalizada y cómo manejarla también con el @ControllerAdvice?"

### Modificaciones Realizadas:
- Se creó la clase GlobalExceptionHandler

### Explicación del Prompt: Quería saber si vale la pena usar este metodo o sería mejor usar otro

### Aprendizajes Obtenidos:
- Aprendí de la existencia del Exception Handler como forma eficiente de manejar excepciones en una API REST con Spring Boot
