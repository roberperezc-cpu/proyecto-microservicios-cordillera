package com.group_cordillera.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PostService {
    private final WebClient webClient;

    public PostService(){
        // Mantiene la misma API de pruebas que usas en tu proyecto
        this.webClient = WebClient.create("https://jsonplaceholder.typicode.com");
    }

    public String obtenerPost(){
        // Hace la petición exactamente igual a como la tenías estructurada
        return webClient.get()
                .uri("/posts/1")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
