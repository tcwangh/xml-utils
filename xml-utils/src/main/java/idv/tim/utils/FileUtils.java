package idv.tim.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileUtils {
	
	public static String readFileAsString(String filePath) {
		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader =
				new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf,0,numRead);
				fileData.append(readData);
			}
		}catch(Exception e) {
			System.out.println(e.toString());
		}finally {
			try{
				reader.close();
			}catch(Exception e) {
				System.out.println(e.toString());
			}
		}
		return fileData.toString();
	}

}
