package com.robertx22.database.map_affixes.detrimental.ele;

import java.util.Arrays;
import java.util.List;

import com.robertx22.database.map_affixes.DetrimentalMapAffix;
import com.robertx22.database.map_mods.minus.all_ele_dmg.LessAllWaterDamageMap;
import com.robertx22.saveclasses.gearitem.StatModData;

public class LessAllWaterDmgAffix extends DetrimentalMapAffix {

	@Override
	public String Name() {
		return "LessAllWaterDmgAffix";
	}

	@Override
	public List<StatModData> Stats(int percent) {
		return Arrays.asList(StatModData.NewStatusEffect(percent, new LessAllWaterDamageMap()));
	}

}
