package com.rorical.nekoproofenhancement;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

/**
 * Allows players to refresh a villager's trade counts by
 * right-clicking with an Emerald Block, consuming one block.
 *
 * <p>The villager shows a heart particle effect on refresh.
 */
public class VillagerTradeRefreshListener implements Listener {

  /**
   * Handles player right-click on a villager with an emerald block.
   *
   * @param event the player interact entity event
   */
  @EventHandler
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
    if (event.getHand() != EquipmentSlot.HAND) {
      return;
    }
    if (!(event.getRightClicked() instanceof Villager villager)) {
      return;
    }

    ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
    if (item.getType() != Material.EMERALD_BLOCK) {
      return;
    }

    event.setCancelled(true);

    item.setAmount(item.getAmount() - 1);

    for (MerchantRecipe recipe : villager.getRecipes()) {
      recipe.setUses(0);
    }

    villager.getWorld().spawnParticle(
        Particle.HEART, villager.getLocation().add(0, 1.5, 0),
        5, 0.3, 0.3, 0.3);
  }
}
