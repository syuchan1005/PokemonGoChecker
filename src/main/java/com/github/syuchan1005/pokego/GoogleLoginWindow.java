package com.github.syuchan1005.pokego;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Created by syuchan on 2016/07/26.
 */
public class GoogleLoginWindow {
	private static GoogleLoginWindow googleLoginWindow;
	private JPanel mainPanel;
	private JTextField urlText;
	private JTextField codeText;

	private GoogleLoginWindow() {
	}

	public static GoogleLoginWindow getInstance() {
		if(googleLoginWindow == null) {
			googleLoginWindow = new GoogleLoginWindow();
			FocusAdapter focusAdapter = new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					((JTextComponent) e.getComponent()).selectAll();
				}
			};
			googleLoginWindow.urlText.addFocusListener(focusAdapter);
			googleLoginWindow.codeText.addFocusListener(focusAdapter);
		}
		return googleLoginWindow;
	}

	public static JPanel getMainPanel(String url, String code) {
		GoogleLoginWindow window = getInstance();
		window.urlText.setText(url);
		window.codeText.setText(code);
		return window.mainPanel;
	}
}
