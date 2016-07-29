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
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private JLabel pokemonType;
	private JLabel pokemonHP;
	private JLabel pokemonWeight;
	private JLabel pokemonHeight;
	private JLabel pokemonHasCandy;
	private JButton renameButton;
	private JButton transferButton;
	private JButton powerUpButton;
	private JButton evolveButton;
	private JLabel powerUpStardustLabel;
	private JLabel powerUpCandyLabel;
	private JLabel evolveCandyLabel;
	private JLabel pokemonUpgrades;
	private Pokemon pokemon;
	private ContentWindow instance;

	public ContentWindow(final Pokemon pokemon) {
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
		pokemonType.setText(PokemonType.ArraytoString(pokemonEnum.getType()));
		pokemonHP.setText(pokemon.getStamina() + " / " + pokemon.getMaxStamina());
		pokemonWeight.setText(String.valueOf(pokemonData.getWeightKg()));
		pokemonHeight.setText(String.valueOf(pokemon.getHeightM()));
		pokemonHasCandy.setText(String.valueOf(pokemon.getCandy()));
		pokemonUpgrades.setText(String.valueOf(pokemonData.getNumUpgrades()));
		powerUpStardustLabel.setText("Stardust: " + Util.getPowerUPStardust(pokemonData.getNumUpgrades()));
		powerUpCandyLabel.setText("Candy: " + Util.getPowerUPCandy(pokemonData.getNumUpgrades()));
		evolveCandyLabel.setText("Candy: " + pokemon.getCandiesToEvolve());
		renameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog("PokemonName:");
				if(name == null) return;
				try {
					pokemon.renamePokemon(name);
					instance.pokemonNickname.setText("(" + name + ")");
				} catch (LoginFailedException e1) {
					errorDialog();
				} catch (RemoteServerException e1) {
					errorDialog();
				}
			}
		});
		transferButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialog = JOptionPane.showConfirmDialog(instance.getMainPanel(),
						"You can't take it back after it's transferred to the Professor.\n" +
						"Do you really want to transfer Bulbasaur to the Professor?");
				if(dialog != 0) return;
				try {
					pokemon.transferPokemon();
					MainWindow.getInstance().updatePokemons();
					MainWindow.getInstance().addTabs();
				} catch (LoginFailedException e1) {
					errorDialog();
				} catch (RemoteServerException e1) {
					errorDialog();
				}
			}
		});
		powerUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		evolveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

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
}
