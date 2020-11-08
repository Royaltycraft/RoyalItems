package com.robertx22.commands;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.robertx22.generation.AwakenRuneWordGen;
import com.robertx22.generation.blueprints.AwakenRuneWordBlueprint;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class GiveAwakenRuneWord extends CommandBase {

	@Override
	public String getName() {
		return "giveawakenruneword";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/giveawakenruneword (player) (runeword name or random) (amount)";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		return new ArrayList<String>() {
			{
				if (args.length < 2) {
					add("username");
				} else if (args.length < 3) {
					add("random");
				} else if (args.length < 4) {
					add("amount");
				}
			}
		};
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		if (args.length < 2)
			throw new WrongUsageException("/giveawakenruneword (player) (runeword name or random) (amount)");
		String word = args[1];
		int amount = Integer.valueOf(args[1]);

		for (int i = 0; i < amount; i++) {
			AwakenRuneWordBlueprint blueprint = new AwakenRuneWordBlueprint();
			if (word != "random") {
				blueprint.word = word;
			}

			EntityPlayer player = getPlayer(server, sender, args[0]);

			try {
				player.addItemStackToInventory(AwakenRuneWordGen.Create(blueprint));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}