package me.apemanzilla.krist.api;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import me.apemanzilla.krist.api.exceptions.KristCredentialsException;

/**
 * Represents a Krist address, containing currency and/or kristscape names.
 * 
 * @author apemanzilla
 *
 */
public class KristAddress {

	/**
	 * Generates a Krist private key from a password (used to authenticate
	 * actions such as transactions and name purchases)
	 * 
	 * @param password
	 * @return
	 */
	public static String privateKey(String password) {
		return (DigestUtils.sha256Hex("KRISTWALLET" + password) + "-000");
	}

	/**
	 * Generates a V1 address from a private key
	 * 
	 * @deprecated V1 addresses are insecure and should no longer be used. This
	 *             method is only provided for legacy reasons.
	 * 
	 * @param pkey
	 *            The private key
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
	 * Generates a V2 address from a private key
	 * 
	 * @param pkey
	 *            The private key
	 * @return The address
	 */
	public static String getAddressV2(String pkey) {
		String[] protein = { "", "", "", "", "", "", "", "", "" };
		int link = 0;
		String v2 = "k";
		String stick = DigestUtils.sha256Hex(DigestUtils.sha256Hex(pkey));
		for (int i = 0; i <= 9; i++) {
			if (i < 9) {
				protein[i] = stick.substring(0, 2);
				stick = DigestUtils.sha256Hex(DigestUtils.sha256Hex(stick));
			}
		}
		int i = 0;
		while (i <= 8) {
			link = Integer.parseInt(stick.substring(2 * i, 2 + (2 * i)), 16) % 9;
			if (protein[link].equals("")) {
				stick = DigestUtils.sha256Hex(stick);
			} else {
				v2 = v2 + numtochar(Integer.parseInt(protein[link], 16));
				protein[link] = "";
				i++;
			}
		}
		return v2;
	}

	/**
	 * Represents the KristAPI in use by this address
	 */
	public final KristAPI api;

	/**
	 * The raw string address
	 */
	public final String address;

	/**
	 * The private key used for authentication for this address, or {@code null}
	 * if not authenticated.
	 */
	public final String pkey;

	protected KristAddress(KristAPI api, String address) {
		this.api = api;
		this.address = address;
		this.pkey = null;
	}

	protected KristAddress(KristAPI api, String address, String pkey) {
		if (!getAddressV2(pkey).equals(address)) {
			throw new KristCredentialsException("Address and privatekey do not match");
		}

		this.api = api;
		this.address = address;
		this.pkey = pkey;
	}

	/**
	 * Checks whether the KristAddress is authenticated to perform actions on
	 * behalf of the given address
	 */
	public boolean isAuthenticated() {
		return pkey != null;
	}

	/**
	 * Gets the data about the address directly from the syncnode
	 * 
	 * @return the JsonNode containing the address data
	 * @throws UnirestException
	 */
	public JSONObject getAddressData() throws UnirestException {
		return Unirest.get("http://krist.ceriat.net/addresses/" + address).asJson().getBody().getObject()
				.getJSONObject("address");
	}

	/**
	 * Gets the balance of this address
	 * 
	 * @throws UnirestException
	 * @throws JSONException
	 */
	public long getBalance() throws JSONException, UnirestException {
		return getAddressData().getLong("balance");
	}

}
