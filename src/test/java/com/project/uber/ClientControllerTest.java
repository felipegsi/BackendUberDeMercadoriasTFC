package com.project.uber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.uber.model.Client;
import com.project.uber.service.implementation.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterClient() throws Exception {
        Client client = new Client();
        client.setName("Test Client");
        // Configure mais campos conforme necessário

        given(clientService.registerClient(client)).willReturn(client);

        mockMvc.perform(post("/api/clients/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk());
        // Verificações adicionais podem ser feitas aqui
    }
}
