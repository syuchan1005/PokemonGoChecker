package com.github.syuchan1005.pokego;

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
	private Pokemon pokemon;
	private ContentWindow instance;
	private static Field pokemonProto, weightKg;

	public ContentWindow(final Pokemon pokemon) {
		instance = this;
		this.pokemon = pokemon;
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
		pokemonWeight.setText(String.valueOf(getWeight(pokemon)));
		pokemonHeight.setText(String.valueOf(pokemon.getHeightM()));
		pokemonHasCandy.setText(String.valueOf(pokemon.getCandy()));
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
	}

	public void errorDialog() {
		JOptionPane.showMessageDialog(instance.getMainPanel(), "Oops! Remote Server Failed", "Error", JOptionPane.ERROR_MESSAGE);
	}

	public Pokemon getPokemon() {
		return pokemon;
	}

	public static float getWeight(Pokemon pokemon) {
		try {
			if(pokemonProto == null) {
				pokemonProto = pokemon.getClass().getDeclaredField("proto");
				pokemonProto.setAccessible(true);
			}
			Object proto = pokemonProto.get(pokemon);
			if(weightKg == null) {
				weightKg = proto.getClass().getDeclaredField("weightKg_");
				weightKg.setAccessible(true);
			}
			return ((float) weightKg.get(proto));
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public JPanel getMainPanel() {
		return mainPanel;
	}
}
