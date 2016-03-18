package ressources;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import tests.FatalGillesError;


public class Images {

	private HashMap<String, Image> images;

	public Images(){
			this.images = new HashMap<String, Image>();
			this.loadRepertoire("ressources/images/");
	}

	private void loadRepertoire(String name){
		File repertoire = new File(name);
		File[] files=repertoire.listFiles();
		String s;
		Image im;
		try {
			for(int i=0; i<files.length; i++){
				s = files[i].getName();
				if(s.contains(".png")){
					// on load l'image
					s = s.substring(0, s.length()-4);
					im = new Image(name+s+".png");
					this.images.put(s.toLowerCase(),im);
//					f = Images.class.getField(s);
//					f.set(this, im);
					//this.images.put(s, new Image(name+s+".png"));
				} else if(s.contains(".jpg")){
					// on load l'image
					s = s.substring(0, s.length()-4);
					im = new Image(name+s+".jpg");
					this.images.put(s,im);
					//this.images.put(s, new Image(name+s+".jpg"));
				} else if(s.contains(".svg")){
					// on load l'image
					s = s.substring(0, s.length()-4);
					im = new Image(name+s+".svg");
					this.images.put(s,im);
					//this.images.put(s, new Image(name+s+".svg"));
				} else if (!s.contains(".") && !s.equals("danger")){
					// nouveau répertoire
					this.loadRepertoire(name+s+"/");
					
				}
			} 
		} catch (SlickException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		}
	}

	public Image get(String name){
		if(this.images.containsKey(name.toLowerCase())){
			return this.images.get(name.toLowerCase());
		} else {
			System.out.println("Error : trying to load an non-existing image : "+name);
			try {
				throw new FatalGillesError("non-existing image : "+name);
			} catch (FatalGillesError e) {
				e.printStackTrace();
			}
			return null;
		}
	}

}
