package com.github.syuchan1005.pokego;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by syuchan on 2016/07/24.
 */
public class MainWindow extends JFrame implements ActionListener, Window {
	private String sep = System.getProperty("line.separator");
	private JPanel mainPanel;
	private JMenuBar menuBar;
	private JMenu sortMenu;
	private List<JMenuItem> sortItems = new ArrayList<>();
	private JMenu otherMenu;
	private JMenuItem createImage, updatePokemon;
	private JTabbedPane tabbedPane1;
	private List<ContentWindow> pokemons;
	private LoginWindow loginWindow;
	private PlayerWindow playerWindow;
	private ItemWindow itemWindow;
	private static JFileChooser chooser;
	private static MainWindow mainWindow;

	public MainWindow() {
		mainWindow = this;
		this.setTitle("PokemonGoChecker");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		loginWindow = new LoginWindow(this);
		tabbedPane1.addTab("Login", loginWindow.getMainPanel());
		this.setContentPane(mainPanel);
		this.setSize(500, 350);
		Util.setLookAndFeel(this);
		if(chooser == null) {
			chooser = new JFileChooser();
			chooser.addChoosableFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".png");
				}

				@Override
				public String getDescription() {
					return "PNG Image File";
				}
			});
		}
	}

	public void createMenuBar() {
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
		otherMenu = new JMenu("Other");
		createImage = new JMenuItem("createImage");
		createImage.addActionListener(this);
		updatePokemon = new JMenuItem("updatePokemons");
		updatePokemon.addActionListener(this);
		otherMenu.add(createImage);
		otherMenu.add(updatePokemon);
		menuBar.add(sortMenu);
		menuBar.add(otherMenu);
		this.setJMenuBar(menuBar);
	}

	public void addComponent() throws LoginFailedException {
		PokemonGo pokemonGo = null;
		try {
			pokemonGo = Util.getPokemonGo();
			this.updatePokemons();
		} catch (RemoteServerException e) {
			throw new LoginFailedException();
		}
		Inventories inventories = pokemonGo.getInventories();
		tabbedPane1.remove(0);
		tabbedPane1.addTab("Player", (playerWindow = new PlayerWindow(pokemonGo.getPlayerProfile(), inventories.getPokebank().getPokemons().size())).getMainPanel());
		tabbedPane1.addTab("Items", (itemWindow = new ItemWindow(inventories.getItemBag())).getMainPanel());
		this.addTabs();
		tabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	public void addTabs() {
		tabbedPane1.removeAll();
		tabbedPane1.addTab("Player", playerWindow.getMainPanel());
		tabbedPane1.addTab("Items", itemWindow.getMainPanel());
		for(ContentWindow pokemon : pokemons) {
			tabbedPane1.addTab(
					PokemonEnum.getPokemonEnumByid(pokemon.getPokemon().getPokemonId().getNumber()).getJpName(),
					pokemon.getMainPanel());
		}
	}

	public void updatePokemons() throws LoginFailedException, RemoteServerException {
		Inventories inventories = Util.getPokemonGo().getInventories();
		inventories.updateInventories();
		pokemons = new ArrayList<>();
		for(Pokemon pokemon : inventories.getPokebank().getPokemons()) {
			pokemons.add(new ContentWindow(pokemon));
		}
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
					Collections.sort(pokemons, new Comparator<ContentWindow>() {
						@Override
						public int compare(ContentWindow p1, ContentWindow p2) {
							return p1.getPokemon().getPokemonId().getNumber() - p2.getPokemon().getPokemonId().getNumber();
						}
					});
					break;
				case "HP":
					Collections.sort(pokemons, new Comparator<ContentWindow>() {
						@Override
						public int compare(ContentWindow p1, ContentWindow p2) {
							return p2.getPokemon().getStamina() - p1.getPokemon().getStamina();
						}
					});
					break;
				case "NAME":
					Collections.sort(pokemons, new Comparator<ContentWindow>() {
						@Override
						public int compare(ContentWindow p1, ContentWindow p2) {
							String p1Name = p1.getPokemon().getNickname().isEmpty() ? PokemonEnum.getPokemonEnumByid(p1.getPokemon().getPokemonId().getNumber()).getJpName() : p1.getPokemon().getNickname();
							String p2Name = p2.getPokemon().getNickname().isEmpty() ? PokemonEnum.getPokemonEnumByid(p2.getPokemon().getPokemonId().getNumber()).getJpName() : p2.getPokemon().getNickname();
							return p1Name.compareToIgnoreCase(p2Name);
						}
					});
					break;
				case "CP":
					Collections.sort(pokemons, new Comparator<ContentWindow>() {
						@Override
						public int compare(ContentWindow p1, ContentWindow p2) {
							return p2.getPokemon().getCp() - p1.getPokemon().getCp();
						}
					});
					break;
				case "CREATEIMAGE":
					if(tabbedPane1.getTabCount() == 1) return;
					Window window;
					if(tabbedPane1.getSelectedIndex() <= 1) {
						window = (tabbedPane1.getSelectedIndex() == 0) ? playerWindow : itemWindow;
					} else {
						window = pokemons.get(tabbedPane1.getSelectedIndex() - 2);
					}
					chooser.showSaveDialog(this);
					File selected = chooser.getSelectedFile();
					try {
						Util.saveImage(window.getMainPanel(), chooser.getSelectedFile());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return;
				case "UPDATEPOKEMONS":
					try {
						updatePokemons();
					} catch (LoginFailedException e1) {
						e1.printStackTrace();
					} catch (RemoteServerException e1) {
						e1.printStackTrace();
					}
					break;
			}
			addTabs();
		}
	}

	@Override
	public JPanel getMainPanel() {
		return mainPanel;
	}

	public static MainWindow getInstance() {
		return mainWindow;
	}

	public static void main(String[] args) {
		MainWindow mainWindow = new MainWindow();
		mainWindow.setVisible(true);
	}
}
