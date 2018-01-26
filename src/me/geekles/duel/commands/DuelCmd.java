package me.geekles.duel.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.geekles.duel.main.Main;
import me.geekles.duel.misc.Data;
import me.geekles.duel.misc.DuelType;
import me.geekles.duel.misc.GameManager;
import me.geekles.duel.misc.gui.MainDuelGUI;
import net.md_5.bungee.api.ChatColor;

public class DuelCmd implements CommandExecutor {

	private Data data;

	public DuelCmd(Data data) {
		this.data = data;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("duel")) {
				if(args.length <= 1) {
					if(player.hasPermission("duel.join")) {
						if(args.length == 1) {
							if(args[0].equalsIgnoreCase("join")) {
								if(!data.PlayerLobby.containsKey(player.getUniqueId())) {
									data.storedLocation.put(player.getUniqueId(), player.getLocation());
								}
								player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 0f);
								player.sendMessage(ChatColor.GREEN + "Opening Duel GUI...");
								player.openInventory(new MainDuelGUI(data, player).createMainGUI());
								return true;
							} else {

							}
						} else {
							if(!data.PlayerLobby.containsKey(player.getUniqueId())) {
								data.storedLocation.put(player.getUniqueId(), player.getLocation());
							}
							player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 0f);
							player.sendMessage(ChatColor.GREEN + "Opening Duel GUI...");
							player.openInventory(new MainDuelGUI(data, player).createMainGUI());
							return true;
						}

					}

				}
				if(args.length == 1) {
					if(player.hasPermission("duel.quit")) {
						if(args[0].equalsIgnoreCase("quit") || args[0].equalsIgnoreCase("leave")) {
							if(data.PlayerLobby.containsKey(player.getUniqueId())) {
								player.sendMessage(ChatColor.GREEN + "Successfully left duel!");
								Main.econ.depositPlayer(player, Main.instance.getConfig().getDouble("Duels." + data.PlayerLobby.get(player.getUniqueId()).toString() + ".Wager"));
								player.teleport(data.storedLocation.get(player.getUniqueId()));
								data.storedLocation.remove(player.getUniqueId());
								DuelType lobby = data.PlayerLobby.get(player.getUniqueId());
								ItemStack[] items = data.storedInventory.get(player.getUniqueId()).toArray(new ItemStack[data.storedInventory.get(player.getUniqueId()).size()]);
								ItemStack[] armor = data.storedArmor.get(player.getUniqueId()).toArray(new ItemStack[data.storedArmor.get(player.getUniqueId()).size()]);
								player.getInventory().setContents(items);
								player.getInventory().setArmorContents(armor);
								data.PlayerLobby.remove(player.getUniqueId());
								data.storedInventory.remove(player.getUniqueId());
								data.storedArmor.remove(player.getUniqueId());
								for (Player p : GameManager.getDuelPlayers(lobby)) {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4" + player.getDisplayName() + "&c has left the duel!"));
									p.sendMessage(ChatColor.translateAlternateColorCodes('&',
											"&7[" + lobby.getColorCodes().get(1) + GameManager.getDuelPlayers(lobby).size() + "&8/" + lobby.getColorCodes().get(0) + "" + lobby.getTotalPopulation() + "&7] &aplayers left!"));
								}
								GameManager.update(lobby);
								return true;
							} else {
								player.sendMessage(ChatColor.RED + "You aren't in a duel. Join by trying /duel join instead.");
								return true;
							}
						}
					}
				}
				if(args.length == 3) {
					if(args[0].equalsIgnoreCase("set")) {
						if(player.hasPermission("duel.admin")) {
							if(args[1].equalsIgnoreCase("lobby")) {
								if(DuelType.isDuelType(args[2])) {
									Location loc = new Location(player.getWorld(), player.getLocation().getBlockX() + 0.5, player.getLocation().getBlockY(), player.getLocation().getBlockZ() + 0.5);
									loc.setYaw(player.getLocation().getYaw());
									loc.setPitch(player.getLocation().getPitch());

									data.Lobbies.put(DuelType.convertFromString(args[2]), loc);

									player.teleport(loc);
									player.sendMessage(ChatColor.GREEN + "Successfully set spawnpoint for lobby type, " + ChatColor.DARK_GREEN + args[2].toLowerCase());
									return true;
								}
							} else if(args[1].equalsIgnoreCase("arena")) {
								if(DuelType.isDuelType(args[2])) {
									DuelType type = DuelType.convertFromString(args[2]);
									Location loc = new Location(player.getWorld(), player.getLocation().getBlockX() + 0.5, player.getLocation().getBlockY(), player.getLocation().getBlockZ() + 0.5);
									loc.setYaw(player.getLocation().getYaw());
									loc.setPitch(player.getLocation().getPitch());

									if(!data.Arenas.containsKey(type)) {
										List<Location> locations = new ArrayList<Location>();
										locations.add(loc);
										data.Arenas.put(type, locations);
									} else {
										List<Location> locations = new ArrayList<Location>(data.Arenas.get(type));
										locations.add(loc);
										data.Arenas.put(type, locations);
									}
									player.teleport(loc);
									player.sendMessage(ChatColor.GREEN + "Successfully set spawnpoint #" + data.Arenas.get(type).size() + " for arena type, " + ChatColor.DARK_GREEN + type.toString());
									return true;
								}

							}
						}
					}
				}
			}
		}
		return false;
	}

}
