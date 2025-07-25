package project.TP5.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.TP5.models.Proyecto;
import project.TP5.services.ProyectoService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProyectoController.class)
public class ProyectoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProyectoService proyectoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void cuandoObtenerTodos_entoncesRetornaListaDeProyectos() throws Exception {
        // Arrange
        Proyecto proyecto1 = new Proyecto();
        proyecto1.setId(1L);
        proyecto1.setNombre("Proyecto 1");

        Proyecto proyecto2 = new Proyecto();
        proyecto2.setId(2L);
        proyecto2.setNombre("Proyecto 2");

        List<Proyecto> proyectos = Arrays.asList(proyecto1, proyecto2);
        given(proyectoService.obtenerTodos()).willReturn(proyectos);

        // Act & Assert
        mockMvc.perform(get("/api/proyecto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(proyectos.size()));
    }

    @Test
    void cuandoObtenerPorId_entoncesRetornaProyecto() throws Exception {
        // Arrange
        Proyecto proyecto = new Proyecto();
        proyecto.setId(1L);
        proyecto.setNombre("Proyecto 1");

        given(proyectoService.buscarPorId(anyLong())).willReturn(proyecto);

        // Act & Assert
        mockMvc.perform(get("/api/proyecto/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Proyecto 1"));
    }

    @Test
    void cuandoCrearProyecto_entoncesRetornaProyectoCreado() throws Exception {
        // Arrange
        Proyecto nuevoProyecto = new Proyecto();
        nuevoProyecto.setNombre("Nuevo Proyecto");

        given(proyectoService.guardar(any(Proyecto.class))).willReturn(nuevoProyecto);

        // Act & Assert
        mockMvc.perform(post("/api/proyecto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoProyecto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Nuevo Proyecto"));
    }

    @Test
    void cuandoActualizarProyecto_entoncesRetornaProyectoActualizado() throws Exception {
        // Arrange
        Proyecto proyectoActualizado = new Proyecto();
        proyectoActualizado.setId(1L);
        proyectoActualizado.setNombre("Proyecto Actualizado");

        given(proyectoService.actualizar(anyLong(), any(Proyecto.class))).willReturn(proyectoActualizado);

        // Act & Assert
        mockMvc.perform(put("/api/proyecto/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyectoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Proyecto Actualizado"));
    }

    @Test
    void cuandoEliminarProyecto_entoncesRetornaNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/proyecto/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void cuandoObtenerProyectosActivos_entoncesRetornaListaDeProyectosActivos() throws Exception {
        // Arrange
        Proyecto proyectoActivo = new Proyecto();
        proyectoActivo.setId(1L);
        proyectoActivo.setNombre("Proyecto Activo");

        List<Proyecto> proyectosActivos = Arrays.asList(proyectoActivo);
        given(proyectoService.buscarProyectosActivos()).willReturn(proyectosActivos);

        // Act & Assert
        mockMvc.perform(get("/api/proyecto/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(proyectosActivos.size()));
    }
}
