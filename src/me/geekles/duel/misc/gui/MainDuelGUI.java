package me.geekles.duel.misc.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.geekles.duel.main.Main;
import me.geekles.duel.misc.Data;
import me.geekles.duel.misc.DuelType;
import me.geekles.duel.misc.GameManager;

public class MainDuelGUI {

	private Data data;

	private Player player;
	public static final String MAIN_GUI_TITLE = ChatColor.translateAlternateColorCodes('&', "&8[&c⚔&8] &c&lDuel&8&l/&c&lFFA"); //title of gui

	public MainDuelGUI() {

	}

	public MainDuelGUI(Data data, Player player) {
		this.player = player;
		this.data = data;
	}

	public enum SwordType {
		WOODEN, IRON, GOLD, DIAMOND
	}

	private int getLobbyAmount(DuelType type) {
		int population = 0;
		switch (type) {
		case ONE_VS_ONE:
			for (UUID id : data.PlayerLobby.keySet()) {
				if(data.PlayerLobby.get(id) == type) {
					population++;
				}
			}
			break;
		case FIVE_VS_FIVE:
			for (UUID id : data.PlayerLobby.keySet()) {
				if(data.PlayerLobby.get(id) == type) {
					population++;
				}
			}
			break;
		case TEN_VS_TEN:
			for (UUID id : data.PlayerLobby.keySet()) {
				if(data.PlayerLobby.get(id) == type) {
					population++;
				}
			}
			break;
		}
		return population;
	}

	public ItemStack getSword(SwordType type) {
		switch (type) {
		case WOODEN:
			ItemStack wooden = new ItemStack(Material.WOOD_SWORD, 1); //creating basic wooden sword item
			ItemMeta data_wooden = wooden.getItemMeta();
			data_wooden.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&3&m-=|&r &3&l1 Player Duel &3&m|=-")); //changing display name of the item
			String[] lores1 = { ChatColor.translateAlternateColorCodes('&', "&7[&b" + getLobbyAmount(DuelType.ONE_VS_ONE) + "&8/&32&7] &aPlayers"), ChatColor.translateAlternateColorCodes('&', "&b&m-=|&l-------------&b&m|=-"),
					ChatColor.translateAlternateColorCodes('&', "&3&l&oWager Amount: &r&b$" + Main.instance.getConfig().getDouble("Duels." + DuelType.ONE_VS_ONE.toString() + ".Wager")) };
			List<String> l1 = new ArrayList<String>(Arrays.asList(lores1));
			if(DuelType.ONE_VS_ONE.ending()) {
				l1.add(ChatColor.translateAlternateColorCodes('&', "&6&lWinner: " + GameManager.getDuelPlayers(DuelType.ONE_VS_ONE).get(0).getDisplayName()));
				l1.add(ChatColor.translateAlternateColorCodes('&', "&c&lDefeated: "));
				for (Player p : data.storedPlayers.get(DuelType.ONE_VS_ONE)) {
					if(p.getUniqueId() != GameManager.getDuelPlayers(DuelType.ONE_VS_ONE).get(0).getUniqueId()) {
						l1.add(ChatColor.translateAlternateColorCodes('&', "&c- " + p.getDisplayName()));
					}
				}
			}
			data_wooden.setLore(l1);
			wooden.setItemMeta(data_wooden);
			if(DuelType.ONE_VS_ONE.isLocked() || DuelType.ONE_VS_ONE.started())
				wooden.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

			return wooden; //returning the item
		case IRON:
			ItemStack iron = new ItemStack(Material.IRON_SWORD, 1);
			ItemMeta data_iron = iron.getItemMeta();
			data_iron.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&m-=|&r &6&l5 Player Duel &6&m|=-"));
			String[] lores2 = { ChatColor.translateAlternateColorCodes('&', "&7[&e" + getLobbyAmount(DuelType.FIVE_VS_FIVE) + "&8/&65&7] &aPlayers"), ChatColor.translateAlternateColorCodes('&', "&e&m-=|&l-------------&e&m|=-"),
					ChatColor.translateAlternateColorCodes('&', "&6&l&oWager Amount: &r&e$" + Main.instance.getConfig().getDouble("Duels." + DuelType.FIVE_VS_FIVE.toString() + ".Wager")) };
			List<String> l2 = new ArrayList<String>(Arrays.asList(lores2));
			if(DuelType.FIVE_VS_FIVE.ending()) {
				l2.add(ChatColor.translateAlternateColorCodes('&', "&6&lWinner: " + GameManager.getDuelPlayers(DuelType.FIVE_VS_FIVE).get(0).getDisplayName()));
				l2.add(ChatColor.translateAlternateColorCodes('&', "&c&lDefeated: "));
				for (Player p : data.storedPlayers.get(DuelType.FIVE_VS_FIVE)) {
					if(p.getUniqueId() != GameManager.getDuelPlayers(DuelType.FIVE_VS_FIVE).get(0).getUniqueId()) {
						l2.add(ChatColor.translateAlternateColorCodes('&', "&c- " + p.getDisplayName()));
					}
				}
			}
			data_iron.setLore(l2);
			iron.setItemMeta(data_iron);
			if(DuelType.FIVE_VS_FIVE.isLocked() || DuelType.FIVE_VS_FIVE.started())
				iron.addUnsafeEnchantment(Enchantment.DURABILITY, 5);

			return iron;
		case GOLD:
			break;
		case DIAMOND:
			ItemStack diamond = new ItemStack(Material.DIAMOND_SWORD);
			ItemMeta data_diamond = diamond.getItemMeta();
			data_diamond.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&m-=|&r &5&l10 Player Duel &5&m|=-"));
			String[] lores3 = { ChatColor.translateAlternateColorCodes('&', "&7[&d" + getLobbyAmount(DuelType.TEN_VS_TEN) + "&8/&510&7] &aPlayers"), ChatColor.translateAlternateColorCodes('&', "&d&m-=|&l--------------&d&m|=-"),
					ChatColor.translateAlternateColorCodes('&', "&5&l&oWager Amount: &r&d$" + Main.instance.getConfig().getDouble("Duels." + DuelType.TEN_VS_TEN.toString() + ".Wager")) };
			List<String> l3 = new ArrayList<String>(Arrays.asList(lores3));
			if(DuelType.TEN_VS_TEN.ending()) {
				l3.add(ChatColor.translateAlternateColorCodes('&', "&6&lWinner: " + GameManager.getDuelPlayers(DuelType.TEN_VS_TEN).get(0).getDisplayName()));
				l3.add(ChatColor.translateAlternateColorCodes('&', "&c&lDefeated: "));
				for (Player p : data.storedPlayers.get(DuelType.TEN_VS_TEN)) {
					if(p.getUniqueId() != GameManager.getDuelPlayers(DuelType.TEN_VS_TEN).get(0).getUniqueId()) {
						l3.add(ChatColor.translateAlternateColorCodes('&', "&c- " + p.getDisplayName()));
					}
				}
			}
			data_diamond.setLore(l3);
			diamond.setItemMeta(data_diamond);
			if(DuelType.TEN_VS_TEN.isLocked() || DuelType.TEN_VS_TEN.started())
				diamond.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

			return diamond;
		}
		return null;

	}

