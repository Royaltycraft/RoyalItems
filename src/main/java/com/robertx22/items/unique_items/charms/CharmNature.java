package com.robertx22.items.unique_items.charms;

import java.util.Arrays;
import java.util.List;

import com.robertx22.database.stat_mods.flat.elemental.resist.NatureResistFlat;
import com.robertx22.database.stat_mods.flat.resources.HealthFlat;
import com.robertx22.database.stat_mods.percent.HealthRegenPercent;
import com.robertx22.database.stat_mods.percent.less.LessCriticalDamagePercent;
import com.robertx22.database.stat_mods.percent.pene.NaturePenePercent;
import com.robertx22.database.stat_mods.percent.spell_ele_dmg.SpellNatureDamagePercent;
import com.robertx22.database.stats.StatMod;
import com.robertx22.items.unique_items.bases.BaseUniqueCharm;

import baubles.api.BaubleType;

public class CharmNature extends BaseUniqueCharm {

    public CharmNature(BaubleType type) {
		super(type);
    }

    @Override
    public int Tier() {
	return 15;
    }

    @Override
    public String GUID() {
	return "charmnature0";
    }

    @Override
    public List<StatMod> uniqueStats() {
	return Arrays.asList(new SpellNatureDamagePercent(), new HealthRegenPercent(), new NaturePenePercent(),
		new HealthFlat(), new NatureResistFlat(), new LessCriticalDamagePercent());
    }

}
