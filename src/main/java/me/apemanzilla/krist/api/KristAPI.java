package me.apemanzilla.krist.api;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;

import me.apemanzilla.krist.api.exceptions.MalformedAddressException;
import me.apemanzilla.krist.api.types.KristAddress;
import me.apemanzilla.utils.net.HTTPGET;

public class KristAPI {

	private final URL syncnode;
	
	public KristAPI(URL syncnode) {
		this.syncnode = syncnode;
	}
	
	public KristAPI() throws IOException {
		syncnode = new URL(HTTPGET.readUrl(new URL("https://raw.githubusercontent.com/BTCTaras/kristwallet/master/staticapi/syncNode")));
	}
	
	public URL getSyncnode() {
		return syncnode;
	}
	
	private static String privateKey(String password) {
		return (DigestUtils.sha256Hex("KRISTWALLET" + password) + "-000");
	}
	
	private static String getAddressV1(String pkey) {
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
	
	private static String getAddressV2(String pkey) {
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
	
	public KristAddress getAddress(String addr) throws MalformedAddressException {
		if (addr.length() != 10) {
			throw new MalformedAddressException("Address should be 10 characters long");
		} else if (addr.charAt(0) == 'k') {
			return new KristAddressV2(addr, this);
		} else {
			// check if address is hex
			try {
				Long.parseLong(addr, 16);
				return new KristAddressV1(addr, this);
			} catch (NumberFormatException e) {
				throw new MalformedAddressException("Address format not recognized");
			}
		}
	}
	
	public KristAddress loginV1(String password) {
		return new KristAddressV1(password.toCharArray(), this);
	}
	
	public KristAddress loginV2(String password) {
		return new KristAddressV2(password.toCharArray(), this);
	}
	
	private class KristAddressV1 extends KristAddress {
		
		private KristAddressV1(String address, KristAPI api) {
			this.api = api;
			this.address = address;
		}
		
		private KristAddressV1(char[] password, KristAPI api) {
			this.api = api;
			this.pkey = privateKey(new String(password));
			this.address = getAddressV1(pkey);
		}
	}
	
	private class KristAddressV2 extends KristAddress {
		private KristAddressV2(String address, KristAPI api) {
			this.api = api;
			this.address = address;
		}
		
		private KristAddressV2(char[] password, KristAPI api) {
			this.api = api;
			this.pkey = privateKey(new String(password));
			this.address = getAddressV2(pkey);
		}
	}
	
}
