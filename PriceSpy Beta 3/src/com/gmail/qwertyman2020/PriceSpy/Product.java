package com.gmail.qwertyman2020.PriceSpy;

// implements Comparable<Product>

public class Product {
	String Price;
	String Desc;
	String ID;

	/**
	 * 
	 * @param newPrice
	 * @param newDesc
	 */
	public Product(String nID){
		ID = nID;
	}
	public Product(String newDesc,String newPrice,String nID){
		Desc= newDesc;
		Price=newPrice;
		ID = nID;
	}
	public void setPrice(String newPrice){
		Price = newPrice;
	}

	public void setDesc(String newDesc){
		Desc = newDesc;
	}



	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Product)) {
			System.out.println("Critical: was not an instance of product.");
			return false;
		}
		Product other = (Product) obj;
		return (this.Desc.toLowerCase().equals(other.Desc.toLowerCase())&&this.Price.equals(other.Price));
	}

	/* expirimental
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Product)) {
			System.out.println("Critical: was not an instance of product.");
			return false;
		}
		Product other = (Product) obj;
		return (this.mainTerm.equals(other.mainTerm)&&this.Part.equals(other.Part)&&this.Type.equals(other.Type)&&this.subType.equals(other.subType)&&this.Preference.toLowerCase().equals(other.Preference)&&this.Price.equals(other.Price));
	}*/

}