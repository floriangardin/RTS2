package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;



public class KeyMapper {
	
	private static String location = "././data/miscellaneous/keymapping.rtsfile";
	public HashMap<Integer, KeyEnum> mapping;

	public KeyMapper(){
		String fichier = location;
		mapping = new HashMap<Integer, KeyEnum>();
		// Constructeur par défaut, associe le mapping standard
		try{
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			String[] tab;
			while ((ligne=br.readLine())!=null){
				if(!ligne.contains("_")){
					throw new Exception();
				}
				tab = ligne.split("_");
				mapping.put(Integer.parseInt(tab[0]),KeyEnum.valueOf(tab[1]));
			}
			br.close(); 
			
		}		
		catch (Exception e){}
			
	}
	
	public void saveMapping(){
		try {
			FileWriter fw = new FileWriter(location);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
			for(Integer i : this.mapping.keySet()){
				fichierSortie.println (i+"_"+this.mapping.get(i).name());
			}
			fichierSortie.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}
	}
	
	public enum KeyEnum {
		LeftClick,
		RightClick,
		MiddleClick,
		Up,
		Down,
		Right,
		Left,
		Prod0,
		Prod1,
		Prod2,
		Prod3,
		TenirPosition,
		DeplacementOffensif,
		PouvoirSpecial,
		AjouterSelection,
		ToutSelection,
		AbandonnerPartie;
	}
}
