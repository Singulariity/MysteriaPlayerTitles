package com.mysteria.titles.listeners;

import com.mysteria.titles.PlayerTitlesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

	public GUIListener() {
		Bukkit.getPluginManager().registerEvents(this, PlayerTitlesPlugin.getInstance());
	}

	@EventHandler(ignoreCancelled = true)
	private void onGUIClick(InventoryClickEvent e) {

		if (e.getView().title().equals(PlayerTitlesPlugin.getTitleManager().getGUIName())) {
			e.setCancelled(true);
		}

	}

}
