package me.geekles.duel.misc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.geekles.duel.main.Main;
import me.geekles.duel.misc.countdown.Countdown;
import me.geekles.duel.misc.gui.KitGUI;
import me.geekles.duel.misc.gui.KitGUIAnimation;
import me.geekles.duel.misc.kits.KitManager;

public enum DuelType {
	ONE_VS_ONE("1v1", 2, "&3", "&b"), FIVE_VS_FIVE("5v5", 5, "&6", "&e"), TEN_VS_TEN("10v10", 10, "&5", "&d");

	private String value;
	private int population;

	private static Data data;

	public static void setup(Data d) {
		data = d;
	}

	private List<String> COLOR_CODES = new ArrayList<String>();

	private DuelType(String value, int population, String... codes) {
		this.value = value;
		this.population = population;

		for (String c : codes) {
			COLOR_CODES.add(c);
		}

	}

	public boolean started() {
		if(value.equalsIgnoreCase("1v1")) {
			return GameManager.ONE_VS_ONE_STARTED;
		}
		if(value.equalsIgnoreCase("5v5")) {
			return GameManager.FIVE_VS_FIVE_STARTED;
		}
		if(value.equalsIgnoreCase("10v10")) {
			return GameManager.TEN_VS_TEN_STARTED;
		}
		return false;
	}

