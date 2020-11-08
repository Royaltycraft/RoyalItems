package com.robertx22.uncommon.effectdatas.interfaces;

import net.minecraft.entity.EntityLivingBase;

public interface IEffect {

	public abstract EntityLivingBase Source();

	public abstract EntityLivingBase Target();

	public abstract float Number();

}
