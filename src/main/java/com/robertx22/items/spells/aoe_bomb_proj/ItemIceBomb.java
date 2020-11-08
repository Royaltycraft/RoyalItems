package com.robertx22.items.spells.aoe_bomb_proj;

import com.robertx22.items.gearitems.bases.BaseSpellItem;
import com.robertx22.mmorpg.Ref;
import com.robertx22.spells.aoe_bomb_proj.SpellIceBomb;
import com.robertx22.spells.bases.BaseSpell;
import com.robertx22.uncommon.utilityclasses.RegisterUtils;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber
public class ItemIceBomb extends BaseSpellItem {

    public ItemIceBomb() {
	super();
    }

    @GameRegistry.ObjectHolder(Ref.MODID + ":spell_Ice_bomb")
    public static final Item ITEM = null;

    @Override
    public BaseSpell Spell() {
	return new SpellIceBomb();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
	event.getRegistry().register(new ItemIceBomb());
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
	RegisterUtils.registerRender(ITEM);
    }

    @Override
    public String GUID() {
	return Ref.MODID + ":spell_Ice_bomb";
    }

}
