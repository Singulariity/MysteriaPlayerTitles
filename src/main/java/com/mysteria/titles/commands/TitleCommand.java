package com.mysteria.titles.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.mysteria.titles.PlayerTitlesPlugin;
import com.mysteria.titles.TitleManager;
import com.mysteria.titles.enums.AcquireReason;
import com.mysteria.titles.enums.Title;
import com.mysteria.utils.MysteriaUtils;
import com.mysteria.utils.NamedColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandAlias("title")
public class TitleCommand extends BaseCommand {

	@Subcommand("list")
	public void list(Player p) {
		TitleManager titleManager = PlayerTitlesPlugin.getTitleManager();
		titleManager.openGUI(p, p);
	}

	@Subcommand("select")
	@CommandCompletion("@title:only=acquired @nothing")
	public void select(Player p, String arg) {
		Title title;

		boolean argIsNone = arg.equalsIgnoreCase("None");
		if (argIsNone) {
			title = null;
		} else {
			title = Title.getFromDisplayName(arg);
		}
		if (title == null && !argIsNone) {
			MysteriaUtils.sendMessageRed(p, "Title not found.");
			return;
		}

		TitleManager titleManager = PlayerTitlesPlugin.getTitleManager();

		if (titleManager.selectTitle(p, title)) {
			String name;
			if (title == null) {
				name = "None";
			} else {
				name = title.getDisplayName();
			}
			MysteriaUtils.sendMessage(p,
					Component.text()
							.append(Component.text("New title selected: ", NamedColor.SOARING_EAGLE))
							.append(Component.text(name, NamedColor.TURBO))
							.build());
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
		} else {
			MysteriaUtils.sendMessageRed(p, "You don't have this title.");
		}

	}

	@Subcommand("grant")
	@CommandCompletion("@players @title @nothing")
	@CommandPermission("playertitles.granttitle")
	public void grant(Player player, String[] args) {

		Player p = Bukkit.getPlayer(args[0]);
		if (p == null) {
			MysteriaUtils.sendMessageRed(player, "Player not found.");
			return;
		}

		String[] displayName = Arrays.copyOfRange(args, 1, args.length);
		Title title = Title.getFromDisplayName(String.join(" ", displayName));

		if (title == null) {
			MysteriaUtils.sendMessageRed(player, "Title not found.");
			return;
		}

		TitleManager titleManager = PlayerTitlesPlugin.getTitleManager();
		if (titleManager.addTitle(p, title, AcquireReason.COMMAND)) {
			MysteriaUtils.sendMessageGreen(player, "Success.");
		} else {
			MysteriaUtils.sendMessageYellow(player, "The player already acquired this title.");
		}
	}

	@Subcommand("revoke")
	@CommandCompletion("@players @title @nothing")
	@CommandPermission("playertitles.revoketitle")
	public void revoke(Player player, String[] args) {

		Player p = Bukkit.getPlayer(args[0]);
		if (p == null) {
			MysteriaUtils.sendMessageRed(player, "Player not found.");
			return;
		}

		String[] displayName = Arrays.copyOfRange(args, 1, args.length);
		Title title = Title.getFromDisplayName(String.join(" ", displayName));

		if (title == null) {
			MysteriaUtils.sendMessageRed(player, "Title not found.");
			return;
		}

		TitleManager titleManager = PlayerTitlesPlugin.getTitleManager();
		if (titleManager.removeTitle(p, title)) {
			MysteriaUtils.sendMessageGreen(player, "Success.");
		} else {
			MysteriaUtils.sendMessageYellow(player, "The player doesn't have the title.");
		}
	}



}
