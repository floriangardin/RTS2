package mapeditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;

import org.newdawn.slick.Color;
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

	public float Xcam;
	public float Ycam;

	public float ACCX = 0;
	public float ACCY = 0;

	public float stepGrid;

	public Vector<EditorObject> units;
	public Vector<EditorObject> buildings;
	public Vector<EditorObject> nature;
	public Vector<EditorObject> headquartersBlue;
	public Vector<EditorObject> headquartersRed;

	public boolean[][] collision;

	public Image seaBackground;
	public Image grassTexture;


	public int Xclicked, Yclicked;
	public EditorObject mouseOverObject;
	public Vector<EditorObject> depotFromMouseOverObject;
	public boolean objetFromObjetBar = false;

	public Vector<EditorAction> actions = new Vector<EditorAction>();
	public boolean downZ, downY;

	public EditorPlateau(MapEditor editor, int maxX, int maxY){
		this.editor = editor;
		this.sizeX = editor.game.resX;
		this.sizeY = editor.game.resY;
		this.maxX = maxX;
		this.maxY = maxY;
		this.stepGrid = 50f;
		collision = new boolean[maxX][maxY];
		Xcam = 0;
		Ycam = 0;
		this.seaBackground = editor.game.images.seaBackground;
		this.grassTexture = editor.game.images.grassTexture;
		this.units = new Vector<EditorObject>();
		this.buildings = new Vector<EditorObject>();
		this.nature = new Vector<EditorObject>();
		this.headquartersBlue = new Vector<EditorObject>();
		this.headquartersRed = new Vector<EditorObject>();
	}

	public void update(InputObject im, Input in){
		if(in.isKeyPressed(Input.KEY_ADD)){
			this.zoomPlus();
		}
		if(in.isKeyPressed(Input.KEY_SUBTRACT)){
			this.zoomMoins();
		}
		updateScale(stepGrid);
		if(in.isKeyDown(Input.KEY_UP)){
			this.Ycam-=5;
		} else if(in.isKeyDown(Input.KEY_DOWN)){
			this.Ycam+=5;
		} 
		if(in.isKeyDown(Input.KEY_LEFT)){
			this.Xcam-=5;
		} else if(in.isKeyDown(Input.KEY_RIGHT)){
			this.Xcam+=5;
		} 

		// gestion de l'annulation
		if(in.isKeyDown(Input.KEY_LCONTROL)){
			if(!in.isKeyDown(Input.KEY_Z)){
				if(downZ){
					// control z
					int i = 0;
					while(i<this.actions.size() && this.actions.get(i).performed==false){
						i++;
					}
					if(i<this.actions.size()){
						this.actions.get(i).moveBackward();
					}
				}
				downZ = false;
			}
			if(!downZ && in.isKeyDown(Input.KEY_Z)){
				downZ = true;
			}
		}
		// gestion de l'annulation
		if(in.isKeyDown(Input.KEY_LCONTROL)){
			if(!in.isKeyDown(Input.KEY_Y)){
				if(downY){
					// control y
					int i = 0;
					while(i<this.actions.size() && this.actions.get(i).performed==false){
						i++;
					}
					if(i>0){
						this.actions.get(i-1).moveForward();
					}
				}
				downY = false;
			}
			if(!downY && in.isKeyDown(Input.KEY_Y)){
				downY = true;
			}
		}

		// gestion de la souris
		if(editor.draggedObject == null){
			this.mouseOverObject = getObjectAt((int)(im.xMouse+Xcam), (int)(im.yMouse+Ycam));
		}
		if(in.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			if(editor.draggedObject == null){
				// déplacement de la caméra
				Xcam -= im.xMouse-Xclicked;
				Ycam -= im.yMouse-Yclicked;
				if(this.mouseOverObject!=null){
					editor.draggedObject = mouseOverObject;
					if(depotFromMouseOverObject!=null)
						depotFromMouseOverObject.remove(mouseOverObject);
					editor.tempX = mouseOverObject.x;
					editor.tempY = mouseOverObject.y;
					this.setCollision(mouseOverObject, false);
				}
			} else {
				// déplacer l'objet
				editor.draggedObject.x = (int)((Xcam+im.xMouse-editor.draggedObject.sizeX/2f)/stepGrid);
				editor.draggedObject.y = (int)((Ycam+im.yMouse-editor.draggedObject.sizeY/2f)/stepGrid);
			}
		} else{
			if(editor.draggedObject!=null){
				if(editor.objectBar.startX<editor.game.resX){
					//suppression de l'objet
					editor.draggedObject.x = editor.tempX;
					editor.draggedObject.y = editor.tempY;
					this.addAction(EditorAction.getSuppression(editor.plateau,editor.draggedObject, depotFromMouseOverObject));
					editor.draggedObject = null;
					this.actions.firstElement().performed = true;

				} else if(!getCollision(editor.draggedObject)){
					if(depotFromMouseOverObject!=null)
						depotFromMouseOverObject.add(editor.draggedObject);
					this.setCollision(editor.draggedObject, true);
					if(this.objetFromObjetBar){
						// ajout de l'objet
						this.addAction(EditorAction.getCreation(editor.plateau,editor.draggedObject, editor.draggedObject.x, editor.draggedObject.y, depotFromMouseOverObject));
						this.objetFromObjetBar = false;
					} else {
						// déplacement de l'objet
						this.addAction(EditorAction.getDeplacement(editor.plateau,editor.draggedObject, editor.tempX, editor.tempY, editor.draggedObject.x, editor.draggedObject.y, depotFromMouseOverObject));
					}
					editor.draggedObject = null;
					this.actions.firstElement().performed = true;
				} else {
					if(depotFromMouseOverObject!=null)
						depotFromMouseOverObject.add(editor.draggedObject);
					editor.draggedObject.x = editor.tempX;
					editor.draggedObject.y = editor.tempY;
					this.setCollision(editor.draggedObject, true);
					editor.draggedObject = null;
				}
			}

		}
		Xclicked = im.xMouse;
		Yclicked = im.yMouse;
	} 

	public void draw(Graphics g){
		g.translate(-Xcam, -Ycam);
		g.drawImage(this.seaBackground, -this.maxX*stepGrid, -this.maxY*stepGrid,
				2*this.maxX*stepGrid, 2*this.maxY*stepGrid, 0, 0, this.seaBackground.getWidth(),this.seaBackground.getHeight());

		g.drawImage(this.grassTexture,0, 0, this.maxX*stepGrid, this.maxY*stepGrid,
				0, 0, this.grassTexture.getWidth(),  this.grassTexture.getHeight());

		// affichage de la grille
		if(editor.optionGridOn){
			g.setColor(Color.gray);
			for(int i=0; i<=maxX; i++){
				g.drawLine(i*stepGrid, 0, i*stepGrid, maxY*stepGrid);
			}
			for(int i=0; i<=maxY; i++){
				g.drawLine(0, i*stepGrid, maxX*stepGrid, i*stepGrid);
			}
			for(EditorObject o : this.buildings){
				if(o.name.startsWith("Bonus"))
					continue;
				switch(o.team){
				case 0 : g.setColor(Color.white);break;
				case 1 : g.setColor(Color.blue);break;
				case 2 : g.setColor(Color.red);break;
				default:
				}
				if(o==this.mouseOverObject)
					g.setColor(Color.green);
				g.drawRect(o.x*stepGrid-1f,o.y*stepGrid-1f,o.sizeX*stepGrid+1f,o.sizeY*stepGrid+1f);
			}
			// hq bleu
			if(headquartersBlue.size()>0){
				g.setColor(Color.blue);
				EditorObject o = headquartersBlue.get(0);
				if(o==this.mouseOverObject)
					g.setColor(Color.green);
				g.drawRect(o.x*stepGrid-1f,o.y*stepGrid-1f,o.sizeX*stepGrid+1f,o.sizeY*stepGrid+1f);
			}
			// hq rouge
			if(headquartersRed.size()>0){
				g.setColor(Color.red);
				EditorObject o = headquartersRed.get(0);
				if(o==this.mouseOverObject)
					g.setColor(Color.green);
				g.drawRect(o.x*stepGrid-1f,o.y*stepGrid-1f,o.sizeX*stepGrid+1f,o.sizeY*stepGrid+1f);
			}
		}
		if(editor.optionCollisionOn){
			for(int i=0; i<maxX; i++){
				for(int j=0; j<maxY; j++){
					if(collision[i][j]){
						g.setColor(new Color(155,0,0,50));
						g.fillRect(i*stepGrid, j*stepGrid, stepGrid, stepGrid);
					}
				}
			}
		}

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
		if(headquartersBlue.size()>0)
			toDraw.add(headquartersBlue.get(0));
		if(headquartersRed.size()>0)
			toDraw.add(headquartersRed.get(0));


		triY(toDraw);

		for(EditorObject o: toDraw)
			o.draw(g);

		if(editor.draggedObject!=null){
			boolean b = getCollision(editor.draggedObject);
			if(b)
				g.setColor(new Color(200,0,0,75));
			else
				g.setColor(new Color(0,200,0,75));
			g.fillRect(editor.draggedObject.x*stepGrid, editor.draggedObject.y*stepGrid, editor.draggedObject.sizeX*stepGrid, editor.draggedObject.sizeY*stepGrid);
			if(b)
				g.setColor(Color.red);
			else
				g.setColor(Color.green);
			g.drawRect(editor.draggedObject.x*stepGrid, editor.draggedObject.y*stepGrid, editor.draggedObject.sizeX*stepGrid, editor.draggedObject.sizeY*stepGrid);
			editor.draggedObject.draw(g);
		}

		g.translate(Xcam, Ycam);

	}

	public EditorObject getObjectAt(int x, int y){
		for(EditorObject o : this.buildings){
			if(o.x*stepGrid<x && x<(o.x+o.sizeX)*stepGrid && o.y*stepGrid<y && y<(o.y+o.sizeY)*stepGrid ){
				this.depotFromMouseOverObject = buildings;
				return o;
			}
		}
		for(EditorObject o : this.units){
			if(o.x*stepGrid<x && x<(o.x+o.sizeX)*stepGrid && o.y*stepGrid<y && y<(o.y+o.sizeY)*stepGrid ){
				this.depotFromMouseOverObject = units;
				return o;
			}
		}
		for(EditorObject o : this.nature){
			if(o.x*stepGrid<x && x<(o.x+o.sizeX)*stepGrid && o.y*stepGrid<y && y<(o.y+o.sizeY)*stepGrid ){
				this.depotFromMouseOverObject = nature;
				return o;
			}
		}
		for(EditorObject o : this.headquartersBlue){
			if(o.x*stepGrid<x && x<(o.x+o.sizeX)*stepGrid && o.y*stepGrid<y && y<(o.y+o.sizeY)*stepGrid ){
				this.depotFromMouseOverObject = headquartersBlue;
				return o;
			}
		}
		for(EditorObject o : this.headquartersRed){
			if(o.x*stepGrid<x && x<(o.x+o.sizeX)*stepGrid && o.y*stepGrid<y && y<(o.y+o.sizeY)*stepGrid ){
				this.depotFromMouseOverObject = headquartersRed;
				return o;
			}
		}
		return null;
	}

	public void addAction(EditorAction e){
		while(this.actions.size()>0 && !this.actions.get(0).performed){
			this.actions.removeElementAt(0);
		}
		this.actions.insertElementAt(e, 0);
	}

	public void zoomPlus(){
		stepGrid = Math.min(200f, stepGrid+10f);
		updateScale(stepGrid);
	}
	public void zoomMoins(){
		stepGrid = Math.max(20f, stepGrid-10f);
		updateScale(stepGrid);
	}

	public void updateScale(float newScale){
		this.stepGrid = Math.min(200f, Math.max(20f, newScale));
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
			o.image = o.image.getScaledCopy(2*stepGrid/o.image.getWidth());
			o.stepGrid = newScale;
		}
		if(headquartersBlue.size()>0){
			headquartersBlue.get(0).image = headquartersBlue.get(0).image.getScaledCopy(3*stepGrid/headquartersBlue.get(0).image.getWidth());
			headquartersBlue.get(0).stepGrid = newScale;
		}
		if(headquartersRed.size()>0){
			headquartersRed.get(0).image = headquartersRed.get(0).image.getScaledCopy(3*stepGrid/headquartersRed.get(0).image.getWidth());
			headquartersRed.get(0).stepGrid = newScale;
		}
		if(editor.draggedObject!=null){
			EditorObject o = editor.draggedObject;
			switch(editor.draggedObject.clas){
			case 0 : 
				o.image = o.image.getScaledCopy(stepGrid/o.image.getWidth());
				o.stepGrid = newScale;
				break;
			case 1: 
			case 2: //building
				sizeX = 0;
				switch(o.name){
				case "Mill" : sizeX = data.millSizeX/Map.stepGrid; break; 
				case "Mine" : sizeX = data.mineSizeX/Map.stepGrid;; break; 
				case "Barrack" : sizeX = data.barrackSizeX/Map.stepGrid; break; 
				case "Stable" : sizeX = data.stableSizeX/Map.stepGrid; break; 
				case "Academy" : sizeX = data.academySizeX/Map.stepGrid; break; 
				case "University" : sizeX = data.universitySizeX/Map.stepGrid;break; 
				case "Tower" : sizeX = data.towerSizeX/Map.stepGrid; break; 
				case "Headquarters" : sizeX = data.headQuartersSizeX/Map.stepGrid; break;
				}
				o.image = o.image.getScaledCopy(sizeX*stepGrid/o.image.getWidth());
				o.stepGrid = newScale;
				break;
			case 3: //nature
				o.image = o.image.getScaledCopy(2*stepGrid/o.image.getWidth());
				o.stepGrid = newScale;
				break;
			default:	
			}
		}
	}

	public void setCollision(EditorObject o, boolean toSet){
		for(int i=(int)o.x; i<(int)o.x+o.sizeX; i++){
			for(int j=(int)o.y; j<(int)o.y+o.sizeY; j++){
				if(i>=0 && i<maxX && j>=0 && j<maxY)
					collision[i][j] = toSet;
			}
		}
	}

	public boolean getCollision(EditorObject o){
		boolean ok = false;
		for(int i=(int)o.x; i<(int)o.x+o.sizeX; i++){
			for(int j=(int)o.y; j<(int)o.y+o.sizeY; j++){
				if(i>=0 && i<maxX && j>=0 && j<maxY)
					ok = ok ||collision[i][j];
			}
		}
		return ok;
	}

	public EditorObject addBuilding(EditorObject o){
		this.buildings.addElement(o);
		this.setCollision(o, true);
		return o;
	}
	public EditorObject removeBuilding(EditorObject o){
		this.buildings.removeElement(o);
		this.setCollision(o, false);
		return o;
	}
	public EditorObject addNature(EditorObject o){
		this.nature.addElement(o);
		this.setCollision(o, true);
		return o;
	}
	public EditorObject removeNature(EditorObject o){
		this.nature.removeElement(o);
		this.setCollision(o, false);
		return o;
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
						case "sizeX" : maxX = (int)Float.parseFloat(tab[2]); 
						collision = new boolean[maxX][maxY];
						break;
						case "sizeY" : maxY = (int)Float.parseFloat(tab[2]); 
						collision = new boolean[maxX][maxY];
						break;
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
			Data data = new Data();
			// Headquarters
			for(int i=0;i<headquarters.size(); i++){
				// format:
				// team_x_y
				String[] tab = headquarters.get(i).split(" ");
				if(tab[0].equals("1")){
					headquartersBlue.addElement(new EditorObject("Headquarters",(int)Float.parseFloat(tab[0]),1,editor.game.images.buildingHeadQuartersBlue,(int)Float.parseFloat(tab[1]),(int)Float.parseFloat(tab[2]),(int)(data.headQuartersSizeX/Map.stepGrid),(int)(data.headQuartersSizeY/Map.stepGrid)));
					this.setCollision(headquartersBlue.get(0), true);
				} else {
					headquartersRed.addElement(new EditorObject("Headquarters",(int)Float.parseFloat(tab[0]),1,editor.game.images.buildingHeadQuartersRed,(int)Float.parseFloat(tab[1]),(int)Float.parseFloat(tab[2]),(int)(data.headQuartersSizeX/Map.stepGrid),(int)(data.headQuartersSizeY/Map.stepGrid)));
					this.setCollision(headquartersRed.get(0), true);
				}
			}
			// Buildings
			for(int i=0; i<buildings.size(); i++){
				//format
				// typeBuilding_team_x_y
				String[] tab = buildings.get(i).split(" ");
				if(tab[1].equals("0")){
					switch(tab[0]){
					// usual buildings
					case "Mill" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingMillNeutral,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.millSizeX/Map.stepGrid),(int)(data.millSizeY/Map.stepGrid)));break;
					case "Mine" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingMineNeutral,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.mineSizeX/Map.stepGrid),(int)(data.mineSizeY/Map.stepGrid)));break;
					case "Barrack" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingBarrackNeutral,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.barrackSizeX/Map.stepGrid),(int)(data.barrackSizeY/Map.stepGrid)));break;
					case "Stable" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingStableNeutral,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.stableSizeX/Map.stepGrid),(int)(data.stableSizeY/Map.stepGrid)));break;
					case "Academy" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingAcademyNeutral,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.academySizeX/Map.stepGrid),(int)(data.academySizeY/Map.stepGrid)));break;
					case "University" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingUniversityNeutral,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.universitySizeX/Map.stepGrid),(int)(data.universitySizeY/Map.stepGrid)));break;
					case "Tower" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingTowerNeutral,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.towerSizeX/Map.stepGrid),(int)(data.towerSizeY/Map.stepGrid)));break;
					// bonus
					case "BonusLifePoints" : this.addBuilding(new EditorObject(tab[0],0,2,editor.game.images.bonusLifePoints,(int)Float.parseFloat(tab[1]),(int)Float.parseFloat(tab[2]),1,1));break;
					case "BonusDamage" : this.addBuilding(new EditorObject(tab[0],0,2,editor.game.images.bonusDamage,(int)Float.parseFloat(tab[1]),(int)Float.parseFloat(tab[2]),1,1));break;
					case "BonusSpeed" : this.addBuilding(new EditorObject(tab[0],0,2,editor.game.images.bonusSpeed,(int)Float.parseFloat(tab[1]),(int)Float.parseFloat(tab[2]),1,1));break;
					default : 
					}
				} else if(tab[1].equals("1")){
					switch(tab[0]){
					// usual buildings
					case "Mill" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingMillBlue,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.millSizeX/Map.stepGrid),(int)(data.millSizeY/Map.stepGrid)));break;
					case "Mine" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingMineBlue,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.mineSizeX/Map.stepGrid),(int)(data.mineSizeY/Map.stepGrid)));break;
					case "Barrack" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingBarrackBlue,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.barrackSizeX/Map.stepGrid),(int)(data.barrackSizeY/Map.stepGrid)));break;
					case "Stable" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingStableBlue,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.stableSizeX/Map.stepGrid),(int)(data.stableSizeY/Map.stepGrid)));break;
					case "Academy" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingAcademyBlue,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.academySizeX/Map.stepGrid),(int)(data.academySizeY/Map.stepGrid)));break;
					case "University" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingUniversityBlue,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.universitySizeX/Map.stepGrid),(int)(data.universitySizeY/Map.stepGrid)));break;
					case "Tower" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingTowerBlue,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.towerSizeX/Map.stepGrid),(int)(data.towerSizeY/Map.stepGrid)));break;
					default : 
					}
				} else if(tab[1].equals("2")){
					switch(tab[0]){
					// usual buildings

					case "Mill" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingMillRed,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.millSizeX/Map.stepGrid),(int)(data.millSizeY/Map.stepGrid)));break;
					case "Mine" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingMineRed,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.mineSizeX/Map.stepGrid),(int)(data.mineSizeY/Map.stepGrid)));break;
					case "Barrack" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingBarrackRed,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.barrackSizeX/Map.stepGrid),(int)(data.barrackSizeY/Map.stepGrid)));break;
					case "Stable" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingStableRed,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.stableSizeX/Map.stepGrid),(int)(data.stableSizeY/Map.stepGrid)));break;
					case "Academy" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingAcademyRed,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.academySizeX/Map.stepGrid),(int)(data.academySizeY/Map.stepGrid)));break;
					case "University" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingUniversityRed,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.universitySizeX/Map.stepGrid),(int)(data.universitySizeY/Map.stepGrid)));break;
					case "Tower" : this.addBuilding(new EditorObject(tab[0],(int)Float.parseFloat(tab[1]),1,editor.game.images.buildingTowerRed,(int)Float.parseFloat(tab[2]),(int)Float.parseFloat(tab[3]),(int)(data.towerSizeX/Map.stepGrid),(int)(data.towerSizeY/Map.stepGrid)));break;
					default : 
					}
				}
			}
			// Units
			for(int i=0; i<units.size(); i++){
				String[] tab = units.get(i).split(" ");
				if(tab[1].equals("1")){
					switch(tab[0]){
					case "Spearman": this.units.addElement(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 0, editor.game.images.spearmanBlue, Float.parseFloat(tab[2]), Float.parseFloat(tab[3]),0,0));break;
					case "Crossbowman": this.units.addElement(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 0, editor.game.images.crossbowmanBlue, Float.parseFloat(tab[2]), Float.parseFloat(tab[3]),0,0));break;
					case "Knight": this.units.addElement(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 0, editor.game.images.knightBlue, Float.parseFloat(tab[2]), Float.parseFloat(tab[3]),0,0));break;
					case "Priest": this.units.addElement(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 0, editor.game.images.priestBlue, Float.parseFloat(tab[2]), Float.parseFloat(tab[3]),0,0));break;
					case "Inquisitor": this.units.addElement(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 0, editor.game.images.inquisitorBlue, Float.parseFloat(tab[2]), Float.parseFloat(tab[3]),0,0));break;
					case "Archange": this.units.addElement(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 0, editor.game.images.archangeBlue, Float.parseFloat(tab[2]), Float.parseFloat(tab[3]),0,0));break;
					}
				} else if(tab[1].equals("2")){
					switch(tab[0]){
					case "Spearman": this.units.addElement(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 0, editor.game.images.spearmanRed, Float.parseFloat(tab[2]), Float.parseFloat(tab[3]),0,0));break;
					case "Crossbowman": this.units.addElement(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 0, editor.game.images.crossbowmanRed, Float.parseFloat(tab[2]), Float.parseFloat(tab[3]),0,0));break;
					case "Knight": this.units.addElement(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 0, editor.game.images.knightRed, Float.parseFloat(tab[2]), Float.parseFloat(tab[3]),0,0));break;
					case "Priest": this.units.addElement(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 0, editor.game.images.priestRed, Float.parseFloat(tab[2]), Float.parseFloat(tab[3]),0,0));break;
					case "Inquisitor": this.units.addElement(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 0, editor.game.images.inquisitorRed, Float.parseFloat(tab[2]), Float.parseFloat(tab[3]),0,0));break;
					case "Archange": this.units.addElement(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 0, editor.game.images.archangeRed, Float.parseFloat(tab[2]), Float.parseFloat(tab[3]),0,0));break;
					}
				} else {

				}
			}
			// Vegetation
			for(int i=0; i<naturalObjects.size(); i++){
				String[] tab = naturalObjects.get(i).split(" ");
				switch(tab[0]){
				case "Tree": 
					switch((int)Float.parseFloat(tab[1])){
					case 1:	this.addNature(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 3, editor.game.images.tree01, (int)Float.parseFloat(tab[2]), (int)Float.parseFloat(tab[3]),1,1));break;
					case 2: this.addNature(new EditorObject(tab[0], (int)Float.parseFloat(tab[1]), 3, editor.game.images.tree02, (int)Float.parseFloat(tab[2]), (int)Float.parseFloat(tab[3]),1,1)); break;
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
			fichierSortie.println (headquartersBlue.get(0).team+" "+headquartersBlue.get(0).x+" "+headquartersBlue.get(0).y);
			fichierSortie.println (headquartersRed.get(0).team+" "+headquartersRed.get(0).x+" "+headquartersRed.get(0).y);
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
			e.printStackTrace();
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
