package com.robertx22.database.sets.armors;

import java.util.HashMap;
import java.util.List;

import com.robertx22.database.gearitemslots.bases.GearItemSlot;
import com.robertx22.database.stat_mods.percent.HealthPercent;
import com.robertx22.database.stats.StatMod;
import com.robertx22.saveclasses.gearitem.gear_bases.Set;

public class RockmanChains extends Set {

    @Override
    public String Name() {
	return "Rockman's Chains";
    }

    @Override
    public HashMap<Integer, StatMod> AllMods() {

	return new HashMap<Integer, StatMod>() {
	    {
		{
		    put(2, new HealthPercent());

		}
	    }
	};
    }

    @Override
    public List<GearItemSlot> GearTypes() {
	return this.armor();
    }

}
