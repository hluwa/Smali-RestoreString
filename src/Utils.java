import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static String gbEncoding(final String gbString) {   //gbString = "测试"  
        char[] utfBytes = gbString.toCharArray();   //utfBytes = [测, 试]  
        String unicodeBytes = "";     
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {     
            String hexB = Integer.toHexString(utfBytes[byteIndex]);   //转换为16进制整型字符串  
              if (hexB.length() <= 2) {  
            	  while(hexB.length() != 4){
            		  hexB = "0" + hexB;
            	  }
             }     
             unicodeBytes = unicodeBytes + "\\u" + hexB;     
        }     
        return unicodeBytes;     
    }  
	public static ArrayList<File> GetInDirFiles(File dir){
		ArrayList<File> fileList = new ArrayList<File>();
		if(!dir.isDirectory()){
			return fileList;
		}
		File[] list = dir.listFiles();
		if(list == null){
			return fileList;
		}
		for(File file : list){
			if(file.isDirectory()){
				fileList.addAll(GetInDirFiles(file));
			}
			else{
				fileList.add(file);
			}
		}
		return fileList;
	}
}
