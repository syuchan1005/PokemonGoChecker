package com.github.syuchan1005.pokego;

import com.pokegoapi.api.player.PlayerProfile;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Created by syuchan on 2016/07/24.
 */
public class PlayerWindow {
	private JPanel mainPanel;
	private JLabel playerName;
	private JLabel playerLevel;
	private JLabel playerNowXP;
	private JLabel playerMaxXP;
	private JLabel playerTeam;

	public PlayerWindow(PlayerProfile profile) {
		playerName.setText(profile.getUsername());
		playerLevel.setText(String.valueOf(profile.getStats().getLevel()));
		playerNowXP.setText(String.valueOf(profile.getStats().getPrevLevelXp()));
		playerMaxXP.setText(String.valueOf(profile.getStats().getNextLevelXp()));
		playerTeam.setText(profile.getTeam().name());
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
}
