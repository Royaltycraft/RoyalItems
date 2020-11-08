package com.robertx22.items.misc;

import java.util.List;

import javax.annotation.Nullable;

import com.robertx22.db_lists.CreativeTabList;
import com.robertx22.items.BaseItem;
import com.robertx22.mmorpg.Ref;
import com.robertx22.uncommon.CLOC;
import com.robertx22.uncommon.capability.EntityData;
import com.robertx22.uncommon.utilityclasses.RegisterItemUtils;
import com.robertx22.uncommon.utilityclasses.RegisterUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class ItemPlayerLevelUp extends BaseItem {

    @GameRegistry.ObjectHolder(Ref.MODID + ":player_levelup")
    public static final Item ITEM = null;

    public ItemPlayerLevelUp() {
	this.setMaxDamage(0);
	this.setCreativeTab(CreativeTabList.CurrencyTab);

	RegisterItemUtils.RegisterItemName(this, "player_levelup");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {

	if (!worldIn.isRemote) {
	    try {

		int req = tokensRequired(player.getCapability(EntityData.Data, null).getLevel());

		if (hasEnoughTokens(player.getHeldItem(handIn), req)) {

		    if (player.getCapability(EntityData.Data, null).LevelUp((EntityPlayerMP) player)) {

			return new ActionResult<ItemStack>(EnumActionResult.PASS,
				EmptyOrDecrease(player.getHeldItem(handIn), req));

		    }
		} else {
		    player.sendMessage(new TextComponentString("You need a total of " + req + " tokens to level up."));
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(handIn));
    }

    private boolean hasEnoughTokens(ItemStack stack, int tokensreq) {
	return stack.getCount() >= tokensreq;
    }

    private int tokensRequired(int level) {

	int req = level / 10;

	if (req < 1) {
	    req = 1;
	}
	if (req >= 64) {
	    return 64;
	} else {
	    return req;
	}
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
	event.getRegistry().register(new ItemPlayerLevelUp());
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
	RegisterUtils.registerRender(ITEM);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

	tooltip.add(CLOC.tooltip("player_levelup"));

    }

}
