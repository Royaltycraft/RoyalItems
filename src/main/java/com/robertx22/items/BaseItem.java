package com.robertx22.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class BaseItem extends Item {

	public BaseItem() {
		super();
	}

	public ItemStack EmptyOrDecrease(ItemStack stack) {
		if (stack.getCount() < 2) {
			stack = ItemStack.EMPTY;
		} else {
			stack.setCount(stack.getCount() - 1);
		}
		return stack;
	}

	public ItemStack EmptyOrDecrease(ItemStack stack, int amount) {

		stack.setCount(stack.getCount() - amount);

		if (stack.getCount() < 1) {
			stack = ItemStack.EMPTY;
		}

		return stack;
	}
}
