package me.geekles.duel.misc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;

import me.geekles.duel.main.Main;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;

public class GameManagerListener implements Listener {

	private Data data;

	public GameManagerListener(Data data) {
		this.data = data;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(data.PlayerLobby.containsKey(player.getUniqueId())) {
			player.getInventory().clear();
			player.getInventory().setHelmet(null);
			player.getInventory().setChestplate(null);
			player.getInventory().setLeggings(null);
			player.getInventory().setBoots(null);
			player.teleport(data.storedLocation.get(player.getUniqueId()));
			if(data.PlayerLobby.get(player.getUniqueId()).started()) {
				player.setHealth(0);
			} else {
				Main.econ.depositPlayer(player, Main.instance.getConfig().getDouble("Duels." + data.PlayerLobby.get(player.getUniqueId()).toString() + ".Wager"));
			}
			DuelType lobby = data.PlayerLobby.get(player.getUniqueId());
			data.PlayerLobby.remove(player.getUniqueId());
			data.storedLocation.remove(player.getUniqueId());
			for (Player p : GameManager.getDuelPlayers(lobby)) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4" + player.getDisplayName() + "&c has left the duel!"));
				p.sendMessage(
						ChatColor.translateAlternateColorCodes('&', "&7[" + lobby.getColorCodes().get(1) + GameManager.getDuelPlayers(lobby).size() + "&8/" + lobby.getColorCodes().get(0) + "" + lobby.getTotalPopulation() + "&7] &aplayers left!"));
			}
			GameManager.update(lobby);
		}

	}

	@EventHandler
	public void onItemInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(data.PlayerLobby.containsKey(player.getUniqueId())) {
			DuelType type = data.PlayerLobby.get(player.getUniqueId());
			if(type.isLocked() && !type.started()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if(data.PlayerLobby.containsKey(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemDrop(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if(data.PlayerLobby.containsKey(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onFlight(PlayerMoveEvent event) {
		if(data.PlayerLobby.containsKey(event.getPlayer().getUniqueId())) {
			Player player = event.getPlayer();
			if(event.getFrom().getBlockY() != event.getTo().getBlockY()) {
				if(player.isFlying()) {
					player.setFlying(false);
					player.setAllowFlight(false);
					Location pLoc = player.getLocation();
					for (int y = 0; y < 15; y++) {
						pLoc.setY(pLoc.getY() - 1);
						if(pLoc.getBlock() == Material.AIR) {
							continue;
						} else {
							pLoc.setY(pLoc.getY() + 1);
							player.teleport(pLoc);
						}
					}

					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFlying is strictly forbidden during duels, please play fairly!"));
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		String cmd = e.getMessage();
		cmd = cmd.replace("/", "");
		String[] args = cmd.split(" ");
		if(args[0].equalsIgnoreCase("kit")) {
			Player player = e.getPlayer();
			if(data.PlayerLobby.containsKey(player.getUniqueId())) {
				BukkitScheduler scheduler = Main.instance.getServer().getScheduler();
				scheduler.scheduleSyncDelayedTask(Main.instance, new Runnable() {
					@Override
					public void run() {
						player.closeInventory();
					}
				}, 1L);

			}
		}
		if(args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("back") || args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("tpaccept") || args[0].equalsIgnoreCase("home")) {
			Player player = e.getPlayer();
			if(data.PlayerLobby.containsKey(player.getUniqueId())) {
				player.sendMessage(ChatColor.GREEN + "Successfully left duel!");
				player.getInventory().clear();
				player.getInventory().setHelmet(null);
				player.getInventory().setChestplate(null);
				player.getInventory().setLeggings(null);
				player.getInventory().setBoots(null);
				Main.econ.depositPlayer(player, Main.instance.getConfig().getDouble("Duels." + data.PlayerLobby.get(player.getUniqueId()).toString() + ".Wager"));
				data.storedLocation.remove(player.getUniqueId());
				DuelType type = data.PlayerLobby.get(player.getUniqueId());
				data.PlayerLobby.remove(player.getUniqueId());
				ItemStack[] items = data.storedInventory.get(player.getUniqueId()).toArray(new ItemStack[data.storedInventory.get(player.getUniqueId()).size()]);
				ItemStack[] armor = data.storedArmor.get(player.getUniqueId()).toArray(new ItemStack[data.storedArmor.get(player.getUniqueId()).size()]);
				player.getInventory().setContents(items);
				player.getInventory().setArmorContents(armor);
				for (Player p : GameManager.getDuelPlayers(type)) {
					p.sendMessage(
							ChatColor.translateAlternateColorCodes('&', "&7[" + type.getColorCodes().get(1) + GameManager.getDuelPlayers(type).size() + "&8/" + type.getColorCodes().get(0) + "" + type.getTotalPopulation() + "&7] &aplayers left!"));
				}
				GameManager.update(type);
			}
		}
		if(args[0].equalsIgnoreCase("duel")) {
			Player player = e.getPlayer();
			if(data.PlayerLobby.containsKey(player.getUniqueId())) {
				if(data.PlayerLobby.get(player.getUniqueId()).ending()) {
					e.setCancelled(true);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou just won, please wait to receive your prize!"));
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Player player = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			if(data.PlayerLobby.containsKey(player.getUniqueId()) || data.PlayerLobby.containsKey(damager.getUniqueId())) {
				if(!data.PlayerLobby.get(player.getUniqueId()).started() || !data.PlayerLobby.get(damager.getUniqueId()).started()) {
					event.setCancelled(true);
				}
			}
		}
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(data.PlayerLobby.containsKey(player.getUniqueId())) {
				if(data.PlayerLobby.get(player.getUniqueId()).started()) {
					if(player.getHealth() - event.getFinalDamage() <= 0) {
						event.setCancelled(true);
						EffectFirework.spawn(player, 10);
						player.getInventory().clear();
						player.getInventory().setHelmet(null);
						player.getInventory().setChestplate(null);
						player.getInventory().setLeggings(null);
						player.getInventory().setBoots(null);
						player.teleport(data.storedLocation.get(player.getUniqueId()));
						if(event.getDamager() instanceof Player) {
							player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&4&lYou Died"), ChatColor.translateAlternateColorCodes('&', "&4" + ((Player) event.getDamager()).getDisplayName() + " &ckilled you!"));

						} else {
							player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&4&lYou Died"), "");
						}
						ItemStack[] items = data.storedInventory.get(player.getUniqueId()).toArray(new ItemStack[data.storedInventory.get(player.getUniqueId()).size()]);
						player.getInventory().setContents(items);
						ItemStack[] armor = data.storedArmor.get(player.getUniqueId()).toArray(new ItemStack[data.storedArmor.get(player.getUniqueId()).size()]);
						player.getInventory().setArmorContents(armor);

						data.storedArmor.remove(player.getUniqueId());
						data.storedInventory.remove(player.getUniqueId());

						DuelType type = data.PlayerLobby.get(player.getUniqueId());
						data.PlayerLobby.remove(player.getUniqueId());
						for (Player p : GameManager.getDuelPlayers(type)) {
							if(event.getDamager() instanceof Player) {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4" + player.getDisplayName() + "&c has been killed by &4" + ((Player) event.getDamager()).getDisplayName() + "&c!"));
							} else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4" + player.getDisplayName() + "&c has died!"));
							}
							player.playSound(player.getLocation(), Sound.GHAST_SCREAM, 5f, 1f);
							player.sendMessage(ChatColor.translateAlternateColorCodes('&',
									"&7[" + type.getColorCodes().get(1) + GameManager.getDuelPlayers(type).size() + "&8/" + type.getColorCodes().get(0) + "" + type.getTotalPopulation() + "&7] &aplayers left!"));
						}

						GameManager.update(type);

						player.setFireTicks(0);
						player.setHealth(player.getMaxHealth());
						player.setFoodLevel(20);
						for (PotionEffect effect : player.getActivePotionEffects())
							player.removePotionEffect(effect.getType());
					}
				}
			}
		}

	}

	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(data.PlayerLobby.containsKey(p.getUniqueId())) {

			e.setKeepLevel(true);
			e.setKeepInventory(true);
			Main.instance.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
				@SuppressWarnings("deprecation")
				public void run() {
					if(p.isDead()) {
						((CraftPlayer) p).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
						p.teleport(data.storedLocation.get(p.getUniqueId()));
						if(p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
							EntityDamageByEntityEvent dmgEvent = (EntityDamageByEntityEvent) p.getLastDamageCause();
							if(dmgEvent.getDamager() instanceof Player) {
								p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&4&lYou Died"), ChatColor.translateAlternateColorCodes('&', "&4" + ((Player) dmgEvent.getDamager()).getDisplayName() + " &ckilled you!"));
							}
						} else {
							p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&4&lYou Died"), "");
						}
						ItemStack[] items = data.storedInventory.get(p.getUniqueId()).toArray(new ItemStack[data.storedInventory.get(p.getUniqueId()).size()]);
						p.getInventory().setContents(items);
						ItemStack[] armor = data.storedArmor.get(p.getUniqueId()).toArray(new ItemStack[data.storedArmor.get(p.getUniqueId()).size()]);
						p.getInventory().setArmorContents(armor);

						data.storedArmor.remove(p.getUniqueId());
						data.storedInventory.remove(p.getUniqueId());

						DuelType type = data.PlayerLobby.get(p.getUniqueId());
						data.PlayerLobby.remove(p.getUniqueId());
						for (Player player : GameManager.getDuelPlayers(type)) {
							if(p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
								EntityDamageByEntityEvent dmgEvent = (EntityDamageByEntityEvent) p.getLastDamageCause();
								if(dmgEvent.getDamager() instanceof Player) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4" + p.getDisplayName() + "&c has been killed by &4" + ((Player) dmgEvent.getDamager()).getDisplayName() + "&c!"));
								}
							} else {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4" + p.getDisplayName() + "&c has died!"));
							}
							player.playSound(player.getLocation(), Sound.GHAST_SCREAM, 5f, 1f);
							player.sendMessage(ChatColor.translateAlternateColorCodes('&',
									"&7[" + type.getColorCodes().get(1) + GameManager.getDuelPlayers(type).size() + "&8/" + type.getColorCodes().get(0) + "" + type.getTotalPopulation() + "&7] &aplayers left!"));
						}
						GameManager.update(type);

						//p.setHealth(p.getMaxHealth());
						//p.setFoodLevel(20);
						//for (PotionEffect effect : p.getActivePotionEffects()) {
						//	p.removePotionEffect(effect.getType());
						//}
					}
				}
			});
		}
	}

}