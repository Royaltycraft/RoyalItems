package com.robertx22.items.unique_items.bracelets;

import java.util.Arrays;
import java.util.List;

import com.robertx22.database.stat_mods.flat.elemental.resist.NatureResistFlat;
import com.robertx22.database.stat_mods.flat.elemental.spell_dmg.SpellNatureDamageFlat;
import com.robertx22.database.stat_mods.flat.resources.HealthFlat;
import com.robertx22.database.stat_mods.percent.HealthRegenPercent;
import com.robertx22.database.stat_mods.percent.much_less.CrippleCriticalDamagePercent;
import com.robertx22.database.stats.StatMod;
import com.robertx22.items.unique_items.bases.BaseUniqueBracelet;

import baubles.api.BaubleType;

public class BraceletNature extends BaseUniqueBracelet {

    public BraceletNature(BaubleType type) {
		super(type);
    }

    @Override
    public int Tier() {
	return 2;
    }

    @Override
    public String GUID() {
	return "braceletnature0";
    }

    @Override
    public List<StatMod> uniqueStats() {
	return Arrays.asList(new SpellNatureDamageFlat(), new NatureResistFlat(), new HealthFlat(),
		new HealthRegenPercent(), new CrippleCriticalDamagePercent());
    }

}