	public void start() {
		if(GameManager.getDuelPlayers(convertFromString(value)).size() >= convertFromString(value).getTotalPopulation()) {
			lock();
			data.storedPlayers.put(convertFromString(value), GameManager.getDuelPlayers(convertFromString(value)));
			for (Player player : GameManager.getDuelPlayers(convertFromString(value))) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&oSelecting Random Kit..."));
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					for (Player player : GameManager.getDuelPlayers(convertFromString(value))) {
						player.openInventory(new KitGUI(data).create(player));
					}
					KitGUIAnimation animation = new KitGUIAnimation(convertFromString(value), data);
					animation.runTaskTimer(Main.instance, 0L, 2L);
					new BukkitRunnable() {
						@Override
						public void run() {
							if(animation.COMPLETE) {
								for (Player player : GameManager.getDuelPlayers(convertFromString(value))) {
									player.closeInventory();
									KitManager.giveKit(player, animation.kit);
								}
								this.cancel();
								Countdown count = new Countdown(convertFromString(value));
								count.runTaskTimer(Main.instance, 60L, 5L);
								new BukkitRunnable() {
									@SuppressWarnings("deprecation")
									@Override
									public void run() {
										if(count.COMPLETE) {
											int location = 0;
											for (Player player : GameManager.getDuelPlayers(convertFromString(value))) {
												player.setHealth(player.getMaxHealth());
												player.setFoodLevel(20);
												for (PotionEffect effect : player.getActivePotionEffects()) {
													player.removePotionEffect(effect.getType());
												}
												player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&c⚔"), ChatColor.translateAlternateColorCodes('&', "&c&lPrepare For Duel"));
												player.teleport(data.Arenas.get(convertFromString(value)).get(location));
												player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 10f, 1f);
												player.getWorld().spigot().playEffect(player.getLocation(), Effect.LAVA_POP, 0, 0, 0.1f, 0.1f, 0.1f, 0.05f, 10, 2);
												location++;
											}
											this.cancel();
											if(value.equalsIgnoreCase("1v1")) {
												GameManager.ONE_VS_ONE_STARTED = true;
											}
											if(value.equalsIgnoreCase("5v5")) {
												GameManager.FIVE_VS_FIVE_STARTED = true;
											}
											if(value.equalsIgnoreCase("10v10")) {
												GameManager.TEN_VS_TEN_STARTED = true;
											}
											return;
										}
									}
								}.runTaskTimer(Main.instance, 40L, 5L);
								return;
							}
						}
					}.runTaskTimer(Main.instance, 40L, 5L);
					return;
				}
			}.runTaskLater(Main.instance, 30L);

		}

	}

	@SuppressWarnings("deprecation")
	public void stop() {
		DuelType type = convertFromString(value);
		if(GameManager.getDuelPlayers(type).size() == 1) {
			end();
			Player player = GameManager.getDuelPlayers(convertFromString(value)).get(0);
			player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&a&lYou Won!"), "");
			new endGameTimer(player, convertFromString(value), data).runTaskTimer(Main.instance, 80, 5);
		}
	}

	public boolean ending() {
		if(value.equalsIgnoreCase("1v1")) {
			return GameManager.ONE_VS_ONE_ENDING;
		}
		if(value.equalsIgnoreCase("5v5")) {
			return GameManager.FIVE_VS_FIVE_ENDING;
		}
		if(value.equalsIgnoreCase("10v10")) {
			return GameManager.TEN_VS_TEN_ENDING;
		}
		return false;
	}

	public void end() {
		if(value.equalsIgnoreCase("1v1")) {
			GameManager.ONE_VS_ONE_ENDING = true;
		}
		if(value.equalsIgnoreCase("5v5")) {
			GameManager.FIVE_VS_FIVE_ENDING = true;
		}
		if(value.equalsIgnoreCase("10v10")) {
			GameManager.TEN_VS_TEN_ENDING = true;
		}
	}

	public void unlock() {
		if(value.equalsIgnoreCase("1v1")) {
			GameManager.ONE_VS_ONE_LOCKED = false;
		}
		if(value.equalsIgnoreCase("5v5")) {
			GameManager.FIVE_VS_FIVE_LOCKED = false;
		}
		if(value.equalsIgnoreCase("10v10")) {
			GameManager.TEN_VS_TEN_LOCKED = false;
		}
	}

	public void lock() {
		if(value.equalsIgnoreCase("1v1")) {
			GameManager.ONE_VS_ONE_LOCKED = true;
		}
		if(value.equalsIgnoreCase("5v5")) {
			GameManager.FIVE_VS_FIVE_LOCKED = true;
		}
		if(value.equalsIgnoreCase("10v10")) {
			GameManager.TEN_VS_TEN_LOCKED = true;
		}
	}

	public boolean isLocked() {
		if(value.equalsIgnoreCase("1v1")) {
			return GameManager.ONE_VS_ONE_LOCKED;
		}
		if(value.equalsIgnoreCase("5v5")) {
			return GameManager.FIVE_VS_FIVE_LOCKED;
		}
		if(value.equalsIgnoreCase("10v10")) {
			return GameManager.TEN_VS_TEN_LOCKED;
		}
		return false;
	}

	public int getTotalPopulation() {
		return population;
	}

	public String toString() {
		return value;
	}

	public List<String> getColorCodes() {
		return new ArrayList<String>(COLOR_CODES);
	}

	public static DuelType convertFromString(String value) {
		if(value.equalsIgnoreCase("1v1") || value.equalsIgnoreCase("ONE_VS_ONE") || value.equalsIgnoreCase("ONE VS ONE")) {
			return ONE_VS_ONE;
		}
		if(value.equalsIgnoreCase("5v5") || value.equalsIgnoreCase("FIVE_VS_FIVE") || value.equalsIgnoreCase("FIVE VS FIVE")) {
			return FIVE_VS_FIVE;
		}
		if(value.equalsIgnoreCase("10v10") || value.equalsIgnoreCase("TEN_VS_TEN") || value.equalsIgnoreCase("TEN VS TEN")) {
			return TEN_VS_TEN;
		}
		return null;
	}

	public static boolean isDuelType(String value) {
		if(value.equalsIgnoreCase("1v1") || value.equalsIgnoreCase("ONE_VS_ONE") || value.equalsIgnoreCase("ONE VS ONE")) {
			return true;
		}
		if(value.equalsIgnoreCase("5v5") || value.equalsIgnoreCase("FIVE_VS_FIVE") || value.equalsIgnoreCase("FIVE VS FIVE")) {
			return true;
		}
		if(value.equalsIgnoreCase("10v10") || value.equalsIgnoreCase("TEN_VS_TEN") || value.equalsIgnoreCase("TEN VS TEN")) {
			return true;
		}
		return false;
	}

}
