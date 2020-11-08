package com.robertx22.items.unique_items.bases;

import com.robertx22.database.gearitemslots.Necklace;
import com.robertx22.items.gearitems.baubles.ItemNecklace;
import com.robertx22.items.unique_items.IUnique;

import baubles.api.BaubleType;
import net.minecraft.item.ItemStack;

public abstract class BaseUniqueNecklace extends ItemNecklace implements IUnique {
	public BaseUniqueNecklace(BaubleType type) {
		super(type);
		IUnique.ITEMS.put(GUID(), this);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}

	@Override
	public String slot() {
		return new Necklace().GUID();
	}
}