package com.robertx22.database.stat_mods.flat.elemental.conversions;

import com.robertx22.database.stats.StatMod;
import com.robertx22.uncommon.enumclasses.StatTypes;

public abstract class BaseConversionFlat extends StatMod {

    @Override
    public float Min() {
	return 10;
    }

    @Override
    public float Max() {
	return 35;
    }

    @Override
    public StatTypes Type() {
	return StatTypes.Flat;
    }

}
