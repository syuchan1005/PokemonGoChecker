package com.github.syuchan1005.pokego;

import POGOProtos.Inventory.Item.ItemIdOuterClass;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.inventory.ItemBag;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by syuchan on 2016/07/25.
 */
public class ItemWindow {
	private JPanel mainPanel;
	private JTable itemTable;
	private static DefaultTableModel model = new DefaultTableModel(new String[] {"ItemName", "Count"}, 0);
	private static Field items;

	public ItemWindow(ItemBag itemBag) {
		try {
			if(items == null) {
				items = itemBag.getClass().getDeclaredField("items");
				items.setAccessible(true);
			}
			for(Map.Entry<ItemIdOuterClass.ItemId, Item> entry : ((HashMap<ItemIdOuterClass.ItemId, Item>) items.get(itemBag)).entrySet()) {
				model.addRow(new Object[] {entry.getKey().name(), entry.getValue().getCount()});
			}
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	private void createUIComponents() {
		itemTable = new JTable(model);
	}
}
