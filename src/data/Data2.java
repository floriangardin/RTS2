package data;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import utils.Utils;

public class Data2 {
	
	public HashMap<String, DataObjet> datas;

	public static final int nullValue = -1;
	public static final int True = 1;
	public static final int False = 0;
	
	public Data2(){
		datas = new HashMap<String, DataObjet>();
		// add the unit
		
		HashMap<String, String> files = Utils.loadRepertoire("data/miscellaneous/", "json");
		for(String name : files.keySet()){
			this.datas.put(name, new DataObjet(files.get(name)));
			
		}
		
		// Do the prototyping (overrides non existing attributes
		for(String s : datas.keySet()){
			overrides(s);
		}
		// Convert to float ...
		convertFloat();

		
	}
	
	public String overrides(String s){
		// On cherche le parent le plus vieux
		if(datas.get(s).attributsString.containsKey(Attributs.prototype)){
			// Merge parent...
			merge(s,overrides(datas.get(s).attributsString.get(Attributs.prototype)));
		}
		return s;
		
		
	}
	public void convertFloat(){
		for(String s : datas.keySet()){
			DataObjet d = datas.get(s);
			d.attributs = new HashMap<Attributs,Float>();
			for(Attributs a : d.attributsString.keySet()){
				if(a!=Attributs.prototype){
					d.attributs.put(a, Float.parseFloat(d.attributsString.get(a)));
				}
			}
		}
	}
	public void merge(String child,String parent){
		HashMap<Attributs,String> c = datas.get(child).attributsString;
		HashMap<Attributs,String> p = datas.get(parent).attributsString;
		for(Attributs s : p.keySet()){
			if(!c.containsKey(s)){
				c.put(s, p.get(s));
			}
		}
	}
}
