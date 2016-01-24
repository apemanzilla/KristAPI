package me.apemanzilla.krist.api.types;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import me.apemanzilla.krist.api.KristAPI;
import me.apemanzilla.krist.api.exceptions.SyncnodeDownException;
import me.apemanzilla.utils.net.HTTPGET;

public abstract class KristAddress {

	protected KristAPI api;

	protected String address;

	protected String pkey;

	public String getAddress() {
		return address;
	}

	public long getBalance() throws SyncnodeDownException {
		try {
			String response = HTTPGET.readUrl(new URL(api.getSyncnode(), String.format("address/%s", address)));
			JSONObject json = new JSONObject(response);
			return json.getLong("balance");
		} catch (IOException e) {
			e.printStackTrace();
			throw new SyncnodeDownException();
		}
	}

	public Date getFirstSeen() throws SyncnodeDownException {
		try {
			String response = HTTPGET.readUrl(new URL(api.getSyncnode(), String.format("address/%s", address)));
			JSONObject json = new JSONObject(response);
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(json.getInt("firstseen_unix") * 1000L);
			return c.getTime();
		} catch (IOException e) {
			e.printStackTrace();
			throw new SyncnodeDownException();
		}
	}

	/**
	 * Lignum wanted this
	 */
	public int getFirstSeenUnix() throws SyncnodeDownException {
		try {
			String response = HTTPGET.readUrl(new URL(api.getSyncnode(), String.format("address/%s", address)));
			JSONObject json = new JSONObject(response);
			return json.getInt("firstseen_unix");
		} catch (IOException e) {
			e.printStackTrace();
			throw new SyncnodeDownException();
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
}
