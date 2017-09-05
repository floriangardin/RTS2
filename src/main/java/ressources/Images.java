package ressources;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

import data.Data;
import display.DisplayRessources;
import main.Main;
import nature.Tree;
import plateau.Building;
import tests.FatalGillesError;
import utils.ObjetsList;


public class Images {

	private static HashMap<String, Image> images= new HashMap<String, Image>();;
	private static HashMap<String, Image> oldimages;
	private static HashMap<ObjetsList, HashMap<String, Image>> imagesUnits;
	private static HashMap<String, Image> sand;	

	private static Data data;
	public static boolean gillesModeEnable = false;
	
	private static boolean isInitialized = false;

	public static void init(){
		data = new Data();
		loadRepertoire("ressources/images/");
		initialize();
		initializeUnits();
		isInitialized = true;
	}
	
	public static boolean isInitialized(){
		return isInitialized;
	}

	private static void initialize() {
		// import other images that will serve during the game
		// icons
		HashMap<String, Image> toPut = new HashMap<String, Image>();
		for(String im : images.keySet()){
			if(im.contains("icon") || im.contains("tech")){
				toPut.put(im+"buildingsize", images.get(im).getScaledCopy(Building.sizeXIcon,Building.sizeXIcon));
			}
		}
		images.putAll(toPut);
		// ressources
		int taille = DisplayRessources.taille;
		images.put("imagegolddisplayressources",images.get("imagegold").getSubImage(7*taille ,15*taille ,taille, taille));
		images.put("imagefooddisplayressources",images.get("imagegold").getSubImage(7*taille ,taille ,taille, taille));
		images.put("rectselectsizebuilding",images.get("rectselect").getScaledCopy(4f));
		images.put("imagepop", images.get("imagepop").getScaledCopy(32,32));

		// buildings
		resizeBuilding("academy");
//		resizeBuilding("barracks");
//		resizeBuilding("headquarters");
//		resizeBuilding("mill");
//		resizeBuilding(q"mine");
//		resizeBuilding("stable");
//		resizeBuilding("tower");
		resizeBuilding("university");

		// bullets
		images.put("arrow",images.get("arrow").getScaledCopy(2f*Main.ratioSpace));

		//initializeSand();
	}
	
	public static boolean exists(String s){
		return images.containsKey(s);
	}

	public void initializeSand(){

		Image im = images.get("sandtile");
		sand = new HashMap<String, Image> ();
		HashMap<String, Image> temp = new HashMap<String, Image>();
		temp.put("3", im.getSubImage(128*0, 128*0, 128, 128));
		temp.put("4", im.getSubImage(128*8, 128*2, 128, 128));
		temp.put("2", im.getSubImage(128*0, 128*1, 128, 128));
		temp.put("8", im.getSubImage(128*4, 128*4, 128, 128));
		temp.put("6", im.getSubImage(128*4, 128*3, 128, 128));
		temp.put("7", im.getSubImage(128*6, 128*0, 128, 128));
		temp.put("1", im.getSubImage(128*1, 128*0, 128, 128));
		temp.put("5", im.getSubImage(128*3, 128*4, 128, 128));

		// special earth
		//sand.put("A111", im.getSubImage(128*4+50, 128*4+50, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		sand.put("E", im.getSubImage(256, 256, 256, 256));
		int w = 256;
		int h = 256;

		for(int a = 1; a<=8; a++){
			for(int b = 1 ; b<=8; b++){
				for(int c = 1; c<=8; c++){
					for(int d = 1; d<=8; d++){
						if(isValid(a,b,c,d)){
							try {
								im = new Image(w, h);
								Graphics g = im.getGraphics();
								g.drawImage(temp.get(""+a), 0, 0);
								temp.get(""+b).rotate(90);
								g.drawImage(temp.get(""+b), w/2, 0);
								temp.get(""+b).rotate(-90);
								temp.get(""+c).rotate(180);
								g.drawImage(temp.get(""+c), w/2, h/2);
								temp.get(""+c).rotate(-180);
								temp.get(""+d).rotate(270);
								g.drawImage(temp.get(""+d), 0, h/2);
								temp.get(""+d).rotate(-270);
								g.flush();
								sand.put(""+a+""+b+""+c+""+d, im);
							} catch (SlickException e) {
								e.printStackTrace();
							}   

						}
					}
				}
			}
		}
	}

	private static boolean isValid(int a, int b, int c, int d){
		return isValid(a,b) && isValid(b,c) && isValid(c,d) && isValid(d,a);
	}

	private static boolean isValid(int a, int b){
		switch(a){
		case 1: return b==2 || b==3;
		case 2: return b==1 || b==4 || b==5 || b==8;
		case 3: return b==2 || b==3;
		case 4: return b==1 || b==4 || b==5 || b==8;
		case 5: return b==6 || b==7;
		case 6: return b==1 || b==4 || b==5 || b==8;
		case 7: return b==6 || b==7;
		case 8: return b==1 || b==4 || b==5 || b==8;
		}
		return false;
	}

