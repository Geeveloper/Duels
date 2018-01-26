package me.geekles.duel.misc.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.geekles.duel.misc.Data;

public class KitGUI {

	public static final String KIT_TITLE = ChatColor.translateAlternateColorCodes('&', "&6&lKit &e&lMenu");

	private Data data;

	public KitGUI(Data d) {
		data = d;
	}

	public Inventory create(Player player) {
		data.OpenGUI.add(player.getUniqueId());
		Inventory inv = Bukkit.createInventory(player, 9, KIT_TITLE);
		inv.setItem(0, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1));
		inv.setItem(1, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0));
		inv.setItem(2, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1));

		inv.setItem(6, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1));
		inv.setItem(7, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0));
		inv.setItem(8, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1));

		return inv;
	}

}
