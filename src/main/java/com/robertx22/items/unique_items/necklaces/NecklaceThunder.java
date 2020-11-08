package com.robertx22.items.unique_items.necklaces;

import java.util.Arrays;
import java.util.List;

import com.robertx22.database.stat_mods.flat.elemental.bonus.ThunderSpellToAttackFlat;
import com.robertx22.database.stat_mods.flat.elemental.resist.NatureResistFlat;
import com.robertx22.database.stat_mods.flat.elemental.spell_dmg.SpellThunderDamageFlat;
import com.robertx22.database.stat_mods.flat.elemental.transfers.NatureToThunderTransferFlat;
import com.robertx22.database.stat_mods.flat.less.LessHealthRegenFlat;
import com.robertx22.database.stat_mods.flat.resources.EnergyRegenFlat;
import com.robertx22.database.stats.StatMod;
import com.robertx22.items.unique_items.bases.BaseUniqueNecklace;

import baubles.api.BaubleType;

public class NecklaceThunder extends BaseUniqueNecklace {

    public NecklaceThunder(BaubleType type) {
		super(type);
    }

    @Override
    public int Tier() {
	return 18;
    }

    @Override
    public String GUID() {
	return "necklacethunder0";
    }

    @Override
    public List<StatMod> uniqueStats() {
	return Arrays.asList(new SpellThunderDamageFlat(), new ThunderSpellToAttackFlat(),
		new NatureToThunderTransferFlat(), new NatureResistFlat(), new EnergyRegenFlat(),
		new LessHealthRegenFlat());

    }

}
