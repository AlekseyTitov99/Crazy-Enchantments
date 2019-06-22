package me.badbones69.crazyenchantments.controllers;

import me.badbones69.crazyenchantments.Methods;
import me.badbones69.crazyenchantments.api.CrazyEnchantments;
import me.badbones69.crazyenchantments.api.objects.FileManager.Files;
import me.badbones69.crazyenchantments.api.objects.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProtectionCrystal implements Listener {
	
	private HashMap<UUID, ArrayList<ItemStack>> playersItems = new HashMap<>();
	private CrazyEnchantments ce = CrazyEnchantments.getInstance();
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		FileConfiguration config = Files.CONFIG.getFile();
		Player player = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		if(inv != null) {
			ItemStack c = e.getCursor();// The Crystal.
			ItemStack item = e.getCurrentItem();// The item your adding the protection to.
			if(item == null) item = new ItemStack(Material.AIR);
			if(c == null) c = new ItemStack(Material.AIR);
			if(item.getType() != Material.AIR && c.getType() != Material.AIR) {
				if(item.getAmount() == 1) {
					if(!Methods.isSimilar(getCrystals(), item)) {
						if(!isProtected(item)) {
							if(c.hasItemMeta()) {
								if(c.getItemMeta().hasDisplayName()) {
									if(c.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.ProtectionCrystal.Name")))) {
										if(c.getType() == getCrystals().getType()) {
											if(player.getGameMode() == GameMode.CREATIVE && c.getAmount() > 1) {
												player.sendMessage(Methods.getPrefix() + Methods.color("&cPlease unstack the crystals for them to work."));
												return;
											}
											e.setCancelled(true);
											player.setItemOnCursor(Methods.removeItem(c));
											e.setCurrentItem(Methods.addLore(item, Methods.color(config.getString("Settings.ProtectionCrystal.Protected"))));
											player.updateInventory();
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		ArrayList<ItemStack> items = new ArrayList<>();
		ArrayList<ItemStack> drops = new ArrayList<>();
		for(ItemStack item : e.getDrops()) {
			if(item != null) {
				if(isProtected(item)) {
					if(isSuccessfull(player)) {
						items.add(item);
					}else {
						drops.add(item);
					}
				}else {
					drops.add(item);
				}
			}
		}
		e.getDrops().clear();
		e.getDrops().addAll(drops);
		playersItems.put(player.getUniqueId(), items);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		String protection = Methods.color(Files.CONFIG.getFile().getString("Settings.ProtectionCrystal.Protected"));
		if(playersItems.containsKey(player.getUniqueId())) {
			if(Files.CONFIG.getFile().contains("Settings.ProtectionCrystal.Lose-Protection-On-Death")) {
				if(Files.CONFIG.getFile().getBoolean("Settings.ProtectionCrystal.Lose-Protection-On-Death")) {
					for(ItemStack item : playersItems.get(player.getUniqueId())) {
						player.getInventory().addItem(Methods.removeLore(item, protection));
					}
				}else {
					for(ItemStack item : playersItems.get(player.getUniqueId())) {
						player.getInventory().addItem(item);
					}
				}
			}else {
				for(ItemStack item : playersItems.get(player.getUniqueId())) {
					player.getInventory().addItem(Methods.removeLore(item, protection));
				}
			}
			playersItems.remove(player.getUniqueId());
		}
	}
	
	@EventHandler
	public void onCrystalClick(PlayerInteractEvent e) {
		ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
		if(item != null) {
			if(Methods.isSimilar(item, getCrystals())) {
				e.setCancelled(true);
			}
		}
	}
	
	public static Boolean isProtected(ItemStack item) {
		if(item.hasItemMeta()) {
			if(item.getItemMeta().hasLore()) {
				for(String lore : item.getItemMeta().getLore()) {
					if(lore.contains(Methods.color(Files.CONFIG.getFile().getString("Settings.ProtectionCrystal.Protected")))) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static Boolean isSuccessfull(Player player) {
		if(player.hasPermission("crazyenchantments.bypass.protectioncrystal")) {
			return true;
		}
		FileConfiguration config = Files.CONFIG.getFile();
		if(config.contains("Settings.ProtectionCrystal.Chance")) {
			if(config.getBoolean("Settings.ProtectionCrystal.Chance.Toggle")) {
				return Methods.randomPicker(config.getInt("Settings.ProtectionCrystal.Chance.Success-Chance"), 100);
			}
		}
		return true;
	}
	
	public static ItemStack getCrystals() {
		FileConfiguration config = Files.CONFIG.getFile();
		String id = config.getString("Settings.ProtectionCrystal.Item");
		String name = config.getString("Settings.ProtectionCrystal.Name");
		List<String> lore = config.getStringList("Settings.ProtectionCrystal.Lore");
		boolean toggle = config.getBoolean("Settings.ProtectionCrystal.Glowing");
		ItemStack item = new ItemBuilder().setMaterial(id).setName(name).setLore(lore).build();
		item = Methods.addGlow(item, toggle);
		return item;
	}
	
	public static ItemStack getCrystals(int amount) {
		FileConfiguration config = Files.CONFIG.getFile();
		String id = config.getString("Settings.ProtectionCrystal.Item");
		String name = config.getString("Settings.ProtectionCrystal.Name");
		List<String> lore = config.getStringList("Settings.ProtectionCrystal.Lore");
		boolean toggle = config.getBoolean("Settings.ProtectionCrystal.Glowing");
		ItemStack item = new ItemBuilder().setMaterial(id).setAmount(amount).setName(name).setLore(lore).build();
		item = Methods.addGlow(item, toggle);
		return item;
	}
	
	public static ItemStack removeProtection(ItemStack item) {
		ItemMeta m = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<>(m.getLore());
		lore.remove(Methods.color(Files.CONFIG.getFile().getString("Settings.ProtectionCrystal.Protected")));
		m.setLore(lore);
		item.setItemMeta(m);
		return item;
	}
	
}