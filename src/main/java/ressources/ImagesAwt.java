package ressources;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

import data.Data;
import main.Main;
import mapeditor.MainEditor.TeamSelected;
import mapeditor.PlateauObjectPanel;
import plateau.Building;
import tests.FatalGillesError;
import utils.ObjetsList;


public strictfp class ImagesAwt {

	private static HashMap<String, Image> images;
	private static HashMap<String, Image> oldimages;
	private static HashMap<ObjetsList, HashMap<String, Image>> imagesUnits;
	private static HashMap<String, Image> sand;	

	private static Data data;
	public static boolean gillesModeEnable = false;
	
	private static boolean isInit = false;

	public static void init(){
		if(!isInit){
			images = new HashMap<String, Image>();
			data = new Data();
			loadRepertoire("ressources/images/");
			initialize();
			isInit = true;
		}
	}
	
	public static boolean isInitialized(){
		return isInit;
	}

	private static void initialize() {
		// import other images that will serve during the game
		// icons
		HashMap<String, Image> toPut = new HashMap<String, Image>();
		for(String im : images.keySet()){
			if(im.contains("icon") || im.contains("tech")){
				toPut.put(im+"buildingsize", images.get(im).getScaledInstance(Building.sizeXIcon,Building.sizeXIcon,Image.SCALE_SMOOTH));
			}
		}
		images.putAll(toPut);

		// buildings
		resizeBuilding("academy");
//		resizeBuilding("barracks");
		resizeBuilding("headquarters");
//		resizeBuilding("mill");
//		resizeBuilding("mine");
//		resizeBuilding("stable");
		resizeBuilding("tower");
		resizeBuilding("university");
		
	}

	public static void resizeBuilding(String s){
		Point p = data.getSize(ObjetsList.get(s));
		images.put("building"+s+"blue",images.get("building"+s+"blue")
				.getScaledInstance((int)(2*p.getX()/1.8), (int)(3*p.getY()/(2)),Image.SCALE_SMOOTH));
		if(!s.equals("headquarters"))
			images.put("building"+s+"neutral",images.get("building"+s+"neutral")
					.getScaledInstance((int)(2*p.getX()/1.8), (int)(3*p.getY()/(2)),Image.SCALE_SMOOTH));
		images.put("building"+s+"red",images.get("building"+s+"red")
				.getScaledInstance((int)(2*p.getX()/1.8), (int)(3*p.getY()/(2)),Image.SCALE_SMOOTH));
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
					im = ImageIO.read(new File(name+s+".png"));
					images.put(s.toLowerCase(),im);
					//					f = Images.class.getField(s);
					//					f.set(this, im);
					//images.put(s, new Image(name+s+".png"));
				} else if(s.contains(".jpg")){
					// on load l'image
					s = s.substring(0, s.length()-4);
					im = ImageIO.read(new File(name+s+".jpg"));
					images.put(s,im);
					//images.put(s, new Image(name+s+".jpg"));
				} else if(s.contains(".svg")){
					// on load l'image
					s = s.substring(0, s.length()-4);
					im = ImageIO.read(new File(name+s+".svg"));
					images.put(s,im);
					//images.put(s, new Image(name+s+".svg"));
				} else if (!s.contains(".")){
					// nouveau répertoire
					loadRepertoire(name+s+"/");

				}
			} 
		} catch (SecurityException | IllegalArgumentException | IOException  e) {
			e.printStackTrace();
		}
	}

	public static Image get(String name){
		if(images.containsKey(name.toLowerCase())){
			return images.get(name.toLowerCase());
		} else {
			System.out.println("Error : trying to load an non-existing image : "+name);
//			try {
//				throw new FatalGillesError("non-existing image : "+name);
//			} catch (FatalGillesError e) {
//				e.printStackTrace();
//			}
			return get("image_manquante");
		}
	}

	public static Image getImage(ObjetsList o, int team, boolean icon){
		if(ImagesAwt.imagesEditor.containsKey(o) && ImagesAwt.imagesEditor.get(o).containsKey(team)  && ImagesAwt.imagesEditor.get(o).get(team).containsKey(icon) ){
			return ImagesAwt.imagesEditor.get(o).get(team).get(icon);
		}
		Image im = null;
		BufferedImage bi;
		try{
			switch(o.type){
			case Character:
				bi = (BufferedImage) get(o.name()+TeamSelected.getText(team));
				im = bi.getSubimage(0, 0, bi.getWidth()/5, bi.getHeight()/4);
				break;
			case Building:
				im = get("building"+o.name()+TeamSelected.getText(team));
				break;
			default:
				im = get(o.name());
				break;
			}
		} catch(Exception e){
			im = get("image_manquante");
		}
		if(icon){
			im = im.getScaledInstance(PlateauObjectPanel.buttonDimension.width, PlateauObjectPanel.buttonDimension.height, Image.SCALE_SMOOTH);
		}
		if(!ImagesAwt.imagesEditor.containsKey(o)){
			ImagesAwt.imagesEditor.put(o, new HashMap<Integer, HashMap<Boolean, Image>>());
		}
		if(!ImagesAwt.imagesEditor.get(o).containsKey(team)){
			ImagesAwt.imagesEditor.get(o).put(team, new HashMap<Boolean, Image>());
		}
		ImagesAwt.imagesEditor.get(o).get(team).put(icon, im);
		return im;
	}

	public static HashMap<ObjetsList, HashMap<Integer, HashMap<Boolean, Image>>> imagesEditor = new HashMap<ObjetsList, HashMap<Integer, HashMap<Boolean, Image>>>();

	
}
