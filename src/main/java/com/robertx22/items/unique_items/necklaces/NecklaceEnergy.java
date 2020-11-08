package com.robertx22.items.unique_items.necklaces;

import java.util.Arrays;
import java.util.List;

import com.robertx22.database.stat_mods.flat.resources.EnergyRegenFlat;
import com.robertx22.database.stat_mods.flat.resources.HealthRegenFlat;
import com.robertx22.database.stat_mods.flat.resources.conversions.ManaToEnergyConvFlat;
import com.robertx22.database.stat_mods.percent.CriticalDamagePercent;
import com.robertx22.database.stat_mods.percent.HealthPercent;
import com.robertx22.database.stat_mods.percent.much_less.CrippleDodgePercent;
import com.robertx22.database.stats.StatMod;
import com.robertx22.items.unique_items.bases.BaseUniqueNecklace;

import baubles.api.BaubleType;

public class NecklaceEnergy extends BaseUniqueNecklace {

    public NecklaceEnergy(BaubleType type) {
		super(type);
    }

    @Override
    public int Tier() {
	return 16;
    }

    @Override
    public String GUID() {
	return "necklaceenergy0";
    }

    @Override
    public List<StatMod> uniqueStats() {
	return Arrays.asList(new ManaToEnergyConvFlat(), new EnergyRegenFlat(), new HealthRegenFlat(),
		new CriticalDamagePercent(), new HealthPercent(), new CrippleDodgePercent());
    }

}