	private ItemStack getPane(DuelType type, short data) {

		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, data);
		ItemMeta meta = glass.getItemMeta();
		meta.setDisplayName(" ");
		glass.setItemMeta(meta);
		if(type.isLocked()) {
			glass.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
		}
		return glass;
	}

	private static final int rows = 3;

	public Inventory createMainGUI() {

		data.OpenGUI.add(player.getUniqueId()); //Mark player to prevent any changes in the inventory they click in. 

		Inventory gui = Bukkit.createInventory(player, rows * 9, MAIN_GUI_TITLE); //creating new null inventory

		/*
		[][][][][][][][][]
		[][][][][][][][][]
		[][]::[]::[]::[][]
		[][][][][][][][][]
		[][]WL[]WL[]WL[][]
		[][][][][][][][][]
		*/

		//row 1
		gui.setItem((0 * 9) + 0, getPane(DuelType.ONE_VS_ONE, (short) 9));
		gui.setItem((0 * 9) + 1, getPane(DuelType.ONE_VS_ONE, (short) 11));
		gui.setItem((0 * 9) + 2, getPane(DuelType.ONE_VS_ONE, (short) 9));
		gui.setItem((0 * 9) + 3, getPane(DuelType.FIVE_VS_FIVE, (short) 1));
		gui.setItem((0 * 9) + 4, getPane(DuelType.FIVE_VS_FIVE, (short) 4));
		gui.setItem((0 * 9) + 5, getPane(DuelType.FIVE_VS_FIVE, (short) 1));
		gui.setItem((0 * 9) + 6, getPane(DuelType.TEN_VS_TEN, (short) 10));
		gui.setItem((0 * 9) + 7, getPane(DuelType.TEN_VS_TEN, (short) 2));
		gui.setItem((0 * 9) + 8, getPane(DuelType.TEN_VS_TEN, (short) 10));

		//row 2
		gui.setItem((1 * 9) + 0, getPane(DuelType.ONE_VS_ONE, (short) 11));
		gui.setItem((1 * 9) + 1, getPane(DuelType.ONE_VS_ONE, (short) 9));
		gui.setItem((1 * 9) + 2, getPane(DuelType.ONE_VS_ONE, (short) 11));
		gui.setItem((1 * 9) + 3, getPane(DuelType.FIVE_VS_FIVE, (short) 4));
		gui.setItem((1 * 9) + 4, getPane(DuelType.FIVE_VS_FIVE, (short) 1));
		gui.setItem((1 * 9) + 5, getPane(DuelType.FIVE_VS_FIVE, (short) 4));
		gui.setItem((1 * 9) + 6, getPane(DuelType.TEN_VS_TEN, (short) 2));
		gui.setItem((1 * 9) + 7, getPane(DuelType.TEN_VS_TEN, (short) 10));
		gui.setItem((1 * 9) + 8, getPane(DuelType.TEN_VS_TEN, (short) 2));

		//row 3
		gui.setItem((2 * 9) + 0, getPane(DuelType.ONE_VS_ONE, (short) 9));
		gui.setItem((2 * 9) + 1, getPane(DuelType.ONE_VS_ONE, (short) 11));
		gui.setItem((2 * 9) + 2, getPane(DuelType.ONE_VS_ONE, (short) 9));
		gui.setItem((2 * 9) + 3, getPane(DuelType.FIVE_VS_FIVE, (short) 1));
		gui.setItem((2 * 9) + 4, getPane(DuelType.FIVE_VS_FIVE, (short) 4));
		gui.setItem((2 * 9) + 5, getPane(DuelType.FIVE_VS_FIVE, (short) 1));
		gui.setItem((2 * 9) + 6, getPane(DuelType.TEN_VS_TEN, (short) 10));
		gui.setItem((2 * 9) + 7, getPane(DuelType.TEN_VS_TEN, (short) 2));
		gui.setItem((2 * 9) + 8, getPane(DuelType.TEN_VS_TEN, (short) 10));

		gui.setItem(9 + 1, getSword(SwordType.WOODEN)); //adding wooden sword to gui
		gui.setItem(9 + 4, getSword(SwordType.IRON)); //adding iron sword to gui
		gui.setItem(9 + 7, getSword(SwordType.DIAMOND)); //adding diamond sword to gui
		return gui;
	}

}
