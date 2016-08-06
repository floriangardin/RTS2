package mapeditor;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import data.Attributs;
import data.Data;
import model.Game;
import ressources.Map;

public class ObjectBar {

	public MapEditor editor;

	public float startX;
	public float sizeX;
	public float sizeY;

	public float startYClass;
	public float startYCiv;
	public float startYColor;
	public float startYObjects;

	public int selectedClass = 0;
	public int overClass = -1;
	public int selectedCiv = 0;	
	public int overCiv = -1;	
	public int selectedColor = 0;	
	public int overColor = -1;	
	public int overObject = -1;	

	public Vector<EditorObject> objects;

	public Vector<EditorObject> unitsDualistsBlue;
	public Vector<EditorObject> unitsZinaidsBlue;
	public Vector<EditorObject> unitsKitanosBlue;
	public Vector<EditorObject> unitsDualistsRed;
	public Vector<EditorObject> unitsZinaidsRed;
	public Vector<EditorObject> unitsKitanosRed;
	public Vector<EditorObject> unitsDualistsNeutral;
	public Vector<EditorObject> unitsZinaidsNeutral;
	public Vector<EditorObject> unitsKitanosNeutral;

	public Vector<EditorObject> buildingsBlue;
	public Vector<EditorObject> buildingsRed;
	public Vector<EditorObject> buildingsNeutral;
	public Vector<EditorObject> others;
	public Vector<EditorObject> nature;

	public ObjectBar(MapEditor editor){
		this.editor = editor;

		this.startX = 5*editor.game.resX/5f;
		this.sizeX = editor.game.resX/5f;
		this.sizeY = editor.game.resY;

		this.startYClass = 0f;
		this.startYCiv = 2f*editor.game.resY/20f;
		this.startYColor = 4f*editor.game.resY/20f;
		this.startYObjects = 6f*editor.game.resY/20f;

		unitsDualistsBlue = new Vector<EditorObject>();
		unitsZinaidsBlue = new Vector<EditorObject>();
		unitsKitanosBlue = new Vector<EditorObject>();
		unitsDualistsRed = new Vector<EditorObject>();
		unitsZinaidsRed = new Vector<EditorObject>();
		unitsKitanosRed = new Vector<EditorObject>();
		unitsDualistsNeutral = new Vector<EditorObject>();
		unitsKitanosNeutral = new Vector<EditorObject>();
		unitsZinaidsNeutral = new Vector<EditorObject>();

		buildingsBlue = new Vector<EditorObject>();
		buildingsRed = new Vector<EditorObject>();
		buildingsNeutral = new Vector<EditorObject>();

		others = new Vector<EditorObject>();
		nature = new Vector<EditorObject>();

		this.objects = unitsDualistsNeutral;

		Data data = new Data(0);

		// dualists
		// blue
		unitsDualistsBlue.add(new EditorObject("Spearman", 1, 0,this.editor.game.images.get("spearmanBlue"),0,0,this,1,1));
		unitsDualistsBlue.add(new EditorObject("Crossbowman", 1, 0,this.editor.game.images.get("crossbowmanBlue"),0,0,this,1,1));
		unitsDualistsBlue.add(new EditorObject("Knight", 1, 0,this.editor.game.images.get("knightBlue"),0,0,this,1,1));
		unitsDualistsBlue.add(new EditorObject("Inquisitor", 1, 0,this.editor.game.images.get("inquisitorBlue"),0,0,this,1,1));
		// red
		unitsDualistsRed.add(new EditorObject("Spearman", 2,0, this.editor.game.images.get("spearmanRed"),0,0,this,1,1));
		unitsDualistsRed.add(new EditorObject("Crossbowman", 2, 0,this.editor.game.images.get("crossbowmanRed"),0,0,this,1,1));
		unitsDualistsRed.add(new EditorObject("Knight", 2, 0,this.editor.game.images.get("knightRed"),0,0,this,1,1));
		unitsDualistsRed.add(new EditorObject("Inquisitor", 2,0, this.editor.game.images.get("inquisitorRed"),0,0,this,1,1));

		// buildings
		// neutral
		for(String s : new String[]{"Mill","Mine","Barracks","Stable","University","Tower"}){			
			if(!s.equals("Headquarters")){
				buildingsNeutral.add(new EditorObject(s, 0, 1,this.editor.game.images.get("building"+s+"Neutral"),0,0,this,(int)(Game.g.data.getAttribut(s, Attributs.sizeX)/Map.stepGrid),(int)(Game.g.data.getAttribut(s, Attributs.sizeY)/Map.stepGrid)));
				buildingsBlue.add(new EditorObject(s, 1, 1,this.editor.game.images.get("building"+s+"blue"),0,0,this,(int)(Game.g.data.getAttribut(s, Attributs.sizeX)/Map.stepGrid),(int)(Game.g.data.getAttribut(s, Attributs.sizeY)/Map.stepGrid)));
				buildingsRed.add(new EditorObject(s, 2, 1,this.editor.game.images.get("building"+s+"red"),0,0,this,(int)(Game.g.data.getAttribut(s, Attributs.sizeX)/Map.stepGrid),(int)(Game.g.data.getAttribut(s, Attributs.sizeY)/Map.stepGrid)));
			}
		}
		
		// others
		others.add(new EditorObject("BonusLifePoints", 0, 2,this.editor.game.images.get("bonusLifePoints"),0,0,this,1,1));
		others.add(new EditorObject("BonusDamage", 0,2, this.editor.game.images.get("bonusDamage"),0,0,this,1,1));
		others.add(new EditorObject("BonusSpeed", 0, 2,this.editor.game.images.get("bonusSpeed"),0,0,this,1,1));

		// nature
		nature.add(new EditorObject("Tree", 0, 3,this.editor.game.images.get("tree01"), 0, 0,this,1,1));
		nature.add(new EditorObject("Tree", 1, 3,this.editor.game.images.get("tree02"), 0, 0,this,1,1));

	}

