package me.geekles.duel.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.geekles.duel.main.Main;

public class endGameTimer extends BukkitRunnable {

	private Data data;
	private Player player;

	private final long COOLDOWN = 1000;
	private final int MAX_FIREWORK_SPAWNS;
	private DuelType type;

	private long lastTime = 0;
	private int counter = 0;

	public endGameTimer(Player player, DuelType type, Data data) {
		this.player = player;
		this.type = type;
		this.lastTime = System.currentTimeMillis();
		this.data = data;
		this.MAX_FIREWORK_SPAWNS = 6;
	}

	private void endGame() {
		player.teleport(data.storedLocation.get(player.getUniqueId()));
		data.PlayerLobby.remove(player.getUniqueId());
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		int slot = 0;
		for (ItemStack content : data.storedInventory.get(player.getUniqueId())) {
			if(content != null) {
				player.getInventory().setItem(slot++, content);
			} else {
				player.getInventory().setItem(slot++, new ItemStack(Material.AIR));
			}

		}
		ItemStack[] armor = data.storedArmor.get(player.getUniqueId()).toArray(new ItemStack[data.storedArmor.get(player.getUniqueId()).size()]);
		player.getInventory().setArmorContents(armor);

		player.setHealth(player.getMaxHealth());
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
		data.storedInventory.remove(player.getUniqueId());
		data.storedArmor.remove(player.getUniqueId());
		data.storedLocation.remove(player.getUniqueId());

		double amount = Main.instance.getConfig().getDouble("Duels." + type.toString() + ".Wager");
		boolean returnMoney = Main.instance.getConfig().getBoolean("Duels." + type.toString() + ".ReturnMoneyToWinner");
		int count = 0;
		if(returnMoney) {
			Main.econ.depositPlayer(player, amount);
			count++;
		}
		for (int c = 0; c < type.getTotalPopulation() - 1; c++) {
			Main.econ.depositPlayer(player, amount);
			count++;
		}
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&c⚔&8] &aCongratulations! You won, &2$" + (amount * count) + "&a!"));

		if(type.toString().equalsIgnoreCase("1v1")) {
			GameManager.ONE_VS_ONE_LOCKED = false;
			GameManager.ONE_VS_ONE_STARTED = false;
			GameManager.ONE_VS_ONE_ENDING = false;
		}
		if(type.toString().equalsIgnoreCase("5v5")) {
			GameManager.FIVE_VS_FIVE_LOCKED = false;
			GameManager.FIVE_VS_FIVE_STARTED = false;
			GameManager.FIVE_VS_FIVE_ENDING = false;
		}
		if(type.toString().equalsIgnoreCase("10v10")) {
			GameManager.TEN_VS_TEN_LOCKED = false;
			GameManager.TEN_VS_TEN_STARTED = false;
			GameManager.TEN_VS_TEN_ENDING = false;
		}

		if(type != DuelType.ONE_VS_ONE)
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&8[&c⚔&8]&a&l " + player.getDisplayName() + " &2won the &a&l" + type.toString() + " FFA/Duel&2 and earned, &a&l$" + (amount * count) + "!"));

	}

	@Override
	public void run() {
		if(System.currentTimeMillis() - lastTime >= COOLDOWN) {
			if(counter == MAX_FIREWORK_SPAWNS) {
				cancel();
				endGame();
				return;
			}
			this.lastTime = System.currentTimeMillis();
			EffectFirework.spawn(player, 10);
			counter++;
		}
	}

}
