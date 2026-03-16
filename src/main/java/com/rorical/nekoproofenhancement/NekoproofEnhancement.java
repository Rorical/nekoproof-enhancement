package com.rorical.nekoproofenhancement;

import io.papermc.lib.PaperLib;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for NekoproofEnhancement.
 */
public class NekoproofEnhancement extends JavaPlugin {

  @Override
  public void onEnable() {
    PaperLib.suggestPaper(this);
    saveDefaultConfig();

    ElytraDuplicationRecipe.register(this);

    getLogger().info("NekoproofEnhancement enabled.");
  }

  @Override
  public void onDisable() {
    getLogger().info("NekoproofEnhancement disabled.");
  }
}
