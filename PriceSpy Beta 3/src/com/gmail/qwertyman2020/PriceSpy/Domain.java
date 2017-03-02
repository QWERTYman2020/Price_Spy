package com.gmail.qwertyman2020.PriceSpy;

import java.util.ArrayList;
import java.util.List;

public class Domain {
	String IDPattern;
	List<Product> Products;
	String Name;
	String Pattern;
	WebHost webHostData;
	
	public void setHostData(WebHost newHostData){
		webHostData=newHostData;
	}
	
	public void setProperties(String newName,String newDesc,String newPrice,String newIDPattern){
		Name=newName;
		Pattern = newDesc+"%"+newPrice;	
		IDPattern= newIDPattern;
	}
	public void removeDuplicates(){
		List<Product> newProducts = new ArrayList<Product>();
		if(Products !=null&&!Products.isEmpty()){
			newProducts.add(Products.get(0));
			boolean found = false;

			for (Product p: Products)
			{
				for(int x=newProducts.size()-1;x>=0;x--){
					System.out.println(newProducts.get(x).Desc+" - "+p.Desc);
					if (newProducts.get(x).equals(p)) 
					{
						found=true;
						break;
					}
				}
				if(!found){
					newProducts.add(p);
				}else{
					found = false;
				}
			}
			Products=newProducts;
		}else{
			System.out.println("skipped remove duplicates for domain "+Name);
		}
	}

	public Domain(String newID){
		IDPattern= newID;
		Products = new ArrayList<Product>();
	}
	public void addProducts(List<Product> newProducts){
		Products.addAll(newProducts);
	}

}
