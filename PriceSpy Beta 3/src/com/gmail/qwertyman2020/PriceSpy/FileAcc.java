package com.gmail.qwertyman2020.PriceSpy;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public final class FileAcc {
	
	/**
	 * 
	 * @param Path
	 * @param domainIDs
	 * @param Domains
	 * @return succes?
	 */
	public static List<Domain> readDataIntoDomain(String Path,List<Domain> Domains){
		File fXmlFile = new File(Path);
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			System.out.println("started reading out Data");

			NodeList nList = doc.getElementsByTagName("website");

			System.out.println("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				//System.out.println("\nCurrent Element :" + nNode.getNodeName());


				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					
						
					//System.out.println("Site id : " + eElement.getAttribute("id"));
					System.out.println("Domain : " + eElement.getElementsByTagName("name").item(0).getTextContent());
					System.out.println("expected pages : " + eElement.getElementsByTagName("POSTPages").item(0).getTextContent());
					System.out.println("expected results : " + eElement.getElementsByTagName("noResults").item(0).getTextContent());
					System.out.println("Desc : " + eElement.getElementsByTagName("Desc").item(0).getTextContent());
					System.out.println("Price : " + eElement.getElementsByTagName("Price").item(0).getTextContent());
					System.out.println("site address : " + eElement.getElementsByTagName("SiteAdress").item(0).getTextContent());
					System.out.println("url Parameters : " + eElement.getElementsByTagName("UrlParameters").item(0).getTextContent());
					System.out.println("POSTpage controller : " + eElement.getElementsByTagName("PostPageController").item(0).getTextContent());


					for(int x=0;x<eElement.getElementsByTagName("case").getLength();x++){
						System.out.println("URLpage controller : " + eElement.getElementsByTagName("case").item(x).getTextContent());
					}
					System.out.println("");

					String newSiteAdress =eElement.getElementsByTagName("SiteAdress").item(0).getTextContent();
					String newUrlParameters =eElement.getElementsByTagName("UrlParameters").item(0).getTextContent();
					String newPostPageController=eElement.getElementsByTagName("PostPageController").item(0).getTextContent();
					String[] newUrlPageController = new String[eElement.getElementsByTagName("case").getLength()];
					int newPostPages =Integer.parseInt(eElement.getElementsByTagName("POSTPages").item(0).getTextContent());
					for(int x=0;x<eElement.getElementsByTagName("case").getLength();x++){
						newUrlPageController[x] = eElement.getElementsByTagName("case").item(x).getTextContent();
					}
					WebHost newHostData = new WebHost(newSiteAdress,newUrlParameters,newPostPageController,newUrlPageController,newPostPages);
		
					Domains.add(new Domain(eElement.getAttribute("id")));
					Domains.get(temp).setHostData(newHostData);
					String newName =eElement.getElementsByTagName("name").item(0).getTextContent();
					String newDesc =eElement.getElementsByTagName("Desc").item(0).getTextContent();
					String newPrice =eElement.getElementsByTagName("Price").item(0).getTextContent();
					String newIDPattern =eElement.getElementsByTagName("ID").item(0).getTextContent();
					Domains.get(temp).setProperties(newName, newDesc, newPrice,newIDPattern);

				}
			}
			return Domains;
		} catch (Exception e) {
			System.out.println("probably failed to find a file");	
			e.printStackTrace();
			return null;
		}finally{
			System.out.println("--------done reading-----------");
		}
	}

	/*
	 * readfile(String path, Charset encoding)
	 * Returns a single string containing all characters in the document encoded with specified codec.
	 */
	public static String readFile(String path, Charset encoding) 
			throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void writeFile(String path, List<String> toWrite){
		Path file = Paths.get(path);
		try {
			Files.write(file, toWrite, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static List<Item> readItems(String Path){
		File fXmlFile = new File(Path);
		List<Item> Items =new ArrayList<Item>();
		
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			System.out.println("started reading out Data");

			NodeList nList = doc.getElementsByTagName("item");

			System.out.println("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				//System.out.println("\nCurrent Element :" + nNode.getNodeName());


				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					
					
					Element eElement = (Element) nNode;
					
						
					//System.out.println("Site id : " + eElement.getAttribute("id"));
					String nID =eElement.getElementsByTagName("ID").item(0).getTextContent();
					List<String> nIDs = new ArrayList<String>();
					for(int x=0;x<eElement.getElementsByTagName("case").getLength();x++){
						nIDs.add(eElement.getElementsByTagName("case").item(x).getTextContent());
					}
					Items.add(new Item(nID,nIDs));
						
				}else{
					System.out.println("critical");

				}
			}
			return Items;
		} catch (Exception e) {
			System.out.println("probably failed to find a file");	
			e.printStackTrace();
			return null;
		}finally{
			System.out.println("--------done reading-----------");
		}
	}
	public static List<Proxy> readProxiesFromFile(String Path){
		List<Proxy> list = new ArrayList<Proxy>();
	    Scanner sc = null;
		try {
			sc = new Scanner(new FileReader(Path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.useDelimiter(":");
	    while (sc.hasNextLine()){
	        List<String> Data=new ArrayList<String>();
	    	for(int x=0;x<4;x++){
	    		if(sc.hasNext()){
	    			Data.add(sc.next());
	    		}else{
	    			break;
	    		}
	    	}
	    	list.add(new Proxy(Data.get(0),Data.get(1),Data.get(2),Data.get(3)));
	    	System.out.println(Data.get(0)+"-"+Data.get(1)+"-"+Data.get(2)+"-"+Data.get(3));
	    	if(sc.hasNextLine()){
	    		sc.nextLine();
	    	}
	    }

		return list;
	}
	}
	



