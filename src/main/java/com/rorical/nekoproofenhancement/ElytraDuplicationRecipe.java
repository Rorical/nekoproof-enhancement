package com.rorical.nekoproofenhancement;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Registers a crafting recipe that duplicates an elytra.
 *
 * <p>Recipe: Elytra in the center, Diamond at top center,
 * remaining slots filled with Phantom Membrane. Produces 2 elytra.
 */
public final class ElytraDuplicationRecipe {

  private ElytraDuplicationRecipe() {
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
  }
}
