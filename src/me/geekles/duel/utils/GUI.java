package me.geekles.duel.utils;

import org.bukkit.entity.Player;

public class GUI {

	private Player player;
	private int slots;

	public GUI(Player Owner, int Slots) {
		this.player = Owner;
		this.slots = Slots;
	}

	public GUI(int Slots) {
		this.slots = Slots;
	}

}
