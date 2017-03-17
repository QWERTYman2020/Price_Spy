package com.gmail.qwertyman2020.PriceSpy;

import java.net.*;
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
    public static void setProxy(Proxy proxy) {
    	Properties props = new Properties(System.getProperties());
    	props.put("http.proxySet","true");
    	props.put("http.proxyHost",proxy.IP);
    	props.put("http.proxyPort",proxy.Port);
    	props.put("https.proxySet","true");
    	props.put("https.proxyHost",proxy.IP);
		props.put("https.proxyPort",proxy.Port);
    	System.setProperties(props);
    }
    
    public static HttpURLConnection setConnection(Proxy proxy, HttpURLConnection con){
		String encoded = new String(Base64.getEncoder().encode((proxy.User + ":" + proxy.Pass).getBytes()));
		con.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
    	Authenticator.setDefault(new ProxyAuth(proxy.User, proxy.Pass));

    return con;
    }
}