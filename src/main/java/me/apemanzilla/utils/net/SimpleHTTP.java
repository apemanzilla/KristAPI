package me.apemanzilla.utils.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public class SimpleHTTP {

	private final CloseableHttpClient client;
	
	public SimpleHTTP() {
		client = HttpClients.createDefault();
	}
	
	public SimpleHTTP(String userAgent) {
		client = HttpClients.custom().setUserAgent(userAgent).build();
	}
	
	public String get(URI uri) throws IOException {
		HttpGet get = new HttpGet(uri);
		try {
			CloseableHttpResponse res = client.execute(get);
			if (res.getStatusLine().getStatusCode() != 200) {
				throw new IOException(String.format("Expected status code 200, got status code %s.", res.getStatusLine().getStatusCode()));
			} else {
				InputStream is = res.getEntity().getContent();
				String data = IOUtils.toString(is);
				is.close();
				res.close();
				return data;
			}
		} catch (UnsupportedOperationException | ClientProtocolException e) {
			throw new IOException(e);
		}
	}
	
	public String post(URI uri, Map<String, String> payload) throws IOException {
		String payload_enc = new JSONObject(payload).toString();
		HttpPost post = new HttpPost(uri);
		try {
			post.setHeader("Content-type", "application/json");
			post.setEntity(new StringEntity(payload_enc));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		CloseableHttpResponse res = client.execute(post);
		if (res.getStatusLine().getStatusCode() != 200) {
			throw new IOException(String.format("Expected status code 200, got status code %s.", res.getStatusLine().getStatusCode()));
		} else {
			InputStream is = res.getEntity().getContent();
			String data = IOUtils.toString(is);
			is.close();
			res.close();
			return data;
		}
	}

}
