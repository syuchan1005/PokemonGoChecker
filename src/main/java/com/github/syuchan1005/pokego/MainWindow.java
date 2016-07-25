package com.github.syuchan1005.pokego;

import com.pokegoapi.api.pokemon.Pokemon;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by syuchan on 2016/07/24.
 */
public class MainWindow extends JFrame implements ActionListener {
	private JPanel mainPanel;
	private JMenuBar menuBar;
	private JMenu sortMenu;
	private List<JMenuItem> sortItems = new ArrayList<>();
	private JTabbedPane tabbedPane1;
	private List<Pokemon> pokemons;
	private LoginWindow loginWindow;
	private PlayerWindow playerWindow;
	private ItemWindow itemWindow;

	public MainWindow() {
		this.setTitle("Pokemon Go 確認くん");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		loginWindow = new LoginWindow(this);
		tabbedPane1.addTab("Login", loginWindow.getMainPanel());
		this.setContentPane(mainPanel);
		this.setSize(450, 300);
		this.createMenuBar();
		Util.setLookAndFeel(this);
	}

	private void createMenuBar() {
		menuBar = new JMenuBar();
		sortMenu = new JMenu("Sort");
		sortItems.add(new JMenuItem("Time"));
		sortItems.add(new JMenuItem("Favorite"));
		sortItems.add(new JMenuItem("Number"));
		sortItems.add(new JMenuItem("HP"));
		sortItems.add(new JMenuItem("Name"));
		sortItems.add(new JMenuItem("CP"));
		for(JMenuItem sortItem : sortItems) {
			sortItem.addActionListener(this);
			sortMenu.add(sortItem);
		}
		menuBar.add(sortMenu);
		this.setJMenuBar(menuBar);
	}

	public void addComponent() {
		tabbedPane1.remove(0);
		tabbedPane1.addTab("Player", (playerWindow = new PlayerWindow(Util.getPokemonGo().getPlayerProfile())).getMainPanel());
		tabbedPane1.addTab("Items", (itemWindow = new ItemWindow(Util.getPokemonGo().getInventories().getItemBag())).getMainPanel());
		pokemons = Util.getPokemonGo().getInventories().getPokebank().getPokemons();
		addTabs(false);
		tabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	public void addTabs(boolean override) {
		if(override) {
			tabbedPane1.removeAll();
			tabbedPane1.addTab("Player", playerWindow.getMainPanel());
			tabbedPane1.addTab("Items", itemWindow.getMainPanel());
		}
		for(Pokemon pokemon : pokemons) {
			tabbedPane1.addTab(PokemonEnum.getPokemonEnumByid(pokemon.getPokemonId().getNumber()).getJpName(),
					new ContentWindow(pokemon).getMainPanel());
		}
	}

	public static void main(String[] args) {
		MainWindow mainWindow = new MainWindow();
		mainWindow.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JMenuItem) {
			JMenuItem item = ((JMenuItem) e.getSource());
			switch (item.getText().toUpperCase()) {
				case "TIME":
				case "FAVORITE":
					JOptionPane.showMessageDialog(this, "Unimplement");
					break;
				case "NUMBER":
					Collections.sort(pokemons, new Comparator<Pokemon>() {
						@Override
						public int compare(Pokemon p1, Pokemon p2) {
							return p1.getPokemonId().getNumber() - p2.getPokemonId().getNumber();
						}
					});
					break;
				case "HP":
					Collections.sort(pokemons, new Comparator<Pokemon>() {
						@Override
						public int compare(Pokemon p1, Pokemon p2) {
							return p2.getStamina() - p1.getStamina();
						}
					});
					break;
				case "NAME":
					Collections.sort(pokemons, new Comparator<Pokemon>() {
						@Override
						public int compare(Pokemon p1, Pokemon p2) {
							String p1Name = p1.getNickname().isEmpty() ? PokemonEnum.getPokemonEnumByid(p1.getPokemonId().getNumber()).getJpName() : p1.getNickname();
							String p2Name = p2.getNickname().isEmpty() ? PokemonEnum.getPokemonEnumByid(p2.getPokemonId().getNumber()).getJpName() : p2.getNickname();
							return p1Name.compareToIgnoreCase(p2Name);
						}
					});
					break;
				case "CP":
					Collections.sort(pokemons, new Comparator<Pokemon>() {
						@Override
						public int compare(Pokemon p1, Pokemon p2) {
							return p2.getCp() - p1.getCp();
						}
					});
					break;
			}
			addTabs(true);
		}
	}
}
