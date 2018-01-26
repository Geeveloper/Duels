package me.geekles.duel.misc.kits;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public abstract class KitManager {

	public enum ItemType {
		HELMET, CHESTPLATE, LEGGINGS, BOOTS, SWORD
	}

	public enum KitType {
		CHAIN, IRON, DIAMOND, OP
	}

	public static void giveKit(Player player, KitType type) {
		PlayerInventory inv = player.getInventory();
		KitManager kit = null;
		if(type == KitType.CHAIN) {
			kit = new ChainKit();
		} else if(type == KitType.IRON) {
			kit = new IronKit();
		} else if(type == KitType.DIAMOND) {
			kit = new DiamondKit();
		} else if(type == KitType.OP) {
			kit = new OpKit();
		}

		inv.setHelmet(kit.getHelmet());
		inv.setChestplate(kit.getChestPlate());
		inv.setLeggings(kit.getLeggings());
		inv.setBoots(kit.getBoots());
		inv.setItem(0, kit.getSword());
		for (int slot = 1; slot <= kit.getMisc().size(); slot++) {
			inv.setItem(slot, kit.getMisc().get(slot - 1));
		}
		return;
	}

	public abstract ItemStack getHelmet();

	public abstract ItemStack getChestPlate();

	public abstract ItemStack getLeggings();

	public abstract ItemStack getBoots();

	public abstract ItemStack getSword();

	public abstract List<ItemStack> getMisc();

}
