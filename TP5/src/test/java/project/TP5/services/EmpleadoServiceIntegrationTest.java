package project.TP5.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.TP5.exceptions.EmpleadoNoEncontradoException;
import project.TP5.models.Departamento;
import project.TP5.models.Empleado;
import project.TP5.repositories.DepartamentoRepository;
import project.TP5.repositories.EmpleadoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
public class EmpleadoServiceIntegrationTest {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    Empleado crearEmpleadoDePrueba() {
        Empleado empleado = new Empleado();
        empleado.setNombre("Juan");
        empleado.setApellido("Pérez");
        empleado.setEmail("juan.perez@empresa.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(Double.valueOf("50000.00"));

        return empleado;
    }

    @Test
    void cuandoGuardarEmpleado_entoncesSePersisteCorrectamente() {
        // Arrange
        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de Tecnología");
        departamento = departamentoRepository.save(departamento);

        Empleado empleado = crearEmpleadoDePrueba();

        empleado.setDepartamento(departamento);

        // Act
        Empleado empleadoGuardado = empleadoService.guardar(empleado);

        // Assert
        assertNotNull(empleadoGuardado.getId());
        assertEquals("juan.perez@empresa.com", empleadoGuardado.getEmail());
        assertTrue(empleadoRepository.existsById(empleadoGuardado.getId()));
    }

    @Test
    void cuandoBuscarPorEmailExistente_entoncesRetornaEmpleado() {
        // Arrange
        Empleado empleado = crearEmpleadoDePrueba();
        empleadoRepository.save(empleado);

        // Act
        Optional<Empleado> resultado = empleadoRepository.findByEmail("juan.perez@empresa.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("juan.perez@empresa.com", resultado.get().getEmail());
    }

    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaEmpleado() throws EmpleadoNoEncontradoException {
        // Arrange
        Empleado empleado = crearEmpleadoDePrueba();
        empleado = empleadoRepository.save(empleado);

        // Act
        Empleado resultado = empleadoService.buscarPorId(empleado.getId());

        // Assert
        assertNotNull(resultado);
        assertEquals(empleado.getId(), resultado.getId());
    }

    @Test
    void cuandoBuscarPorIdNoExistente_entoncesLanzaExcepcion() {
        // Act & Assert
        assertThrows(EmpleadoNoEncontradoException.class, () -> {
            empleadoService.buscarPorId(999L);
        });
    }

    @Test
    void cuandoBuscarPorDepartamento_entoncesRetornaEmpleados() {
        // Arrange
        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de Tecnologia");
        departamento = departamentoRepository.save(departamento);

        Empleado empleado1 = crearEmpleadoDePrueba();
        empleado1.setDepartamento(departamento);
        empleadoRepository.save(empleado1);

        Empleado empleado2 = crearEmpleadoDePrueba();
        empleado2.setEmail("otro.empleado@empresa.com");
        empleado2.setDepartamento(departamento);
        empleadoRepository.save(empleado2);

        // Act
        List<Empleado> empleados = empleadoService.buscarPorDepartamento("IT");

        // Assert
        assertEquals(2, empleados.size());
    }

    @Test
    void cuandoBuscarPorRangoSalario_entoncesRetornaEmpleados() {
        // Arrange
        Empleado empleado1 = crearEmpleadoDePrueba();
        empleado1.setSalario(40000.00);
        empleadoRepository.save(empleado1);

        Empleado empleado2 = crearEmpleadoDePrueba();
        empleado2.setEmail("otro.empleado@empresa.com");
        empleado2.setSalario(60000.00);
        empleadoRepository.save(empleado2);

        // Act
        List<Empleado> empleados = empleadoService.buscarPorRangoSalario(45000.00, 70000.00);

        // Assert
        assertEquals(1, empleados.size());
        assertEquals("otro.empleado@empresa.com", empleados.get(0).getEmail());
    }

    @Test
    void cuandoActualizarEmpleado_entoncesSeActualizaCorrectamente() throws EmpleadoNoEncontradoException {
        // Arrange
        Empleado empleado = crearEmpleadoDePrueba();
        empleado = empleadoRepository.save(empleado);

        // Act
        empleado.setSalario(55000.00);
        Empleado empleadoActualizado = empleadoService.actualizar(empleado.getId(), empleado);

        // Assert
        assertEquals(55000.00, empleadoActualizado.getSalario());
    }

    @Test
    void cuandoEliminarEmpleado_entoncesSeEliminaCorrectamente() throws EmpleadoNoEncontradoException {
        // Arrange
        Empleado empleado = crearEmpleadoDePrueba();
        empleado = empleadoRepository.save(empleado);

        // Act
        empleadoService.eliminar(empleado.getId());

        // Assert
        assertFalse(empleadoRepository.existsById(empleado.getId()));
    }
}
