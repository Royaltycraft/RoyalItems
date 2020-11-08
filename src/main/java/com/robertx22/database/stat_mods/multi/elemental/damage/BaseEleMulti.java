package com.robertx22.database.stat_mods.multi.elemental.damage;

import com.robertx22.database.stats.StatMod;
import com.robertx22.uncommon.enumclasses.StatTypes;

public abstract class BaseEleMulti extends StatMod {

    @Override
    public float Min() {
	return 5;
    }

    @Override
    public float Max() {
	return 15;
    }

    @Override
    public StatTypes Type() {
	return StatTypes.Multi;
    }

}
