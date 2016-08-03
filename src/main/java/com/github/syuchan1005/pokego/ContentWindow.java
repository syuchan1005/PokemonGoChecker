package com.github.syuchan1005.pokego;

import POGOProtos.Data.PokemonDataOuterClass;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Image;
import java.lang.reflect.Field;

/**
 * Created by syuchan on 2016/07/24.
 */
public class ContentWindow implements Window {
	private JPanel mainPanel;
	private JLabel pokemonImage;
	private JLabel pokemonName;
	private JLabel pokemonNickname;
	private JLabel pokemonCP;
	private JButton renameButton;
	private JButton transferButton;
	private JButton powerUpButton;
	private JButton evolveButton;
	private JLabel powerUpStardustLabel;
	private JLabel powerUpCandyLabel;
	private JLabel evolveCandyLabel;
	private DefaultTableModel model;
	private JTable contentTable;
	private JPanel changePanel;
	private Pokemon pokemon;
	private ContentWindow instance;

	public ContentWindow(final Pokemon pokemon) throws LoginFailedException, RemoteServerException {
		instance = this;
		this.pokemon = pokemon;
		PokemonDataOuterClass.PokemonData pokemonData = null;
		try {
			pokemonData = getPokemonData(pokemon);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		PokemonEnum pokemonEnum = PokemonEnum.getPokemonEnumByid(pokemon.getPokemonId().getNumber());
		final ImageIcon imageIcon = Util.getPokemonImage(pokemon.getPokemonId().getNumber());
		if(imageIcon == null) {
			pokemonImage.setText("NO IMAGE");
		} else {
			imageIcon.setImage(imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
			pokemonImage.setIcon(imageIcon);
			pokemonImage.setText("");
		}
		pokemonImage.setBorder(new LineBorder(Color.black));
		pokemonName.setText(PokemonEnum.getPokemonEnumByid(pokemon.getPokemonId().getNumber()).getJpName());
		pokemonNickname.setText((pokemon.getNickname().length() != 0) ? "(" + pokemon.getNickname() + ")" : "NoNickname");
		pokemonCP.setText("CP: " + pokemon.getCp());
		powerUpStardustLabel.setText("Stardust: " + Util.getPowerUPStardust(pokemonData.getNumUpgrades()));
		powerUpCandyLabel.setText("Candy: " + Util.getPowerUPCandy(pokemonData.getNumUpgrades()));
		evolveCandyLabel.setText("Candy: " + pokemon.getCandiesToEvolve());

		model.addRow(new String[]{"Type", PokemonType.ArraytoString(pokemonEnum.getType())});
		model.addRow(new String[]{"HP", pokemon.getStamina() + " / " + pokemon.getMaxStamina()});
		model.addRow(new String[]{"Weight", String.valueOf(pokemonData.getWeightKg())});
		model.addRow(new String[]{"Height", String.valueOf(pokemon.getHeightM())});
		model.addRow(new String[]{"Candy", String.valueOf(pokemon.getCandy())});
		model.addRow(new String[]{"UpgradesCount", String.valueOf(pokemonData.getNumUpgrades())});
		model.addRow(new String[]{"BattlesAttacked", String.valueOf(pokemon.getBattlesAttacked())});
		model.addRow(new String[]{"BattlesDefended", String.valueOf(pokemon.getBattlesDefended())});
		model.addRow(new String[]{"IndividualAttack", String.valueOf(pokemon.getIndividualAttack())});
		model.addRow(new String[]{"IndividualDefense", String.valueOf(pokemon.getIndividualDefense())});
		model.addRow(new String[]{"IndividualStamina", String.valueOf(pokemon.getIndividualStamina())});

		renameButton.addActionListener(e -> {
			String name = JOptionPane.showInputDialog("PokemonName:");
			if(name == null) return;
			if(name.length() > 8) {
				JOptionPane.showMessageDialog(instance.getMainPanel(), "");
			}
			try {
				pokemon.renamePokemon(name);
				instance.pokemonNickname.setText("(" + name + ")");
			} catch (Exception e1) {
				errorDialog();
			}
		});

		transferButton.addActionListener(e -> {
			int dialog = JOptionPane.showConfirmDialog(instance.getMainPanel(),
					"You can't take it back after it's transferred to the Professor.\n" +
							"Do you really want to transfer Bulbasaur to the Professor?");
			if(dialog != 0) return;
			try {
				pokemon.transferPokemon();
				MainWindow.getInstance().updatePokemons();
				MainWindow.getInstance().addTabs();
			} catch (Exception e1) {
				errorDialog();
			}
		});
		powerUpButton.addActionListener(e -> {
			int dialog = JOptionPane.showConfirmDialog(instance.getMainPanel(),
					"Do you wait to power up " +
							(pokemon.getNickname().isEmpty() ? PokemonEnum.getPokemonEnumByid(pokemon.getPokemonId().getNumber()).getName() : pokemon.getNickname()));
			if(dialog != 0) return;
			try {
				pokemon.powerUp();
				MainWindow.getInstance().updatePokemons();
				MainWindow.getInstance().addTabs();
			} catch (Exception e1) {
				errorDialog();
			}
		});
		evolveButton.addActionListener(e -> {
			int dialog = JOptionPane.showConfirmDialog(instance.getMainPanel(),
					"Do you wait to evolve " +
							(pokemon.getNickname().isEmpty() ? PokemonEnum.getPokemonEnumByid(pokemon.getPokemonId().getNumber()).getName() : pokemon.getNickname()));
			if(dialog != 0) return;
			try {
				pokemon.evolve();
				MainWindow.getInstance().updatePokemons();
				MainWindow.getInstance().addTabs();
			} catch (Exception e1) {
				errorDialog();
			}
		});
	}

	private static Field protoField;

	public static PokemonDataOuterClass.PokemonData getPokemonData(Pokemon pokemon) throws ReflectiveOperationException {
		if(protoField == null) {
			protoField = pokemon.getClass().getDeclaredField("proto");
			protoField.setAccessible(true);
		}
		return ((PokemonDataOuterClass.PokemonData) protoField.get(pokemon));
	}

	public void errorDialog() {
		JOptionPane.showMessageDialog(instance.getMainPanel(), "Oops! Remote Server Failed", "Error", JOptionPane.ERROR_MESSAGE);
	}

	public Pokemon getPokemon() {
		return pokemon;
	}

	@Override
	public JPanel getMainPanel() {
		return mainPanel;
	}

	private void createUIComponents() {
		model = new DefaultTableModel(new String[]{"Name", "Value"}, 0);
		contentTable = new JTable(model);
	}
}
