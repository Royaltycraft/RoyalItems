package com.robertx22.uncommon.testing.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.robertx22.db_lists.Rarities;
import com.robertx22.db_lists.Runes;
import com.robertx22.items.runes.base.BaseRuneItem;

public class CreateJsonsOfRunes {

    public void doit() {

	String text = "{\n    \"parent\": \"item/generated\",\n    \"textures\": {\n        \"layer0\": \"mmorpg:items/runes/cen/1\"\n    }\n}\n";

	String lang = "";

	for (BaseRuneItem rune : Runes.All.values()) {

	    for (int i = 0; i < 6; i++) {

		String runename = Rarities.Items.get(i).Color() + "" + rune.name() + " - "
			+ Rarities.Items.get(i).GUID() + " Rune ";

		// for lang file
		lang += "item.mmorpg:runes/" + rune.name().toLowerCase() + i + ".name=" + runename + "\n";

		// for model files
		String newtext = text.replaceAll("1", i + "");

		String textfinal = newtext.replace("cen", rune.name().toLowerCase());

		String name = rune.name().toLowerCase() + i;

		Path path = Paths.get("C:\\Users\\User\\Desktop\\here\\" + name + ".json");
		byte[] strToBytes = textfinal.getBytes();

		try {
		    Files.write(path, strToBytes);
		} catch (IOException e) {

		    e.printStackTrace();
		}

	    }

	    Path path = Paths.get("C:\\Users\\User\\Desktop\\here\\" + "LANG_FILE" + ".json");
	    byte[] strToBytes = lang.getBytes();

	    try {
		Files.write(path, strToBytes);
	    } catch (IOException e) {

		e.printStackTrace();
	    }

	}

    }

}
