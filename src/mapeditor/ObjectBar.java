package mapeditor;

import java.util.Vector;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import model.Data;
import model.Map;
import multiplaying.InputObject;

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

		Data data = new Data();

		// dualists
		// blue
		unitsDualistsBlue.add(new EditorObject("Spearman", 1, 0,this.editor.game.images.spearmanBlue,0,0,this,1,1));
		unitsDualistsBlue.add(new EditorObject("Crossbowman", 1, 0,this.editor.game.images.crossbowmanBlue,0,0,this,1,1));
		unitsDualistsBlue.add(new EditorObject("Knight", 1, 0,this.editor.game.images.knightBlue,0,0,this,1,1));
		unitsDualistsBlue.add(new EditorObject("Inquisitor", 1, 0,this.editor.game.images.inquisitorBlue,0,0,this,1,1));
		unitsDualistsBlue.add(new EditorObject("Archange", 1, 0,this.editor.game.images.archangeBlue,0,0,this,1,1));
		// red
		unitsDualistsRed.add(new EditorObject("Spearman", 2,0, this.editor.game.images.spearmanRed,0,0,this,1,1));
		unitsDualistsRed.add(new EditorObject("Crossbowman", 2, 0,this.editor.game.images.crossbowmanRed,0,0,this,1,1));
		unitsDualistsRed.add(new EditorObject("Knight", 2, 0,this.editor.game.images.knightRed,0,0,this,1,1));
		unitsDualistsRed.add(new EditorObject("Inquisitor", 2,0, this.editor.game.images.inquisitorRed,0,0,this,1,1));
		unitsDualistsRed.add(new EditorObject("Archange", 2,0, this.editor.game.images.archangeRed,0,0,this,1,1));

		// buildings
		// neutral
		buildingsNeutral.add(new EditorObject("Mill", 0, 1,this.editor.game.images.buildingMillNeutral,0,0,this,(int)(data.millSizeX/Map.stepGrid),(int)(data.millSizeY/Map.stepGrid)));
		buildingsNeutral.add(new EditorObject("Mine", 0, 1,this.editor.game.images.buildingMineNeutral,0,0,this,(int)(data.mineSizeX/Map.stepGrid),(int)(data.mineSizeY/Map.stepGrid)));
		buildingsNeutral.add(new EditorObject("Barrack", 0,1, this.editor.game.images.buildingBarrackNeutral,0,0,this,(int)(data.barrackSizeX/Map.stepGrid),(int)(data.barrackSizeY/Map.stepGrid)));
		buildingsNeutral.add(new EditorObject("Stable", 0, 1,this.editor.game.images.buildingStableNeutral,0,0,this,(int)(data.stableSizeX/Map.stepGrid),(int)(data.stableSizeY/Map.stepGrid)));
		buildingsNeutral.add(new EditorObject("Academy", 0, 1,this.editor.game.images.buildingAcademyNeutral,0,0,this,(int)(data.academySizeX/Map.stepGrid),(int)(data.academySizeY/Map.stepGrid)));
		buildingsNeutral.add(new EditorObject("University", 0, 1,this.editor.game.images.buildingUniversityNeutral,0,0,this,(int)(data.universitySizeX/Map.stepGrid),(int)(data.universitySizeY/Map.stepGrid)));
		buildingsNeutral.add(new EditorObject("Tower", 0, 1,this.editor.game.images.buildingTowerNeutral,0,0,this,(int)(data.towerSizeX/Map.stepGrid),(int)(data.towerSizeY/Map.stepGrid)));
		// blue
		buildingsBlue.add(new EditorObject("Mill", 1, 1,this.editor.game.images.buildingMillBlue,0,0,this,(int)(data.millSizeX/Map.stepGrid),(int)(data.millSizeY/Map.stepGrid)));
		buildingsBlue.add(new EditorObject("Mine", 1, 1,this.editor.game.images.buildingMineBlue,0,0,this,(int)(data.mineSizeX/Map.stepGrid),(int)(data.mineSizeY/Map.stepGrid)));
		buildingsBlue.add(new EditorObject("Barrack", 1, 1,this.editor.game.images.buildingBarrackBlue,0,0,this,(int)(data.barrackSizeX/Map.stepGrid),(int)(data.barrackSizeY/Map.stepGrid)));
		buildingsBlue.add(new EditorObject("Stable", 1, 1,this.editor.game.images.buildingStableBlue,0,0,this,(int)(data.stableSizeX/Map.stepGrid),(int)(data.stableSizeY/Map.stepGrid)));
		buildingsBlue.add(new EditorObject("Academy", 1, 1,this.editor.game.images.buildingAcademyBlue,0,0,this,(int)(data.academySizeX/Map.stepGrid),(int)(data.academySizeY/Map.stepGrid)));
		buildingsBlue.add(new EditorObject("University", 1, 1,this.editor.game.images.buildingUniversityBlue,0,0,this,(int)(data.universitySizeX/Map.stepGrid),(int)(data.universitySizeY/Map.stepGrid)));
		buildingsBlue.add(new EditorObject("Tower", 1, 1,this.editor.game.images.buildingTowerBlue,0,0,this,(int)(data.towerSizeX/Map.stepGrid),(int)(data.towerSizeY/Map.stepGrid)));
		buildingsBlue.add(new EditorObject("HeadQuarters", 1, 1,this.editor.game.images.buildingHeadQuartersBlue,0,0,this,(int)(data.headQuartersSizeX/Map.stepGrid),(int)(data.headQuartersSizeY/Map.stepGrid)));
		// red
		buildingsRed.add(new EditorObject("Mill", 2, 1,this.editor.game.images.buildingMillRed,0,0,this,(int)(data.millSizeX/Map.stepGrid),(int)(data.millSizeY/Map.stepGrid)));
		buildingsRed.add(new EditorObject("Mine", 2, 1,this.editor.game.images.buildingMineRed,0,0,this,(int)(data.mineSizeX/Map.stepGrid),(int)(data.mineSizeY/Map.stepGrid)));
		buildingsRed.add(new EditorObject("Barrack", 2, 1,this.editor.game.images.buildingBarrackRed,0,0,this,(int)(data.barrackSizeX/Map.stepGrid),(int)(data.barrackSizeY/Map.stepGrid)));
		buildingsRed.add(new EditorObject("Stable", 2, 1,this.editor.game.images.buildingStableRed,0,0,this,(int)(data.stableSizeX/Map.stepGrid),(int)(data.stableSizeY/Map.stepGrid)));
		buildingsRed.add(new EditorObject("Academy", 2, 1,this.editor.game.images.buildingAcademyRed,0,0,this,(int)(data.academySizeX/Map.stepGrid),(int)(data.academySizeY/Map.stepGrid)));
		buildingsRed.add(new EditorObject("University", 2, 1,this.editor.game.images.buildingUniversityRed,0,0,this,(int)(data.universitySizeX/Map.stepGrid),(int)(data.universitySizeY/Map.stepGrid)));
		buildingsRed.add(new EditorObject("Tower", 2, 1,this.editor.game.images.buildingTowerRed,0,0,this,(int)(data.towerSizeX/Map.stepGrid),(int)(data.towerSizeY/Map.stepGrid)));
		buildingsRed.add(new EditorObject("HeadQuarters", 2,1, this.editor.game.images.buildingHeadQuartersRed,0,0,this,(int)(data.headQuartersSizeX/Map.stepGrid),(int)(data.headQuartersSizeY/Map.stepGrid)));

		// others
		others.add(new EditorObject("BonusLifePoints", 0, 2,this.editor.game.images.bonusLifePoints,0,0,this,1,1));
		others.add(new EditorObject("BonusDamage", 0,2, this.editor.game.images.bonusDamage,0,0,this,1,1));
		others.add(new EditorObject("BonusSpeed", 0, 2,this.editor.game.images.bonusSpeed,0,0,this,1,1));

		// nature
		nature.add(new EditorObject("Tree", 0, 3,this.editor.game.images.tree01, 0, 0,this,1,1));
		nature.add(new EditorObject("Tree", 1, 3,this.editor.game.images.tree02, 0, 0,this,1,1));

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
		if(im.xMouse>startX){
			// classes
			if(im.yMouse<startYCiv){
				for(int i=0; i<4; i++){
					if(im.xMouse>startX+(i%2)*sizeX/2f && im.xMouse<startX+(i%2+1)*sizeX/2f && im.yMouse>(i/2)*sizeY/20f && im.yMouse<(i/2+1)*sizeY/20f){
						if(this.overClass!=i){
							this.editor.game.sounds.menuMouseOverItem.play(1f,editor.game.options.soundVolume);
						}
						overClass = i;
						if(im.pressedLeftClick){
							selectedClass = i;
							this.editor.game.sounds.menuItemSelected.play(1f,editor.game.options.soundVolume);
							updateObjects();
						}
					}
				}
			} else {
				overClass = -1;
			}
			// civs
			if(im.yMouse>=startYCiv+sizeY/20f && im.yMouse<startYColor){
				for(int i=0; i<3; i++){
					if(im.xMouse>startX+(i)*sizeX/3f && im.xMouse<startX+(i+1)*sizeX/3f){
						if(this.overCiv!=i){
							this.editor.game.sounds.menuMouseOverItem.play(1f,editor.game.options.soundVolume);
						}
						overCiv = i;
						if(im.pressedLeftClick){
							selectedCiv = i;
							this.editor.game.sounds.menuItemSelected.play(1f,editor.game.options.soundVolume);
							updateObjects();
						}
					}
				}
			} else {
				overCiv = -1;
			}
			// color
			if(im.yMouse>=startYColor+sizeY/20f && im.yMouse<startYColor+sizeY/10f){
				for(int i=0; i<3; i++){
					if(im.xMouse>startX+(i)*sizeX/3f && im.xMouse<startX+(i+1)*sizeX/3f){
						if(this.overColor!=i){
							this.editor.game.sounds.menuMouseOverItem.play(1f,editor.game.options.soundVolume);
						}
						overColor = i;
						if(im.pressedLeftClick){
							selectedColor = i;
							this.editor.game.sounds.menuItemSelected.play(1f,editor.game.options.soundVolume);
							updateObjects();
						}
					}
				}
			} else {
				overColor = -1;
			}
			// object
			if(im.yMouse>=startYObjects+sizeY/20f){
				for(int i=0; i<this.objects.size(); i++){
					if(im.xMouse>startX+(i%2)*sizeX/2f && im.xMouse<startX+(i%2+1)*sizeX/2f 
							&& im.yMouse>startYObjects+(i/2)*sizeY/6f && im.yMouse<startYObjects+(i/2+1)*sizeY/6f){
						if(this.overObject!=i){
							this.editor.game.sounds.menuMouseOverItem.play(1f,editor.game.options.soundVolume);
						}
						if(im.pressedLeftClick){
							EditorObject e = this.objects.get(overObject);
							editor.draggedObject = new EditorObject(e.name,e.team,e.clas,e.image,e.x,e.y,e.sizeX,e.sizeY);
							switch(this.selectedClass){
							case 0: this.editor.plateau.depotFromMouseOverObject = this.editor.plateau.units; break;
							case 1:
								if(e.name=="headquarters"){
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
