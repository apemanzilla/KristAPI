package me.apemanzilla.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HTTPGET {
	public static String readUrl(URL link) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(link.openStream()));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = in.readLine()) != null) {
			out.append(line + "\n");
		}
		return out.toString();
	}
}