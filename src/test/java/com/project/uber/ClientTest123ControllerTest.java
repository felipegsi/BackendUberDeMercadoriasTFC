package com.project.uber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.uber.model.ClientTest123;
import com.project.uber.service.implementation.ClientServiceTeste123;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientControllerTest123.class)
public class ClientTest123ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientServiceTeste123 clientServiceTeste123;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterClient() throws Exception {
        ClientTest123 clientTest123 = new ClientTest123();
        clientTest123.setName("Test Client");
        // Configure mais campos conforme necessário

        given(clientServiceTeste123.registerClient(clientTest123)).willReturn(clientTest123);

        mockMvc.perform(post("/api/clients/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientTest123)))
                .andExpect(status().isOk());
        // Verificações adicionais podem ser feitas aqui
    }
}
