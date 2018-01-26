package me.geekles.duel.misc.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.geekles.duel.misc.Data;
import me.geekles.duel.misc.DuelType;
import me.geekles.duel.misc.LobbyManager;

public class GUIListener implements Listener {

	private String[] gui_names = { ChatColor.stripColor(MainDuelGUI.MAIN_GUI_TITLE), ChatColor.stripColor(KitGUI.KIT_TITLE) };
	private List<String> GUIS = new ArrayList<String>(Arrays.asList(gui_names));

	private Data data;

	public GUIListener(Data data) {
		this.data = data;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		Player player = (Player) event.getWhoClicked();
		if(GUIS.contains(ChatColor.stripColor(inv.getName()))) {
			CheckItemClick(event);
			if(data.OpenGUI.contains(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onInventoryClose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		Player player = (Player) event.getPlayer();
		if(GUIS.contains(inv.getName())) {
			if(data.OpenGUI.contains(player.getUniqueId())) {
				data.OpenGUI.remove(player.getUniqueId());
			}
		}
	}

	private void CheckItemClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		Player player = (Player) event.getWhoClicked();
		if(ChatColor.stripColor(inv.getName()).equals(ChatColor.stripColor(MainDuelGUI.MAIN_GUI_TITLE))) {
			if(data.PlayerLobby.containsKey(player.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "You're already in a duel. Try /duel quit instead.");
				return;
			}
			ItemStack item = event.getCurrentItem();
			if(item.getType() == Material.WOOD_SWORD) {
				if(DuelType.ONE_VS_ONE.isLocked()) {
					List<UUID> list = new ArrayList<UUID>();
					if(data.Waitlist.containsKey(DuelType.ONE_VS_ONE)) {
						list = new ArrayList<UUID>(data.Waitlist.get(DuelType.ONE_VS_ONE));
						if(list.contains(player.getUniqueId())) {
							player.sendMessage(ChatColor.RED + "A duel is in progress. You've been already added to the waitlist along with " + (list.size() - 1) + " other players!");
							return;
						}
					}
					list.add(player.getUniqueId());
					data.Waitlist.put(DuelType.ONE_VS_ONE, list);
					player.sendMessage(ChatColor.RED + "A duel is in progress. You've been added to the waitlist!");
					return;
				}
				LobbyManager.addPlayer(player, DuelType.ONE_VS_ONE);
				return;
			} else if(item.getType() == Material.IRON_SWORD) {
				if(DuelType.FIVE_VS_FIVE.isLocked()) {
					player.sendMessage(ChatColor.RED + "A duel is in progress. You've been added to the waitlist!");
					List<UUID> list = new ArrayList<UUID>();
					if(data.Waitlist.containsKey(DuelType.FIVE_VS_FIVE)) {
						list = new ArrayList<UUID>(data.Waitlist.get(DuelType.FIVE_VS_FIVE));
					}
					list.add(player.getUniqueId());
					data.Waitlist.put(DuelType.FIVE_VS_FIVE, list);
					return;
				}
				LobbyManager.addPlayer(player, DuelType.FIVE_VS_FIVE);
				return;
			} else if(item.getType() == Material.DIAMOND_SWORD) {
				if(DuelType.TEN_VS_TEN.isLocked()) {
					player.sendMessage(ChatColor.RED + "A duel is in progress. You've been added to the waitlist!");
					List<UUID> list = new ArrayList<UUID>();
					if(data.Waitlist.containsKey(DuelType.TEN_VS_TEN)) {
						list = new ArrayList<UUID>(data.Waitlist.get(DuelType.TEN_VS_TEN));
					}
					list.add(player.getUniqueId());
					data.Waitlist.put(DuelType.TEN_VS_TEN, list);
					return;
				}
				LobbyManager.addPlayer(player, DuelType.TEN_VS_TEN);
				return;
			}
		}

	}

}
