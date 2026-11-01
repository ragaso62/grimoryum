package com.lucas;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EspadaListener implements Listener {
    // NamespacedKey é como uma "etiqueta única" pra marcar o item.
    // Precisa do nome do plugin (passado no construtor) pra evitar conflito com outros plugins.
    private final ItemKeys Keys;

    public EspadaListener(ItemKeys Keys) {
        this.Keys = Keys;
    }

    // Esse evento dispara toda vez que uma entidade (jogador, mob, etc) bate em outra
    @EventHandler
    public void aoAttack(EntityDamageByEntityEvent event) {

        // Só nos interessa quando quem bateu é um jogador
        if (!(event.getDamager() instanceof Player player)) return;

        // Só nos interessa quando quem bateu é um jogador
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getItemMeta() == null) return; //não tem item na mão, ignora

        // Verifica se o item tem nossa "etiqueta" gravada nele
        boolean ehEspadaEspecial = item.getItemMeta().getPersistentDataContainer()
                .has(Keys.espadaEspecial, PersistentDataType.BOOLEAN);

        if (!ehEspadaEspecial) return; //não é a espada especial, iginora

        // Se quem foi atingido também é uma "LivingEntity" (mob ou jogador), aplica o efeito
        if (event.getEntity() instanceof LivingEntity alvo) {
            // Aplica Lentidão no alvo por 3 segundos (60 ticks = 3s, já que 1s = 20 ticks)
            alvo.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1)); // 1 é o nível do efeito (Slowness II)
        }
    }
}
