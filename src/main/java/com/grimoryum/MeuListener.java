package com.lucas;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

// "implements Listener" avisa o Bukkit que essa classe vai escutar eventos do jogo
public class MeuListener implements Listener {

    // @EventHandler marca esse método como "executa automaticamente quando o evento ocorrer"
    // PlayerJoinEvent é o evento de "jogador entrou no servidor" (já vem pronto na API do Paper)
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // event.getPlayer() retorna o jogador que entrou
        // getName() pega o nome dele, sendMessage() manda uma mensagem no chat dele
        event.getPlayer().sendMessage("Bem-vindo ao servidor, " + event.getPlayer().getName() + "!");
    }
}