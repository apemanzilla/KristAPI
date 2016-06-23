package me.apemanzilla.krist.api;

import java.net.MalformedURLException;
import java.net.URL;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Represents an interface to an external Krist syncnode
 * 
 * @author apemanzilla
 *
 */
public class KristAPI {

	/**
	 * The URL of the syncnode in use
	 */
	public final URL syncnode;

	/**
	 * Gets the official syncnode from the <a href=
	 * "https://github.com/BTCTaras/kristwallet/blob/master/staticapi/syncNode">official
	 * repository</a>.
	 * 
	 * @return the URL of the official syncnode
	 * @throws UnirestException
	 *             thrown when an error occurs while connecting
	 * @throws MalformedURLException
	 *             thrown when the URL in the repository is malformed
	 */
	public static URL getOfficialSyncnode() throws UnirestException, MalformedURLException {
		return new URL(Unirest.get("https://raw.githubusercontent.com/BTCTaras/kristwallet/master/staticapi/syncNode")
				.asString().getBody());
	}

	/**
	 * Constructs a new KristAPI using the given syncnode
	 * 
	 * @param syncnode
	 */
	public KristAPI(URL syncnode) {
		this.syncnode = syncnode;
	}

	/**
	 * Constructs a new KristAPI using the official syncnode
	 * 
	 * @throws MalformedURLException
	 *             thrown when the URL of the official syncnode is malformed
	 * @throws UnirestException
	 *             thrown when an error occurs while connecting
	 * @see #getOfficialSyncnode
	 */
	public KristAPI() throws MalformedURLException, UnirestException {
		this.syncnode = getOfficialSyncnode();
	}

	/**
	 * Gets an address
	 */
	public KristAddress getAddress(String address) {
		return new KristAddress(this, address);
	}

	/**
	 * Creates and authenticates an address
	 * 
	 * @param password
	 *            the password for the Krist address (NOT the private key)
	 * @return the authenticated KristAddress
	 */
	public KristAddress login(String password) {
		String pkey = KristAddress.privateKey(password);
		return new KristAddress(this, KristAddress.getAddressV2(pkey), pkey);
	}

}
