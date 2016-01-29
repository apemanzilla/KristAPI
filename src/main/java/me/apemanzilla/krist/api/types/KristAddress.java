package me.apemanzilla.krist.api.types;

import me.apemanzilla.krist.api.KristAPI;
import me.apemanzilla.krist.api.exceptions.InvalidNonceException;
import me.apemanzilla.krist.api.exceptions.KristException;
import me.apemanzilla.krist.api.exceptions.SyncnodeDownException;
import me.apemanzilla.utils.net.HTTPErrorException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class KristAddress {

	protected KristAPI api;

	protected String address;

	protected String pkey;

	public String getAddress() {
		return address;
	}

	private static void checkResponse(JSONObject json) throws KristException {
		if (!json.getBoolean("ok")) {
			throw new KristException("address request was not ok!");
		}
	}

	private JSONObject getAddressInfo() throws KristException {
		try {
			String response = api.getClient().get(new URL(api.getSyncnode(),String.format("address/%s", address)).toURI());
			JSONObject json = new JSONObject(response);
			checkResponse(json);
			return json;
		} catch (IOException e) {
			throw new SyncnodeDownException();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		} catch (HTTPErrorException e) {
			return null;
		}
	}
	
	public long getBalance() throws KristException {
		JSONObject json = getAddressInfo();
		return (json != null ? json.getLong("balance") : 0);
	}
	
	public long getTotalIn() throws KristException {
		JSONObject json = getAddressInfo();
		return (json != null ? json.getLong("totalin") : 0);
	}
	
	public long getTotalOut() throws KristException {
		JSONObject json = getAddressInfo();
		return (json != null ? json.getLong("totalout") : 0);
	}

	public Date getFirstSeen() throws KristException {
		JSONObject json = getAddressInfo();
		if (json == null) {
			return Calendar.getInstance().getTime();
		}
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(json.getInt("firstseen_unix") * 1000L);
		return c.getTime();
	}

	/**
	 * Lignum wanted this
	 *
	 * I wanted this. - Lignum
	 */
	public int getFirstSeenUnix() throws KristException {
		JSONObject json = getAddressInfo();
		return (json != null ? json.getInt("firstseen_unix") : (int) Calendar.getInstance().getTime().getTime() / 1000);
	}
	
	public boolean submitBlock(String nonce) throws InvalidNonceException, SyncnodeDownException {
		if (nonce.length() < 1) {
			throw new InvalidNonceException("Nonce must be at least 1 character long");
		} else if (nonce.length() > 12) {
			throw new InvalidNonceException("Nonce must not be longer than 12 characters");
		}
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("address", address);
		payload.put("nonce", nonce);
		try {
			String response = api.getClient().post(new URL(api.getSyncnode(), "submit").toURI(), payload);
			JSONObject json = new JSONObject(response);
			return json.getBoolean("success");
		} catch (IOException e) {
			throw new SyncnodeDownException();
		} catch (HTTPErrorException e) {
			return false;
		} catch (URISyntaxException e) {
			return false;
		}
	}
	
	/**
	 * Checks whether the given address is 'special' - i.e. mining reward
	 * address
	 * 
	 * @return
	 */
	public boolean isSpecial() {
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("Address %s", getAddress());
	}
}
