package me.apemanzilla.kristapi.types;

import java.io.IOException;
import java.net.URL;

import me.apemanzilla.kristapi.KristAPI;
import me.apemanzilla.kristapi.exceptions.InvalidDataFormatException;
import me.apemanzilla.kristapi.exceptions.SyncnodeDownException;
import me.apemanzilla.utils.net.HTTPGET;

/**
 * Represents an address used by Krist. To create a new KristAddress, please use the subclasses {@link V1} or {@link V2}.
 * @author apemanzilla
 *
 */
public abstract class KristAddress {

	/**
	 * Gets the address as a string.
	 * @return The address.
	 */
	public abstract String getAddress();
	
	/**
	 * Returns whether the KristAddress in question is a special address (e.g. mining reward distributor) or not.
	 * @return Whether the address is special or not.
	 */
	public boolean isSpecialAddress() {
		return false;
	}
	
	/**
	 * Creates a KristAddress object from a Krist address as a String.
	 * @param address
	 * @return
	 */
	public static KristAddress auto(String address) {
		if (address.substring(0, 1) == "k") {
			return new KristAddress.V2(address);
		} else {
			return new KristAddress.V1(address);
		}
	}
	
	/**
	 * Gets the balance present in a KristAddress.
	 * @return The balance stored in the KristAddress.
	 * @throws SyncnodeDownException Thrown when the syncnode is inaccessible for any reason
	 */
	public long getBalance() throws SyncnodeDownException {
		try {
			String got = HTTPGET.readUrl(new URL(KristAPI.getSyncNode(), "?getbalance=" + this.getAddress()));
			got = got.replaceAll("[^\\d.]", "");
			return Long.parseLong(got);
		} catch (IOException e) {
			throw new SyncnodeDownException();
		}
	}
	
	/**
	 * Gets an array of transactions to and from a KristAddress
	 * @return The array of transactions
	 * @throws SyncnodeDownException Thrown when the syncnode is inaccessible for any reason
	 * @throws InvalidDataFormatException Thrown when the data cannot be parsed
	 */
	public KristTransaction[] getTransactions() throws SyncnodeDownException, InvalidDataFormatException {
		try {
			String got = HTTPGET.readUrl(new URL(KristAPI.getSyncNode(), "?listtx=" + this.getAddress()));
			KristTransaction[] out = new KristTransaction[(int) Math.ceil((double) got.length() / 31)];
			int i = 0;
			while (got.length() >= 31) {
				String txdata = got.substring(0,31);
				got = got.substring(31);
				out[i] = new KristTransaction(txdata, this.getAddress());
				i++;
			}
			return out;
		} catch (IOException e) {
			throw new SyncnodeDownException();
		}
	}
	
	/**
	 * Submits a nonce to the server; if the hash of the nonce is below the work, the address will be credited an award.
	 * @param nonce The nonce to submit to the server
	 * @return {@code true} when the hash is low enough for a reward to be credited, {@code false} otherwise.
	 * @throws SyncnodeDownException Thrown when the syncnode is inaccessible for any reason
	 */
	public boolean submitBlock(String nonce) throws SyncnodeDownException {
		try {
			return HTTPGET.readUrl(new URL(KristAPI.getSyncNode(), "?submitblock&address=" + this.getAddress() + "&nonce=" + nonce)).contains("solved");
		} catch (IOException e) {
			throw new SyncnodeDownException();
		}
		
	}
	
	/**
	 * Used to create a new KristAddress using the original (v1) specification.
	 * @author apemanzilla
	 *
	 */
	public static class V1 extends KristAddress {
		
		private String address;
		private String pkey;
		
		/**
		 * Creates an address using the original (v1) specification.
		 * @param address The address as a string.
		 */
		public V1(String address) {
			this.address = address;
		}
		
		/**
		 * Creates an address using the original (v1) specification from a password.
		 * @param password The address's password
		 */
		public V1(char[] password) {
			this.pkey = KristAPI.privateKey(new String(password));
			this.address = KristAPI.getAddressV1(pkey);
		}

		@Override
		public String getAddress() {
			return address;
		}
	}
	
	/**
	 * Used to create a new KristAddress using the current (v2) specification.
	 * @author apemanzilla
	 *
	 */
	public static class V2 extends KristAddress {
		
		private String address;
		private String pkey;
		
		/**
		 * Creates an address using the original (v2) specification.
		 * @param address The address as a string.
		 */
		public V2(String address) {
			this.address = address;
		}
		
		/**
		 * Creates an address using the original (v2) specification from a password.
		 * @param password The address's password
		 */
		public V2(char[] password) {
			this.pkey = KristAPI.privateKey(new String(password));
			this.address = KristAPI.getAddressV2(pkey);
		}

		@Override
		public String getAddress() {
			return address;
		}
	}
	
	/**
	 * Represents a 'special' KristAddress, such as the 'address' that mining rewards are sent from. Used internally only.
	 * @author apemanzilla
	 *
	 */
	private static abstract class SpecialAddress extends KristAddress {

		public abstract String getAddress();
		
		@Override
		public boolean isSpecialAddress() {
			return true;
		}
		
		@Override
		public long getBalance() {
			return 0;
		}
		
	}

	/**
	 * Represents the address that distributes rewards for mining. Used within the Krist API only.
	 * @author apemanzilla
	 *
	 */
	static class BlockRewardAddress extends SpecialAddress {
		
		@Override
		public String getAddress() {
			return "(Mined)";
		}
		
	}
	
	/**
	 * Represents the address that Krist is sent to when purchasing a name for KristScape. Used within the Krist API only.
	 * @author apemanzilla
	 *
	 */
	static class NamePurchaseAddress extends SpecialAddress {
		
		@Override
		public String getAddress() {
			return "(Names)";
		}
		
	}
}
