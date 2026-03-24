package com.rorical.nekoproofenhancement;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Registers a crafting recipe that duplicates an elytra.
 *
 * <p>Recipe: Elytra in the center, Diamond at top center,
 * remaining slots filled with Phantom Membrane. Produces 2 elytra.
 * The input elytra must have full durability.
 */
public final class ElytraDuplicationRecipe implements Listener {

  private final NamespacedKey recipeKey;

  private ElytraDuplicationRecipe(NamespacedKey recipeKey) {
    this.recipeKey = recipeKey;
  }

  /**
   * Registers the elytra duplication recipe with the server.
   *
   * @param plugin the plugin instance
   */
  public static void register(JavaPlugin plugin) {
    NamespacedKey key = new NamespacedKey(plugin, "elytra_duplication");

    ItemStack result = new ItemStack(Material.ELYTRA, 2);
    ShapedRecipe recipe = new ShapedRecipe(key, result);

    recipe.shape("MDM", "MEM", "MMM");
    recipe.setIngredient('M', Material.PHANTOM_MEMBRANE);
    recipe.setIngredient('D', Material.DIAMOND);
    recipe.setIngredient('E', Material.ELYTRA);

    plugin.getServer().addRecipe(recipe);
    plugin.getServer().getPluginManager().registerEvents(
        new ElytraDuplicationRecipe(key), plugin);
  }

  /**
   * Unlocks the recipe for players when they join.
   *
   * @param event the player join event
   */
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.getPlayer().discoverRecipe(recipeKey);
  }

  /**
   * Cancels the craft if the input elytra is damaged.
   *
   * @param event the prepare item craft event
   */
  @SuppressFBWarnings(value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
      justification = "CraftingInventory.getMatrix() is never null")
  @EventHandler
  public void onPrepareCraft(PrepareItemCraftEvent event) {
    if (!(event.getRecipe() instanceof ShapedRecipe shaped)) {
      return;
    }
    if (!shaped.getKey().equals(recipeKey)) {
      return;
    }

    for (ItemStack item : event.getInventory().getMatrix()) {
      if (item != null && item.getType() == Material.ELYTRA) {
        if (item.getItemMeta() instanceof Damageable damageable
            && damageable.getDamage() > 0) {
          event.getInventory().setResult(null);
        }
        return;
      }
    }
  }
}
