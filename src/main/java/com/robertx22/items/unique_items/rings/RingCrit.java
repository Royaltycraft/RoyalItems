package com.robertx22.items.unique_items.rings;

import java.util.Arrays;
import java.util.List;

import com.robertx22.database.stat_mods.flat.resources.EnergyRegenFlat;
import com.robertx22.database.stat_mods.flat.resources.HealthFlat;
import com.robertx22.database.stat_mods.percent.MajorCriticalDamagePercent;
import com.robertx22.database.stat_mods.percent.MajorCriticalHitPercent;
import com.robertx22.database.stats.StatMod;
import com.robertx22.items.unique_items.bases.BaseUniqueRing;

import baubles.api.BaubleType;

public class RingCrit extends BaseUniqueRing {

    public RingCrit(BaubleType type) {
		super(type);
    }

    @Override
    public int Tier() {
	return 4;
    }

    @Override
    public String GUID() {
	return "ringcrit0";
    }

    @Override
    public List<StatMod> uniqueStats() {
	return Arrays.asList(new MajorCriticalHitPercent(), new MajorCriticalDamagePercent(), new HealthFlat(),
		new EnergyRegenFlat());
    }

}
