package com.project.uberdemercadoriastfc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    @GetMapping("/hello")
    public String sayHello() {
        return "Beytacia, Teste!";
    }

    @GetMapping("/message")
    public String getMessage() {
        return "This is a message from the Spring Boot server!";
    }

    @PostMapping("/processWord")
    public String processWord(@RequestBody String word) {
        // Lógica para processar a palavra e retornar uma resposta
        // Por exemplo, simplesmente retorna uma mensagem contendo a palavra recebida
        if(word == null || word.isEmpty()) {
            return "Você não enviou nenhuma palavra!";
        } else if(word.length() < 3) {
            return "A palavra enviada é muito curta!";
        } else if(word.length() > 10) {
            return "A palavra enviada é muito longa!";
        }
        return "Você enviou a palavra: " + word; // Servidor
    }
}


