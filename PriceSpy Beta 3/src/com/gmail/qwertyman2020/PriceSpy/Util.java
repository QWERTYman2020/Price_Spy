package com.gmail.qwertyman2020.PriceSpy;

import java.util.ArrayList;
import java.util.List;

public class  Util {
	public static String[][][][] FilterArray(String[][] Target,int totX, int totY,String mainTerm,String[] subTerm,String[] types){
		String[][][][] ValidResults = new String[subTerm.length][types.length][30][totX*2];
		System.out.println( "started filtering");
		System.out.println( "index of test: "+Target[0][0].indexOf(mainTerm));
		System.out.println( "totX: "+totX+". totY:"+totY);
		for(int x=0;x<totX;x++){
			for(int y=0;y<totY;y++){
				if(Target[y][x*2]==null){
					break;
				}
				System.out.println("currently checking: "+Target[y][x*2]);
				if(Target[y][x*2].indexOf(mainTerm)!= -1){
					for(int t=0;t<subTerm.length;t++){
						if(Target[y][x*2].toLowerCase().indexOf(subTerm[t])!=-1){
							for(int p=0;p<types.length;p++){
								if(Target[y][x*2].toLowerCase().indexOf(types[p])!=-1){

									for(int l = 0;l<30;l++){
										if(ValidResults[t][p][l][x*2]==null){
											System.out.println("writen!");
											System.out.println("subTerm "+subTerm[t]+". type: "+types[p]);
											System.out.println("at : ["+t+"]["+p+"]["+l+"]["+x*2+"]");
											ValidResults[t][p][l][x*2]=Target[y][x*2];
											ValidResults[t][p][l][1+x*2]=Target[y][1+x*2];
											break;
										}
									}
									break;
								}
							}
							break;
						}
					}

				}
			}
			//totValidResults=0;

		}
		return ValidResults;
	}


	public static List<String> ConstructList(String[][][][] Target,int TypeTotal,int totX,String[] DomainNames){
		List<String> toWrite=new ArrayList<String>();     

		//[t][p][totValidResults][1+x*2]

		//construct Strings
		String temp = "";
		int iter=0;

		for(int x=0;x<DomainNames.length;x++){
			temp+=DomainNames[x]+"			";
		}
		toWrite.add(temp);
		toWrite.add("");

		for(int sub=0;sub<Target.length;sub++){
			for(int tl=0;tl<TypeTotal;tl++){
				temp="";
				for(int empt=0;empt<DomainNames.length;iter++){
					for(int x=0;x<DomainNames.length;x++){
						if(Target[sub][tl][iter][x*2] !=null){
							System.out.println("WRITTEN: ["+sub+"]["+tl+"]["+iter+"]["+x*2+"] ");
							temp+=Target[sub][tl][iter][x*2]+"	"+Target[sub][tl][iter][1+x*2]+"		";
						}else{
							temp+="			";
							System.out.println("["+sub+"]["+tl+"]["+iter+"]["+x*2+"] is empty!");
							empt++;
						}
					}
					toWrite.add(temp);
					temp="";
				}
				iter=0;
			}

		}
		return toWrite;
	}
	
	public static boolean contains(String[] array, String key) {
		for(String s:array){
			if(s.equals(key)){
				return true;
			}
		}
		return false;
	}
	/* DEPRECATED
	public static String[][] FilterXML(String[][] Original,String[] IDs){//String[][]
		String[][] Results = new String[IDs.length][30];
		int noRes=0;
		for(int x=0;x<Original.length;x++){
			for(int y=0;y<IDs.length;y++){
				if(Original[x][12].indexOf(IDs[y])>=0){
					Results[noRes]=Original[x];
					noRes++;
				}
			}
		}
		return Results;
	}*/

	public static String[] formatQuery(String SiteAdress,String parameters,String POSTController,String URLController,int CurrPOST){
		String[] result = new String[2];	
		result[1] = "";

		if(URLController.equalsIgnoreCase("false")){
			result[0] = SiteAdress;
		}else{
			result[0]= SiteAdress+URLController;
		}

		if(!parameters.equalsIgnoreCase("false")){
			result[1]="?";
			if(!POSTController.equalsIgnoreCase("false")){
				result[1] +=parameters+"&"+POSTController+CurrPOST;
			}else{
				result[1] +=parameters;

			}
		}else{
			if(!POSTController.equalsIgnoreCase("false")){
				result[1] = "?"+POSTController+CurrPOST;
			}
		}
		//System.out.println(POSTController+"   --  "+parameters+"   -    "+URLController);
		return result;
	}
	public static int nextInteger(String Page,int startIndex){
		char[] test = Page.substring(startIndex).toCharArray();
		for(char c:test){
			if(Character.isDigit(c)){
				return c;
			}
		}
		return -1;
	}


	public static List<String> constructForm(List<Domain> domains, List<Item> items) {
		List<String> toWrite = new ArrayList<String>();
		System.out.println("STARTED CONSTRUCTION");
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		String newLine="";
		String newFormula="";
		for(Domain d:domains){
			newLine+=d.Name+"			";
		}
		toWrite.add(newLine);
		toWrite.add("");
		boolean found=false;
		for(Item i:items){
			System.out.println("item: "+i.ID);
			newLine="";
			int pos = toWrite.size()+1;
			newFormula="";
			for(int x=0;x<domains.size();x++){
				for(Product p:domains.get(x).Products){
					System.out.println("? "+p.ID+" - "+i.IDs.get(x));
					if(p.ID.equals(i.IDs.get(x))){
						newLine+=p.Desc+"	"+p.Price+"		";
						if(x!=0){
						newFormula+="=ALS(B"+pos+">=("+alphabet[1+x*3]+pos+"+0,05);\""+domains.get(x).Name+" is goedkoper\";\"pass\")"+"		";
						}
						found=true;
						break;
					}
				}
				if(!found){
					newFormula+="		";
					newLine+="			";
				}
				found=false;
			}
			toWrite.add(newLine+newFormula);
			System.out.println(newLine);
		}
		return toWrite;
	}

}

