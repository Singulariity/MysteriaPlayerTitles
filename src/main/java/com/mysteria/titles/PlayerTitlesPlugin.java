package com.mysteria.titles;

import co.aikar.commands.PaperCommandManager;
import com.mysteria.titles.commands.TitleCommand;
import com.mysteria.titles.enums.Title;
import com.mysteria.titles.listeners.GUIListener;
import com.mysteria.titles.listeners.PlayerAcquireTitleListener;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class PlayerTitlesPlugin extends JavaPlugin {

	private static PlayerTitlesPlugin instance;
	private static TitleManager titleManager;
	private static Connection connection;
	private static PaperCommandManager commandManager;

	public PlayerTitlesPlugin() {
		if (instance != null) throw new IllegalStateException();
		instance = this;
	}

	@Override
	public void onEnable() {

		setupDatabase();
		titleManager = new TitleManager();

		commandManager = new PaperCommandManager(getInstance());

		registerListeners();
		registerCommands();
	}


	private void registerListeners() {
		new PlayerAcquireTitleListener();
		new GUIListener();
	}

	private void registerCommandCompletions() {
		getCommandManager().getCommandCompletions().registerAsyncCompletion("title", c -> {
			String setting = c.getConfig("only", "all");

			List<String> list = new ArrayList<>();
			switch (setting.toLowerCase()) {
				case "acquired":
					list.add("None");
					for (Title title : getTitleManager().getTitles(c.getPlayer())) {
						list.add(title.getDisplayName());
					}
					break;
				case "notacquired":
					List<Title> playerTitles = getTitleManager().getTitles(c.getPlayer());
					for (Title title : Title.values()) {
						if (!playerTitles.contains(title)) {
							list.add(title.getDisplayName());
						}
					}
					break;
				case "all":
				default:
					for (Title title : Title.values()) {
						list.add(title.getDisplayName());
					}
					break;
			}
			return list;

		});
	}

	private void registerCommands() {
		registerCommandCompletions();
		getCommandManager().registerCommand(new TitleCommand());
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private void setupDatabase() {
		HikariConfig config = new HikariConfig();

		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		try {
			String url = "jdbc:sqlite:./" + this.getDataFolder() + "/playertitles.db";

			config.setJdbcUrl(url);
			config.setUsername("mysteria");

			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

			HikariDataSource ds = new HikariDataSource(config);

			connection = ds.getConnection();
			Statement st = connection.createStatement();
			st.execute("CREATE TABLE IF NOT EXISTS player_titles (" +
					"PLAYER UUID NOT NULL, " +
					"TITLE STRING NOT NULL)");
			st.execute("CREATE TABLE IF NOT EXISTS selected_titles (" +
					"PLAYER UUID NOT NULL PRIMARY KEY, " +
					"TITLE STRING NOT NULL)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static PlayerTitlesPlugin getInstance() {
		if (instance == null) throw new IllegalStateException();
		return instance;
	}

	public static TitleManager getTitleManager() {
		return titleManager;
	}

	public static PaperCommandManager getCommandManager() {
		return commandManager;
	}

	public static Connection getConnection() {
		return connection;
	}
}
