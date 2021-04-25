package com.mysteria.titles.events;

import com.mysteria.titles.enums.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerSelectTitleEvent extends Event {

	private final Player player;
	private final Title selectedTitle;

	public PlayerSelectTitleEvent(@Nonnull Player player, @Nullable Title selectedTitle) {
		this.player = player;
		this.selectedTitle = selectedTitle;
	}

	private static final HandlerList HANDLERS = new HandlerList();

	@Override
	public @Nonnull
	HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Nonnull
	public Player getPlayer() {
		return player;
	}

	@Nullable
	public Title getSelectedTitle() {
		return selectedTitle;
	}

}