package me.apemanzilla.kristapi.types;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import me.apemanzilla.kristapi.exceptions.InvalidDataFormatException;

public class KristTransaction {

	private Date time;
	private KristAddress from;
	private KristAddress to;
	private long amount;
	
	public KristTransaction(String txdata, String source) throws InvalidDataFormatException {
		if (txdata.length() != 31) {
			throw new InvalidDataFormatException();
		} else {
			String date = txdata.substring(0, 12);
			txdata = txdata.substring(12);
			try {
				DateFormat format = new SimpleDateFormat("MMM dd HH:mm", Locale.US);
				time = format.parse(date);
				// adjust time to local timezone
				TimeZone tz = TimeZone.getDefault();
				time.setTime(time.getTime() + (tz.getOffset(new Date().getTime()) - 3600000));
			} catch (ParseException e) {
				throw new InvalidDataFormatException();
			}
			String addr = txdata.substring(0, 10);
			txdata = txdata.substring(10);
			KristAddress ka;
			switch (addr) {
			case "N/A(Mined)": {
				ka = new KristAddress.BlockRewardAddress();
				break;
			}
			case "N/A(Names)": {
				ka = new KristAddress.NamePurchaseAddress();
				break;
			}
			default: {
				ka = KristAddress.auto(addr);
				break;
			}
			}
			String amtstr = txdata.substring(0,9);
			amount = Long.parseLong(amtstr);
			if (amount > 0) {
				from = ka;
				to = KristAddress.auto(source);
			} else {
				amount *= -1;
				from = KristAddress.auto(source);
				to = ka;
			}
		}
	}

	public Date getTime() {
		return time;
	}

	public KristAddress getFrom() {
		return from;
	}

	public KristAddress getTo() {
		return to;
	}

	public long getAmount() {
		return amount;
	}
}
