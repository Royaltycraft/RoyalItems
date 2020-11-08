package com.robertx22.uncommon.gui.player_stats;

import com.libraries.rabbit.gui.RabbitGui;
import com.robertx22.mmorpg.Keybinds;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class OnPlayerStatsKeyPress {

	@SubscribeEvent
	public static void onKeyInput(KeyInputEvent event) {

		if (Keybinds.Player_Stats.isPressed()) {
			RabbitGui.proxy.display(new PlayerStatsGui());
		}
	}
}
