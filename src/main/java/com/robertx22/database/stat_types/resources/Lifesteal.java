package com.robertx22.database.stat_types.resources;

import java.util.Arrays;
import java.util.List;

import com.robertx22.database.stats.IStatEffect;
import com.robertx22.database.stats.IStatEffects;
import com.robertx22.database.stats.Stat;
import com.robertx22.database.stats.stat_effects.LifestealEffect;
import com.robertx22.uncommon.enumclasses.Elements;

public class Lifesteal extends Stat implements IStatEffects {
    public static String GUID = "Lifesteal";

    @Override
    public List<IStatEffect> GetEffects() {
	return Arrays.asList(new LifestealEffect());
    }

    @Override
    public String unlocString() {
	return "lifesteal";
    }

    public Lifesteal() {
    }

    @Override
    public String Guid() {
	return GUID;
    }

    @Override
    public boolean ScalesToLevel() {
	return false;
    }

    @Override
    public Elements Element() {
	return null;
    }

    @Override
    public boolean IsPercent() {
	return true;
    }

}
