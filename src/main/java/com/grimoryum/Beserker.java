package com.lucas;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Beserker implements Listener {
    private final ItemKeys keys;

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private static final long COOLDOWN_TICKS = 20 * 60; // 60 segundos de cooldown

    public Beserker(ItemKeys Keys){
        this.keys = Keys;
    }

    @EventHandler
    public void LevarDano(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack armadura = player.getInventory().getChestplate();
        if (armadura == null || armadura.getItemMeta() == null) return;


        boolean ehBeserker = armadura.getItemMeta().getPersistentDataContainer()
                .has(keys.beserker, PersistentDataType.BOOLEAN);
        
        if (!ehBeserker) return;

        double vidaAtual = player.getHealth() - event.getFinalDamage();

        if (vidaAtual <= 0 && !estaEmCooldown(player)) {
            event.setDamage(player.getHealth() - 1.0);
            ativarBeserker(player);
            player.sendMessage("§6§lSua armadura te salvou da morte!");
            player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 1.0f);
            player.getWorld().spawnParticle(
                Particle.TOTEM_OF_UNDYING,
                player.getLocation().add(0, 1, 0),
                50,
                0.5, 1.0, 0.5
            );
            return;
            
        }

        if (vidaAtual <= 4.0 && vidaAtual > 0.0) {
            ativarBeserker(player);
        }

    }
    
private boolean estaEmCooldown(Player player) {
    Long ultimoUso = cooldowns.get(player.getUniqueId());
    if (ultimoUso == null) return false;
    
    long agora = player.getWorld().getFullTime();
    return (agora - ultimoUso) < COOLDOWN_TICKS;
}
    private void ativarBeserker(Player player) {
        long agora = player.getWorld().getFullTime();
        Long ultimoUso = cooldowns.get(player.getUniqueId());

        if (ultimoUso != null && (agora -ultimoUso) < COOLDOWN_TICKS) {
            return;
        }

        cooldowns.put(player.getUniqueId(), player.getWorld().getFullTime());

        int duracao = 20 * 30;

        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duracao, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duracao, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, duracao, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, duracao, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duracao, 1));

        player.sendMessage("§c§l Modo Beserker ativo!");
    }
}