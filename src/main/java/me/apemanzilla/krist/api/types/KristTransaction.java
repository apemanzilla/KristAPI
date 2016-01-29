package me.apemanzilla.krist.api.types;

import me.apemanzilla.krist.api.KristAPI;
import me.apemanzilla.krist.api.exceptions.KristException;
import me.apemanzilla.krist.api.exceptions.MalformedAddressException;
import me.apemanzilla.krist.api.exceptions.SyncnodeDownException;
import me.apemanzilla.utils.net.HTTPErrorException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class KristTransaction {
    protected KristAPI api;
    protected long id;
    protected JSONObject transactionData;

    public KristTransaction(KristAPI api, long id) throws KristException {
        this.id = id;
        this.api = api;
        transactionData = getTransactionData();
    }

    private static void checkResponse(JSONObject json) throws KristException {
        if (!json.getBoolean("ok")) {
            throw new KristException("transaction request was not ok!");
        }
    }

    private JSONObject getTransactionData() throws KristException {
        try {
            String response = api.getClient().get(new URL(api.getSyncnode(), "transaction/" + id).toURI());
            JSONObject obj = new JSONObject(response);
            checkResponse(obj);
            return obj;
        } catch (IOException e) {
            throw new SyncnodeDownException();
        } catch (HTTPErrorException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public long getID() {
        return id;
    }

    public KristAddress getRecipient() throws MalformedAddressException {
        return api.getAddress(transactionData.getString("to"));
    }

    public KristAddress getSender() throws MalformedAddressException {
        if (transactionData.isNull("from")) {
            return api.makeVirtualAddress("Mining Reward");
        }

        return api.getAddress(transactionData.getString("from"));
    }

    public long getValue() {
        return transactionData.getLong("value");
    }

    public Date getTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(transactionData.getLong("firstseen_unix") * 1000L);
        return c.getTime();
    }

    public long getTimeUnix() {
        return transactionData.getLong("time_unix");
    }

    public String getOP() {
        return transactionData.getString("op");
    }
}
