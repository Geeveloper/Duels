package me.geekles.duel.misc.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.geekles.duel.main.Main;
import me.geekles.duel.misc.Data;
import me.geekles.duel.misc.DuelType;
import me.geekles.duel.misc.GameManager;
import me.geekles.duel.misc.kits.ChainKit;
import me.geekles.duel.misc.kits.DiamondKit;
import me.geekles.duel.misc.kits.IronKit;
import me.geekles.duel.misc.kits.KitManager.KitType;
import me.geekles.duel.misc.kits.OpKit;
import net.md_5.bungee.api.ChatColor;

public class KitGUIAnimation extends BukkitRunnable {

	public boolean COMPLETE = false;
	public KitType kit = null;

	private DuelType type;
	private final long COOLDOWN;
	private long LAST;

	private Data data;

	private long SPEED = 50;
	private long LAST2;

	public KitGUIAnimation(DuelType type, Data data) {
		this.type = type;
		this.data = data;
		this.LAST = System.currentTimeMillis();
		this.LAST2 = System.currentTimeMillis();
		this.COOLDOWN = getRandomNum(3000, 8000);
	}

	private int getRandomNum(int min, int max) {
		Random random = new Random();
		return random.nextInt(max + 1 - min) + min;
	}

	private ItemStack[] helms = { new ChainKit().getHelmet(), new IronKit().getHelmet(), new DiamondKit().getHelmet(), new OpKit().getHelmet() };
	private List<ItemStack> kits = new ArrayList<ItemStack>(Arrays.asList(helms));

	@Override
	public void run() {
		if(System.currentTimeMillis() - LAST2 >= SPEED) {
			List<ItemStack> storedkits = null;
			for (Player player : GameManager.getDuelPlayers(type)) {
				if(player.getOpenInventory() == null || !player.getOpenInventory().getTitle().equals(ChatColor.stripColor(KitGUI.KIT_TITLE))) {
					player.openInventory(new KitGUI(data).create(player));
				}
				storedkits = new ArrayList<ItemStack>(kits);
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 5f, 1f);
				if(ChatColor.stripColor(player.getOpenInventory().getTitle()).equals(ChatColor.stripColor(KitGUI.KIT_TITLE))) {
					for (int s = 0; s < 9; s++) {
						if(s == 3 || s == 4 || s == 5) {
							ItemStack item = null;
							if(s == 3) {
								ItemStack get0 = new ItemStack(kits.get(0));
								storedkits.remove(0);
								storedkits.add(get0);
								item = storedkits.get(0);
							} else if(s == 4) {
								item = storedkits.get(1);
							} else if(s == 5) {
								item = storedkits.get(2);
							}
							player.getOpenInventory().setItem(s, item);
						} else {
							player.getOpenInventory().setItem(s, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) getRandomNum(0, 15)));
						}
					}
				}
			}
			kits = new ArrayList<ItemStack>(storedkits);
			this.LAST2 = System.currentTimeMillis();
			this.SPEED += 10 * ((SPEED * 2) / SPEED);
		} else if(System.currentTimeMillis() - LAST >= COOLDOWN) {
			cancel();
			stop();
		}

	}

	private void stop() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(ChatColor.stripColor(kits.get(1).getItemMeta().getDisplayName()).equals(ChatColor.stripColor(new ChainKit().getHelmet().getItemMeta().getDisplayName()))) {
					kit = KitType.CHAIN;
				} else if(ChatColor.stripColor(kits.get(1).getItemMeta().getDisplayName()).equals(ChatColor.stripColor(new IronKit().getHelmet().getItemMeta().getDisplayName()))) {
					kit = KitType.IRON;
				} else if(ChatColor.stripColor(kits.get(1).getItemMeta().getDisplayName()).equals(ChatColor.stripColor(new DiamondKit().getHelmet().getItemMeta().getDisplayName()))) {
					kit = KitType.DIAMOND;
				} else if(ChatColor.stripColor(kits.get(1).getItemMeta().getDisplayName()).equals(ChatColor.stripColor(new OpKit().getHelmet().getItemMeta().getDisplayName()))) {
					kit = KitType.OP;
				}
				COMPLETE = true;
			}

		}.runTaskLater(Main.instance, 20);
	}

}
