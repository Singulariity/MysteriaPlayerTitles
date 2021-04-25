package com.mysteria.titles.events;

import com.mysteria.titles.enums.AcquireReason;
import com.mysteria.titles.enums.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class PlayerAcquireTitleEvent extends Event {

	private final Player player;
	private final Title title;
	private final AcquireReason acquireReason;

	public PlayerAcquireTitleEvent(@Nonnull Player player, @Nonnull Title title, @Nonnull AcquireReason acquireReason) {
		this.player = player;
		this.title = title;
		this.acquireReason = acquireReason;
	}

	public PlayerAcquireTitleEvent(@Nonnull Player player, @Nonnull Title title) {
		this(player, title, AcquireReason.NATURAL);
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

	@Nonnull
	public Title getTitle() {
		return title;
	}

	@Nonnull
	public AcquireReason getAcquireReason() {
		return acquireReason;
	}

}

