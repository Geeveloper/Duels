package me.geekles.duel.misc.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import net.md_5.bungee.api.ChatColor;

public class DiamondKit extends KitManager {

	@Override
	public ItemStack getHelmet() {
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		ItemMeta helm = helmet.getItemMeta();
		helm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&l&oDiamond Kit"));
		helmet.setItemMeta(helm);
		helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		helmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		return helmet;
	}

	@Override
	public ItemStack getChestPlate() {
		ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
		chest.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		chest.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		return chest;
	}

	@Override
	public ItemStack getLeggings() {
		ItemStack leg = new ItemStack(Material.IRON_LEGGINGS);
		leg.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		leg.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		return leg;
	}

	@Override
	public ItemStack getBoots() {
		ItemStack boot = new ItemStack(Material.DIAMOND_BOOTS);
		boot.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		boot.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		return boot;
	}

	@Override
	public ItemStack getSword() {
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		sword.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		return sword;
	}

	@Override
	public List<ItemStack> getMisc() {
		List<ItemStack> misc = new ArrayList<ItemStack>();

		//speed
		ItemStack speed = new ItemStack(Material.POTION, 2, (short) 8194);
		PotionMeta speed_meta = (PotionMeta) speed.getItemMeta();
		speed_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		speed.setItemMeta(speed_meta);
		misc.add(speed);

		//heal
		ItemStack heal = new ItemStack(Material.POTION, 6, (short) 16389);
		PotionMeta heal_meta = (PotionMeta) heal.getItemMeta();
		heal_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		heal.setItemMeta(heal_meta);
		misc.add(heal);

		//apples
		misc.add(new ItemStack(Material.GOLDEN_APPLE, 8));

		//food
		ItemStack food = new ItemStack(Material.COOKED_BEEF, 18);
		misc.add(food);

		return misc;
	}

}
