package project.TP5.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.TP5.exceptions.DepartamentoNoEncontradoException;
import project.TP5.models.Departamento;
import project.TP5.repositories.DepartamentoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
public class DepartamentoServiceIntegrationTest {
    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento crearDepartamentoDePrueba() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Departamento de Prueba");
        departamento.setDescripcion("Descripción del Departamento de Prueba");
        return departamento;
    }

    @Test
    void cuandoGuardarDepartamento_entoncesSePersisteCorrectamente() {
        // Arrange
        Departamento departamento = crearDepartamentoDePrueba();

        // Act
        Departamento departamentoGuardado = departamentoService.guardar(departamento);

        // Assert
        assertNotNull(departamentoGuardado.getId());
        assertEquals("Departamento de Prueba", departamentoGuardado.getNombre());
        assertTrue(departamentoRepository.existsById(departamentoGuardado.getId()));
    }

    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaDepartamento() throws DepartamentoNoEncontradoException {
        // Arrange
        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);

        // Act
        Departamento resultado = departamentoService.buscarPorId(departamento.getId());

        // Assert
        assertNotNull(resultado);
        assertEquals(departamento.getId(), resultado.getId());
    }

    @Test
    void cuandoBuscarPorIdNoExistente_entoncesLanzaExcepcion() {
        // Act & Assert
        assertThrows(DepartamentoNoEncontradoException.class, () -> {
            departamentoService.buscarPorId(999L);
        });
    }

    @Test
    void cuandoActualizarDepartamento_entoncesSeActualizaCorrectamente() throws DepartamentoNoEncontradoException {
        // Arrange
        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);

        // Act
        departamento.setDescripcion("Nueva Descripción");
        Departamento departamentoActualizado = departamentoService.actualizar(departamento.getId(), departamento);

        // Assert
        assertEquals("Nueva Descripción", departamentoActualizado.getDescripcion());
    }

    @Test
    void cuandoEliminarDepartamento_entoncesSeEliminaCorrectamente() throws DepartamentoNoEncontradoException {
        // Arrange
        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);

        // Act
        departamentoService.eliminar(departamento.getId());

        // Assert
        assertFalse(departamentoRepository.existsById(departamento.getId()));
    }

    @Test
    void cuandoObtenerTodos_entoncesRetornaTodosLosDepartamentos() {
        // Arrange
        Departamento departamento1 = crearDepartamentoDePrueba();
        departamentoRepository.save(departamento1);

        Departamento departamento2 = crearDepartamentoDePrueba();
        departamento2.setNombre("Otro Departamento");
        departamentoRepository.save(departamento2);

        // Act
        List<Departamento> departamentos = departamentoService.obtenerTodos();

        // Assert
        assertEquals(2, departamentos.size());
    }
}
