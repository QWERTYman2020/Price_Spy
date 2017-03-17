package com.gmail.qwertyman2020.PriceSpy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.gmail.qwertyman2020.PriceSpy.Product;
import com.gmail.qwertyman2020.PriceSpy.Domain;
/**
 * a class that handles retrieval of webpages and it's own proxy settings.
 */
public class WebHandler {
	private String Page;
	
	public WebHandler(){
	}

	/**
	 * attempts to download the specified page at targetUrl using urlParameters.
	 * sets Page to the downloaded page.
	 * @return true if downloading the page succeeded.
	 * @return false if downloading the page failed.
	 */
	public Boolean DownloadPage(String targetURL,String urlParameters,Proxy proxy){
		
		HttpURLConnection connection = null;
		System.out.println("downloading page: "+targetURL+urlParameters);
		try {
			//Create connection
			URL url = new URL(targetURL);
			ProxyAuth.setProxy(proxy);	//might be redundant.
			//java.net.Proxy prox = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxy.IP,Integer.parseInt(proxy.Port)));
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("User-Agent",
			        "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
			connection.setRequestProperty("Content-Length", 
					Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");  
			connection.setReadTimeout(10000);
			connection = ProxyAuth.setConnection(proxy, connection);
			/*
			System.out.println("started getting cookies");
			String headerName=null;
			List<String> cookies = new ArrayList<String>();
			for (int i=1; (headerName = connection.getHeaderFieldKey(i))!=null; i++) {
			 	if (headerName.equals("Set-Cookie")) {                  
			 		cookies.add(connection.getHeaderField(i));               
			 	}
			}
			for(String cookie:cookies){
				cookie = cookie.substring(0, cookie.indexOf(";"));
		        String cookieName = cookie.substring(0, cookie.indexOf("="));
		        String cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
		        System.out.println("cookie name: "+cookieName+"\ncookieValue: "+cookieValue);
			}*/
			
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			//connection.connect();
			
			//Send request
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			//Get Response  
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuffer Response = new StringBuffer(); // or StringBuffer if Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				Response.append(line);
				Response.append('\r');
			}
			rd.close();
			Page = Response.toString();
			return true;
		} catch (Exception e) {
			System.out.print("!!caught error: ");
			System.out.println(e.toString());
			return false;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}


	}
	public String getPage(){
		return Page;
	}

	/**
	 * 
	 * @param dom the Domain which specifications we should use AND the domain we extract to.
	 */
	public void extractToDomain(Domain dom){
		//maybe boolean
		List<Product> tempProducts = new  ArrayList<Product>();//x is POSTPAGES*urlControllers*results
		//(something)[(something)-(something)](something)

		//TODO UPDATE
		String[] strDesc=new String[]{"","","",""};
		int[] regLength = new int[4];
		String[] regDesc=new String[]{"","","",""};	//only 1 "-" expected
		//ind caring is chars since current regdesc start
		int[] indCaring = new int[8];//number of result types desc,price*start end
		
		
		String[] strID = new String[]{"",""};
		String[] regID = new String[]{"",""};
		int[] IDlength = new int[2];
		int[] IDCaring= new int[4];
		
		
		int currCaring=0;
		int currDesc=0;
		int currIndex=0;
		
		int currIDIndex=0;
		int currIDDesc=0;
		int currIDCaring=0;
		boolean write =true;
		boolean search = true;
		for(char c:dom.Pattern.toCharArray()){//for everychar in pattern
			
			switch(c){
			case '%':currDesc++;write=true;
			break;
			case '*':write=false;regDesc[currDesc]+=c;
			break;
			case '[':indCaring[currCaring]=regDesc[currDesc].length();currCaring++;
			break;
			case ']':if(regDesc[currDesc]!=null){indCaring[currCaring]=regDesc[currDesc].length();}else{indCaring[currCaring]=0;}currCaring++;
			break;
			default:if(write){strDesc[currDesc]+=c;};regDesc[currDesc]+=c;
			break;
			}					
		}
		
		write =true;
		for(char c:dom.IDPattern.toCharArray()){//TODO change to ID
			
			switch(c){
			case '%':currIDDesc++;write=true;
			break;
			case '*':write=false;regID[currIDDesc]+=c;
			break;
			case '[':IDCaring[currIDCaring]=regID[currIDDesc].length();currIDCaring++;
			break;
			case ']':if(regID[currIDDesc]!=null){IDCaring[currIDCaring]=regID[currIDDesc].length();}else{IDCaring[currIDCaring]=0;}currIDCaring++;
			break;
			default:if(write){strID[currIDDesc]+=c;};regID[currIDDesc]+=c;
			break;
			}					
		}
		for(int s=0;s<regDesc.length;s++){
			regLength[s] =regDesc[s].length();
			regDesc[s].replaceAll("\\*", "\\.*");
		}
		for(int s=0;s<regID.length;s++){
			IDlength[s] =regID[s].length();
			regID[s].replaceAll("\\*", "\\.*");
		}// TODO change to ID

		int[] IndexResult = new int[4];
		int[] IndexIDResult = new int[2];
		
		while(search){
			for(int x=0;x<strDesc.length/2;x++){			//DESC
				int currTest =Page.indexOf(strDesc[x], currIndex);
				if(currTest>=0){
					//System.out.println(currTest+"+"+regLength[x]);
					
					//System.out.println("testing: "+Page.substring(currTest,currTest+regLength[x])+" matches :"+regDesc[x]);
					if(Page.substring(currTest,currTest+regLength[x]).matches(regDesc[x])){//if exact match
							IndexResult[x]=currTest+indCaring[x];//desc position
							currIndex=IndexResult[x];//TODO probably wrong

					}
				}else{
	
					//could not find initial
					search=false;
				}
			}
			currIDIndex=currIndex;
			for(int x=0;x<strID.length;x++){	//ID data
				int currTest =Page.indexOf(strID[x], currIDIndex);
				if(currTest>=0){
					//System.out.println(currTest+"+"+regLength[x]);
					
					//System.out.println("testing: "+Page.substring(currTest,currTest+regLength[x])+" matches :"+regDesc[x]);
					if(Page.substring(currTest,currTest+IDlength[x]).matches(regID[x])){//if exact match
							IndexIDResult[x]=currTest+IDCaring[x];//desc position
							currIDIndex=IndexIDResult[x];//TODO probably wrong
					}
				}else{
					System.out.println("could not find ID details and search = "+search);
					//could not find initial
					search=false;
				}
			}
			for(int x=2;x<strDesc.length;x++){			//PRICE		

				int currTest =Page.indexOf(strDesc[x], currIndex);
				if(currTest>=0){
					//System.out.println("testing: "+Page.substring(currTest,currTest+regLength[x])+" matches :"+regDesc[x]);
					if(Page.substring(currTest,currTest+regLength[x]).matches(regDesc[x])){//if exact match
						if(x==2){
						IndexResult[x]=Page.indexOf(Util.nextInteger(Page,currTest+indCaring[x]),currTest+indCaring[x]);
						currIndex=IndexResult[x];
						}else{
						IndexResult[x]=currTest+indCaring[x];//price position
						currIndex=currTest;
						}
					}
				}else{
					//could not find initial
					search=false;
				}
			}
			//TODO ID SEARCH
			
			//get substrings and add them properly
			if(search){
			System.out.println("found new product: "+Page.substring(IndexResult[0],IndexResult[1])+" - "+Page.substring(IndexResult[2],IndexResult[3])+" - "+Page.substring(IndexIDResult[0],IndexIDResult[1]));
			tempProducts.add(new Product(Page.substring(IndexResult[0],IndexResult[1]),Page.substring(IndexResult[2],IndexResult[3]),Page.substring(IndexIDResult[0],IndexIDResult[1])));
			}
		}		
		dom.addProducts(tempProducts);
	}
	public static String DownloadPage2(String targetURL,String urlParameters,Proxy proxy){
		HttpURLConnection connection = null;
		System.out.println("downloading page: "+targetURL+urlParameters);
		try {
			//Create connection
			URL url = new URL(targetURL);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded");
			
			connection.setRequestProperty("Content-Length", 
					Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");  
			ProxyAuth.setProxy(proxy);
			connection = ProxyAuth.setConnection(proxy, connection);
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			//connection.connect();

			//Send request
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			//Get Response  
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuffer Response = new StringBuffer(); // or StringBuffer if Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				Response.append(line);
				Response.append('\r');
			}
			rd.close();
			return Response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}


	}

}


