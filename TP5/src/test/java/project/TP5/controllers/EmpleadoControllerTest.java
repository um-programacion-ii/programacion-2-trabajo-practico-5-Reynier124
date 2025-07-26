package project.TP5.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.TP5.models.Empleado;
import project.TP5.services.EmpleadoService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmpleadoController.class)
public class EmpleadoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void cuandoObtenerTodos_entoncesRetornaListaDeEmpleados() throws Exception {
        // Arrange
        Empleado empleado1 = new Empleado();
        empleado1.setId(1L);
        empleado1.setNombre("Juan");

        Empleado empleado2 = new Empleado();
        empleado2.setId(2L);
        empleado2.setNombre("Ana");

        List<Empleado> empleados = Arrays.asList(empleado1, empleado2);
        given(empleadoService.obtenerTodos()).willReturn(empleados);

        // Act & Assert
        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(empleados.size()));
    }

    @Test
    void cuandoObtenerPorId_entoncesRetornaEmpleado() throws Exception {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Juan");

        given(empleadoService.buscarPorId(anyLong())).willReturn(empleado);

        // Act & Assert
        mockMvc.perform(get("/api/empleados/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void cuandoCrearEmpleado_entoncesRetornaEmpleadoCreado() throws Exception {
        // Arrange
        Empleado nuevoEmpleado = new Empleado();
        nuevoEmpleado.setNombre("Nuevo Empleado");

        given(empleadoService.guardar(any(Empleado.class))).willReturn(nuevoEmpleado);

        // Act & Assert
        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoEmpleado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Nuevo Empleado"));
    }

    @Test
    void cuandoActualizarEmpleado_entoncesRetornaEmpleadoActualizado() throws Exception {
        // Arrange
        Empleado empleadoActualizado = new Empleado();
        empleadoActualizado.setId(1L);
        empleadoActualizado.setNombre("Empleado Actualizado");

        given(empleadoService.actualizar(anyLong(), any(Empleado.class))).willReturn(empleadoActualizado);

        // Act & Assert
        mockMvc.perform(put("/api/empleados/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleadoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Empleado Actualizado"));
    }

    @Test
    void cuandoEliminarEmpleado_entoncesRetornaNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/empleados/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void cuandoObtenerPorDepartamento_entoncesRetornaListaDeEmpleados() throws Exception {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Juan");

        List<Empleado> empleados = Arrays.asList(empleado);
        given(empleadoService.buscarPorDepartamento(any(String.class))).willReturn(empleados);

        // Act & Assert
        mockMvc.perform(get("/api/empleados/departamento/{nombre}", "IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(empleados.size()));
    }

    @Test
    void cuandoObtenerPorRangoSalario_entoncesRetornaListaDeEmpleados() throws Exception {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Juan");

        List<Empleado> empleados = Arrays.asList(empleado);
        given(empleadoService.buscarPorRangoSalario(any(Double.class), any(Double.class))).willReturn(empleados);

        // Act & Assert
        mockMvc.perform(get("/api/empleados/salario")
                        .param("min", "50000")
                        .param("max", "70000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(empleados.size()));
    }
}
