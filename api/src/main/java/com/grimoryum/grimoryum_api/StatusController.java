package com.grimoryum.grimoryum_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/api")
public class StatusController {

    public record StatusResposta(boolean online,Metrica metrica) {}

    private final MetricasRepository repo;

    public StatusController(MetricasRepository repo){
        this.repo = repo;
    }

    @GetMapping("/status")
    public StatusResposta status(){
        Metrica m = repo.ultima().orElse(null);
        boolean online = m != null
                && Duration.between(m.coletadoEm(), Instant.now()).getSeconds() <15;
        return new StatusResposta(online, m);
    }
}
