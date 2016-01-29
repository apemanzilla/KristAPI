package me.apemanzilla.krist.api.types;

import me.apemanzilla.krist.api.KristAPI;
import me.apemanzilla.krist.api.exceptions.MalformedAddressException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class KristBlock {

	private KristAddress solver;
	private final Date time;
	private final String hash;
	private final long height;
	private final int value;
	private final long timeUnix;

	public KristBlock(JSONObject json, KristAPI api) {
		this(json.getInt("height"), json.getString("address"), json.getString("hash"), json.getInt("value"), json.getInt("time_unix"), api);
	}
	
	protected KristBlock(long height, String address, String hash, int value, int ptime, KristAPI api) {
		this.timeUnix = ptime;
		this.height = height;
		try {
			this.solver = api.getAddress(address);
		} catch (MalformedAddressException e) {
			e.printStackTrace();
		}
		this.hash = hash;
		this.value = value;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(ptime * 1000L);
		this.time = c.getTime();
	}

	public KristAddress getSolver() {
		return solver;
	}

	public Date getTime() {
		return time;
	}

	public long getTimeUnix() {
		return timeUnix;
	}

	public String getHash() {
		return hash;
	}

	public String getShortHash() {
		return hash.substring(0, 12);
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
