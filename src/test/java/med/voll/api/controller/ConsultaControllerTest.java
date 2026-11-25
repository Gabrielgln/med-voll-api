package med.voll.api.controller;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.enums.Especialidade;
import med.voll.api.service.ConsultaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> jsonRequest;
    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> jsonResponse;
    @MockitoBean
    private ConsultaService service;

    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    void agendarCenario1() throws Exception {
        String json = "{}";
        var response = mvc.perform(post("/consultas")
                        .with(user("teste"))
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informações estão validas")
    @WithMockUser
    void agendarCenario2() throws Exception {
        LocalDateTime data = LocalDateTime.now().plusHours(1);
        Especialidade especialidade = Especialidade.CARDIOLOGIA;
        DadosDetalhamentoConsulta result = new DadosDetalhamentoConsulta(null, 2L, 5L, data);

        when(service.agendar(any())).thenReturn(result);

        var response = mvc.perform(post("/consultas")
                        .with(user("teste"))
                        .content(jsonRequest.write(
                                new DadosAgendamentoConsulta(
                                        2L,
                                        5L,
                                        data,
                                        especialidade)
                                ).getJson()
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        var jsonEsperado = jsonResponse.write(result).getJson();

        assertEquals(jsonEsperado, response.getContentAsString());
    }
}