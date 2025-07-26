package project.TP5.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.TP5.models.Proyecto;
import project.TP5.services.ProyectoService;

import java.util.List;

@RestController
@RequestMapping("/api/proyecto")
@Validated
public class ProyectoController {
    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    /**
     * Obtiene una lista de todos los proyectos.
     * @return Lista de proyectos.
     */
    @GetMapping
    public List<Proyecto> obtenerTodos() {
        return proyectoService.obtenerTodos();
    }

    /**
     * Obtiene un proyecto por su ID.
     * @param id El ID del proyecto a buscar.
     * @return El proyecto encontrado.
     */

    @GetMapping("/{id}")
    public Proyecto obtenerPorId(@PathVariable Long id) {
        return proyectoService.buscarPorId(id);
    }

    /**
     * Crea un nuevo proyecto.
     * @param proyecto El proyecto a crear.
     * @return El proyecto creado.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Proyecto crear(@RequestBody Proyecto proyecto) {
        return proyectoService.guardar(proyecto);
    }

    /**
     * Actualiza un proyecto existente.
     * @param id El ID del proyecto a actualizar.
     * @param proyecto Los datos actualizados del proyecto.
     * @return El proyecto actualizado.
     */
    @PutMapping("/{id}")
    public Proyecto actualizar(@PathVariable Long id, @RequestBody Proyecto proyecto) {
        return proyectoService.actualizar(id, proyecto);
    }

    /**
     * Elimina un proyecto por su ID.
     * @param id El ID del proyecto a eliminar.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        proyectoService.eliminar(id);
    }

    /**
     * Obtiene una lista de proyectos activos.
     * @return Lista de proyectos activos.
     */
    @GetMapping("/activos")
    public List<Proyecto> obtenerPorDepartamento() {
        return proyectoService.buscarProyectosActivos();
    }
}
