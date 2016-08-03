package com.github.syuchan1005.pokego;

import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by syuchan on 2016/07/24.
 */
public class LoginWindow {
	private JPanel mainPanel;
	private JTextField userText;
	private JPasswordField passText;
	private JButton loginButton;
	private JComboBox<Enum> comboBox1;
	private JPanel ptcLoginPanel;

	public LoginWindow(final MainWindow mainWindow) {
		loginButton.addActionListener(e -> {
			Util.LoginType type = (Util.LoginType) comboBox1.getSelectedItem();
			Util.setType(type);
			Util.setUserName(getUserText());
			Util.setPassWord(getPassText());
			try {
				mainWindow.addComponent();
			} catch (LoginFailedException |RemoteServerException e1) {
				JOptionPane.showMessageDialog(mainWindow, "Login Failed", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		comboBox1.addActionListener(e -> {
			JComboBox<Enum> comboBox = (JComboBox<Enum>) e.getSource();
			ptcLoginPanel.setVisible(((Util.LoginType) comboBox.getSelectedItem()) == Util.LoginType.PTC);
		});
		passText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() != KeyEvent.VK_ENTER) return;
				loginButton.doClick();
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
