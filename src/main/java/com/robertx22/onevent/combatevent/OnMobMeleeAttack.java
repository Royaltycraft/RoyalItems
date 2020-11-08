package com.robertx22.onevent.combatevent;

import com.robertx22.saveclasses.Unit;
import com.robertx22.spells.bases.MyDamageSource;
import com.robertx22.uncommon.capability.EntityData.UnitData;
import com.robertx22.uncommon.capability.WorldData.IWorldData;
import com.robertx22.uncommon.datasaving.Load;
import com.robertx22.uncommon.effectdatas.DamageEffect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class OnMobMeleeAttack {

	/**
	 * On attack, cancel and spawn the real attack with my damage source, mobs don't
	 * use energy but players do
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void onMobMeleeAttack(LivingAttackEvent event) {

		if (event.getEntityLiving().world.isRemote) {
			return;
		}

		if (event.getSource() instanceof MyDamageSource
				|| event.getSource().getDamageType().equals(DamageEffect.DmgSourceName)) {
			return;
		}
		try {
			if (event.getEntityLiving() == null || event.getSource().getTrueSource() == null
					|| !(event.getSource().getTrueSource() instanceof EntityLivingBase)) {
				return;
			}

			EntityLivingBase source = (EntityLivingBase) event.getSource().getTrueSource();

			EntityLivingBase target = event.getEntityLiving();

			if (target.isEntityAlive() == false) {
				return; // stops attacking dead mobs
			}

			UnitData targetData = Load.Unit(target);
			UnitData sourceData = Load.Unit(source);

			if (targetData == null || sourceData == null) {
				return;
			}

			Unit targetUnit = targetData.getUnit();
			Unit sourceUnit = sourceData.getUnit();

			if (targetUnit == null || sourceUnit == null) {
				return;
			}

			IWorldData world = Load.World(target.world);

			if (world == null) {
				return;
			}

			targetData.recalculateStats(target, world);
			sourceData.recalculateStats(source, world);

			if (source instanceof EntityPlayer) {

				ItemStack stack = source.getHeldItemMainhand();

				if (sourceData.isWeapon(stack)) {

					if (sourceData.tryUseWeapon(source, stack)) {
						sourceData.attackWithWeapon(source, target, stack);
					}

				} else {
					sourceData.unarmedAttack(source, target);
				}

			} else { // if its a mob

				sourceData.getUnit().MobBasicAttack(source, target, sourceData, event.getAmount());

				if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
					EntityLivingBase defender = event.getEntityLiving();
					EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
					defender.knockBack(attacker, 0.3F, attacker.posX - defender.posX, attacker.posZ - defender.posZ);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
