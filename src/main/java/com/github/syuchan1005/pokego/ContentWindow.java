package com.github.syuchan1005.pokego;

import com.pokegoapi.api.pokemon.Pokemon;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Image;

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

	public ContentWindow(Pokemon pokemon) {
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
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
}
