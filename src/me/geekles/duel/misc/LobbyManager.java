package me.geekles.duel.misc;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.geekles.duel.main.Main;

public class LobbyManager {

	private static Data data;

	public LobbyManager(Data d) {
		data = d;
	}

	public static void addPlayer(Player player, DuelType type) {
		if(data.Lobbies.containsKey(type)) {
			addToLobby(player, type);
		} else {
			player.sendMessage(ChatColor.RED + "An error occurred, please contact an administrator for further assistance!");
		}

	}

	private static void addToLobby(Player player, DuelType type) {
		Double amount = Main.instance.getConfig().getDouble("Duels." + type.toString() + ".Wager");
		if(Main.econ.getBalance(player) < amount) {
			player.sendMessage(ChatColor.RED + "You can't afford to wager, " + ChatColor.DARK_RED + "$" + amount + ChatColor.RED + "!");
			return;
		}

		player.sendMessage(ChatColor.GRAY + "Teleporting...");
		player.teleport(data.Lobbies.get(type));

		data.PlayerLobby.put(player.getUniqueId(), type);
		Main.econ.withdrawPlayer(player, amount);
		player.sendMessage(ChatColor.GREEN + "Successfully wagered $" + amount);
		data.storedInventory.put(player.getUniqueId(), Arrays.asList(player.getInventory().getContents()));
		data.storedArmor.put(player.getUniqueId(), Arrays.asList(player.getInventory().getArmorContents()));
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		player.setAllowFlight(false);
		if(player.isFlying()) {
			player.setFlying(false);
		}
		for (UUID id : data.PlayerLobby.keySet()) {
			if(data.PlayerLobby.get(id) == type) {
				Player p = Bukkit.getPlayer(id);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2" + player.getDisplayName() + " &ahas joined the duel!"));
				p.sendMessage(
						ChatColor.translateAlternateColorCodes('&', "&7[" + type.getColorCodes().get(1) + GameManager.getDuelPlayers(type).size() + "&8/" + type.getColorCodes().get(0) + "" + type.getTotalPopulation() + "&7] &acurrent players!"));
			}
		}
		GameManager.update(type);
	}

}
