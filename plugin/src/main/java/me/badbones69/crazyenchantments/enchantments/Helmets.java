package me.badbones69.crazyenchantments.enchantments;

import me.badbones69.crazyenchantments.api.CrazyEnchantments;
import me.badbones69.crazyenchantments.api.enums.CEnchantments;
import me.badbones69.crazyenchantments.api.events.ArmorEquipEvent;
import me.badbones69.crazyenchantments.api.events.EnchantmentUseEvent;
import me.badbones69.crazyenchantments.multisupport.Support;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Helmets implements Listener {
	
	private CrazyEnchantments ce = CrazyEnchantments.getInstance();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEquip(ArmorEquipEvent e) {
		Player player = e.getPlayer();
		ItemStack newItem = e.getNewArmorPiece();
		ItemStack oldItem = e.getOldArmorPiece();
		if(ce.hasEnchantments(newItem)) {
			int time = Integer.MAX_VALUE;
			if(ce.hasEnchantment(newItem, CEnchantments.GLOWING)) {
				if(CEnchantments.GLOWING.isActivated()) {
					EnchantmentUseEvent event = new EnchantmentUseEvent(player, CEnchantments.GLOWING, newItem);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, time, ce.getLevel(newItem, CEnchantments.GLOWING) - 1));
					}
				}
			}
			if(ce.hasEnchantment(newItem, CEnchantments.MERMAID)) {
				if(CEnchantments.MERMAID.isActivated()) {
					EnchantmentUseEvent event = new EnchantmentUseEvent(player, CEnchantments.MERMAID, newItem);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, time, ce.getLevel(newItem, CEnchantments.MERMAID) - 1));
					}
				}
			}
		}
		if(ce.hasEnchantments(oldItem)) {
			if(ce.hasEnchantment(oldItem, CEnchantments.GLOWING)) {
				if(CEnchantments.GLOWING.isActivated()) {
					player.removePotionEffect(PotionEffectType.NIGHT_VISION);
				}
			}
			if(ce.hasEnchantment(oldItem, CEnchantments.MERMAID)) {
				if(CEnchantments.MERMAID.isActivated()) {
					player.removePotionEffect(PotionEffectType.WATER_BREATHING);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMovment(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		for(ItemStack armor : player.getEquipment().getArmorContents()) {
			if(ce.hasEnchantment(armor, CEnchantments.COMMANDER)) {
				if(CEnchantments.COMMANDER.isActivated()) {
					int radius = 4 + ce.getLevel(armor, CEnchantments.COMMANDER);
					ArrayList<Player> players = new ArrayList<>();
					for(Entity en : player.getNearbyEntities(radius, radius, radius)) {
						if(en instanceof Player) {
							Player o = (Player) en;
							if(Support.isFriendly(player, o)) {
								players.add(o);
							}
						}
					}
					if(players.size() > 0) {
						EnchantmentUseEvent event = new EnchantmentUseEvent(player, CEnchantments.COMMANDER, armor);
						Bukkit.getPluginManager().callEvent(event);
						if(!event.isCancelled()) {
							for(Player P : players) {
								P.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 3 * 20, 1));
							}
						}
					}
				}
			}
		}
	}
	
}