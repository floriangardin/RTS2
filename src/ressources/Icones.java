package ressources;


import java.io.File;
import java.util.HashMap;

import javax.swing.ImageIcon;


public strictfp class Icones {
	private static HashMap<String, ImageIcon> images;
	private static boolean isInit;

	public static void init(){
		if(!isInit){
			images = new HashMap<String, ImageIcon>();
			images.put("default", new ImageIcon("ressources/images/icons/iconExit2.png"));
			loadRepertoire("ressources/images/icons/");
			isInit = true;
		}
	}
	
	public static ImageIcon getIcone(String name){
		if(isInit){
			if(images.containsKey(name.toLowerCase())){
				return images.get(name.toLowerCase());
			} else {
				return images.get("default");
			}
		} else {
			return null;
		}
//		return images.get("default");
	}

	private static void loadRepertoire(String name){
		File repertoire = new File(name);
		File[] files=repertoire.listFiles();
		String s;
		ImageIcon im;
		for(int i=0; i<files.length; i++){
			s = files[i].getName();
			if(s.contains(".png")){
				// on load l'image
				s = s.substring(0, s.length()-4);
				im = new ImageIcon(name+s+".png");
				images.put(s.toLowerCase(),im);
			} else if(s.contains(".jpg")){
				// on load l'image
				s = s.substring(0, s.length()-4);
				im = new ImageIcon(name+s+".jpg");
				images.put(s.toLowerCase(),im);
			} else if(s.contains(".svg")){
				// on load l'image
				s = s.substring(0, s.length()-4);
				im = new ImageIcon(name+s+".svg");
				images.put(s.toLowerCase(),im);
			} else if (!s.contains(".") && !s.equals("unit")){
				// nouveau répertoire
				loadRepertoire(name+s+"/");
			}
		} 
	}
}



