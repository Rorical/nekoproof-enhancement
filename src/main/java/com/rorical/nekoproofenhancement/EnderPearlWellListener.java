package com.rorical.nekoproofenhancement;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Converts ender pearls dropped into a village water well (水井)
 * into emeralds.
 *
 * <p>A well is detected as a water block surrounded by at least three
 * solid blocks on its cardinal sides. When the dropped ender pearl
 * item enters such water, it transforms into an emerald.
 */
public class EnderPearlWellListener implements Listener {

  private static final BlockFace[] CARDINAL = {
      BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST
  };
  private static final int CHECK_INTERVAL_TICKS = 5;
  private static final int MAX_CHECKS = 40;

  private final JavaPlugin plugin;

  /**
   * Creates a new listener for ender pearl well interactions.
   *
   * @param plugin the plugin instance used for scheduling
   */
  @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
      justification = "Plugin reference is intentionally shared")
  public EnderPearlWellListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Tracks dropped ender pearls and checks whether they land in a well.
   *
   * @param event the player drop item event
   */
  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    Item itemEntity = event.getItemDrop();
    if (itemEntity.getItemStack().getType() != Material.ENDER_PEARL) {
      return;
    }

    final int[] checkCount = {0};

    itemEntity.getScheduler().runAtFixedRate(plugin, task -> {
      if (++checkCount[0] > MAX_CHECKS) {
        task.cancel();
        return;
      }

      Block block = itemEntity.getLocation().getBlock();
      if (block.getType() != Material.WATER) {
        return;
      }
      if (!isWell(block)) {
        return;
      }

      task.cancel();

      int count = itemEntity.getItemStack().getAmount();
      itemEntity.setItemStack(new ItemStack(Material.EMERALD, count));

      Location effectLoc = block.getLocation().add(0.5, 1.0, 0.5);
      block.getWorld().spawnParticle(
          Particle.HAPPY_VILLAGER, effectLoc, 10, 0.3, 0.5, 0.3);
      block.getWorld().playSound(
          effectLoc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
    }, null, CHECK_INTERVAL_TICKS, CHECK_INTERVAL_TICKS);
  }

  /**
   * Checks whether a water block is inside a well structure by verifying
   * that at least three of four cardinal neighbors are solid.
   */
  private boolean isWell(Block waterBlock) {
    int solidCount = 0;
    for (BlockFace face : CARDINAL) {
      if (waterBlock.getRelative(face).getType().isSolid()) {
        solidCount++;
      }
    }
    return solidCount >= 3;
  }
}
