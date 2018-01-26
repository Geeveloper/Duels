package me.geekles.duel.misc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Data {

	public Set<UUID> OpenGUI = new HashSet<UUID>();
	public Map<DuelType, Location> Lobbies = new HashMap<DuelType, Location>();
	public Map<DuelType, List<Location>> Arenas = new HashMap<DuelType, List<Location>>();
	public Map<UUID, DuelType> PlayerLobby = new HashMap<UUID, DuelType>();
	public Map<UUID, Location> storedLocation = new HashMap<UUID, Location>();
	public Map<UUID, List<ItemStack>> storedInventory = new HashMap<UUID, List<ItemStack>>();
	public Map<UUID, List<ItemStack>> storedArmor = new HashMap<UUID, List<ItemStack>>();
	public Map<DuelType, List<UUID>> Waitlist = new HashMap<DuelType, List<UUID>>();
	public Map<DuelType, List<Player>> storedPlayers = new HashMap<DuelType, List<Player>>();

}
