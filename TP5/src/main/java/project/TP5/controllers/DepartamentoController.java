package project.TP5.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.TP5.models.Departamento;
import project.TP5.services.DepartamentoService;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@Validated
public class DepartamentoController {
    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    /**
     * Obtiene una lista de todos los departamentos.
     * @return Lista de departamentos.
     */
    @GetMapping
    public List<Departamento> obtenerTodos() {
        return departamentoService.obtenerTodos();
    }

    /**
     * Obtiene un departamento por su ID.
     * @param id El ID del departamento a buscar.
     * @return El departamento encontrado.
     */
    @GetMapping("/{id}")
    public Departamento obtenerPorId(@PathVariable Long id) {
        return departamentoService.buscarPorId(id);
    }

    /**
     * Crea un nuevo departamento.
     * @param departamento El departamento a crear.
     * @return El departamento creado.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Departamento crear(@RequestBody Departamento departamento) {
        return departamentoService.guardar(departamento);
    }

    /**
     * Actualiza un departamento existente.
     * @param id El ID del departamento a actualizar.
     * @param departamento Los datos actualizados del departamento.
     * @return El departamento actualizado.
     */
    @PutMapping("/{id}")
    public Departamento actualizar(@PathVariable Long id, @RequestBody Departamento departamento) {
        return departamentoService.actualizar(id, departamento);
    }

    /**
     * Elimina un departamento por su ID.
     * @param id El ID del departamento a eliminar.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        departamentoService.eliminar(id);
    }

}