	public static void resizeBuilding(String s){
		Point p = data.getSize(ObjetsList.get(s));
		images.put("building"+s+"blue",images.get("building"+s+"blue")
				.getScaledCopy((int)(2*p.getX()/1.8), (int)(3*p.getY()/(2))));
		if(!s.equals("headquarters"))
			images.put("building"+s+"neutral",images.get("building"+s+"neutral")
					.getScaledCopy((int)(2*p.getX()/1.8), (int)(3*p.getY()/(2))));
		images.put("building"+s+"red",images.get("building"+s+"red")
				.getScaledCopy((int)(2*p.getX()/1.8), (int)(3*p.getY()/(2))));
	}

	private static void loadRepertoire(String name){
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
					images.put(s.toLowerCase(),im);
					//					f = Images.class.getField(s);
					//					f.set(this, im);
					//images.put(s, new Image(name+s+".png"));
				} else if(s.contains(".jpg")){
					// on load l'image
					s = s.substring(0, s.length()-4);
					im = new Image(name+s+".jpg");
					images.put(s,im);
					//images.put(s, new Image(name+s+".jpg"));
				} else if(s.contains(".svg")){
					// on load l'image
					s = s.substring(0, s.length()-4);
					im = new Image(name+s+".svg");
					images.put(s,im);
					//images.put(s, new Image(name+s+".svg"));
				} else if (!s.contains(".") && !s.equals("unit") && !s.equals("ignore")){
					// nouveau répertoire
					loadRepertoire(name+s+"/");

				}
			} 
		} catch (SlickException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		}
	}

	public static  Image get(String name){
		if(images.containsKey(name.toLowerCase())){
			return images.get(name.toLowerCase());
		} else {
			System.out.println("Error : trying to load an non-existing image : "+name);
//			try {
//				throw new FatalGillesError("non-existing image : "+name);
//			} catch (FatalGillesError e) {
//				e.printStackTrace();
//			}
			return images.get("image_manquante");
		}
	}

	private static void initializeUnits(){
		File repertoire = new File("ressources/images/unit");
		File[] files=repertoire.listFiles();
		String s,s2,s4;
		imagesUnits = new HashMap<ObjetsList, HashMap<String, Image>>();
		for(ObjetsList o : ObjetsList.getUnits()){
			imagesUnits.put(o, new HashMap<String, Image>());
		}
		Image im;
		int imageHeight, imageWidth;
		try {
			for(int i=0; i<files.length; i++){
				s = files[i].getName();
				if(s.contains(".png")){
					// on load l'image
					im = new Image("ressources/images/unit/"+s);
					imageHeight = im.getHeight()/4;
					imageWidth = im.getWidth()/5;
					s = s.substring(0, s.length()-4);
					for(ObjetsList s1 : imagesUnits.keySet()){
						if(s.toLowerCase().contains(s1.name().toLowerCase())){
							s2 = (s.toLowerCase().startsWith("attack") ? "1" : "0");
							s4 = (s.toLowerCase().endsWith("blue") ? "1" : "2");
							for(int orientation =0; orientation<4; orientation++){
								for(int animation=0; animation<5; animation++){
									imagesUnits.get(s1).put(s2+""+orientation+""+animation+""+s4, 
											im.getSubImage(imageWidth*animation,imageHeight*(int)orientation,imageWidth,imageHeight)
											.getScaledCopy(120*Main.ratioSpace/imageWidth));
								}
							}
						}
					}
					images.put(s.toLowerCase(),im);
				}
			} 
			HashMap<String, Image> toAdd = new HashMap<String, Image>();
			for(ObjetsList s1 : imagesUnits.keySet()){
				toAdd.clear();
				if(imagesUnits.get(s1).size()==40){
					for(String s3 : imagesUnits.get(s1).keySet()){
						toAdd.put("1"+s3.substring(1), imagesUnits.get(s1).get(s3));
					}
				}
				imagesUnits.get(s1).putAll(toAdd);
			}
		} catch (SlickException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		}
	}

	public static Image getUnit(ObjetsList name, int direction, int animation, int team, boolean attack){
		if(imagesUnits.containsKey(name) && imagesUnits.get(name).containsKey((attack?"1":"0")+direction+""+animation+"" +team)){
			return imagesUnits.get(name).get((attack?"1":"0")+direction+""+animation+"" +team);
		} else {
			try {
				throw new FatalGillesError("images d'unité non existante : "+name+":"+animation+" "+direction+" " +attack+" "+team);
			} catch (FatalGillesError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void activateGdBMode() {
		Image GdB = images.get("gilles");
		oldimages = images;
		images = new HashMap<String, Image>();
		for(String s : oldimages.keySet()){
			images.put(s,GdB);
		}
		gillesModeEnable = true;
	}

	public static void deactivateGdBMode(){
		gillesModeEnable = false;
		images.clear();
		images = oldimages;
	}

	// about sand tiles

	public static Image getSand(String s){
		return sand.get(s);
	}

	public static void updateScaleSend(float stepGrid) {
		HashMap<String, Image> temp = new HashMap<String, Image>();
		for(String s : sand.keySet()){
			temp.put(s, sand.get(s).getScaledCopy((int)(stepGrid), (int)(stepGrid)));
		}
		sand = temp;
		images.put("watertile", images.get("watertile").getScaledCopy((int)(2*stepGrid), (int)(2*stepGrid)));
		images.put("watertile2", images.get("watertile2").getScaledCopy((int)(stepGrid), (int)(stepGrid)));
	}
}
