package me.geekles.duel.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.geekles.duel.main.Main;
import net.md_5.bungee.api.ChatColor;

public class GameManager {

	public static boolean ONE_VS_ONE_LOCKED = false;
	public static boolean FIVE_VS_FIVE_LOCKED = false;
	public static boolean TEN_VS_TEN_LOCKED = false;

	public static boolean ONE_VS_ONE_STARTED = false;
	public static boolean FIVE_VS_FIVE_STARTED = false;
	public static boolean TEN_VS_TEN_STARTED = false;

	public static boolean ONE_VS_ONE_ENDING = false;
	public static boolean FIVE_VS_FIVE_ENDING = false;
	public static boolean TEN_VS_TEN_ENDING = false;

	protected static Data data;
	protected static Main plugin;

	private static long lastTime = 0;
	private static final long COOLDOWN = (60000 * 8);

	public GameManager(Data d) {
		data = d;
	}

	public GameManager setup(Main p) {
		plugin = p;
		lastTime = System.currentTimeMillis();
		return this;
	}

	public static List<Player> getDuelPlayers(DuelType type) {
		List<Player> players = new ArrayList<Player>();
		for (UUID id : data.PlayerLobby.keySet()) {
			if(data.PlayerLobby.get(id) == type) {
				players.add(Bukkit.getPlayer(id));
			}
		}
		return players;
	}

	public static void update(DuelType type) {
		if(type.getTotalPopulation() > getDuelPlayers(type).size()) {
			if(!type.ending() && type.started() && getDuelPlayers(type).size() == 1) {
				type.stop();
			} else if(!type.started() && getDuelPlayers(type).size() == 1) {
				if(System.currentTimeMillis() - lastTime >= COOLDOWN) {
					GameManager.lastTime = System.currentTimeMillis();
					Bukkit.broadcastMessage(
							ChatColor.translateAlternateColorCodes('&', "&8[&c⚔&8]&c&l " + getDuelPlayers(type).get(0).getDisplayName() + " &8wants to start a new &c&l" + type.toString() + " FFA/Duel&8! Type &c&l/duel join&8 to join!"));
				}
			} else if(!type.started() && getDuelPlayers(type).size() == (type.getTotalPopulation() - 1)) {
				if(System.currentTimeMillis() - lastTime >= COOLDOWN) {
					GameManager.lastTime = System.currentTimeMillis();
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&8[&c⚔&8] &8Only &c&lONE &8player is needed to &c&lSTART &8a &c&l" + type.toString() + " FFA/Duel&8!"));
				}
			}
		}
		if(type.getTotalPopulation() <= getDuelPlayers(type).size()) {
			if(!type.isLocked()) {
				type.lock();
				if(!type.started()) {
					type.start();
				}
			}
		}
		if(!type.isLocked() && !type.started()) {
			if(data.Waitlist.containsKey(type)) {
				if(data.Waitlist.get(type).size() > 0) {
					while (GameManager.getDuelPlayers(type).size() < type.getTotalPopulation()) {
						Player player = Bukkit.getPlayer(data.Waitlist.get(type).get(0));
						List<UUID> list = new ArrayList<UUID>(data.Waitlist.get(type));
						list.remove(player.getUniqueId());
						data.Waitlist.put(type, list);
						LobbyManager.addPlayer(player, type);
					}

				}
			}
		}
	}

}
