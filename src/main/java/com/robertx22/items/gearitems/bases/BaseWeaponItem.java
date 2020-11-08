package com.robertx22.items.gearitems.bases;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.robertx22.uncommon.SLOC;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.util.EnumHelper;

public abstract class BaseWeaponItem extends ItemTool implements IGearItem, IWeapon {

	static ItemSword.ToolMaterial Mat = EnumHelper.addToolMaterial("swordmat", 0, 900, 1F, 1F, 10);

	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet();

	public abstract String Name();

	public BaseWeaponItem() {
		super(Mat, EFFECTIVE_ON);
		this.setMaxStackSize(1);
		this.setMaxDamage(BaseArmorItem.MAX_GEAR_DURABILITY);

	}

	public static boolean checkDurability(EntityLivingBase attacker, ItemStack stack) {

		if (stack.getItemDamage() > stack.getMaxDamage() - 20) {
			attacker.sendMessage(SLOC.chat("low_weapon_durability"));
			return false;

		}
		return true;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) {
		return enchantment.type.canEnchantItem(Items.DIAMOND_SWORD) && isNotInEnchantBlackList(enchantment);
	}

	public static List<Enchantment> blacklist = Arrays.asList(Enchantments.SMITE, Enchantments.SHARPNESS,
			Enchantments.BANE_OF_ARTHROPODS, Enchantments.SWEEPING);

	public static boolean isNotInEnchantBlackList(Enchantment ench) {
		return blacklist.contains(ench) == false;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

		stack.damageItem(1, attacker);

		return true;
	}

}
