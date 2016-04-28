package me.BadBones69.CrazyEnchantments.Enchantments.Armor;

import me.BadBones69.CrazyEnchantments.Api;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SmokeBomb implements Listener{
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e){
		if(!Api.isEnchantmentEnabled("SmokeBomb"))return;
		if(!Api.allowsPVP(e.getEntity()))return;
		if(!Api.allowsPVP(e.getDamager()))return;
		if(e.isCancelled())return;
		if(e.getEntity() instanceof Player){
			if(e.getDamager() instanceof LivingEntity){
				Player player = (Player) e.getEntity();
				LivingEntity en = (LivingEntity) e.getDamager();
				for(ItemStack armor : player.getEquipment().getArmorContents()){
					if(armor.hasItemMeta()){
						if(!armor.getItemMeta().hasLore())return;
						for(String lore : armor.getItemMeta().getLore()){
							if(lore.contains(Api.getEnchName("SmokeBomb"))){
								if(Api.randomPicker(11-Api.getPower(lore, Api.getEnchName("SmokeBomb")))){
									en.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3*20, 1));
									en.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3*20, 0));
								}
							}
						}
					}
				}
			}
		}
	}
}