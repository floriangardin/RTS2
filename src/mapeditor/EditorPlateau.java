package mapeditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import model.Data;
import model.Map;
import multiplaying.InputObject;

public class EditorPlateau {

	public MapEditor editor;

	public int maxX;
	public int maxY;

	public float sizeX;
	public float sizeY;
	
	public String name;

	public int Xcam;
	public int Ycam;
	
	public float stepGrid;

	public Vector<EditorObject> units;
	public Vector<EditorObject> buildings;
	public Vector<EditorObject> nature;

	public Image seaBackground;
	public Image grassTexture;

	public EditorObject headquartersBlue;
	public EditorObject headquartersRed;

	public EditorPlateau(MapEditor editor, int sizeX, int sizeY){
		this.editor = editor;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.stepGrid = 50f;
		this.seaBackground = editor.game.images.seaBackground;
		this.grassTexture = editor.game.images.grassTexture;
		this.units = new Vector<EditorObject>();
		this.buildings = new Vector<EditorObject>();
		this.nature = new Vector<EditorObject>();
	}

	public void update(InputObject im, Input in){
		if(in.isKeyPressed(Input.KEY_ADD)){
			this.updateScale(this.stepGrid-10f);
		}
		if(in.isKeyPressed(Input.KEY_SUBTRACT)){
			this.updateScale(this.stepGrid+10f);
		}
	} 

	public void draw(Graphics g){
		g.drawImage(this.seaBackground, -this.maxX*Map.stepGrid, -this.maxY*Map.stepGrid,
				2*this.maxX*Map.stepGrid, 2*this.maxY*Map.stepGrid, 0, 0, this.seaBackground.getWidth(),this.seaBackground.getHeight());

		g.drawImage(this.grassTexture,0, 0, this.maxX*Map.stepGrid, this.maxY*Map.stepGrid,
				0, 0, this.grassTexture.getWidth(),  this.grassTexture.getHeight());

		//Creation of the drawing Vector
		Vector<EditorObject> toDraw = new Vector<EditorObject>();
		for(EditorObject o : units){
			toDraw.add(o);
		}
		for(EditorObject o : buildings){
			toDraw.add(o);
		}
		for(EditorObject o : nature){
			toDraw.add(o);
		}
		if(headquartersBlue!=null)
			toDraw.add(headquartersBlue);
		if(headquartersRed!=null)
			toDraw.add(headquartersRed);

		triY(toDraw);

		for(EditorObject o: toDraw)
			o.draw(g);

	}
	
	public void updateScale(float newScale){
		this.stepGrid = newScale;
		for(EditorObject o : units){
			o.image = o.image.getScaledCopy(stepGrid/o.image.getWidth());
			o.stepGrid = newScale;
		}
		Data data = new Data();
		for(EditorObject o : buildings){
			sizeX = 0;
			switch(o.name){
			case "Mill" : sizeX = data.millSizeX/Map.stepGrid; break; 
			case "Mine" : sizeX = data.mineSizeX/Map.stepGrid;; break; 
			case "Barrack" : sizeX = data.barrackSizeX/Map.stepGrid; break; 
			case "Stable" : sizeX = data.stableSizeX/Map.stepGrid; break; 
			case "Academy" : sizeX = data.academySizeX/Map.stepGrid; break; 
			case "University" : sizeX = data.universitySizeX/Map.stepGrid;break; 
			case "Tower" : sizeX = data.towerSizeX/Map.stepGrid; break; 
			}
			o.image = o.image.getScaledCopy(sizeX*stepGrid/o.image.getWidth());
			o.stepGrid = newScale;
		}
		for(EditorObject o : nature){
			o.image = o.image.getScaledCopy(stepGrid/o.image.getWidth());
			o.stepGrid = newScale;
		}
		if(headquartersBlue!=null){
			headquartersBlue.image = headquartersBlue.image.getScaledCopy(3*stepGrid/headquartersBlue.image.getWidth());
			headquartersBlue.stepGrid = newScale;
		}
		if(headquartersRed!=null){
			headquartersRed.image = headquartersRed.image.getScaledCopy(3*stepGrid/headquartersRed.image.getWidth());
			headquartersRed.stepGrid = newScale;
		}
	}