	public void draw(Graphics gc){
		gc.setColor(Color.white);
		gc.fillRect(startX-2f, -2f, sizeX+4f, sizeY+4f);
		gc.setColor(Color.black);
		gc.fillRect(startX, 0, sizeX, sizeY);
		gc.setColor(Color.white);
		gc.fillRect(startX, startYCiv-1f, sizeX, 2f);
		gc.fillRect(startX, startYColor-1f, sizeX, 2f);
		gc.fillRect(startX, startYObjects-1f, sizeX, 2f);

		if(this.startX>=editor.game.resX){
			gc.setColor(Color.white);
			gc.fillRect(39*editor.game.resX/40f-2f, editor.game.resY/2-editor.game.resY/10-2f, editor.game.resX/40f+2f, editor.game.resY/5+4f);
			gc.setColor(Color.black);
			gc.fillRect(39*editor.game.resX/40f, editor.game.resY/2-editor.game.resY/10, editor.game.resX/40f, editor.game.resY/5);
		}
		//classes
		String s = "";
		for(int i=0; i<4; i++){
			switch(i){
			case 0: s = "Unités";break;
			case 1: s = "Bâtiments";break;
			case 2: s = "Autres"; break;
			case 3: s = "Nature"; break;
			default:
			}
			if(selectedClass == i)
				gc.setColor(Color.white);
			else if(overClass == i)
				gc.setColor(Color.yellow);
			else
				gc.setColor(Color.gray);
			gc.drawString(s, startX+(sizeX*(i%2))/2f+sizeX/4f-editor.game.font.getWidth(s)/2f, (sizeY*((int)(i/2))/20f+sizeY/40f-editor.game.font.getHeight("Ry")/2f));
		}

		// civs
		gc.setColor(Color.white);
		gc.drawString("Civilisation:", startX+10f, startYCiv+sizeY/40f-editor.game.font.getHeight("Ry")/2f);
		for(int i=0; i<4; i++){
			switch(i){
			case 0: s = "Dualists";break;
			case 1: s = "Zinaids";break;
			case 2: s = "Kitanos"; break;
			default:
			}
			if(selectedCiv == i)
				gc.setColor(Color.white);
			else if(overCiv == i)
				gc.setColor(Color.yellow);
			else
				gc.setColor(Color.gray);
			gc.drawString(s, startX+i*sizeX/3f+sizeX/6f-editor.game.font.getWidth(s)/2f, startYCiv+3*sizeY/40f-editor.game.font.getHeight("Ry")/2f);
		}

		// color
		gc.setColor(Color.white);
		gc.drawString("Equipe:", startX+10f, startYColor+sizeY/40f-editor.game.font.getHeight("Ry")/2f);
		for(int i=0; i<3; i++){
			if(selectedColor==i)
				gc.setColor(Color.white);
			else if (overColor==i)
				gc.setColor(Color.yellow);
			else
				gc.setColor(Color.black);
			gc.fillRect(startX+sizeX*(i+1)/3-5*sizeX/18-3f, startYColor+sizeY/20f+sizeY/160f-3f, 4*sizeX/18f+6f, 6*sizeY/160f+6f);
			switch(i){
			case 0: gc.setColor(Color.white);break;
			case 1: gc.setColor(Color.blue);break;
			case 2: gc.setColor(Color.red);break;
			default:
			}
			gc.fillRect(startX+sizeX*(i+1)/3-5*sizeX/18, startYColor+sizeY/20f+sizeY/160f, 4*sizeX/18f, 6*sizeY/160f);
		}

		// objects
		if(this.objects.size()==0){
			gc.setColor(Color.white);
			gc.drawString("aucun objet", startX+sizeX/2f-editor.game.font.getWidth("aucun objet")/2f, startYObjects+20f);
		}
		for(int i=0; i<this.objects.size() ;i++){
			this.objects.get(i).draw(gc, startX + (i%2)*sizeX/2f+sizeX/4f, startYObjects+((int)(i/2))*sizeY/6f+sizeY/12f);
			if(overObject == i){
				gc.setColor(Color.yellow);
				gc.drawRect(startX + (i%2)*sizeX/2f, startYObjects+((int)(i/2))*sizeY/6f,sizeX/2f,sizeY/6f);
			}
		}
	}

