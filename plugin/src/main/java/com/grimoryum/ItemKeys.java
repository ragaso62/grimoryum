package com.grimoryum;
import org.bukkit.NamespacedKey;

public class ItemKeys {
    public final NamespacedKey espadaEspecial;
    public final NamespacedKey beserker;
    public final NamespacedKey fortuneBlock;
    public final NamespacedKey blockBreak;

    public ItemKeys(GrimoryumPlugin plugin) {
        this.espadaEspecial = new NamespacedKey(plugin, "espada_especial");
        this.beserker = new NamespacedKey(plugin, "beserker");
        this.fortuneBlock = new NamespacedKey(plugin, "fortune_block");
        this.blockBreak = new NamespacedKey(plugin, "block_break");

    }
}
