package com.grimoryum.grimoryum_api;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.List;

@Repository
public class MetricasRepository {

    private final JdbcTemplate jdbc;

    public MetricasRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Metrica> ultima() {
        return jdbc.query(
                "SELECT * FROM metricas ORDER BY coletado_em DESC LIMIT 1",
                this::mapear
        ).stream().findFirst();
    }

    public List<Metrica> ultimosMinutos(int minutos) {
        return jdbc.query("""
                SELECT * FROM metricas
                WHERE coletado_em >= UTC_TIMESTAMP(3) - INTERVAL ? MINUTE
                ORDER BY coletado_em
                """, this::mapear, minutos);
    }

    private Metrica mapear(ResultSet rs, int numLinha) throws SQLException{
        return new Metrica(
                rs.getObject("coletado_em", LocalDateTime.class).toInstant(ZoneOffset.UTC),
                rs.getDouble("cpu_percent"),
                rs.getInt("ram_usada_mb"),
                rs.getInt("ram_max_mb"),
                rs.getDouble("disco_usado_gb"),
                rs.getDouble("disco_total_gb"),
                rs.getLong("rede_entrada_bps"),
                rs.getLong("rede_saida_bps"),
                rs.getDouble("tps"),
                rs.getInt("players_online"),
                rs.getLong("tempo_ativo_seg")
        );
    }
}