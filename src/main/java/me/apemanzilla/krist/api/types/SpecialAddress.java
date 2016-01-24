package me.apemanzilla.krist.api.types;

import java.util.Calendar;
import java.util.Date;

abstract class SpecialAddress extends KristAddress {
	
	@Override
	public boolean isSpecial() {
		return true;
	}
	
	@Override
	public long getBalance() {
		return 0;
	}
	
	@Override
	public Date getFirstSeen() {
		Calendar c =  Calendar.getInstance();
		c.setTimeInMillis(getFirstSeenUnix() * 1000L);
		return c.getTime();
	}
	
	@Override
	public int getFirstSeenUnix() {
		return 0;
	}
}