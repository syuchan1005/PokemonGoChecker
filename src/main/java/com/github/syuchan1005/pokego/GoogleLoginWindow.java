package com.github.syuchan1005.pokego;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Created by syuchan on 2016/07/26.
 */
public class GoogleLoginWindow {
	private static GoogleLoginWindow googleLoginWindow;
	private JPanel mainPanel;
	private JTextField urlText;
	private JTextField codeText;

	private GoogleLoginWindow() {}

	public static GoogleLoginWindow getInstance() {
		if(googleLoginWindow == null) googleLoginWindow = new GoogleLoginWindow();
		return googleLoginWindow;
	}

	public static JPanel getMainPanel(String url, String code) {
		GoogleLoginWindow window = getInstance();
		window.urlText.setText(url);
		window.codeText.setText(code);
		return window.mainPanel;
	}
}
