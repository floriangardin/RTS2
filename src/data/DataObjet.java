package data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class DataObjet implements java.io.Serializable {
	// Generic fields
	public HashMap<Attributs, Float> attributs = new HashMap<Attributs, Float>();
	public HashMap<Attributs, String> attributsString = new HashMap<Attributs, String>();
	private static String location ="";
	public static Gson gson = new Gson();
	public DataObjet(String filename){
		try {
			attributsString = gson.fromJson(new JsonReader(new FileReader(location+filename)), new TypeToken<HashMap<Attributs, String>>(){}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public String toString(){
		return gson.toJson(attributs) + " " +gson.toJson(attributsString);
	}
	public String toStringPre(){
		return gson.toJson(attributsString);
	}
}