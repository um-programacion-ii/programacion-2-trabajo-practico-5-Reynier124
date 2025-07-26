package project.TP5.repositories.empleado;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import project.TP5.models.Departamento;
import project.TP5.models.Empleado;
import project.TP5.repositories.DepartamentoRepository;
import project.TP5.repositories.EmpleadoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public abstract class AbstractEmpleadoRepositoryTest {

    @Autowired
    protected EmpleadoRepository empleadoRepository;

    @Autowired
    protected DepartamentoRepository departamentoRepository;

    @Test
    void cuandoBuscarPorEmail_entoncesRetornaEmpleado() {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setNombre("Juan");
        empleado.setApellido("Pérez");
        empleado.setEmail("juan.perez@empresa.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(50000.00);
        empleadoRepository.save(empleado);

        // Act
        Optional<Empleado> resultado = empleadoRepository.findByEmail("juan.perez@empresa.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("juan.perez@empresa.com", resultado.get().getEmail());
    }

    @Test
    void cuandoBuscarPorDepartamento_entoncesRetornaEmpleados() {
        // Arrange
        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de Tecnología");
        departamentoRepository.save(departamento);

        Empleado empleado = new Empleado();
        empleado.setNombre("Juan");
        empleado.setApellido("Pérez");
        empleado.setEmail("juan.perez@empresa.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(50000.00);
        empleado.setDepartamento(departamento);
        empleadoRepository.save(empleado);

        // Act
        List<Empleado> empleados = empleadoRepository.findByDepartamento(departamento);

        // Assert
        assertEquals(1, empleados.size());
    }

    @Test
    void cuandoBuscarPorRangoSalario_entoncesRetornaEmpleados() {
        // Arrange
        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Juan");
        empleado1.setApellido("Pérez");
        empleado1.setEmail("juan.perez@empresa.com");
        empleado1.setFechaContratacion(LocalDate.now());
        empleado1.setSalario(40000.00);
        empleadoRepository.save(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Ana");
        empleado2.setApellido("Gómez");
        empleado2.setEmail("ana.gomez@empresa.com");
        empleado2.setFechaContratacion(LocalDate.now());
        empleado2.setSalario(60000.00);
        empleadoRepository.save(empleado2);

        // Act
        List<Empleado> empleados = empleadoRepository.findBySalarioBetween(45000.00, 70000.00);

        // Assert
        assertEquals(1, empleados.size());
        assertEquals("ana.gomez@empresa.com", empleados.get(0).getEmail());
    }

    @Test
    void cuandoBuscarPorFechaContratacion_entoncesRetornaEmpleados() {
        // Arrange
        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Juan");
        empleado1.setApellido("Pérez");
        empleado1.setEmail("juan.perez@empresa.com");
        empleado1.setFechaContratacion(LocalDate.now().minusDays(10));
        empleado1.setSalario(50000.00);
        empleadoRepository.save(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Ana");
        empleado2.setApellido("Gómez");
        empleado2.setEmail("ana.gomez@empresa.com");
        empleado2.setFechaContratacion(LocalDate.now().minusDays(5));
        empleado2.setSalario(55000.00);
        empleadoRepository.save(empleado2);

        // Act
        List<Empleado> empleados = empleadoRepository.findByFechaContratacionAfter(LocalDate.now().minusDays(7));

        // Assert
        assertEquals(1, empleados.size());
        assertEquals("ana.gomez@empresa.com", empleados.get(0).getEmail());
    }

    @Test
    void cuandoBuscarPorNombreDepartamento_entoncesRetornaEmpleados() {
        // Arrange
        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de Tecnología");
        departamentoRepository.save(departamento);

        Empleado empleado = new Empleado();
        empleado.setNombre("Juan");
        empleado.setApellido("Pérez");
        empleado.setEmail("juan.perez@empresa.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(50000.00);
        empleado.setDepartamento(departamento);
        empleadoRepository.save(empleado);

        // Act
        List<Empleado> empleados = empleadoRepository.findByNombreDepartamento("IT");

        // Assert
        assertEquals(1, empleados.size());
        assertEquals("juan.perez@empresa.com", empleados.get(0).getEmail());
    }

    @Test
    void cuandoCalcularSalarioPromedioPorDepartamento_entoncesRetornaPromedio() {
        // Arrange
        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de Tecnología");
        departamentoRepository.save(departamento);

        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Juan");
        empleado1.setApellido("Pérez");
        empleado1.setEmail("juan.perez@empresa.com");
        empleado1.setFechaContratacion(LocalDate.now());
        empleado1.setSalario(50000.00);
        empleado1.setDepartamento(departamento);
        empleadoRepository.save(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Ana");
        empleado2.setApellido("Gómez");
        empleado2.setEmail("ana.gomez@empresa.com");
        empleado2.setFechaContratacion(LocalDate.now());
        empleado2.setSalario(70000.00);
        empleado2.setDepartamento(departamento);
        empleadoRepository.save(empleado2);

        // Act
        Optional<Double> salarioPromedio = empleadoRepository.findAverageSalarioByDepartamento(departamento.getId());

        // Assert
        assertTrue(salarioPromedio.isPresent());
        assertEquals(60000.00, salarioPromedio.get(), 0.01);
    }
}
