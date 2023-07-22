package com.ruseps.world.content.aoesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.ruseps.model.CombatIcon;

public class AOESystem {

	private List<AOEWeaponData> weaponData = new ArrayList<>();

	public void parseData() { // look this parses the data from the file, thats why it was never working, forgot that cuz its midnight and im tired:

		Path filePath = Paths.get("data", "AOEWeapons.txt");

		try (Stream<String> lines = Files.lines(filePath)) {
			lines.forEach(line -> {
				String[] wepData = line.split(" ");
				//System.out.println(Arrays.toString(wepData));
				weaponData.add(new AOEWeaponData(parseInt(wepData[0]), parseInt(wepData[1]), parseInt(wepData[2]),
						parseInt(wepData[3]), getIcon(wepData[4])));
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public AOEWeaponData getAOEData(int id) {

		int index = -1;

		for (int i = 0; i < weaponData.size(); i++) {
			if (weaponData.get(i).getId() == id) {
				index = i;
				break;
			}
		}

		return index > -1 ? weaponData.get(index) : null;
	}

	private CombatIcon getIcon(String str) {
		CombatIcon icon = null;

		if (str.equalsIgnoreCase("Range")) {
			icon = CombatIcon.RANGED;
		} else if (str.equalsIgnoreCase("Melee")) {
			icon = CombatIcon.MELEE;
		} else {
			icon = CombatIcon.MAGIC;
		}

		return icon;
	}

	private int parseInt(String str) {
		return Integer.parseInt(str);
	}

	private static AOESystem SINGLETON = null;//One is The Type of atack and first one is the weapon id and the other 2 igot no idea lmao

	public static AOESystem getSingleton() {
		if (SINGLETON == null) {
			SINGLETON = new AOESystem();
		}
		return SINGLETON;
	}

}
