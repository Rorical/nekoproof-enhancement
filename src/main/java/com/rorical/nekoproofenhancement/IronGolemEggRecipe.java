package com.rorical.nekoproofenhancement;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Registers a crafting recipe for an Iron Golem spawn egg.
 *
 * <p>Recipe shape matches the iron golem building pattern:
 * <pre>
 *   _P_
 *   III
 *   _I_
 * </pre>
 * Where P = Carved Pumpkin, I = Iron Block.
 */
public final class IronGolemEggRecipe implements Listener {

  private final NamespacedKey recipeKey;

  private IronGolemEggRecipe(NamespacedKey recipeKey) {
    this.recipeKey = recipeKey;
  }

  /**
   * Registers the iron golem egg recipe with the server.
   *
   * @param plugin the plugin instance
   */
  public static void register(JavaPlugin plugin) {
    NamespacedKey key = new NamespacedKey(plugin, "iron_golem_egg");

    ItemStack result = new ItemStack(Material.IRON_GOLEM_SPAWN_EGG, 1);
    ShapedRecipe recipe = new ShapedRecipe(key, result);

    recipe.shape(" P ", "III", " I ");
    recipe.setIngredient('P', Material.CARVED_PUMPKIN);
    recipe.setIngredient('I', Material.IRON_BLOCK);

    plugin.getServer().addRecipe(recipe);
    plugin.getServer().getPluginManager().registerEvents(
        new IronGolemEggRecipe(key), plugin);
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
}
