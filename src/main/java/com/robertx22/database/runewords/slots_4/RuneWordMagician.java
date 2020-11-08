package com.robertx22.database.runewords.slots_4;

import java.util.Arrays;
import java.util.List;

import com.robertx22.database.runewords.RuneWord;
import com.robertx22.database.stat_mods.flat.elemental.resist.ThunderResistFlat;
import com.robertx22.database.stat_mods.flat.resources.ManaFlat;
import com.robertx22.database.stat_mods.flat.resources.ManaRegenFlat;
import com.robertx22.database.stats.StatMod;
import com.robertx22.items.runes.AnoItem;
import com.robertx22.items.runes.DosItem;
import com.robertx22.items.runes.RahItem;
import com.robertx22.items.runes.XahItem;
import com.robertx22.items.runes.base.BaseRuneItem;

public class RuneWordMagician extends RuneWord {

    @Override
    public List<StatMod> mods() {
	return Arrays.asList(new ManaFlat(), new ManaRegenFlat(), new ThunderResistFlat());
    }

    @Override
    public String GUID() {
	return "Magician";
    }

    @Override
    public List<BaseRuneItem> runes() {
	return Arrays.asList(new DosItem(0), new AnoItem(0), new XahItem(0), new RahItem(0));

    }

    @Override
    public String unlocName() {
	return "magician";
    }

}
