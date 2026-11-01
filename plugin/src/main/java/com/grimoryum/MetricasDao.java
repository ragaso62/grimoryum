package com.grimoryum;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MetricasDao {

    public record Metrica(
            LocalDateTime coletadoEm,
            double cpuPercent,
            int ramUsadaMb,
            int ramMaxMb,
            double discoUsadoGb,
            double discoTotalGb,
            long redeEntradaBps,
            long redeSaidaBps,
            double tps,
            int playersOnline,
            long tempoAtivoSeg) {}

    private final String url, user, pass;

    public MetricasDao(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public void inserir(Metrica m) throws SQLException {
        String sql = """
            INSERT INTO metricas (coletado_em, cpu_percent, ram_usada_mb, ram_max_mb,
                disco_usado_gb, disco_total_gb, rede_entrada_bps, rede_saida_bps,
                tps, players_online, tempo_ativo_seg)
            VALUES (?,?,?,?,?,?,?,?,?,?,?)
            """;
        try (Connection c = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, m.coletadoEm());
            ps.setDouble(2, m.cpuPercent());
            ps.setInt(3, m.ramUsadaMb());
            ps.setInt(4, m.ramMaxMb());
            ps.setDouble(5, m.discoUsadoGb());
            ps.setDouble(6, m.discoTotalGb());
            ps.setLong(7, m.redeEntradaBps());
            ps.setLong(8, m.redeSaidaBps());
            ps.setDouble(9, m.tps());
            ps.setInt(10, m.playersOnline());
            ps.setLong(11, m.tempoAtivoSeg());
            ps.executeUpdate();
        }
    }

    /** Devolve as últimas n linhas, da mais antiga para a mais nova (boa pra gráfico). */
    public List<Metrica> ultimas(int n) throws SQLException {
        String sql = "SELECT * FROM metricas ORDER BY coletado_em DESC LIMIT ?";
        List<Metrica> lista = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, n);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Metrica(
                            rs.getObject("coletado_em", LocalDateTime.class),
                            rs.getDouble("cpu_percent"),
                            rs.getInt("ram_usada_mb"),
                            rs.getInt("ram_max_mb"),
                            rs.getDouble("disco_usado_gb"),
                            rs.getDouble("disco_total_gb"),
                            rs.getLong("rede_entrada_bps"),
                            rs.getLong("rede_saida_bps"),
                            rs.getDouble("tps"),
                            rs.getInt("players_online"),
                            rs.getLong("tempo_ativo_seg")));
                }
            }
        }
        java.util.Collections.reverse(lista);
        return lista;
    }
    public int limparAntigas(int dias) throws SQLException {
        String sql = "DELETE FROM metricas WHERE coletado_em < UTC_TIMESTAMP(3) - INTERVAL ? DAY";
        try (Connection c = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, dias);
            return ps.executeUpdate();
        }
    }
}