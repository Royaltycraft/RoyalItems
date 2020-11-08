package com.robertx22.network;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.robertx22.mmorpg.Main;
import com.robertx22.uncommon.capability.EntityData;
import com.robertx22.uncommon.capability.EntityData.UnitData;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EntityUnitPackage implements IMessage {

    public int id;
    public NBTTagCompound nbt;

    public EntityUnitPackage() {

    }

    public EntityUnitPackage(Entity entity) {
	this.id = entity.getEntityId();
	this.nbt = entity.getCapability(EntityData.Data, null).getNBT();
    }

    public EntityUnitPackage(Entity entity, UnitData data) {
	this.id = entity.getEntityId();
	this.nbt = data.getNBT();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
	nbt = ByteBufUtils.readTag(buf);
	id = nbt.getInteger("id");
    }

    @Override
    public void toBytes(ByteBuf buf) {
	nbt.setInteger("id", id);
	ByteBufUtils.writeTag(buf, nbt);

    }

    public static class Handler implements IMessageHandler<EntityUnitPackage, IMessage> {

	@Override
	public IMessage onMessage(EntityUnitPackage message, MessageContext ctx) {

	    Runnable noteThread = new Runnable() {
		@Override
		public void run() {
		    try {

			final EntityPlayer player = Main.proxy.getPlayerEntityFromContext(ctx);

			if (player != null && player.world != null) {
			    Entity entity = player.world.getEntityByID(message.id);

			    EntityLivingBase en = (EntityLivingBase) entity;

			    if (en != null) {
				en.getCapability(EntityData.Data, null).setNBT(message.nbt);

			    }

			}

		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}

	    };
	    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	    scheduler.schedule(noteThread, 1, TimeUnit.SECONDS);

	    return null;
	}

    }
}
