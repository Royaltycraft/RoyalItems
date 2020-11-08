package com.robertx22.database.stat_types.spell_buff_traits;

import java.util.Arrays;
import java.util.List;

import com.robertx22.database.stat_types.spell_buff_traits.base.SpellBuffTrait;
import com.robertx22.database.stats.IStatEffect;
import com.robertx22.database.stats.stat_effects.spell_buffs.ManaRegenBuffEffect;

public class BuffManaRegenTrait extends SpellBuffTrait {

    public static String GUID = "Buff Mana Regen";

    @Override
    public String Guid() {
	return GUID;
    }

    @Override
    public String unlocString() {
	return "buff_mana_regen";
    }

    @Override
    public List<IStatEffect> GetEffects() {
	return Arrays.asList(new ManaRegenBuffEffect());

    }
}
