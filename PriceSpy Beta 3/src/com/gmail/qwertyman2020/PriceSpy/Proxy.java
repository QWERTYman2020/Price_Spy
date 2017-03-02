package com.gmail.qwertyman2020.PriceSpy;

import java.util.List;

public class Proxy {
	String IP;
	String Port;
	String User;
	String Pass;
	public Proxy(String nIP,String nPort,String nUser,String nPass){
		IP= nIP;
		Port = nPort;
		User=nUser;
		Pass = nPass;
		//further init
	}
	public boolean validate(){
		String Page="";
		try{
			Page=WebHandler.DownloadPage2("http://checkip.amazonaws.com","",this);
			if(Page.trim().equals(IP)){
					return true;
				}else{
					return false;
				}
		}finally{
			System.out.println(Page+"<we got.we want> "+IP);
		}
	}
	
	public static List<Proxy> Randomize(List<Proxy> proxies){
		return null;
	}
	//create webhost?
}

