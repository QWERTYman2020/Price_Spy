package com.gmail.qwertyman2020.PriceSpy;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.util.Base64;
import java.util.Properties;

public class ProxyAuth extends Authenticator {
    private PasswordAuthentication auth;

    private ProxyAuth(String user, String password) {
        auth = new PasswordAuthentication(user, password == null ? new char[]{} : password.toCharArray());
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return auth;
    }
    public static HttpURLConnection setProxy(Proxy proxy,HttpURLConnection con) {
    	Properties props = new Properties(System.getProperties());
    	props.put("http.proxySet","true");
    	props.put("http.proxyHost",proxy.IP);
    	props.put("http.proxyPort",proxy.Port);
    	props.put("https.proxySet","true");
    	props.put("https.proxyHost",proxy.IP);
		props.put("https.proxyPort",proxy.Port);
		String encoded = new String(Base64.getEncoder().encode((proxy.User + ":" + proxy.Pass).getBytes()));
		con.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
    	Authenticator.setDefault(new ProxyAuth(proxy.User, proxy.Pass));
    	System.setProperties(props);
    return con;
    }
}