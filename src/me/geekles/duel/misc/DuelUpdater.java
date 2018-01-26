package me.geekles.duel.misc;

import org.bukkit.scheduler.BukkitRunnable;

public class DuelUpdater extends BukkitRunnable {

	@Override
	public void run() {
		for (DuelType type : DuelType.values()) {
			GameManager.update(type);
		}
	}

}
