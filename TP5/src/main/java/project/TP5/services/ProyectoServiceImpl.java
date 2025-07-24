package project.TP5.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import project.TP5.models.Proyecto;
import project.TP5.repositories.ProyectoRepository;
import project.TP5.exceptions.ProyectoNoEncontradoException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProyectoServiceImpl implements ProyectoService {
    private final ProyectoRepository proyectoRepository;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public Proyecto guardar(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    public Proyecto buscarPorId(Long id) throws ProyectoNoEncontradoException {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id));
    }

    @Override
    public Proyecto actualizar(Long id, Proyecto proyecto) throws ProyectoNoEncontradoException {
        if (!proyectoRepository.existsById(id)) {
            throw new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id);
        }
        proyecto.setId(id);
        return proyectoRepository.save(proyecto);
    }

    @Override
    public void eliminar(Long id) throws ProyectoNoEncontradoException {
        if (!proyectoRepository.existsById(id)) {
            throw new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id);
        }
        proyectoRepository.deleteById(id);
    }

    @Override
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAll();
    }

    @Override
    public List<Proyecto> buscarProyectosActivos() {
        return proyectoRepository.findByFechaFinAfter(LocalDate.now());
    }
}
