package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Options {
	
	public float soundVolume;
	public float musicVolume;
	
	public Options(){
		String fichier ="././options.txt";
		//lecture du fichier texte	
		try{
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			String[] tab;
			while ((ligne=br.readLine())!=null){
				tab = ligne.split(" ");
				switch(tab[0]){
				case "musics:": musicVolume = Float.parseFloat(tab[1]); break;
				case "sounds:": soundVolume = Float.parseFloat(tab[1]); break;
				default:
				}
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
	}
}
