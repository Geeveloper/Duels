package me.geekles.duel.misc.countdown;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.geekles.duel.misc.DuelType;
import me.geekles.duel.misc.GameManager;

public class Countdown extends BukkitRunnable {

	private DuelType type;

	private int Countdown = 10;
	private long storedTime = 0;
	private final long COOLDOWN = 1000;

	public boolean COMPLETE = false;

	public Countdown(DuelType type) {
		this.type = type;
		this.storedTime = System.currentTimeMillis();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if(System.currentTimeMillis() - storedTime >= COOLDOWN) {
			if(Countdown == 0) {
				COMPLETE = true;
				cancel();
				return;
			}
			this.storedTime = System.currentTimeMillis();
			for (Player player : GameManager.getDuelPlayers(type)) {
				if(Countdown == 3)
					player.sendTitle(ChatColor.GOLD + "" + Countdown, "");
				else if(Countdown == 2)
					player.sendTitle(ChatColor.YELLOW + "" + Countdown, "");
				else if(Countdown == 1)
					player.sendTitle(ChatColor.RED + "" + Countdown, "");
				else
					player.sendTitle(ChatColor.GREEN + "" + Countdown, "");

				player.playSound(player.getLocation(), Sound.NOTE_PLING, 10f, 1f);
			}
			Countdown--;
		}
		if(GameManager.getDuelPlayers(type).size() < type.getTotalPopulation()) {
			for (Player player : GameManager.getDuelPlayers(type)) {
				player.sendTitle("", ChatColor.RED + "Cancelling Duel...");

			}
			type.unlock();
			cancel();
		}
	}

}
