package com.robertx22.database.rarities.mobs;

import com.robertx22.config.ModConfig;
import com.robertx22.database.rarities.MobRarity;
import com.robertx22.database.rarities.base.BaseCommon;

public class CommonMob extends BaseCommon implements MobRarity {

    @Override
    public float StatMultiplier() {
	return 0.7F;
    }

    @Override
    public float DamageMultiplier() {
	return 1F;
    }

    @Override
    public float HealthMultiplier() {
	return 0.55F;
    }

    @Override
    public float LootMultiplier() {
	return 0.7F;
    }

    @Override
    public int MaxMobEffects() {
	return 0;
    }

    @Override
    public float ExpOnKill() {
	return 3;
    }

    @Override
    public int Weight() {
	return ModConfig.RarityWeightConfig.MOBS.COMMON_WEIGHT;
    }

}
