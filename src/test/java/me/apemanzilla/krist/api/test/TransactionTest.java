package me.apemanzilla.krist.api.test;

import me.apemanzilla.krist.api.KristAPI;
import me.apemanzilla.krist.api.exceptions.SyncnodeDownException;
import me.apemanzilla.krist.api.types.KristTransaction;
import me.apemanzilla.utils.net.HTTPErrorException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class TransactionTest {
    private KristAPI kapi;

    @Before
    public void setup() throws HTTPErrorException, IOException, URISyntaxException {
        kapi = new KristAPI();
    }

    @Test
    public void transactionGetTest() {
        try {
            KristTransaction transaction = kapi.getTransaction(144271);
            Assert.assertEquals(144271L, transaction.getID());
            Assert.assertTrue(transaction.getSender().isSpecial());
            Assert.assertEquals("Mining Reward", transaction.getSender().getAddress());
            Assert.assertEquals("kyscekhdpy", transaction.getRecipient().getAddress());
            Assert.assertEquals(12L, transaction.getValue());
            Assert.assertEquals(1454026486L, transaction.getTimeUnix());
        } catch (SyncnodeDownException e) {
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
