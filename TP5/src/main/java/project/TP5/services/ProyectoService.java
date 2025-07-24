package project.TP5.services;


import project.TP5.exceptions.ProyectoNoEncontradoException;
import project.TP5.models.Proyecto;

import java.util.List;

public interface ProyectoService{
    Proyecto guardar(Proyecto proyecto);
    Proyecto buscarPorId(Long id) throws ProyectoNoEncontradoException;
    Proyecto actualizar(Long id, Proyecto proyecto) throws ProyectoNoEncontradoException;
    void eliminar(Long id) throws ProyectoNoEncontradoException;
    List<Proyecto> obtenerTodos();
    List<Proyecto> buscarProyectosActivos();
}
