package com.github.syuchan1005.pokego;

import com.pokegoapi.util.Log;
import com.pokegoapi.util.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by syuchan on 2016/07/26.
 */
public class PLogger implements Logger {
	private MainWindow mainWindow;

	public PLogger(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void v(String tag, String msg) {
		this.log(2, tag, msg, (Throwable)null);
	}

	public void v(String tag, String msg, Throwable tr) {
		this.log(2, tag, msg, tr);
	}

	public void d(String tag, String msg) {
		this.log(3, tag, msg, (Throwable)null);
	}

	public void d(String tag, String msg, Throwable tr) {
		this.log(3, tag, msg, tr);
	}

	public void i(String tag, String msg) {
		this.log(4, tag, msg, (Throwable)null);
	}

	public void i(String tag, String msg, Throwable tr) {
		this.log(4, tag, msg, tr);
	}

	public void w(String tag, String msg) {
		this.log(5, tag, msg, (Throwable)null);
	}

	public void w(String tag, String msg, Throwable tr) {
		this.log(5, tag, msg, tr);
	}

	public void w(String tag, Throwable tr) {
		this.log(5, tag, (String)null, tr);
	}

	public void e(String tag, String msg) {
		this.log(6, tag, msg, (Throwable)null);
	}

	public void e(String tag, String msg, Throwable tr) {
		this.log(6, tag, msg, tr);
	}

	public void wtf(String tag, String msg) {
		this.log(7, tag, msg, (Throwable)null);
	}

	public void wtf(String tag, Throwable tr) {
		this.log(7, tag, (String)null, tr);
	}

	public void wtf(String tag, String msg, Throwable tr) {
		this.log(7, tag, msg, tr);
	}

	private static Pattern getUser = Pattern.compile("Get user to go to:(.*) and enter code:(.*)");
	private static String[] levelName = {"V", "D", "I", "W", "E", "A"};
	public void log(int level, String tag, String msg, Throwable tr) {
		String prefix = levelName[level - 2];
		String body = "";
		if(msg != null) body = body + msg;
		if(tr != null) {
			StringWriter result = new StringWriter();
			PrintWriter printWriter = new PrintWriter(result);
			tr.printStackTrace(printWriter);
			body = body + "\n" + result.toString();
		}
		Matcher matcher = getUser.matcher(body);
		if(matcher.find()) {
			mainWindow.showInputCode(matcher.group(1), matcher.group(2));
		} else if(body.startsWith("Got token:")) {
			mainWindow.authComplete();
		}
		System.out.println(prefix + "/" + tag + ": " + body);
	}
}
