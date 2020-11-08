package com.robertx22.database.gearitemslots;

import java.util.HashMap;

import com.robertx22.database.gearitemslots.bases.BaseArmor;
import com.robertx22.items.gearitems.armor.ItemPants;

import net.minecraft.item.Item;

public class Pants extends BaseArmor {

	@Override
	public String GUID() {
		return "Pants";
	}

	@Override
	public Item DefaultItem() {
		return ItemPants.Items.get(0);
	}

	@Override
	public HashMap<Integer, Item> ItemsForRarities() {
		return ItemPants.Items;
	}

}
