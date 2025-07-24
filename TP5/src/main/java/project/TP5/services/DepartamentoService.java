package project.TP5.services;

import project.TP5.exceptions.DepartamentoNoEncontradoException;
import project.TP5.models.Departamento;
import project.TP5.models.Empleado;

import java.util.List;

public interface DepartamentoService {
    Departamento guardar(Departamento departamento);
    Departamento buscarPorId(Long id) throws DepartamentoNoEncontradoException;
    List<Departamento> obtenerTodos();
    Departamento actualizar(Long id, Departamento departamento) throws DepartamentoNoEncontradoException;
    void eliminar(Long id) throws DepartamentoNoEncontradoException;

}
