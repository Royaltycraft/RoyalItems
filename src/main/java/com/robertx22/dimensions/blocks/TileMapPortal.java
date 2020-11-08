package com.robertx22.dimensions.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileMapPortal extends TileEntity {

	public int id;

	public TileMapPortal(int id) {
		this.id = id;
	}

	public TileMapPortal() {

	}

	int ticks = 0;

	public void ontick() {
		ticks++;
	}

	public boolean readyToTeleport() {

		if (ticks > 80) {
			ticks = 0;
			return true;
		}
		return false;

	}

	@SideOnly(Side.CLIENT)
	public boolean shouldRenderFace(EnumFacing face) {
		return face == EnumFacing.UP;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		id = nbt.getInteger("dim_Id");
		ticks = nbt.getInteger("ticks");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt); // The super call is required to save and load the tile loc

		nbt.setInteger("dim_Id", id);
		nbt.setInteger("ticks", ticks);

		return nbt;
	}

}
