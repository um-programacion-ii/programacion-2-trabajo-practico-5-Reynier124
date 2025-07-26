package project.TP5.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.TP5.models.Departamento;
import project.TP5.services.DepartamentoService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers = DepartamentoController.class)
public class DepartamentoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartamentoService departamentoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void cuandoObtenerTodos_entoncesRetornaListaDeDepartamentos() throws Exception {
        // Arrange
        Departamento departamento1 = new Departamento();
        departamento1.setId(1L);
        departamento1.setNombre("Departamento 1");

        Departamento departamento2 = new Departamento();
        departamento2.setId(2L);
        departamento2.setNombre("Departamento 2");

        List<Departamento> departamentos = Arrays.asList(departamento1, departamento2);
        given(departamentoService.obtenerTodos()).willReturn(departamentos);

        // Act & Assert
        mockMvc.perform(get("/api/departamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(departamentos.size()));
    }

    @Test
    void cuandoObtenerPorId_entoncesRetornaDepartamento() throws Exception {
        // Arrange
        Departamento departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("Departamento 1");

        given(departamentoService.buscarPorId(anyLong())).willReturn(departamento);

        // Act & Assert
        mockMvc.perform(get("/api/departamentos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Departamento 1"));
    }

    @Test
    void cuandoCrearDepartamento_entoncesRetornaDepartamentoCreado() throws Exception {
        // Arrange
        Departamento nuevoDepartamento = new Departamento();
        nuevoDepartamento.setNombre("Nuevo Departamento");

        given(departamentoService.guardar(any(Departamento.class))).willReturn(nuevoDepartamento);

        // Act & Assert
        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoDepartamento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Nuevo Departamento"));
    }

    @Test
    void cuandoActualizarDepartamento_entoncesRetornaDepartamentoActualizado() throws Exception {
        // Arrange
        Departamento departamentoActualizado = new Departamento();
        departamentoActualizado.setId(1L);
        departamentoActualizado.setNombre("Departamento Actualizado");

        given(departamentoService.actualizar(anyLong(), any(Departamento.class))).willReturn(departamentoActualizado);

        // Act & Assert
        mockMvc.perform(put("/api/departamentos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departamentoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Departamento Actualizado"));
    }

    @Test
    void cuandoEliminarDepartamento_entoncesRetornaNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/departamentos/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
