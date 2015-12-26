package me.apemanzilla.kristapi;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;

import me.apemanzilla.utils.net.HTTPGET;

/**
 * General/miscellaneous methods for Krist Unless otherwise specified, all
 * methods here will block until completion
 * 
 * @author apemanzilla
 *
 */
public class KristAPI {

	private static URL syncnode;
	
	static {
		try {
			syncnode = new URL(HTTPGET.readUrl(new URL("https://raw.githubusercontent.com/BTCTaras/kristwallet/master/staticapi/syncNode")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the syncnode URL from the official GitHub repository
	 * @return The syncnode URL
	 */
	public static URL getSyncNode() {
		return syncnode;
	}

	/**
	 * Generates the key for an address
	 * @param password The password
	 * @return The private key
	 */
	public static String privateKey(String password) {
		return (DigestUtils.sha256Hex("KRISTWALLET" + password) + "-000");
	}

	/**
	 * Generates a v1 address from a private key
	 * @param pkey The private key
	 * @return The address
	 */
	public static String getAddressV1(String pkey) {
		return DigestUtils.sha256Hex(pkey).substring(0, 10);
	}
	
	private static char numtochar(int inp) {
		for (int i = 6; i <= 251; i += 7) {
			if (inp <= i) {
				if (i <= 69) {
					return (char) ('0' + (i - 6) / 7);
				}
				return (char) ('a' + ((i - 76) / 7));
			}
		}
		return 'e';
	}
	
	/**
	 * Generates a v2 address from a private key
	 * @param pkey The private key
	 * @return The address
	 */
	public static String getAddressV2(String pkey) {
		String[] protein = {"", "", "", "", "", "", "", "", ""};
		int link = 0;
		String v2 = "k";
		String stick = DigestUtils.sha256Hex(DigestUtils.sha256Hex(pkey));
		for (int i = 0; i <= 9; i++) {
			if (i < 9) {
				protein[i] = stick.substring(0,2);
				stick = DigestUtils.sha256Hex(DigestUtils.sha256Hex(stick));
			}
		}
		int i = 0;
		while (i <= 8) {
			link = Integer.parseInt(stick.substring(2*i,2+(2*i)),16) % 9;
			if (protein[link].equals("")) {
				stick = DigestUtils.sha256Hex(stick);
			} else {
				v2 = v2 + numtochar(Integer.parseInt(protein[link],16));
				protein[link] = "";
				i++;
			}
		}
		return v2;
	}
}