	public void update(InputObject im){
		if(im.x>startX){
			// classes
			if(im.y<startYCiv){
				for(int i=0; i<4; i++){
					if(im.x>startX+(i%2)*sizeX/2f && im.x<startX+(i%2+1)*sizeX/2f && im.y>(i/2)*sizeY/20f && im.y<(i/2+1)*sizeY/20f){
						if(this.overClass!=i){
							this.editor.game.sounds.get("menuMouseOverItem").play(1f,editor.game.options.soundVolume);
						}
						overClass = i;
						if(im.isPressed(KeyEnum.LeftClick)){
							selectedClass = i;
							this.editor.game.sounds.get("menuItemSelected").play(1f,editor.game.options.soundVolume);
							updateObjects();
						}
					}
				}
			} else {
				overClass = -1;
			}
			// civs
			if(im.y>=startYCiv+sizeY/20f && im.y<startYColor){
				for(int i=0; i<3; i++){
					if(im.x>startX+(i)*sizeX/3f && im.x<startX+(i+1)*sizeX/3f){
						if(this.overCiv!=i){
							this.editor.game.sounds.get("menuMouseOverItem").play(1f,editor.game.options.soundVolume);
						}
						overCiv = i;
						if(im.isPressed(KeyEnum.LeftClick)){
							selectedCiv = i;
							this.editor.game.sounds.get("menuItemSelected").play(1f,editor.game.options.soundVolume);
							updateObjects();
						}
					}
				}
			} else {
				overCiv = -1;
			}
			// color
			if(im.y>=startYColor+sizeY/20f && im.y<startYColor+sizeY/10f){
				for(int i=0; i<3; i++){
					if(im.x>startX+(i)*sizeX/3f && im.x<startX+(i+1)*sizeX/3f){
						if(this.overColor!=i){
							this.editor.game.sounds.get("menuMouseOverItem").play(1f,editor.game.options.soundVolume);
						}
						overColor = i;
						if(im.isPressed(KeyEnum.LeftClick)){
							selectedColor = i;
							this.editor.game.sounds.get("menuItemSelected").play(1f,editor.game.options.soundVolume);
							updateObjects();
						}
					}
				}
			} else {
				overColor = -1;
			}
			// object
			if(im.y>=startYObjects+sizeY/20f){
				for(int i=0; i<this.objects.size(); i++){
					if(im.x>startX+(i%2)*sizeX/2f && im.x<startX+(i%2+1)*sizeX/2f 
							&& im.y>startYObjects+(i/2)*sizeY/6f && im.y<startYObjects+(i/2+1)*sizeY/6f){
						if(this.overObject!=i){
							this.editor.game.sounds.get("menuMouseOverItem").play(1f,editor.game.options.soundVolume);
						}
						if(im.isPressed(KeyEnum.LeftClick)){
							EditorObject e = this.objects.get(overObject);
							editor.draggedObject = EditorObject.createFromObjectBar(e);
							switch(this.selectedClass){
							case 0: this.editor.plateau.depotFromMouseOverObject = this.editor.plateau.units; break;
							case 1:
								if(e.name.equals("Headquarters")){
									if(e.team==1)
										this.editor.plateau.depotFromMouseOverObject = this.editor.plateau.headquartersBlue;
									else
										this.editor.plateau.depotFromMouseOverObject = this.editor.plateau.headquartersRed;
								} else {
									this.editor.plateau.depotFromMouseOverObject = this.editor.plateau.buildings; 
								}
								break;
							case 2: this.editor.plateau.depotFromMouseOverObject = this.editor.plateau.buildings; break;
							case 3: this.editor.plateau.depotFromMouseOverObject = this.editor.plateau.nature; break;
							}
							editor.plateau.objetFromObjetBar = true;
						}
						overObject = i;

					}
				}
			} else {
				overObject = -1;
			}
		} else {
			overClass = -1;
			overCiv = -1;
			overColor = -1;
			overObject = -1;
		}
	}

	public void updateObjects(){
		switch(selectedClass){
		case 0: 
			switch(selectedCiv){
			case 0 : 
				switch(selectedColor){
				case 0 : this.objects = unitsDualistsNeutral;break;
				case 1 : this.objects = unitsDualistsBlue;break;
				case 2 : this.objects = unitsDualistsRed;break;
				} break;
			case 1 : 
				switch(selectedColor){
				case 0 : this.objects = unitsZinaidsNeutral;break;
				case 1 : this.objects = unitsZinaidsBlue;break;
				case 2 : this.objects = unitsZinaidsRed;break;
				} break;
			case 2 : 
				switch(selectedColor){
				case 0 : this.objects = unitsKitanosNeutral;break;
				case 1 : this.objects = unitsKitanosBlue;break;
				case 2 : this.objects = unitsKitanosRed;break;
				} break;	
			} break;
		case 1: 
			switch(selectedColor){
			case 0 : this.objects = this.buildingsNeutral;break;
			case 1 : this.objects = this.buildingsBlue;break;
			case 2 : this.objects = this.buildingsRed;break;
			} break;
		case 2:
			this.objects = this.others;
			break;
		case 3: 
			this.objects = this.nature;
			break;
		}
	}


}