	public void openFrom(String name){
		String fichier = "ressources/"+name+".rtsmap";
		this.name = name;
		try{
			//lecture du fichier texte	
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			Vector<String> buildings = new Vector<String>();
			Vector<String> units = new Vector<String>();
			Vector<String> naturalObjects = new Vector<String>();
			Vector<String> headquarters = new Vector<String>();
			Vector<String> currentVector = null;
			String ligne;
			boolean param;
			while ((ligne=br.readLine())!=null){
				param = false;
				if(ligne.length()<=0){
					continue;
				}
				switch(ligne.charAt(0)){
				case '#': continue; 
				case '&': param = true; break;
				case '=': 
					switch(ligne.substring(2)){
					case "buildings": currentVector = buildings; break;
					case "units": currentVector = units; break;
					case "naturalObjects": currentVector = naturalObjects; break;
					case "headquarters": currentVector = headquarters; break;
					default:
					} continue;
				default: 
				}
				if(ligne.length()>0){
					if(param){
						String[] tab = ligne.split(" ");
						switch(tab[1]){
						case "sizeX" : maxX = Integer.parseInt(tab[2]); break;
						case "sizeY" : maxY = Integer.parseInt(tab[2]); break;
						default:
						}
					} else if(currentVector!=null){
						currentVector.add(ligne);
					} else {
						throw new Exception();
					}
				}
			}
			br.close(); 
			// Création de la map
			// Headquarters
			for(int i=0;i<headquarters.size(); i++){
				// format:
				// team_x_y
				String[] tab = headquarters.get(i).split(" ");
				if(tab[0].equals("1"))
					headquartersBlue = new EditorObject("Headquarters",Integer.parseInt(tab[0]),1,editor.game.images.buildingHeadQuartersBlue,Integer.parseInt(tab[1]),Integer.parseInt(tab[2]));
				else
					headquartersRed = new EditorObject("Headquarters",Integer.parseInt(tab[0]),1,editor.game.images.buildingHeadQuartersRed,Integer.parseInt(tab[1]),Integer.parseInt(tab[2]));
			}
			// Buildings
			for(int i=0; i<buildings.size(); i++){
				//format
				// typeBuilding_team_x_y
				String[] tab = buildings.get(i).split(" ");
				if(tab[1].equals("0")){
					switch(tab[0]){
					// usual buildings
					case "Mill" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingMillNeutral,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Mine" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingMineNeutral,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Barrack" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingBarrackNeutral,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Stable" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingStableNeutral,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Academy" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingAcademyNeutral,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "University" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingUniversityNeutral,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Tower" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingTowerNeutral,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					// bonus
					case "BonusLifePoints" : this.buildings.addElement(new EditorObject(tab[0],0,2,editor.game.images.bonusLifePoints,Integer.parseInt(tab[1]),Integer.parseInt(tab[2])));break;
					case "BonusDamage" : this.buildings.addElement(new EditorObject(tab[0],0,2,editor.game.images.bonusDamage,Integer.parseInt(tab[1]),Integer.parseInt(tab[2])));break;
					case "BonusSpeed" : this.buildings.addElement(new EditorObject(tab[0],0,2,editor.game.images.bonusSpeed,Integer.parseInt(tab[1]),Integer.parseInt(tab[2])));break;
					default : 
					}
				} else if(tab[1].equals("1")){
					switch(tab[0]){
					// usual buildings
					case "Mill" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingMillBlue,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Mine" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingMineBlue,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Barrack" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingBarrackBlue,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Stable" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingStableBlue,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Academy" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingAcademyBlue,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "University" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingUniversityBlue,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Tower" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingTowerBlue,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					default : 
					}
				} else if(tab[1].equals("2")){
					switch(tab[0]){
					// usual buildings
					case "Mill" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingMillRed,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Mine" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingMineRed,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Barrack" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingBarrackRed,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Stable" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingStableRed,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Academy" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingAcademyRed,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "University" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingUniversityRed,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					case "Tower" : this.buildings.addElement(new EditorObject(tab[0],Integer.parseInt(tab[1]),1,editor.game.images.buildingTowerRed,Integer.parseInt(tab[2]),Integer.parseInt(tab[3])));break;
					default : 
					}
				}
			}
			// Units
			for(int i=0; i<units.size(); i++){
				String[] tab = units.get(i).split(" ");
				if(tab[1].equals("1")){
					switch(tab[0]){
					case "Spearman": this.units.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 0, editor.game.images.spearmanBlue, Float.parseFloat(tab[2]), Float.parseFloat(tab[3])));break;
					case "Crossbowman": this.units.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 0, editor.game.images.crossbowmanBlue, Float.parseFloat(tab[2]), Float.parseFloat(tab[3])));break;
					case "Knight": this.units.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 0, editor.game.images.knightBlue, Float.parseFloat(tab[2]), Float.parseFloat(tab[3])));break;
					case "Priest": this.units.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 0, editor.game.images.priestBlue, Float.parseFloat(tab[2]), Float.parseFloat(tab[3])));break;
					case "Inquisitor": this.units.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 0, editor.game.images.inquisitorBlue, Float.parseFloat(tab[2]), Float.parseFloat(tab[3])));break;
					case "Archange": this.units.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 0, editor.game.images.archangeBlue, Float.parseFloat(tab[2]), Float.parseFloat(tab[3])));break;
					}
				} else if(tab[1].equals("2")){
					switch(tab[0]){
					case "Spearman": this.units.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 0, editor.game.images.spearmanRed, Float.parseFloat(tab[2]), Float.parseFloat(tab[3])));break;
					case "Crossbowman": this.units.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 0, editor.game.images.crossbowmanRed, Float.parseFloat(tab[2]), Float.parseFloat(tab[3])));break;
					case "Knight": this.units.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 0, editor.game.images.knightRed, Float.parseFloat(tab[2]), Float.parseFloat(tab[3])));break;
					case "Priest": this.units.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 0, editor.game.images.priestRed, Float.parseFloat(tab[2]), Float.parseFloat(tab[3])));break;
					case "Inquisitor": this.units.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 0, editor.game.images.inquisitorRed, Float.parseFloat(tab[2]), Float.parseFloat(tab[3])));break;
					case "Archange": this.units.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 0, editor.game.images.archangeRed, Float.parseFloat(tab[2]), Float.parseFloat(tab[3])));break;
					}
				} else {

				}
			}
			// Vegetation
			for(int i=0; i<naturalObjects.size(); i++){
				String[] tab = naturalObjects.get(i).split(" ");
				switch(tab[0]){
				case "Tree": 
					switch(Integer.parseInt(tab[1])){
					case 1:	this.nature.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 3, editor.game.images.tree01, Integer.parseInt(tab[2]), Integer.parseInt(tab[3])));break;
					case 2: this.nature.addElement(new EditorObject(tab[0], Integer.parseInt(tab[1]), 3, editor.game.images.tree02, Integer.parseInt(tab[2]), Integer.parseInt(tab[3]))); break;
					} break;
				}
			}
			this.updateScale(50f);
		} catch (Exception e){
			System.out.print("erreur");
			e.printStackTrace();
		}
	}

	public void saveTo(String name){
		/**
		 * creating or changing the file name.rtsmap in location ressources
		 */
		try {
			FileWriter fw = new FileWriter ("ressources/"+name+".rtsmap");
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
			fichierSortie.println ("###########################");
			fichierSortie.println ("# Map "+name+" #");
			fichierSortie.println ("###########################");
			fichierSortie.println ();
			fichierSortie.println ();
			fichierSortie.println ("#Creator: " + this.editor.game.options.nickname);
			String format = "dd/MM/yyyy";
			java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
			java.util.Date date = new java.util.Date();
			fichierSortie.println ("# date: " + formater.format( date ));
			fichierSortie.println ();
			fichierSortie.println ("& sizeX "+maxX);
			fichierSortie.println ("& sizeY "+maxY);
			fichierSortie.println ();
			fichierSortie.println ();
			fichierSortie.println ("##############");
			fichierSortie.println ("= headquarters");
			fichierSortie.println ("##############");
			fichierSortie.println ();
			fichierSortie.println (headquartersBlue.team+" "+headquartersBlue.x+" "+headquartersBlue.y);
			fichierSortie.println (headquartersRed.team+" "+headquartersBlue.x+" "+headquartersBlue.y);
			fichierSortie.println ();
			fichierSortie.println ("##############");
			fichierSortie.println ("= buildings");
			fichierSortie.println ("##############");
			fichierSortie.println ();
			for(EditorObject o : this.buildings){
				fichierSortie.println(o.name+" "+o.team+" "+o.x+" "+o.y);
			}
			fichierSortie.println ();
			fichierSortie.println ("#######");
			fichierSortie.println ("= units");
			fichierSortie.println ("#######");
			fichierSortie.println ();
			for(EditorObject o : this.units){
				fichierSortie.println(o.name+" "+o.team+" "+o.x+" "+o.y);
			}
			fichierSortie.println ();
			fichierSortie.println ("###########");
			fichierSortie.println ("= naturalObjects");
			fichierSortie.println ("###########");
			fichierSortie.println ();
			for(EditorObject o : this.nature){
				fichierSortie.println(o.name+" "+o.team+" "+o.x+" "+o.y);
			}
			fichierSortie.close();

		}
		catch (Exception e){
			System.out.println(e.toString());
		}	
	}

	public static void triY(Vector<EditorObject> liste){
		if(liste.size()<=1)
			return;
		Vector<EditorObject> liste1 = new Vector<EditorObject>(), liste2= new Vector<EditorObject>();
		for(int i=0;i<liste.size();i++){
			if(i<liste.size()/2)
				liste1.add(liste.get(i));
			else
				liste2.add(liste.get(i));
		}
		liste.clear();
		triY(liste1);
		triY(liste2);
		float y1=0f,y2=0f;
		boolean b1=true, b2=true;
		while(true){
			b1 = !liste1.isEmpty();
			b2 = !liste2.isEmpty();
			if(!b1 && !b2)
				break;
			if(!b1){
				liste.add(liste2.firstElement());
				liste2.remove(0);
				continue;
			}
			if(!b2){
				liste.add(liste1.firstElement());
				liste1.remove(0);
				continue;
			}
			y1 = liste1.firstElement().y;
			y2 = liste2.firstElement().y;
			if(y1<y2){
				liste.add(liste1.firstElement());
				liste1.remove(0);
			} else {
				liste.add(liste2.firstElement());
				liste2.remove(0);
			}
		}

	}
}
