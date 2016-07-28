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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

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
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginButton.setEnabled(false);
				Util.LoginType type = (Util.LoginType) comboBox1.getSelectedItem();
				Util.setType(type);
				Util.setUserName(getUserText());
				Util.setPassWord(getPassText());
				try {
					mainWindow.addComponent();
				} catch (LoginFailedException e1) {
					JOptionPane.showMessageDialog(mainWindow, "Login Failed", "Error", JOptionPane.ERROR_MESSAGE);
					loginButton.setEnabled(true);
				}
			}
		});
		comboBox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Enum> comboBox = (JComboBox<Enum>) e.getSource();
				ptcLoginPanel.setVisible(((Util.LoginType) comboBox.getSelectedItem()) == Util.LoginType.PTC);
			}
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
