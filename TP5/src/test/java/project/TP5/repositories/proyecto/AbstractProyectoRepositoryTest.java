package project.TP5.repositories.proyecto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import project.TP5.models.Proyecto;
import project.TP5.repositories.ProyectoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public abstract class AbstractProyectoRepositoryTest {
    @Autowired
    protected ProyectoRepository proyectoRepository;

    @Test
    void cuandoGuardarProyecto_entoncesSePersisteCorrectamente() {
        // Arrange
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Nuevo Proyecto");
        proyecto.setDescripcion("Descripción del Nuevo Proyecto");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFin(LocalDate.now().plusDays(10));

        // Act
        Proyecto proyectoGuardado = proyectoRepository.save(proyecto);

        // Assert
        assertNotNull(proyectoGuardado.getId());
        assertEquals("Nuevo Proyecto", proyectoGuardado.getNombre());
    }

    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaProyecto() {
        // Arrange
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Existente");
        proyecto.setDescripcion("Descripción del Proyecto Existente");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFin(LocalDate.now().plusDays(10));
        Proyecto proyectoGuardado = proyectoRepository.save(proyecto);

        // Act
        Optional<Proyecto> resultado = proyectoRepository.findById(proyectoGuardado.getId());

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Proyecto Existente", resultado.get().getNombre());
    }

    @Test
    void cuandoActualizarProyecto_entoncesSeActualizaCorrectamente() {
        // Arrange
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto a Actualizar");
        proyecto.setDescripcion("Descripción del Proyecto a Actualizar");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFin(LocalDate.now().plusDays(10));
        Proyecto proyectoGuardado = proyectoRepository.save(proyecto);

        // Act
        proyectoGuardado.setDescripcion("Descripción Actualizada");
        Proyecto proyectoActualizado = proyectoRepository.save(proyectoGuardado);

        // Assert
        assertEquals("Descripción Actualizada", proyectoActualizado.getDescripcion());
    }

    @Test
    void cuandoEliminarProyecto_entoncesSeEliminaCorrectamente() {
        // Arrange
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto a Eliminar");
        proyecto.setDescripcion("Descripción del Proyecto a Eliminar");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFin(LocalDate.now().plusDays(10));
        Proyecto proyectoGuardado = proyectoRepository.save(proyecto);

        // Act
        proyectoRepository.delete(proyectoGuardado);

        // Assert
        Optional<Proyecto> resultado = proyectoRepository.findById(proyectoGuardado.getId());
        assertFalse(resultado.isPresent());
    }

    @Test
    void cuandoBuscarProyectosActivos_entoncesRetornaProyectosActivos() {
        // Arrange
        Proyecto proyectoActivo = new Proyecto();
        proyectoActivo.setNombre("Proyecto Activo");
        proyectoActivo.setDescripcion("Descripción del Proyecto Activo");
        proyectoActivo.setFechaInicio(LocalDate.now().minusDays(10));
        proyectoActivo.setFechaFin(LocalDate.now().plusDays(10));
        proyectoRepository.save(proyectoActivo);

        Proyecto proyectoInactivo = new Proyecto();
        proyectoInactivo.setNombre("Proyecto Inactivo");
        proyectoInactivo.setDescripcion("Descripción del Proyecto Inactivo");
        proyectoInactivo.setFechaInicio(LocalDate.now().minusDays(20));
        proyectoInactivo.setFechaFin(LocalDate.now().minusDays(5));
        proyectoRepository.save(proyectoInactivo);

        // Act
        List<Proyecto> proyectosActivos = proyectoRepository.findByFechaFinAfter(LocalDate.now());

        // Assert
        assertEquals(1, proyectosActivos.size());
        assertEquals("Proyecto Activo", proyectosActivos.get(0).getNombre());
    }

}
