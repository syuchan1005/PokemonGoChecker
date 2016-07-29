package com.github.syuchan1005.pokego;

import com.pokegoapi.api.player.PlayerProfile;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Created by syuchan on 2016/07/24.
 */
public class PlayerWindow implements Window {
	private JPanel mainPanel;
	private JLabel playerName;
	private JLabel playerLevel;
	private JLabel playerNowXP;
	private JLabel playerMaxXP;
	private JLabel playerTeam;
	private JLabel pokemonCount;
	private JLabel stardustLabel;
	private JLabel pokecoinLabel;

	public PlayerWindow(PlayerProfile profile, int pCount) {
		playerName.setText(profile.getUsername());
		playerLevel.setText(String.valueOf(profile.getStats().getLevel()));
		playerNowXP.setText(String.valueOf(profile.getStats().getPrevLevelXp()));
		playerMaxXP.setText(String.valueOf(profile.getStats().getNextLevelXp()));
		stardustLabel.setText(String.valueOf(profile.getCurrencies().get(PlayerProfile.Currency.STARDUST)));
		pokecoinLabel.setText(String.valueOf(profile.getCurrencies().get(PlayerProfile.Currency.POKECOIN)));
		playerTeam.setText(profile.getTeam().name());
		pokemonCount.setText(pCount + " / " + profile.getPokemonStorage());
	}

	public JLabel getPokemonCount() {
		return pokemonCount;
	}

	@Override
	public JPanel getMainPanel() {
		return mainPanel;
	}
}
