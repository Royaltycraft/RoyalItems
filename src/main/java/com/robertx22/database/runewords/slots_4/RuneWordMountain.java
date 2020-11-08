package com.robertx22.database.runewords.slots_4;

import java.util.Arrays;
import java.util.List;

import com.robertx22.database.runewords.RuneWord;
import com.robertx22.database.stat_mods.flat.resources.HealthFlat;
import com.robertx22.database.stat_mods.flat.resources.HealthRegenFlat;
import com.robertx22.database.stat_mods.percent.ArmorPercent;
import com.robertx22.database.stats.StatMod;
import com.robertx22.items.runes.BerItem;
import com.robertx22.items.runes.CenItem;
import com.robertx22.items.runes.ItaItem;
import com.robertx22.items.runes.VohItem;
import com.robertx22.items.runes.base.BaseRuneItem;

public class RuneWordMountain extends RuneWord {

    @Override
    public List<StatMod> mods() {
	return Arrays.asList(new HealthFlat(), new ArmorPercent(), new HealthRegenFlat());
    }

    @Override
    public String GUID() {
	return "Mountain";
    }

    @Override
    public List<BaseRuneItem> runes() {
	return Arrays.asList(new ItaItem(0), new CenItem(0), new VohItem(0), new BerItem(0));

    }

    @Override
    public String unlocName() {
	return "mountain";
    }

}
