package me.apemanzilla.krist.api.test;

import static org.junit.Assert.*;

import org.junit.Test;

import me.apemanzilla.krist.api.KristAddress;

public class TestKristAddressGen {

	@Test
	public void testGetAddressV2() {
		assertEquals("kxxk8invlf", KristAddress.getAddressV2(KristAddress.privateKey("a")));
		assertEquals("kv8wgubjl1", KristAddress.getAddressV2(KristAddress.privateKey("b")));
		assertEquals("k3c6ygr2c8", KristAddress.getAddressV2(KristAddress.privateKey("abc")));
		assertEquals("kch1fzhp0c", KristAddress.getAddressV2(KristAddress.privateKey("this is a test with a really really REALLY long password")));
	}

}
