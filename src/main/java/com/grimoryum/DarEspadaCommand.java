package com.lucas;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class DarEspadaCommand implements CommandExecutor {
    private final NamespacedKey chave;
    
    public DarEspadaCommand(MeuPlugin plugin){
        this.chave = new NamespacedKey(plugin, "espada_especial");
    }

    // Esse método roda quando o jogador digita o comando registrado (ex: /darespada)
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Comando só faz sentido se quem usou for um jogador (não funciona via console)
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Esse commando só funciona em jogo.");
            return true;
        }

        // Cria o item base: espada de netherite
        ItemStack espada = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = espada.getItemMeta();

        // Dá um nome customizado pro item (aparece no inventário)
       meta.displayName(
            Component.text("Espada do Bhaiano", NamedTextColor.AQUA)
        );

        // Grava a "etiqueta" no item pra nosso Listener conseguir identificar ela depois
        meta.getPersistentDataContainer().set(chave, PersistentDataType.BOOLEAN, true);

        espada.setItemMeta(meta);

        // Coloca a espada no inventário do jogador
        player.getInventory().addItem(espada);
        player.sendMessage("Você recebeu a Espada do Bhaiano!");
        return true;
    }
    
}
