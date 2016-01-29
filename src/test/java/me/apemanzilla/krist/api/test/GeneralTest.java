package me.apemanzilla.krist.api.test;

import me.apemanzilla.krist.api.KristAPI;
import me.apemanzilla.krist.api.exceptions.SyncnodeDownException;
import me.apemanzilla.utils.net.HTTPErrorException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class GeneralTest {
    private KristAPI kapi;

    @Before
    public void setup() throws HTTPErrorException, IOException, URISyntaxException {
        kapi = new KristAPI();
    }

    @Test
    public void workTest() {
        try {
            // we can't really do testing here
            // so let's just make sure it runs
            kapi.getWork();
        } catch (SyncnodeDownException e) {
            // still okay
        } catch (Exception e) {
            // Not okay!
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
