package com.robertx22.database.rarities.runes;

import com.robertx22.config.ModConfig;
import com.robertx22.database.rarities.RuneRarity;
import com.robertx22.database.rarities.items.RareItem;

public class RareRune extends RareItem implements RuneRarity {

    @Override
    public int minimumRunewordPower() {
	return 1;
    }

    @Override
    public int maximumRunewordPower() {
	return 2;
    }

    @Override
    public int Weight() {
	return ModConfig.RarityWeightConfig.RUNES.RARE_WEIGHT;
    }
}
