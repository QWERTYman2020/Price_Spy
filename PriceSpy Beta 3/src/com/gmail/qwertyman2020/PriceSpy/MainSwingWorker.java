package com.gmail.qwertyman2020.PriceSpy;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.SwingWorker;


public class MainSwingWorker extends SwingWorker<Void,Status> {

	String version = "2.0";

	private SpyFrame window;
	public MainSwingWorker(SpyFrame nwindow){
		window=nwindow;
	}

	@Override
	protected Void doInBackground() throws Exception {
		try{

			String[] props = new String[9];
			try {
				props = PropertyAcc.getPropValues();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//randTime,staticTime,DataPath,LangPath,ExportPath,QueryPath,Logging
			String DataPath=props[2];
			String ProxyPath=props[3];
			boolean Logging= props[6].equals("true");
			String ExportPath=props[4];
			int randTime = Integer.parseInt(props[0]);
			int staticTime =Integer.parseInt(props[1]);
			String QueryPath = props[5];

			if(Logging){
				try {
					Date date= new Date();
					Format formatter = new SimpleDateFormat("YYYY-MM-dd_hh-mm-ss");
					System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("log/Log From "+formatter.format(date)+".txt",true))));
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				}}

			WebHandler webHandler = new WebHandler();
			List<Proxy> proxies = FileAcc.readProxiesFromFile(ProxyPath);
			for(int x= proxies.size()-1;x>=0;x--){
				if(!proxies.get(x).validate()){
					proxies.remove(x);
				}
			}
			if(proxies.size()<=0){
				System.out.println("All proxies failed.");
			}
			//Proxy proxy = proxies.get(0);
			Random rand = new Random();
			List<Item> Items = new ArrayList<Item>();
			List<Domain> Domains = new ArrayList<Domain>();
			Domains = FileAcc.readDataIntoDomain(DataPath,Domains);
			Items = FileAcc.readItems(QueryPath);
			int total = Domains.size();

			Status status = new Status();

			status.setTitle("PriceSpy Beta V"+version+"  -  started working!!!");	
			this.publish(status);

			for(int x=0;x<total;x++){	//domains
				Domain currDomain = Domains.get(x);
				WebHost webData = currDomain.webHostData;
				System.out.println("Ints and strings for "+currDomain.Name+" done");	
				List<String[]> queries = new ArrayList<String[]>();
				//do all pages
				System.out.println(webData.getUrlPageControllerLength()+" - "+webData.getPostPages());
				for(int n=0;n<webData.getUrlPageControllerLength();n++){	//url pages
					for(int m=1;m<=webData.getPostPages();m++){  //Post pages          	
						//incase the sites use different paging systems   	
						queries.add(Util.formatQuery(webData.siteAdress,webData.urlParameters, webData.postPageController, webData.urlPageController[n], m));
					}
				}

				double prog = 100.0/total/queries.size();
				
				for(int y = queries.size()-1;y>=0;y--){
					
					int queryRand;
					String[] currQuery;
					if(y != 0){
						queryRand = rand.nextInt(y);
						currQuery = queries.get(queryRand);
					}else{
						queryRand=0;
						currQuery = queries.get(0);
					}
					int proxyRand = rand.nextInt(proxies.size());
					Proxy proxy = proxies.get(proxyRand);
					if(webHandler.DownloadPage(currQuery[0],currQuery[1],proxy)){
						System.out.println("data from "+Domains.get(x).Name+" recieved");	
						webHandler.extractToDomain(Domains.get(x));
						System.out.println("items extracted from "+Domains.get(x).Name);

					}else{
						//page downloading failed
						System.out.println("page downloading failed :')");
					}

					queries.remove(queryRand);
					
					status.setTitle("Price Spy - "+y+" pages to download for this "+Domains.get(x).Name);
					status.addProgress(prog);
					this.publish(status);

					//look, i know this is a very bad idea but it works.
					//if you want to redesign the program for mutithreading feel free to do so.
					try {
						Thread.sleep(rand.nextInt(randTime) + staticTime);

					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.print(e.toString());
					}

				}
				//System.out.println("load next url:");						

				//System.out.println("load next domain:");

			}
			System.out.println("no next domain");
			status.setTitle("no next domain.");
			this.publish(status);

			//Domains = Domains.filter();//TODO CHANGE
			List<String> toWrite = new ArrayList<String>();
			for(Domain d:Domains){
				for(Product p:d.Products){
					toWrite.add(p.ID+" - "+p.Desc+" - "+p.Price);
				}
				for(Item a:Items){
					toWrite.add(a.ID +" - "+a.IDs.get(0));
				}
			}
			FileAcc.writeFile("RAW.txt", toWrite);

			toWrite = Util.constructForm(Domains,Items);//TODO CHANGE
			FileAcc.writeFile(ExportPath, toWrite);
			System.out.println("DONEEEEEEEEEEEEEEEE!!!*fireworks*");
			status.setTitle("PriceSpy Beta V"+version+"- DONE");
			this.publish(status);

			System.out.flush(); // make sure all messages are written to log
		}finally{
			System.out.println("-_-^-_-^-_-^-_-^---EOF---_-^-_-^-_-^-_-^-");
			System.out.flush();
		}
		return null;
	}   
	@Override
	protected void process(List<Status> res){
		for(Status status : res){
			System.out.println(status.getTitle()+" prog:"+status.getProgress());
			window.updateGUI(status);
		}
	}
	@Override
	protected void done() {
		// TODO Auto-generated method stub
		window.switchButton();
		super.done();
	}
};
