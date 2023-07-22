package com.ruseps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.definitions.ItemDefinition.EquipmentType;

public class DumpBonuses {

	public static void main(String[] args) {
		new DumpBonuses().run2();
	}

	public void run2() {
		try {
			ItemDefinition.init();

			File f = new File("hovers.txt");
			FileWriter fw = new FileWriter(f);

			fw.append(ItemDefinition.getMaxAmountOfItems() + "\n");

			for (ItemDefinition item : ItemDefinition.getDefinitions()) {
				if (item == null)
					continue;
				System.out.println("dumping " + item.getId());

				int[] intbonus = new int[14]; // 5 att, 5 def, 1 pray, 1 str, 1 ranged str, 1 magic dmg, 1 tier
				int index = 0;
				for (int i = 0; i < item.getBonus().length; i++) {
					if (i >= 10 && i <= 13) {
						continue;
					}
					if (i >= 19 && i <= 21) {
						continue;
					}
					int value = (int) item.getBonus()[i];
//					if (value > 150000)
//						value = 150000;
					intbonus[index] = value;
					index++;
				}

				fw.append(item.getId() + "\n");
				for (int bonusNumber : intbonus) {
					fw.append(bonusNumber + "\n");
				}
			}
			fw.close();
			System.out.println();
			System.out.println(ItemDefinition.getMaxAmountOfItems());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
