package me.apemanzilla.krist.api.types;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import me.apemanzilla.krist.api.KristAPI;
import me.apemanzilla.krist.api.exceptions.MalformedAddressException;

public class KristBlock {

	private KristAddress solver;
	private final Date time;
	private final String hash;
	private final long height;
	private final int value;
	
	public KristBlock(JSONObject json, KristAPI api) {
		this(json.getInt("height"), json.getString("address"), json.getString("hash"), json.getInt("value"), json.getInt("time_unix"), api);
	}
	
	protected KristBlock(long height, String address, String hash, int value, int time, KristAPI api) {
		this.height = height;
		try {
			this.solver = api.getAddress(address);
		} catch (MalformedAddressException e) {
			e.printStackTrace();
		}
		this.hash = hash;
		this.value = value;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time * 1000L);
		this.time = c.getTime();
	}

	public KristAddress getSolver() {
		return solver;
	}

	public Date getTime() {
		return time;
	}

	public String getHash() {
		return hash;
	}

	public long getHeight() {
		return height;
	}

	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.format("Block #%d: %s", height, hash.substring(0, 12));
	}

}
