package com.mysteria.titles.listeners;

import com.mysteria.customapi.rarity.Rarity;
import com.mysteria.titles.PlayerTitlesPlugin;
import com.mysteria.titles.events.PlayerAcquireTitleEvent;
import com.mysteria.utils.MysteriaUtils;
import com.mysteria.utils.NamedColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAcquireTitleListener implements Listener {

	public PlayerAcquireTitleListener() {
		Bukkit.getPluginManager().registerEvents(this, PlayerTitlesPlugin.getInstance());
	}

	@EventHandler
	private void onAcquireTitle(PlayerAcquireTitleEvent e) {

		Player p = e.getPlayer();
		Rarity rarity = e.getTitle().getRarity();

		Component outerLine = MysteriaUtils.centeredComponent(
				Component.text()
						.append(Component.text("---------------------------------------------", NamedColor.WIZARD_GREY)
								.decorate(TextDecoration.BOLD)
								.decorate(TextDecoration.STRIKETHROUGH))
						.build());
		Component firstLine = MysteriaUtils.centeredComponent(
				Component.text()
						.append(Component.text("You earned a "))
						.append(Component.text(rarity.toString(), rarity.getColor())
								.decorate(TextDecoration.BOLD))
						.append(Component.text(" title!  ( "))
						.append(Component.text(e.getTitle().getDisplayName(), NamedColor.TURBO)
								.decorate(TextDecoration.BOLD))
						.append(Component.text(" )"))
						.colorIfAbsent(NamedTextColor.GRAY)
						.build());
		Component secondLine = MysteriaUtils.centeredComponent(
				Component.text()
						.append(Component.text("< Click here to select this title >", NamedColor.DOWNLOAD_PROGRESS)
								.hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
										Component.text("Click to select", NamedColor.SKIRRET_GREEN)))
								.clickEvent(ClickEvent.runCommand("/title select " + e.getTitle().getDisplayName())))
						.build());
		Component thirdLine = MysteriaUtils.centeredComponent(
				Component.text()
						.append(Component.text("Or type "))
						.append(Component.text("/title select ")
								.append(Component.text(e.getTitle().getDisplayName())
										.decorate(TextDecoration.BOLD))
								.color(NamedColor.CARMINE_PINK))
						.append(Component.text(" to select manually."))
						.colorIfAbsent(NamedTextColor.GRAY)
						.build());

		p.sendMessage(outerLine);
		p.sendMessage(firstLine);
		p.sendMessage(" ");
		p.sendMessage(secondLine);
		p.sendMessage(" ");
		p.sendMessage(thirdLine);
		p.sendMessage(outerLine);

		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);


	}

}
