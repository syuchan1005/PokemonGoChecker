package com.github.syuchan1005.pokego;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.PtcLogin;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by syuchan on 2016/07/24.
 */
public class Util {
	private static PokemonGo pokemonGo;
	private static String userName, passWord;

	public static void setUserName(String userName) {
		Util.userName = userName;
	}

	public static void setPassWord(String passWord) {
		Util.passWord = passWord;
	}

	public static ImageIcon getPokemonImage(int pokemonID) {
		ImageIcon urlIcon = getImageIcon("http://img.yakkun.com/poke/xy/n" + pokemonID + ".gif");
		return (urlIcon == null) ? null : urlIcon;
	}

	public static ImageIcon getImageIcon(String url) {
		try {
			return new ImageIcon(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JButton getURLIcon(String url) {
		ImageIcon imageIcon = getImageIcon(url);
		return new JButton((imageIcon == null) ? null : imageIcon);
	}

	public static PokemonGo getPokemonGo() {
		if(pokemonGo == null) {
			OkHttpClient http = new OkHttpClient();
			RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth = null;
			try {
				auth = new PtcLogin(http).login(userName, passWord);
				pokemonGo = new PokemonGo(auth, http);
			} catch (LoginFailedException | RemoteServerException e) {
				e.printStackTrace();
			}
		}
		return pokemonGo;
	}

	public static void setLookAndFeel(JFrame jFrame) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ReflectiveOperationException | UnsupportedLookAndFeelException e) {
			JOptionPane.showMessageDialog(jFrame, e.getMessage());
		}
		SwingUtilities.updateComponentTreeUI(jFrame);
	}
}
