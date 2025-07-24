package project.TP5.services;

import project.TP5.exceptions.DepartamentoNoEncontradoException;
import project.TP5.models.Departamento;
import project.TP5.repositories.DepartamentoRepository;

import java.util.List;

public class DepertamentoServiceImpl implements DepartamentoService {
    private final DepartamentoRepository departamentoRepository;

    public DepertamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Departamento guardar(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento buscarPorId(Long id) throws DepartamentoNoEncontradoException {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Proyecto no encontrado con ID: " + id));
    }

    @Override
    public Departamento actualizar(Long id, Departamento departamento) throws DepartamentoNoEncontradoException {
        if (!departamentoRepository.existsById(id)) {
            throw new DepartamentoNoEncontradoException("Departamento no encontrado con ID: " + id);
        }
        departamento.setId(id);
        return departamentoRepository.save(departamento);
    }

    @Override
    public void eliminar(Long id) throws DepartamentoNoEncontradoException {
        if (!departamentoRepository.existsById(id)) {
            throw new DepartamentoNoEncontradoException("Departamento no encontrado con ID: " + id);
        }
        departamentoRepository.deleteById(id);
    }

    @Override
    public List<Departamento> obtenerTodos() {
        return departamentoRepository.findAll();
    }

}
