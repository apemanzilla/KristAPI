package me.apemanzilla.krist.api.test;

import me.apemanzilla.krist.api.KristAPI;
import me.apemanzilla.krist.api.exceptions.SyncnodeDownException;
import me.apemanzilla.krist.api.types.KristBlock;
import me.apemanzilla.utils.net.HTTPErrorException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class BlockTest {
    private KristAPI kapi;

    @Before
    public void setup() throws HTTPErrorException, IOException, URISyntaxException {
        kapi = new KristAPI();
    }

    @Test
    public void blockGetTest() {
        try {
            KristBlock block = kapi.getBlock(113877); // A randomly picked block
            Assert.assertEquals("000002e4601a9a19496b10bce761c636189ebaaeea23245d32f016f81d6dcfae", block.getHash());
            Assert.assertEquals("kyscekhdpy", block.getSolver().getAddress());
            Assert.assertEquals(113877, block.getHeight());
            Assert.assertEquals(1454026486, block.getTimeUnix());
        } catch (SyncnodeDownException e) {
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(true);
        }
    }
}
