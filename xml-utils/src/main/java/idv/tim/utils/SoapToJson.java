package idv.tim.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class SoapToJson {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String soapXMLPath = "D:\\soapInhibition.xml";
		InputStream soapInputStream = null;
		try{
			soapInputStream = new FileInputStream( soapXMLPath );
			
		}catch(FileNotFoundException fe){
			System.out.println(fe.toString());
		}		
		Document doc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		    DocumentBuilder db = dbf.newDocumentBuilder();
		    doc = db.parse(new InputSource(soapInputStream));
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		
		BufferedReader br = null;
		String jsonString = "";
		try {
			br = new BufferedReader(new FileReader("data.json"));
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line);
		        line = br.readLine();
		    }
		    jsonString = sb.toString();
		}catch(Exception e) {
			System.out.println(e.toString());
		}finally {
		    try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(jsonString);
		JsonArray jsonArr = null;
		JsonObject jsonObj = null;
		Object o = jsonFromString(jsonString);
		if (o instanceof JsonArray) {
			jsonArr = (JsonArray)o;
			System.out.println("Source string is a json array");
			System.out.println(jsonArr);
		}else if ( o instanceof JsonObject) {
			jsonObj = (JsonObject)o;
			System.out.println("Source string is a json Object");
			System.out.println(jsonObj);
		}else {
			System.out.println("Error json string");
		}
	}
	
	private static Object jsonFromString(String jsonObjectStr) {
		JsonParser parser = new JsonParser();
		JsonElement elem   = parser.parse( jsonObjectStr );
		JsonArray elemArr = null;
		JsonObject elemObj = null;
		if (elem.isJsonArray()) {
			elemArr = elem.getAsJsonArray();
			return elemArr;
		}else if (elem.isJsonObject()) {
			elemObj = elem.getAsJsonObject();
			return elemObj;
		}else {
			return null;
		}
    }
	
	

}
