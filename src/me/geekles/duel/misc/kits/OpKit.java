package me.geekles.duel.misc.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class OpKit extends KitManager {
	@Override
	public ItemStack getHelmet() {
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
		ItemMeta helm = helmet.getItemMeta();
		helm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l&oOp Kit"));
		helmet.setItemMeta(helm);
		helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		helmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		return helmet;
	}

	@Override
	public ItemStack getChestPlate() {
		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
		chest.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		chest.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		return chest;
	}

	@Override
	public ItemStack getLeggings() {
		ItemStack leg = new ItemStack(Material.DIAMOND_LEGGINGS);
		leg.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		leg.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		return leg;
	}

	@Override
	public ItemStack getBoots() {
		ItemStack boot = new ItemStack(Material.DIAMOND_BOOTS);
		boot.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		boot.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		return boot;
	}

	@Override
	public ItemStack getSword() {
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
		sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
		sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		sword.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		return sword;
	}

	@Override
	public List<ItemStack> getMisc() {
		List<ItemStack> misc = new ArrayList<ItemStack>();

		//speed
		ItemStack speed = new ItemStack(Material.POTION, 1, (short) 8194);
		PotionMeta speed_meta = (PotionMeta) speed.getItemMeta();
		speed_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		speed.setItemMeta(speed_meta);
		misc.add(speed);

		ItemStack speed2 = new ItemStack(Material.POTION, 1, (short) 8226);
		PotionMeta speed2_meta = (PotionMeta) speed2.getItemMeta();
		speed2_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		speed2.setItemMeta(speed2_meta);
		misc.add(speed2);

		//heal
		ItemStack heal = new ItemStack(Material.POTION, 4, (short) 16389);
		PotionMeta heal_meta = (PotionMeta) heal.getItemMeta();
		heal_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		heal.setItemMeta(heal_meta);
		misc.add(heal);

		//regen
		ItemStack regen = new ItemStack(Material.POTION, 2, (short) 8193);
		PotionMeta regen_meta = (PotionMeta) regen.getItemMeta();
		regen_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		regen.setItemMeta(regen_meta);
		misc.add(regen);

		//strength
		ItemStack strength = new ItemStack(Material.POTION, 2, (short) 8201);
		PotionMeta strength_meta = (PotionMeta) strength.getItemMeta();
		strength_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		strength_meta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 5, 0, false, false), false);
		strength.setItemMeta(strength_meta);
		misc.add(strength);

		//apples
		misc.add(new ItemStack(Material.GOLDEN_APPLE, 5));

		//notch
		misc.add(new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1));

		//food
		ItemStack food = new ItemStack(Material.COOKED_BEEF, 24);
		misc.add(food);

		return misc;
	}
}
