package com.github.syuchan1005.pokego;

import com.pokegoapi.api.pokemon.Pokemon;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by syuchan on 2016/07/24.
 */
public class MainWindow extends JFrame {
	private JPanel mainPanel;
	private JTabbedPane tabbedPane1;
	private List<Pokemon> pokemons;
	private LoginWindow loginWindow;

	public MainWindow() {
		this.setTitle("Pokemon Go 確認くん");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		loginWindow = new LoginWindow(this);
		tabbedPane1.addTab("Login", loginWindow.getMainPanel());
		this.setContentPane(mainPanel);
		this.setSize(450, 300);
		Util.setLookAndFeel(this);
	}

	// private static Field protoField;

	public void addComponent() {
		tabbedPane1.remove(0);
		tabbedPane1.addTab("Player", new PlayerWindow(Util.getPokemonGo().getPlayerProfile()).getMainPanel());
		tabbedPane1.addTab("Items", new ItemWindow(Util.getPokemonGo().getInventories().getItemBag()).getMainPanel());
		pokemons = Util.getPokemonGo().getInventories().getPokebank().getPokemons();
		Collections.sort(pokemons, new Comparator<Pokemon>() {
			@Override
			public int compare(Pokemon p1, Pokemon p2) {
				return p2.getCp() - p1.getCp();
			}
		});
		for(Pokemon pokemon : pokemons) {
			tabbedPane1.addTab(PokemonEnum.getPokemonEnumByid(pokemon.getPokemonId().getNumber()).getJpName(),
					new ContentWindow(pokemon).getMainPanel());
		}
		tabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		/*
		tabbedPane1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				try {
					if(protoField == null) {
						protoField = Pokemon.class.getDeclaredField("proto");
						protoField.setAccessible(true);
					}
					System.out.println(protoField.get(pokemons.get(((JTabbedPane) event.getSource()).getSelectedIndex())).toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		*/
	}

	public static void main(String[] args) {
		MainWindow mainWindow = new MainWindow();
		mainWindow.setVisible(true);
	}
}
