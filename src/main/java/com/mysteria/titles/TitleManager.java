package com.mysteria.titles;

import com.mysteria.customapi.items.CustomItem;
import com.mysteria.customapi.rarity.Rarity;
import com.mysteria.essentials.EssentialsPlugin;
import com.mysteria.titles.enums.AcquireReason;
import com.mysteria.titles.enums.Title;
import com.mysteria.titles.events.PlayerAcquireTitleEvent;
import com.mysteria.titles.events.PlayerSelectTitleEvent;
import com.mysteria.utils.ItemBuilder;
import com.mysteria.utils.NamedColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TitleManager {

	private final Component GUIName;

	public TitleManager() {
		if (PlayerTitlesPlugin.getTitleManager() != null) {
			throw new IllegalStateException();
		}
		GUIName = Component.translatable("mystery.gui.titlelist.name", NamedTextColor.DARK_GRAY);
	}

	public void openGUI(@Nonnull Player guiP, @Nonnull Player titleP) {

		TitleManager titleManager = PlayerTitlesPlugin.getTitleManager();

		Inventory inv = Bukkit.createInventory(null, 36, GUIName);

		ItemStack empty = ItemBuilder.builder(CustomItem.EMPTY.getItemStack())
				.adaptGUI()
				.build();
		for (int i = 0; i < inv.getSize(); i++) {
			inv.setItem(i, empty);
		}

		inv.setItem(4, ItemBuilder.builder(CustomItem.TOME.getItemStack())
				.name(
						Component.translatable("mystery.gui.titlelist.information.name", NamedTextColor.GREEN)
								.decorate(TextDecoration.BOLD))
				.lore(
						Component.translatable("mystery.gui.titlelist.information.description", NamedTextColor.GRAY),
						Component.text(" "),
						Component.text()
								.append(Component.translatable("mystery.gui.titlelist.information.player",
										NamedTextColor.YELLOW))
								.append(Component.text(titleP.getName(), NamedTextColor.RED))
								.build(),
						Component.text()
								.append(Component.translatable("mystery.gui.titlelist.information.selected_title",
										NamedTextColor.YELLOW))
								.append(EssentialsPlugin.getPlayerManager().getTitle(titleP)
										.color(EssentialsPlugin.getPlayerManager().getTitleColor(titleP)))
								.build())
				.glow()
				.adaptGUI()
				.build());


		HashMap<Rarity, List<Component>> lists = new HashMap<>();
		for (Rarity rarity : Rarity.values()) {
			lists.put(rarity, new ArrayList<>());
		}
		for (Title title :  titleManager.getTitles(titleP)) {
			Component line = Component.text("  • ", NamedColor.SILVER)
					.append(Component.text(title.getDisplayName(), NamedTextColor.GRAY));
			lists.get(title.getRarity()).add(line);
		}

		ItemStack blessing_scroll = CustomItem.BLESSING_SCROLL.getItemStack();
		inv.setItem(18, ItemBuilder.builder(blessing_scroll)
				.name(Rarity.COMMON.getDisplayName())
				.lore(lists.get(Rarity.COMMON))
				.adaptGUI()
				.build());
		inv.setItem(19, ItemBuilder.builder(blessing_scroll)
				.name(Rarity.UNCOMMON.getDisplayName())
				.lore(lists.get(Rarity.UNCOMMON))
				.adaptGUI()
				.build());
		inv.setItem(20, ItemBuilder.builder(blessing_scroll)
				.name(Rarity.RARE.getDisplayName())
				.lore(lists.get(Rarity.RARE))
				.adaptGUI()
				.build());
		inv.setItem(21, ItemBuilder.builder(blessing_scroll)
				.name(Rarity.EPIC.getDisplayName())
				.lore(lists.get(Rarity.EPIC))
				.adaptGUI()
				.build());
		inv.setItem(22, ItemBuilder.builder(blessing_scroll)
				.name(Rarity.HEAVENLY.getDisplayName())
				.lore(lists.get(Rarity.HEAVENLY))
				.adaptGUI()
				.build());
		inv.setItem(23, ItemBuilder.builder(blessing_scroll)
				.name(Rarity.LEGENDARY.getDisplayName())
				.lore(lists.get(Rarity.LEGENDARY))
				.adaptGUI()
				.build());
		inv.setItem(24, ItemBuilder.builder(blessing_scroll)
				.name(Rarity.SPECIAL.getDisplayName())
				.lore(lists.get(Rarity.SPECIAL))
				.adaptGUI()
				.build());
		inv.setItem(25, ItemBuilder.builder(blessing_scroll)
				.name(Rarity.EVENT.getDisplayName())
				.lore(lists.get(Rarity.EVENT))
				.adaptGUI()
				.build());
		inv.setItem(26, ItemBuilder.builder(blessing_scroll)
				.name(Rarity.PREMIUM.getDisplayName())
				.lore(lists.get(Rarity.PREMIUM))
				.adaptGUI()
				.build());

		guiP.openInventory(inv);

	}

	public Component getGUIName() {
		return GUIName;
	}





	public boolean addTitle(@Nonnull Player p, @Nonnull Title title) {
		return addTitle(p, title, AcquireReason.NATURAL);
	}

	public boolean addTitle(@Nonnull Player p, @Nonnull Title title, @Nonnull AcquireReason acquireReason) {
		if (hasTitle(p, title)) {
			return false;
		}

		try {
			PreparedStatement prst = PlayerTitlesPlugin.getConnection()
					.prepareStatement("INSERT INTO player_titles (Player, Title) VALUES (?, ?)");
			prst.setString(1, p.getUniqueId().toString());
			prst.setString(2, title.toString());
			prst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		PlayerAcquireTitleEvent playerAcquireTitleEvent = new PlayerAcquireTitleEvent(p, title, acquireReason);
		Bukkit.getPluginManager().callEvent(playerAcquireTitleEvent);
		return true;
	}

	public boolean removeTitle(@Nonnull Player p, @Nonnull Title title) {
		if (!hasTitle(p, title)) {
			return false;
		}

		try {
			PreparedStatement prst = PlayerTitlesPlugin.getConnection()
					.prepareStatement("DELETE FROM player_titles WHERE Player = '" + p.getUniqueId() + "' and Title = '" + title + "'");
			prst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean hasTitle(@Nonnull Player p, @Nonnull Title title) {
		try {
			Statement st = PlayerTitlesPlugin.getConnection().createStatement();
			String query = "SELECT TITLE FROM player_titles WHERE Player = '" + p.getUniqueId() + "' and Title = '" + title + "'";
			ResultSet resultSet = st.executeQuery(query);

			if (resultSet.next()) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Title> getTitles(@Nonnull Player p) {
		ArrayList<Title> titles = new ArrayList<>();

		try {
			Statement st = PlayerTitlesPlugin.getConnection().createStatement();
			String query = "SELECT TITLE FROM player_titles WHERE Player = '" + p.getUniqueId() + "'";
			ResultSet resultSet = st.executeQuery(query);

			while (resultSet.next()) {
				try {
					titles.add(Title.valueOf(resultSet.getString("Title")));
				} catch (IllegalArgumentException ignored) {}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}


		return titles;
	}

	public boolean selectTitle(@Nonnull Player p, @Nullable Title title) {

		if (title == null) {
			try {
				PreparedStatement prst = PlayerTitlesPlugin.getConnection()
						.prepareStatement("DELETE FROM selected_titles WHERE Player = '" + p.getUniqueId() + "'");
				prst.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			if (!hasTitle(p, title)) {
				return false;
			}

			try {
				Statement st = PlayerTitlesPlugin.getConnection().createStatement();
				String query = "SELECT Player FROM selected_titles WHERE Player = '" + p.getUniqueId() + "'";
				ResultSet resultSet = st.executeQuery(query);

				if (resultSet.next()) {
					try {
						PreparedStatement prst = PlayerTitlesPlugin.getConnection()
								.prepareStatement("UPDATE selected_titles SET (Title) = (?) WHERE Player = ?");
						prst.setString(1, title.toString());
						prst.setString(2, p.getUniqueId().toString());
						prst.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					try {
						PreparedStatement prst = PlayerTitlesPlugin.getConnection()
								.prepareStatement("INSERT INTO selected_titles (Player, Title) VALUES (?, ?)");
						prst.setString(1, p.getUniqueId().toString());
						prst.setString(2, title.toString());
						prst.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		PlayerSelectTitleEvent playerSelectTitleEvent = new PlayerSelectTitleEvent(p, title);
		Bukkit.getPluginManager().callEvent(playerSelectTitleEvent);
		return true;

	}

	@Nullable
	public Title getPlayerTitle(@Nonnull Player p) {

		try {
			Statement st = PlayerTitlesPlugin.getConnection().createStatement();
			String query = "SELECT TITLE FROM selected_titles WHERE Player = '" + p.getUniqueId() + "'";
			ResultSet resultSet = st.executeQuery(query);

			while (resultSet.next()) {
				try {
					return Title.valueOf(resultSet.getString("Title"));
				} catch (IllegalArgumentException ignored) {}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}



}
