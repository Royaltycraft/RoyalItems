package com.robertx22.dimensions.biome_providers;

import com.libraries.prospector.traverse.RegisterBiomes;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProviderSingle;

public class BPThicket extends BiomeProviderSingle {

	public BPThicket(World world) {
		super(RegisterBiomes.THICKET);

	}

}