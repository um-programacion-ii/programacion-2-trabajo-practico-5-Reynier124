package project.TP5.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import project.TP5.models.Empleado;
import project.TP5.repositories.DepartamentoRepository;
import project.TP5.repositories.EmpleadoRepository;
import project.TP5.exceptions.EmailDuplicadoException;
import project.TP5.exceptions.EmpleadoNoEncontradoException;

import java.util.List;

@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {
    private final EmpleadoRepository empleadoRepository;
    private final DepartamentoRepository departamentoRepository;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository,
                               DepartamentoRepository departamentoRepository) {
        this.empleadoRepository = empleadoRepository;
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Empleado guardar(Empleado empleado) throws EmailDuplicadoException {
        if (empleadoRepository.findByEmail(empleado.getEmail()).isPresent()) {
            throw new EmailDuplicadoException("El email ya está registrado: " + empleado.getEmail());
        }
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado buscarPorId(Long id) throws EmpleadoNoEncontradoException {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado no encontrado con ID: " + id));
    }

    @Override
    public List<Empleado> buscarPorDepartamento(String nombreDepartamento) {
        return empleadoRepository.findByNombreDepartamento(nombreDepartamento);
    }

    @Override
    public List<Empleado> buscarPorRangoSalario(Double salarioMin, Double salarioMax) {
        return empleadoRepository.findBySalarioBetween(salarioMin, salarioMax);
    }

    @Override
    public Double obtenerSalarioPromedioPorDepartamento(Long departamentoId) {
        return empleadoRepository.findAverageSalarioByDepartamento(departamentoId)
                .orElse(Double.NaN);
    }

    @Override
    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    @Override
    public Empleado actualizar(Long id, Empleado empleado) throws EmpleadoNoEncontradoException {
        if (!empleadoRepository.existsById(id)) {
            throw new EmpleadoNoEncontradoException("Empleado no encontrado con ID: " + id);
        }
        empleado.setId(id);
        return empleadoRepository.save(empleado);
    }

    @Override
    public void eliminar(Long id) throws EmpleadoNoEncontradoException {
        if (!empleadoRepository.existsById(id)) {
            throw new EmpleadoNoEncontradoException("Empleado no encontrado con ID: " + id);
        }
        empleadoRepository.deleteById(id);
    }

}
