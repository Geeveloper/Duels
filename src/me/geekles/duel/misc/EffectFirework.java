package me.geekles.duel.misc;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.geekles.duel.main.Main;

public class EffectFirework {

	private static Color colorList[] = { Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.TEAL, Color.WHITE,
			Color.YELLOW };

	private static Color getColor(int i) {

		return colorList[i];
	}

	public static void spawn(Player p, long duration) {

		Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		//Our random generator
		Random r = new Random();

		//Get the type
		int rt = r.nextInt(4) + 1;
		Type type = Type.BALL;
		if(rt == 1)
			type = Type.BALL;
		if(rt == 2)
			type = Type.BALL_LARGE;
		if(rt == 3)
			type = Type.BURST;
		if(rt == 4)
			type = Type.CREEPER;
		if(rt == 5)
			type = Type.STAR;

		//Get our random colours   
		int r1i = r.nextInt(17);
		int r2i = r.nextInt(17);
		Color c1 = getColor(r1i);
		Color c2 = getColor(r2i);

		//Create our effect with this
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

		//Then apply the effect to the meta
		fwm.addEffect(effect);

		//Generate some random power and set it
		int rp = r.nextInt(2) + 1;
		fwm.setPower(rp);

		//Then apply this to our rocket
		fw.setFireworkMeta(fwm);

		new BukkitRunnable() {

			@Override
			public void run() {
				fw.detonate();
			}

		}.runTaskLater(Main.instance, (duration + 5));
	}

}
