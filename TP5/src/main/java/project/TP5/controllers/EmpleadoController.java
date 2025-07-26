package project.TP5.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.TP5.models.Empleado;
import project.TP5.services.EmpleadoService;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@Validated
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    /**
     * Obtiene una lista de todos los empleados.
     * @return Lista de empleados.
     */
    @GetMapping
    public List<Empleado> obtenerTodos() {
        return empleadoService.obtenerTodos();
    }

    /**
     * Obtiene un empleado por su ID.
     * @param id El ID del empleado a buscar.
     * @return El empleado encontrado.
     */
    @GetMapping("/{id}")
    public Empleado obtenerPorId(@PathVariable Long id) {
        return empleadoService.buscarPorId(id);
    }

    /**
     * Crea un nuevo empleado.
     * @param empleado El empleado a crear.
     * @return El empleado creado.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Empleado crear(@RequestBody Empleado empleado) {
        return empleadoService.guardar(empleado);
    }

    /**
     * Actualiza un empleado existente.
     * @param id El ID del empleado a actualizar.
     * @param empleado Los datos actualizados del empleado.
     * @return El empleado actualizado.
     */
    @PutMapping("/{id}")
    public Empleado actualizar(@PathVariable Long id, @RequestBody Empleado empleado) {
        return empleadoService.actualizar(id, empleado);
    }

    /**
     * Elimina un empleado por su ID.
     * @param id El ID del empleado a eliminar.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
    }

    /**
     * Obtiene una lista de empleados por departamento.
     * @param nombre El nombre del departamento.
     * @return Lista de empleados en el departamento.
     */
    @GetMapping("/departamento/{nombre}")
    public List<Empleado> obtenerPorDepartamento(@PathVariable String nombre) {
        return empleadoService.buscarPorDepartamento(nombre);
    }

    /**
     * Obtiene una lista de empleados por rango de salario.
     * @param min El salario mínimo.
     * @param max El salario máximo.
     * @return Lista de empleados dentro del rango de salario.
     */
    @GetMapping("/salario")
    public List<Empleado> obtenerPorRangoSalario(
            @RequestParam Double min,
            @RequestParam Double max) {
        return empleadoService.buscarPorRangoSalario(min, max);
    }
}
