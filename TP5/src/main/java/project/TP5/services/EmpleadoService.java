package project.TP5.services;

import project.TP5.exceptions.EmailDuplicadoException;
import project.TP5.exceptions.EmpleadoNoEncontradoException;
import project.TP5.models.Empleado;

import java.util.List;

public interface EmpleadoService {
    Empleado guardar(Empleado empleado) throws EmailDuplicadoException;
    Empleado buscarPorId(Long id) throws EmpleadoNoEncontradoException;
    List<Empleado> buscarPorDepartamento(String nombreDepartamento);
    List<Empleado> buscarPorRangoSalario(Double salarioMin, Double salarioMax);
    Double obtenerSalarioPromedioPorDepartamento(Long departamentoId);
    List<Empleado> obtenerTodos();
    Empleado actualizar(Long id, Empleado empleado) throws EmpleadoNoEncontradoException;
    void eliminar(Long id) throws EmpleadoNoEncontradoException;
}
