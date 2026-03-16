package com.rorical.nekoproofenhancement;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Allows dispensers with buckets to interact with cauldrons,
 * similar to interacting with water/lava source blocks.
 *
 * <p>Empty buckets collect from cauldrons containing lava or full water.
 * Water/lava buckets fill empty cauldrons.
 */
public class CauldronDispenserListener implements Listener {

  private final JavaPlugin plugin;

  /**
   * Creates a new listener for dispenser-cauldron interactions.
   *
   * @param plugin the plugin instance used for scheduling
   */
  @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
      justification = "Plugin reference is intentionally shared")
  public CauldronDispenserListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Handles dispenser bucket interaction with cauldrons.
   *
   * @param event the block dispense event
   */
  @EventHandler
  public void onDispense(BlockDispenseEvent event) {
    Block dispenserBlock = event.getBlock();
    if (dispenserBlock.getType() != Material.DISPENSER) {
      return;
    }

    ItemStack item = event.getItem();
    Material itemType = item.getType();

    Directional directional = (Directional) dispenserBlock.getBlockData();
    Block target = dispenserBlock.getRelative(directional.getFacing());

    if (itemType == Material.BUCKET) {
      handleCollect(event, dispenserBlock, target);
    } else if (itemType == Material.WATER_BUCKET
        || itemType == Material.LAVA_BUCKET) {
      handleFill(event, dispenserBlock, target, itemType);
    }
  }

  private void handleCollect(BlockDispenseEvent event,
      Block dispenserBlock, Block target) {
    Material targetType = target.getType();
    Material filledBucket;

    if (targetType == Material.LAVA_CAULDRON) {
      filledBucket = Material.LAVA_BUCKET;
    } else if (targetType == Material.WATER_CAULDRON) {
      Levelled levelled = (Levelled) target.getBlockData();
      if (levelled.getLevel() < levelled.getMaximumLevel()) {
        return;
      }
      filledBucket = Material.WATER_BUCKET;
    } else {
      return;
    }

    event.setCancelled(true);

    plugin.getServer().getScheduler().runTask(plugin, () -> {
      target.setType(Material.CAULDRON);
      replaceBucket(dispenserBlock, Material.BUCKET, filledBucket);
    });
  }

  private void handleFill(BlockDispenseEvent event,
      Block dispenserBlock, Block target, Material bucketType) {
    if (target.getType() != Material.CAULDRON) {
      return;
    }

    event.setCancelled(true);

    plugin.getServer().getScheduler().runTask(plugin, () -> {
      if (bucketType == Material.LAVA_BUCKET) {
        target.setType(Material.LAVA_CAULDRON);
      } else {
        target.setType(Material.WATER_CAULDRON);
        Levelled levelled = (Levelled) target.getBlockData();
        levelled.setLevel(levelled.getMaximumLevel());
        target.setBlockData(levelled);
      }
      replaceBucket(dispenserBlock, bucketType, Material.BUCKET);
    });
  }

  private void replaceBucket(Block dispenserBlock,
      Material from, Material to) {
    Container container = (Container) dispenserBlock.getState();
    Inventory inventory = container.getInventory();
    int slot = inventory.first(from);
    if (slot < 0) {
      return;
    }

    ItemStack stack = inventory.getItem(slot);
    if (stack == null) {
      return;
    }
    if (stack.getAmount() > 1) {
      stack.setAmount(stack.getAmount() - 1);
      ItemStack replacement = new ItemStack(to, 1);
      var leftover = inventory.addItem(replacement);
      for (ItemStack drop : leftover.values()) {
        dispenserBlock.getWorld().dropItemNaturally(
            dispenserBlock.getLocation(), drop);
      }
    } else {
      inventory.setItem(slot, new ItemStack(to, 1));
    }
  }
}
