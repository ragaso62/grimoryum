package com.lucas;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Location;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.event.block.BlockPlaceEvent;



public class fortuneBlock implements Listener {
    private final ItemKeys keys;
    private final Set<Location> blocosPlantados = new HashSet<>();

    public fortuneBlock(ItemKeys Keys){
        this.keys = Keys;
    }
     @EventHandler
    public void colocaBloco(BlockPlaceEvent event) {
        ItemStack itemNaMao = event.getItemInHand();
        Block block = event.getBlock();

        if (block.getType() == Material.ANCIENT_DEBRIS
                && itemNaMao.hasItemMeta()
                && itemNaMao.getItemMeta().getPersistentDataContainer()
                    .has(keys.blockBreak, PersistentDataType.BYTE)) {

            blocosPlantados.add(block.getLocation());
        }
    }

    @EventHandler
    public void quebraBloco(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getItemMeta() == null) return;
        if (block.getType() != Material.ANCIENT_DEBRIS) return;

        // bloco foi plantado -> não dá bônus, comportamento normal
        if (blocosPlantados.remove(block.getLocation())) {
            return;
        }

        if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer()
                .has(keys.fortuneBlock, PersistentDataType.BYTE)) {

            int fortuneLevel = item.getEnchantmentLevel(Enchantment.FORTUNE);
            int dropAmount = 1 + fortuneLevel;
            if (dropAmount > 192) dropAmount = 192;

            ItemStack drop = new ItemStack(Material.ANCIENT_DEBRIS, dropAmount);
            ItemMeta dropMeta = drop.getItemMeta();
            dropMeta.getPersistentDataContainer().set(keys.blockBreak, PersistentDataType.BYTE, (byte) 1);
            drop.setItemMeta(dropMeta);

            block.getWorld().dropItemNaturally(block.getLocation(), drop);
            event.setDropItems(false);
        }
    }

}