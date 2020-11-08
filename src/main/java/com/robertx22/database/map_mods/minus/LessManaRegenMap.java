package com.robertx22.database.map_mods.minus;

import com.robertx22.database.stat_types.resources.ManaRegen;
import com.robertx22.database.stats.Stat;
import com.robertx22.database.stats.StatMod;
import com.robertx22.uncommon.enumclasses.StatTypes;

public class LessManaRegenMap extends StatMod {

	public LessManaRegenMap() {
	}

	@Override
	public String GUID() {
		return "LessManaRegenMap";
	}

	@Override
	public float Min() {
		return -30;
	}

	@Override
	public float Max() {
		return -80;
	}

	@Override
	public StatTypes Type() {
		return StatTypes.Multi;
	}

	@Override
	public Stat GetBaseStat() {
		return new ManaRegen();
	}

}