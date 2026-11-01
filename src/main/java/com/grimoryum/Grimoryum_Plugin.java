package com.lucas;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// JavaPlugin é a classe base que todo plugin Paper/Bukkit precisa estender
public class MeuPlugin extends JavaPlugin {

    private ItemKeys itemKeys;

    @Override
    public void onEnable() {
        itemKeys = new ItemKeys(this);

        // Esse método roda automaticamente quando o servidor liga o plugin
        getLogger().info("Plugin ligado!");

        //Registra o novo listener da espada
        getServer().getPluginManager().registerEvents(new EspadaListener(itemKeys), this);

        //Registra o comando - o nome "darespada" precisa bater com o que vamos colocar no plugin.yml
        getCommand("darespada").setExecutor(new DarEspadaCommand(this));
        getCommand("darpeitoral").setExecutor(new DarPeitoralCommand(this));

        getServer().getPluginManager().registerEvents(new Beserker(itemKeys), this);
        getServer().getPluginManager().registerEvents(new fortuneBlock(itemKeys), this);

        // Aqui é o passo MAIS IMPORTANTE: registrar o listener.
        // Sem essa linha, o @EventHandler no MeuListener nunca seria chamado,
        // mesmo a classe existindo - o Bukkit não saberia que ela existe.
        // "this" é o próprio plugin (precisa informar pra qual plugin esse listener pertence)
        getServer().getPluginManager().registerEvents(new MeuListener(), this);
        saveDefaultConfig();

        String host = getConfig().getString("database.host");
        int port = getConfig().getInt("database.port");
        String nome = getConfig().getString("database.name");
        String user = getConfig().getString("database.user");
        String pass = getConfig().getString("database.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + nome;

        MetricasDao dao = new MetricasDao(url, user, pass);
        DadosColet coletor = new DadosColet(getConfig().getDouble("disco.total-gb", 40.0));

        getServer().getScheduler().runTaskTimerAsynchronously(this, coletor::atualizarDisco, 0L, 6000L);

        getServer().getScheduler().runTaskTimer(this, () -> {
            double tps = Math.min(20.0, Bukkit.getTPS()[0]);
            int players = Bukkit.getOnlinePlayers().size();

            MetricasDao.Metrica m = new MetricasDao.Metrica(
                    LocalDateTime.now(ZoneOffset.UTC),
                    coletor.cpuPercent(),
                    coletor.ramUsadaMb(),
                    coletor.ramMaxMb(),
                    coletor.discoUsadoGb(),
                    coletor.discoTotalGb(),
                    0, 0,
                    tps,
                    players,
                    coletor.tempoAtivoSeg());

            getServer().getScheduler().runTaskAsynchronously(this, () -> {
                try {
                    dao.inserir(m);
                } catch (SQLException e) {
                    getLogger().warning("falha ao gravar métricas: " + e.getMessage());
                }
            });
        }, 100L, 100L);
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            try {
                int apagadas = dao.limparAntigas(2);
                if (apagadas > 0) {
                    getLogger().info("Métricas antigas removidas: " + apagadas);
                }
            } catch (SQLException e) {
                getLogger().warning("Falha ao limpar métricas antigas: " + e.getMessage());
            }
        }, 200L, 72000L);
    }

    public ItemKeys getItemKeys() {
        return itemKeys;
    }

    @Override
    public void onDisable() {
        // Esse método roda automaticamente quando o servidor desliga ou recarrega o plugin
        getLogger().info("Plugin desligado!");
    }


}