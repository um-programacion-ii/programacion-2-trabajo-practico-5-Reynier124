package project.TP5.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.TP5.exceptions.ProyectoNoEncontradoException;
import project.TP5.models.Proyecto;
import project.TP5.repositories.ProyectoRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
public class ProyectoServiceIntegrationTest {
    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private ProyectoRepository proyectoRepository;

    private Proyecto crearProyectoDePrueba() {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto de Prueba");
        proyecto.setDescripcion("Descripción del Proyecto de Prueba");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFin(LocalDate.now().plusDays(10));
        return proyecto;
    }

    @Test
    void cuandoGuardarProyecto_entoncesSePersisteCorrectamente() {
        // Arrange
        Proyecto proyecto = crearProyectoDePrueba();

        // Act
        Proyecto proyectoGuardado = proyectoService.guardar(proyecto);

        // Assert
        assertNotNull(proyectoGuardado.getId());
        assertEquals("Proyecto de Prueba", proyectoGuardado.getNombre());
        assertTrue(proyectoRepository.existsById(proyectoGuardado.getId()));
    }

    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaProyecto() throws ProyectoNoEncontradoException {
        // Arrange
        Proyecto proyecto = crearProyectoDePrueba();
        proyecto = proyectoRepository.save(proyecto);

        // Act
        Proyecto resultado = proyectoService.buscarPorId(proyecto.getId());

        // Assert
        assertNotNull(resultado);
        assertEquals(proyecto.getId(), resultado.getId());
    }

    @Test
    void cuandoBuscarPorIdNoExistente_entoncesLanzaExcepcion() {
        // Act & Assert
        assertThrows(ProyectoNoEncontradoException.class, () -> {
            proyectoService.buscarPorId(999L);
        });
    }

    @Test
    void cuandoActualizarProyecto_entoncesSeActualizaCorrectamente() throws ProyectoNoEncontradoException {
        // Arrange
        Proyecto proyecto = crearProyectoDePrueba();
        proyecto = proyectoRepository.save(proyecto);

        // Act
        proyecto.setDescripcion("Nueva Descripción");
        Proyecto proyectoActualizado = proyectoService.actualizar(proyecto.getId(), proyecto);

        // Assert
        assertEquals("Nueva Descripción", proyectoActualizado.getDescripcion());
    }

    @Test
    void cuandoEliminarProyecto_entoncesSeEliminaCorrectamente() throws ProyectoNoEncontradoException {
        // Arrange
        Proyecto proyecto = crearProyectoDePrueba();
        proyecto = proyectoRepository.save(proyecto);

        // Act
        proyectoService.eliminar(proyecto.getId());

        // Assert
        assertFalse(proyectoRepository.existsById(proyecto.getId()));
    }

    @Test
    void cuandoObtenerTodos_entoncesRetornaTodosLosProyectos() {
        // Arrange
        Proyecto proyecto1 = crearProyectoDePrueba();
        proyectoRepository.save(proyecto1);

        Proyecto proyecto2 = crearProyectoDePrueba();
        proyecto2.setNombre("Otro Proyecto");
        proyectoRepository.save(proyecto2);

        // Act
        List<Proyecto> proyectos = proyectoService.obtenerTodos();

        // Assert
        assertEquals(2, proyectos.size());
    }

    @Test
    void cuandoBuscarProyectosActivos_entoncesRetornaProyectosActivos() {
        // Arrange
        Proyecto proyectoActivo = crearProyectoDePrueba();
        proyectoActivo.setFechaFin(LocalDate.now().plusDays(10));
        proyectoRepository.save(proyectoActivo);

        Proyecto proyectoInactivo = crearProyectoDePrueba();
        proyectoInactivo.setNombre("Proyecto Inactivo");
        proyectoInactivo.setFechaFin(LocalDate.now().minusDays(1));
        proyectoRepository.save(proyectoInactivo);

        // Act
        List<Proyecto> proyectosActivos = proyectoService.buscarProyectosActivos();

        // Assert
        assertEquals(1, proyectosActivos.size());
        assertEquals("Proyecto de Prueba", proyectosActivos.get(0).getNombre());
    }
}

