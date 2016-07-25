package com.github.syuchan1005.pokego;

import com.pokegoapi.api.pokemon.Pokemon;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Image;
import java.lang.reflect.Field;

/**
 * Created by syuchan on 2016/07/24.
 */
public class ContentWindow {
	private JPanel mainPanel;
	private JLabel pokemonImage;
	private JLabel pokemonName;
	private JLabel pokemonCP;
	private JLabel pokemonType;
	private JLabel pokemonHP;
	private JLabel pokemonWeight;
	private JLabel pokemonHeight;
	private JLabel pokemonHasCandy;
	private static Field pokemonProto, weightKg;

	public ContentWindow(Pokemon pokemon) {
		PokemonEnum pokemonEnum = PokemonEnum.getPokemonEnumByid(pokemon.getPokemonId().getNumber());
		ImageIcon imageIcon = Util.getPokemonImage(pokemon.getPokemonId().getNumber());
		if(imageIcon == null) {
			pokemonImage.setText("NO IMAGE");
		} else {
			imageIcon.setImage(imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
			pokemonImage.setIcon(imageIcon);
			pokemonImage.setText("");
		}
		pokemonName.setText(PokemonEnum.getPokemonEnumByid(pokemon.getPokemonId().getNumber()).getJpName());
		pokemonCP.setText("CP: " + pokemon.getCp());
		pokemonType.setText(PokemonType.ArraytoString(pokemonEnum.getType()));
		pokemonHP.setText(pokemon.getStamina() + " / " + pokemon.getMaxStamina());
		pokemonWeight.setText(String.valueOf(getWeight(pokemon)));
		pokemonHeight.setText(String.valueOf(pokemon.getHeightM()));
		pokemonHasCandy.setText(String.valueOf(pokemon.getCandy()));
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

	public JPanel getMainPanel() {
		return mainPanel;
	}
}
