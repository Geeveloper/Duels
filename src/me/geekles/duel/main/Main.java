package me.geekles.duel.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.geekles.duel.commands.DuelCmd;
import me.geekles.duel.misc.Data;
import me.geekles.duel.misc.DuelType;
import me.geekles.duel.misc.DuelUpdater;
import me.geekles.duel.misc.GameManager;
import me.geekles.duel.misc.GameManagerListener;
import me.geekles.duel.misc.LobbyManager;
import me.geekles.duel.misc.gui.GUIListener;
import me.geekles.duel.misc.gui.KitGUI;
import me.geekles.duel.misc.gui.KitGUIAnimation;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	protected Data data;
	protected DuelCmd cmd;
	protected GUIListener gui_listener;
	protected GameManagerListener game_listener;
	protected GameManager game;
	protected LobbyManager lobby;
	protected KitGUI wager;
	protected KitGUIAnimation wager_updater;
	protected DuelUpdater updater;

	private File dataYml = new File(this.getDataFolder() + "/data.yml");
	public FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataYml);
	public static Economy econ = null;

	public static Main instance;

	public void onEnable() {
		instance = this;

		if(!new File(this.getDataFolder(), "config.yml").exists()) {
			System.out.println(ChatColor.YELLOW + "No Config Found! Generating a new one...");
			this.saveDefaultConfig();
		}
		loadConfig();

		if(!setupEconomy()) {
			getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		getLogger().info("Initializing Classes...");
		initializeClasses();
		getLogger().info("Success!");

		checkData();
		getLogger().info("Loading stored data...");
		loadData();
		getLogger().info("Success!");

		getLogger().info("Registering Commands...");
		registerCommands(cmd, "duel");
		getLogger().info("Success!");

		getLogger().info("Registering Listeners...");
		registerEvents(gui_listener, game_listener);
		getLogger().info("Success!");

		System.out.println(ChatColor.translateAlternateColorCodes('&', "&7&l༼ つ ͡◕ Ѿ ͡◕ ༽つ &a&lSuccessfully &2&lEnabled&a&l Duels!"));
	}

	public void onDisable() {
		storeData();
		saveData();

		for (UUID id : data.PlayerLobby.keySet()) {
			Player player = Bukkit.getPlayer(id);
			player.teleport(data.storedLocation.get(id));
			player.getInventory().clear();
			player.getInventory().setHelmet(null);
			player.getInventory().setChestplate(null);
			player.getInventory().setLeggings(null);
			player.getInventory().setBoots(null);
			ItemStack[] items = data.storedInventory.get(id).toArray(new ItemStack[data.storedInventory.get(id).size()]);
			player.getInventory().setContents(items);
			ItemStack[] armor = data.storedArmor.get(id).toArray(new ItemStack[data.storedArmor.get(id).size()]);
			player.getInventory().setArmorContents(armor);
			Main.econ.depositPlayer(player, Main.instance.getConfig().getDouble("Duels." + data.PlayerLobby.get(player.getUniqueId()).toString() + ".Wager"));
		}

		System.out.println(ChatColor.translateAlternateColorCodes('&', "&7(╯°□°）╯︵ ┻━┻ &a&lSuccessfully &c&lDisabled &a&lDuels!"));
	}

	private void initializeClasses() {
		data = new Data();
		cmd = new DuelCmd(data);
		gui_listener = new GUIListener(data);
		game_listener = new GameManagerListener(data);
		game = new GameManager(data).setup(this);
		lobby = new LobbyManager(data);
		wager = new KitGUI(data);
		updater = new DuelUpdater();
		updater.runTaskTimer(this, 120L, 20L);
		DuelType.setup(data);
	}

	private void registerCommands(CommandExecutor executor, String... commands) {
		for (String cmd : commands) {
			getCommand(cmd).setExecutor(executor);
		}
	}

	private void registerEvents(Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getPluginManager().registerEvents(listener, this);
		}
	}

	private void loadConfig() {

	}

	private boolean setupEconomy() {
		if(getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	private void checkData() {
		if(!new File(this.getDataFolder(), "data.yml").exists()) {
			try {
				dataConfig.save(dataYml);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadData() {
		if(dataConfig.contains("Lobbies")) {
			for (String key : dataConfig.getConfigurationSection("Lobbies").getKeys(false)) {
				World world = Bukkit.getWorld(dataConfig.getString("Lobbies." + key + ".world"));
				double x = dataConfig.getDouble("Lobbies." + key + ".x");
				double y = dataConfig.getDouble("Lobbies." + key + ".y");
				double z = dataConfig.getDouble("Lobbies." + key + ".z");
				float yaw = (float) dataConfig.getDouble("Lobbies." + key + ".yaw");
				float pitch = (float) dataConfig.getDouble("Lobbies." + key + ".pitch");
				Location loc = new Location(world, x, y, z);
				loc.setYaw(yaw);
				loc.setPitch(pitch);
				data.Lobbies.put(DuelType.convertFromString(key), loc);
			}
		}
		if(dataConfig.contains("Arenas")) {
			for (String ke : dataConfig.getConfigurationSection("Arenas").getKeys(false)) {
				List<Location> locations = new ArrayList<Location>();
				for (String key : dataConfig.getConfigurationSection("Arenas." + ke).getKeys(false)) {
					String path = "Arenas." + ke + "." + key;
					World world = Bukkit.getWorld(dataConfig.getString(path + ".world"));
					double x = dataConfig.getDouble(path + ".x");
					double y = dataConfig.getDouble(path + ".y");
					double z = dataConfig.getDouble(path + ".z");
					float yaw = (float) dataConfig.getDouble(path + ".yaw");
					float pitch = (float) dataConfig.getDouble(path + ".pitch");
					Location loc = new Location(world, x, y, z);
					loc.setYaw(yaw);
					loc.setPitch(pitch);
					locations.add(loc);
				}
				data.Arenas.put(DuelType.convertFromString(ke), locations);
			}
		}
	}

	private void storeData() {
		for (DuelType lobbies : data.Lobbies.keySet()) {
			Location loc = data.Lobbies.get(lobbies);
			dataConfig.set("Lobbies." + lobbies.toString() + ".world", loc.getWorld().getName());
			dataConfig.set("Lobbies." + lobbies.toString() + ".x", loc.getX());
			dataConfig.set("Lobbies." + lobbies.toString() + ".y", loc.getY());
			dataConfig.set("Lobbies." + lobbies.toString() + ".z", loc.getZ());
			dataConfig.set("Lobbies." + lobbies.toString() + ".yaw", loc.getYaw());
			dataConfig.set("Lobbies." + lobbies.toString() + ".pitch", loc.getPitch());
		}

		for (DuelType lobbies : data.Arenas.keySet()) {
			int spawnpoint = 1;
			for (Location loc : data.Arenas.get(lobbies)) {
				dataConfig.set("Arenas." + lobbies.toString() + "." + spawnpoint + ".world", loc.getWorld().getName());
				dataConfig.set("Arenas." + lobbies.toString() + "." + spawnpoint + ".x", loc.getX());
				dataConfig.set("Arenas." + lobbies.toString() + "." + spawnpoint + ".y", loc.getY());
				dataConfig.set("Arenas." + lobbies.toString() + "." + spawnpoint + ".z", loc.getZ());
				dataConfig.set("Arenas." + lobbies.toString() + "." + spawnpoint + ".yaw", loc.getYaw());
				dataConfig.set("Arenas." + lobbies.toString() + "." + spawnpoint + ".pitch", loc.getPitch());
				spawnpoint++;
			}
		}

	}

	private void saveData() {
		try {
			dataConfig.save(dataYml);
		} catch (IOException e) {
			System.out.println(ChatColor.RED + "Failed to save data!");
			e.printStackTrace();
		}
	}

}
