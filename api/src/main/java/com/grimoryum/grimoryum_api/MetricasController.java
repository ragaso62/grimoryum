package com.grimoryum.grimoryum_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MetricasController {
    private final MetricasRepository repo;

    public MetricasController(MetricasRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/metricas")
    public List<Metrica> metricas(@RequestParam(defaultValue = "30") int minutos) {
        int limitado = Math.clamp(minutos, 1, 2880);
        return repo.ultimosMinutos(limitado);
    }
}
