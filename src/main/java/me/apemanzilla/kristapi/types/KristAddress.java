package me.apemanzilla.kristapi.types;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import me.apemanzilla.kristapi.KristAPI;
import me.apemanzilla.kristapi.exceptions.InvalidKristKeyException;
import me.apemanzilla.kristapi.exceptions.SyncnodeDownException;
import me.apemanzilla.utils.net.HTTPGET;

public abstract class KristAddress {

	public abstract String getAddress();

	public long getBalance() throws SyncnodeDownException, MalformedURLException {
		try {
			String got = HTTPGET.readUrl(new URL(KristAPI.getSyncNode(), "?getbalance=" + this.getAddress()));
			got = got.replaceAll("[^\\d.]", "");
			return Long.parseLong(got);
		} catch (MalformedURLException e) {
			throw e;
		} catch (IOException e) {
			throw new SyncnodeDownException("Krist is down; go yell at Taras a bit.");
		}
	}
	
	public static class V1 extends KristAddress {
		
		private String address;
		private String pkey;
		
		public V1(String address) {
			this.address = address;
		}
		
		public V1(String password, String address) throws InvalidKristKeyException {
			String pkey = KristAPI.privateKey(password);
			if (KristAPI.getAddressV1(pkey) == address) {
				this.address = address;
				this.pkey = pkey;
			} else {
				throw new InvalidKristKeyException();
			}
		}

		@Override
		public String getAddress() {
			return address;
		}
	}
	
	public static class V2 extends KristAddress {
		
		private String address;
		private String pkey;
		
		public V2(String address) {
			this.address = address;
		}
		
		public V2(String password, String address) throws InvalidKristKeyException {
			String pkey = KristAPI.privateKey(password);
			if (KristAPI.getAddressV2(pkey) == address) {
				this.address = address;
				this.pkey = pkey;
			} else {
				throw new InvalidKristKeyException();
			}
		}

		@Override
		public String getAddress() {
			return address;
		}
	}
}
