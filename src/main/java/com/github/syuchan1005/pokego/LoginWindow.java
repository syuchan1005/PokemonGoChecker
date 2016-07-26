package com.github.syuchan1005.pokego;

import com.pokegoapi.exceptions.LoginFailedException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by syuchan on 2016/07/24.
 */
public class LoginWindow {
	private JPanel mainPanel;
	private JTextField userText;
	private JPasswordField passText;
	private JButton loginButton;
	private JComboBox<Enum> comboBox1;

	public LoginWindow(final MainWindow mainWindow) {
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Util.LoginType type = (Util.LoginType) comboBox1.getSelectedItem();
				Util.setType(type);
				Util.setUserName(getUserText());
				Util.setPassWord(getPassText());
				try {
					mainWindow.addComponent();
				} catch (LoginFailedException e1) {
					JOptionPane.showMessageDialog(mainWindow, "Login Failed");
				}
			}
		});
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public String getUserText() {
		return userText.getText();
	}

	public String getPassText() {
		return String.valueOf(passText.getPassword());
	}

	private void createUIComponents() {
		comboBox1 = new JComboBox<Enum>(Util.LoginType.values());
	}
}
