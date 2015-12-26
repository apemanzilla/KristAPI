package me.apemanzilla.kristapi;

import java.net.MalformedURLException;

import me.apemanzilla.kristapi.exceptions.SyncnodeDownException;
import me.apemanzilla.kristapi.types.KristAddress;

public class ManualTest {

	public static void main(String[] args) throws SyncnodeDownException, MalformedURLException {
		System.out.println(KristAPI.getSyncNode().toString());
		KristAddress test = new KristAddress.V2("k5ztameslf");
		System.out.println(test.getBalance());
		System.out.println(test.getBalance());
	}

}
