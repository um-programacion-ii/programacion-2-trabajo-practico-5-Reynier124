package project.TP5.repositories.departamento;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import project.TP5.models.Departamento;
import project.TP5.repositories.DepartamentoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public abstract class AbstractDepartamentoRepositoryTest {
    @Autowired
    protected DepartamentoRepository departamentoRepository;

    @Test
    void cuandoGuardarDepartamento_entoncesSePersisteCorrectamente() {
        // Arrange
        Departamento departamento = new Departamento();
        departamento.setNombre("Nuevo Departamento");
        departamento.setDescripcion("Descripción del Nuevo Departamento");

        // Act
        Departamento departamentoGuardado = departamentoRepository.save(departamento);

        // Assert
        assertNotNull(departamentoGuardado.getId());
        assertEquals("Nuevo Departamento", departamentoGuardado.getNombre());
    }

    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaDepartamento() {
        // Arrange
        Departamento departamento = new Departamento();
        departamento.setNombre("Departamento Existente");
        departamento.setDescripcion("Descripción del Departamento Existente");
        Departamento departamentoGuardado = departamentoRepository.save(departamento);

        // Act
        Optional<Departamento> resultado = departamentoRepository.findById(departamentoGuardado.getId());

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Departamento Existente", resultado.get().getNombre());
    }

    @Test
    void cuandoActualizarDepartamento_entoncesSeActualizaCorrectamente() {
        // Arrange
        Departamento departamento = new Departamento();
        departamento.setNombre("Departamento a Actualizar");
        departamento.setDescripcion("Descripción del Departamento a Actualizar");
        Departamento departamentoGuardado = departamentoRepository.save(departamento);

        // Act
        departamentoGuardado.setDescripcion("Descripción Actualizada");
        Departamento departamentoActualizado = departamentoRepository.save(departamentoGuardado);

        // Assert
        assertEquals("Descripción Actualizada", departamentoActualizado.getDescripcion());
    }

    @Test
    void cuandoEliminarDepartamento_entoncesSeEliminaCorrectamente() {
        // Arrange
        Departamento departamento = new Departamento();
        departamento.setNombre("Departamento a Eliminar");
        departamento.setDescripcion("Descripción del Departamento a Eliminar");
        Departamento departamentoGuardado = departamentoRepository.save(departamento);

        // Act
        departamentoRepository.delete(departamentoGuardado);

        // Assert
        Optional<Departamento> resultado = departamentoRepository.findById(departamentoGuardado.getId());
        assertFalse(resultado.isPresent());
    }

    @Test
    void cuandoObtenerTodos_entoncesRetornaTodosLosDepartamentos() {
        // Arrange
        Departamento departamento1 = new Departamento();
        departamento1.setNombre("Departamento 1");
        departamento1.setDescripcion("Descripción del Departamento 1");
        departamentoRepository.save(departamento1);

        Departamento departamento2 = new Departamento();
        departamento2.setNombre("Departamento 2");
        departamento2.setDescripcion("Descripción del Departamento 2");
        departamentoRepository.save(departamento2);

        // Act
        List<Departamento> departamentos = departamentoRepository.findAll();

        // Assert
        assertEquals(2, departamentos.size());
    }
}
