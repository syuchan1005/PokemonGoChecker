package com.github.syuchan1005.pokego;

import POGOProtos.Data.PlayerDataOuterClass;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

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

	public PlayerWindow(PlayerProfile profile, int pCount) throws LoginFailedException, RemoteServerException {
		PlayerDataOuterClass.PlayerData playerData = profile.getPlayerData();
		playerName.setText(playerData.getUsername());
		playerLevel.setText(String.valueOf(profile.getStats().getLevel()));
		playerNowXP.setText(String.valueOf(profile.getStats().getPrevLevelXp()));
		playerMaxXP.setText(String.valueOf(profile.getStats().getNextLevelXp()));
		stardustLabel.setText(String.valueOf(profile.getCurrencies().get(PlayerProfile.Currency.STARDUST)));
		pokecoinLabel.setText(String.valueOf(profile.getCurrencies().get(PlayerProfile.Currency.POKECOIN)));
		playerTeam.setText(playerData.getTeam().name());
		pokemonCount.setText(pCount + " / " + playerData.getMaxPokemonStorage());
	}

	public JLabel getPokemonCount() {
		return pokemonCount;
	}

	@Override
	public JPanel getMainPanel() {
		return mainPanel;
	}
}
