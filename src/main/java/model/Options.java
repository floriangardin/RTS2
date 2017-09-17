package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public strictfp class Options {
	
	public static float soundVolume;
	public static float musicVolume;
	public static String nickname;
	public static String pythonPath;
	
	public static void init(){
		String fichier ="././options.opts";
		//lecture du fichier texte	
		try{
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			String[] tab;
			int nlignes = 0;
			while ((ligne=br.readLine())!=null){
				nlignes++;
				if(!ligne.contains("-")){
					throw new Exception();
				}
				tab = ligne.split("-");
				switch(tab[0]){
				case "musics:": musicVolume = Float.parseFloat(tab[1]); break;
				case "sounds:": soundVolume = Float.parseFloat(tab[1]); break;
				case "nickname:": nickname = tab[1]; break;
				case "pythonpath:": pythonPath = tab[1]; break;
				default:
				}
			}
			br.close(); 
			if(nlignes<=1){
				musicVolume = 0.5f;
				soundVolume = 0.1f;
				nickname = "";
				pythonPath = "";
			}
		}		
		catch (Exception e){
			soundVolume = 1f;
			musicVolume = 1f;
			nickname = "";
			pythonPath = "";
		}
	}
}
