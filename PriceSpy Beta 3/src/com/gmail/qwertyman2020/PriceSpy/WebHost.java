package com.gmail.qwertyman2020.PriceSpy;

public class WebHost {
String siteAdress;
String urlParameters;
String postPageController;
String[] urlPageController;
private int postPages;

	public WebHost(String newSiteAdress,String newUrlParameters,String newPostPageController,String[] newUrlPageController,int newPostPages){
		siteAdress = newSiteAdress;
		urlParameters=newUrlParameters;
		postPageController=newPostPageController;
		urlPageController=newUrlPageController;
		postPages = newPostPages;
		
		if(urlPageController==null){
			urlPageController=new String[]{"false"};
		}
	}
	/*construct query*/
	/*get query*/
	
	/**
	 * returns the amount or url-ONLY variations in WebHost
	 * @return .length of urlPageController
	 */
	public int getUrlPageControllerLength(){
		if(urlPageController.length==0){
			return 1;
		}else{
		return urlPageController.length;
		}
	}
	public int getPostPages(){
		return postPages;
	}
}
