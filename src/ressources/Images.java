package ressources;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

import buildings.Building;
import buildings.BuildingsList;
import display.DisplayRessources;
import main.Main;
import model.Data;
import nature.Tree;
import tests.FatalGillesError;
import units.Character;


public class Images {

	private HashMap<String, Image> images;
	private HashMap<String, Image> oldimages;
	private HashMap<String, HashMap<String, Image>> imagesUnits;
	private HashMap<String, Image> sand;	
	

	public Images(){
			this.images = new HashMap<String, Image>();
			this.loadRepertoire("ressources/images/");
			this.initialize();
			this.initializeUnits();
	}

	private void initialize() {
		// import other images that will serve during the game
		// icons
		HashMap<String, Image> toPut = new HashMap<String, Image>();
		for(String im : this.images.keySet()){
			if(im.contains("icon") || im.contains("tech")){
				toPut.put(im+"buildingsize", this.images.get(im).getScaledCopy(Building.sizeXIcon,Building.sizeXIcon));
			}
		}
		this.images.putAll(toPut);
		// ressources
		int taille = DisplayRessources.taille;
		this.images.put("imagegolddisplayressources",this.images.get("imagegold").getSubImage(7*taille ,15*taille ,taille, taille));
		this.images.put("imagefooddisplayressources",this.images.get("imagegold").getSubImage(7*taille ,taille ,taille, taille));
		this.images.put("rectselectsizebuilding",this.images.get("rectselect").getScaledCopy(4f));
		this.images.put("imagepop", this.images.get("imagepop").getScaledCopy(32,32));
		
		// buildings
		this.resizeBuilding("academy");
		this.resizeBuilding("barrack");
		this.resizeBuilding("headquarters");
		this.resizeBuilding("mill");
		this.resizeBuilding("mine");
		this.resizeBuilding("stable");
		this.resizeBuilding("tower");
		this.resizeBuilding("university");
		
		// bullets
		this.images.put("arrow",this.images.get("arrow").getScaledCopy(2f*Main.ratioSpace));
		
		// trees
		for(String im : this.images.keySet()){
			if(im.contains("tree")){		
				this.images.put(im, this.images.get(im).getScaledCopy(Tree.coeffDraw));
			}
		}
		
		//sand
		Image im = this.images.get("sandtile");
		this.sand = new HashMap<String, Image> ();
		this.sand.put("A222", im.getSubImage(128*0, 128*0, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A212", im.getSubImage(128*0, 128*0, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A121", im.getSubImage(128*8, 128*2, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A211", im.getSubImage(128*0, 128*1, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A221", im.getSubImage(128*0, 128*1, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A101", im.getSubImage(128*4, 128*4, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A111", im.getSubImage(128*4+50, 128*4+50, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A001", im.getSubImage(128*4, 128*3, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A011", im.getSubImage(128*4, 128*3, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A010", im.getSubImage(128*6, 128*0, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A000", im.getSubImage(128*6, 128*0, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A112", im.getSubImage(128*1, 128*0, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A122", im.getSubImage(128*1, 128*0, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A110", im.getSubImage(128*3, 128*4, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("A100", im.getSubImage(128*3, 128*4, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		// importing B
		HashMap<String, Image> temp = new HashMap<String, Image>();
		Image image;
		for(String s : sand.keySet()){
			image = sand.get(s);
			image.rotate(180);
			temp.put("B"+s.substring(1), image);
		}
		this.sand.putAll(temp);
		// importing C and D
		temp.clear();
		for(String s : sand.keySet()){
			image = sand.get(s);
			image.rotate(180);
			temp.put((s.charAt(0)=='A' ? "C":"D")+s.substring(1), image);
		}
		this.sand.putAll(temp);
		this.sand.put("E", im.getSubImage(128*2, 128*2, 256, 256).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
	}
	
	public void resizeBuilding(String s){
		Point p = Data.getSize(s);
		this.images.put("building"+s+"blue",this.images.get("building"+s+"blue")
				.getScaledCopy((int)(2*p.getX()/1.8), (int)(3*p.getY()/(2))));
		if(!s.equals("headquarters"))
			this.images.put("building"+s+"neutral",this.images.get("building"+s+"neutral")
					.getScaledCopy((int)(2*p.getX()/1.8), (int)(3*p.getY()/(2))));
		this.images.put("building"+s+"red",this.images.get("building"+s+"red")
				.getScaledCopy((int)(2*p.getX()/1.8), (int)(3*p.getY()/(2))));
		
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
				} else if (!s.contains(".") && !s.equals("unit")){
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
	
	private void initializeUnits(){
		File repertoire = new File("ressources/images/unit");
		File[] files=repertoire.listFiles();
		String s,s2,s4;
		this.imagesUnits = new HashMap<String, HashMap<String, Image>>();
		this.imagesUnits.put("spearman", new HashMap<String, Image>());
		this.imagesUnits.put("crossbowman", new HashMap<String, Image>());
		this.imagesUnits.put("knight", new HashMap<String, Image>());
		this.imagesUnits.put("priest", new HashMap<String, Image>());
		this.imagesUnits.put("inquisitor", new HashMap<String, Image>());
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
					for(String s1 : this.imagesUnits.keySet()){
						if(s.toLowerCase().contains(s1)){
							s2 = (s.toLowerCase().startsWith("attack") ? "1" : "0");
							s4 = (s.toLowerCase().endsWith("blue") ? "1" : "2");
							for(int orientation =0; orientation<4; orientation++){
								for(int animation=0; animation<5; animation++){
									this.imagesUnits.get(s1).put(s2+""+orientation+""+animation+""+s4, 
											im.getSubImage(imageWidth*animation,imageHeight*(int)orientation,imageWidth,imageHeight)
									.getScaledCopy(120*Main.ratioSpace/imageWidth));
								}
							}
						}
					}
					this.images.put(s.toLowerCase(),im);
				}
			} 
			HashMap<String, Image> toAdd = new HashMap<String, Image>();
			for(String s1 : this.imagesUnits.keySet()){
				toAdd.clear();
				if(this.imagesUnits.get(s1).size()==40){
					for(String s3 : this.imagesUnits.get(s1).keySet()){
						toAdd.put("1"+s3.substring(1), this.imagesUnits.get(s1).get(s3));
					}
				}
				this.imagesUnits.get(s1).putAll(toAdd);
			}
		} catch (SlickException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		}
	}
	
	public Image getUnit(String name, int direction, int animation, int team, boolean attack){
		if(this.imagesUnits.containsKey(name) && this.imagesUnits.get(name).containsKey((attack?"1":"0")+direction+""+animation+"" +team)){
			return this.imagesUnits.get(name).get((attack?"1":"0")+direction+""+animation+"" +team);
		} else {
			try {
				throw new FatalGillesError("images d'unité non existante : "+name+":"+animation+" "+direction+" " +attack+" "+team);
			} catch (FatalGillesError e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void activateGdBMode() {
		Image GdB = this.images.get("gilles");
		this.oldimages = images;
		this.images = new HashMap<String, Image>();
		for(String s : this.oldimages.keySet()){
			this.images.put(s,GdB);
		}
	}
	
	public void deactivateGdBMode(){
		this.images.clear();
		this.images = this.oldimages;
	}

	public Image getSand(String s){
		return this.sand.get(s);
	}

	public void updateScaleSend(float stepGrid) {
		HashMap<String, Image> temp = new HashMap<String, Image>();
		for(String s : this.sand.keySet()){
			if(s.equals("E"))
				temp.put(s, sand.get(s).getScaledCopy((int)(stepGrid), (int)(stepGrid)));
			else
				temp.put(s, sand.get(s).getScaledCopy((int)(stepGrid/2), (int)(stepGrid/2)));
		}
		this.sand = temp;
		this.images.put("watertile", this.images.get("watertile").getScaledCopy((int)(2*stepGrid), (int)(2*stepGrid)));
		this.images.put("watertile2", this.images.get("watertile2").getScaledCopy((int)(stepGrid), (int)(stepGrid)));
	}
}
