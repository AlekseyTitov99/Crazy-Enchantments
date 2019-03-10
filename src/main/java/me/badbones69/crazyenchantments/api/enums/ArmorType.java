package me.badbones69.crazyenchantments.api.enums;

import org.bukkit.inventory.ItemStack;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://thederpygolems.ca/
 * Jul 30, 2015 6:46:16 PM
 */
public enum ArmorType {
	
	HELMET(5), CHESTPLATE(6), LEGGINGS(7), BOOTS(8);
	
	private final int slot;
	
	private ArmorType(int slot) {
		this.slot = slot;
	}
	
	/**
	 * Attempts to match the ArmorType for the specified ItemStack.
	 *
	 * @param itemStack The ItemStack to parse the type of.
	 * @return The parsed ArmorType. (null if none were found.)
	 */
	public static ArmorType matchType(final ItemStack itemStack) {
		if(itemStack == null) {
			return null;
		}
		if(itemStack.getType().name().toLowerCase().contains("skull")) {
			return HELMET;
		}
		switch(itemStack.getType()) {
			case DIAMOND_HELMET:
			case GOLDEN_HELMET:
			case IRON_HELMET:
			case CHAINMAIL_HELMET:
			case LEATHER_HELMET:
			case TURTLE_HELMET:
				return HELMET;
			case DIAMOND_CHESTPLATE:
			case GOLDEN_CHESTPLATE:
			case IRON_CHESTPLATE:
			case CHAINMAIL_CHESTPLATE:
			case LEATHER_CHESTPLATE:
			case ELYTRA:
				return CHESTPLATE;
			case DIAMOND_LEGGINGS:
			case GOLDEN_LEGGINGS:
			case IRON_LEGGINGS:
			case CHAINMAIL_LEGGINGS:
			case LEATHER_LEGGINGS:
				return LEGGINGS;
			case DIAMOND_BOOTS:
			case GOLDEN_BOOTS:
			case IRON_BOOTS:
			case CHAINMAIL_BOOTS:
			case LEATHER_BOOTS:
				return BOOTS;
			default:
				return null;
		}
	}
	
	public int getSlot() {
		return slot;
	}
	
}
