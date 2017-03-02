package com.gmail.qwertyman2020.PriceSpy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyAcc {

	public static String[] getPropValues() throws IOException {
		String[] result=new String[9];
		InputStream inputStream=null;
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";

			File test = new File(propFileName);
			inputStream = new FileInputStream(test);

			if (inputStream != null) {
				prop.load(inputStream);
			}


			// get the property value and print it out
			String randTime= prop.getProperty("randTime");
			String staticTime = prop.getProperty("staticTime");
			String DataPath = prop.getProperty("DataPath");
			String LangPath = prop.getProperty("ProxyPath");
			String ExportPath = prop.getProperty("ExportPath");
			String QueryPath = prop.getProperty("QueryPath");
			String Logging = prop.getProperty("Logging");


			result = new String[]{randTime,staticTime,DataPath,LangPath,ExportPath,QueryPath,Logging};
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}

}
