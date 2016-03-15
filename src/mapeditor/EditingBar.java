package mapeditor;

import java.io.File;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import menu.Menu_TextScanner;
import multiplaying.InputObject;

public class EditingBar {

	public float x;
	public float y;
	public float sizeX;
	public float sizeY;
	public float spaceY;

	public MapEditor editor;

	public int mouseOver =-1;
	public int mouseOverBis =-1;

	// Icones
	public Image iconNewFile;
	public Image iconOpenFile;
	public Image iconSaveFile;
	public Image iconExit;
	public Image iconGridOn;
	public Image iconGridOff;
	public Image iconCamOn;
	public Image iconCamOff;
	public Image iconCollisionOn;
	public Image iconCollisionOff;
	public Image iconZoomPlus;
	public Image iconZoomMoins;

	public int fileSelected = -1;
	public boolean nameEnter;
	public String name;
	public int currentSize = 0;
	public String sizeXs;
	public String sizeYs;
	public Menu_TextScanner textScanner;
	public Menu_TextScanner scannerSizeX;
	public Menu_TextScanner scannerSizeY;


	public Vector<String> mapNames;

	public EditingBar(MapEditor editor){
		this.editor = editor;
		this.x = 0f;
		this.y = 0f;
		this.sizeX = editor.game.resX;
		this.sizeY = editor.game.resY/20f;
		this.spaceY = this.sizeY/3;
		name = "";
		mapNames = new Vector<String>();
		File repertoire = new File("ressources/");
		File[] files=repertoire.listFiles();
		for(int i=0; i<files.length; i++){
			if(files[i].getName().endsWith(".rtsmap")){
				mapNames.add(files[i].getName().substring(0, files[i].getName().length()-7));
			}
		}
		this.textScanner = new Menu_TextScanner(name, 3f, sizeY+3f, sizeX/6f-30f, sizeY-6f, editor.game);
		this.textScanner.autocompletion = mapNames;
		this.scannerSizeX = new Menu_TextScanner(sizeXs, sizeX/40f , sizeY+3f, sizeX/40f, sizeY-6f, editor.game);
		this.scannerSizeY = new Menu_TextScanner(sizeYs, 4*sizeX/40f , sizeY+3f, sizeX/40f, sizeY-6f, editor.game);
		try {
			this.iconExit = new Image("pics/icons/iconExit.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconNewFile = new Image("pics/icons/iconNewFile.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconOpenFile = new Image("pics/icons/iconOpenFile.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconSaveFile = new Image("pics/icons/iconSaveFile.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconGridOff = new Image("pics/icons/iconGridOff.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconCamOn = new Image("pics/icons/iconCameraOn.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconCamOff = new Image("pics/icons/iconCameraOff.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconCollisionOff = new Image("pics/icons/iconCollisionOff.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconGridOn = new Image("pics/icons/iconGridOn.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconCollisionOn = new Image("pics/icons/iconCollisionOn.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconZoomPlus = new Image("pics/icons/iconZoomPlus.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconZoomMoins = new Image("pics/icons/iconZoomMoins.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
		} catch (SlickException e) {

		}
	}

	public void callItem(int i){
		switch(i){
		case 0: 
			nameEnter = true;
			scannerSizeX.s = "";
			scannerSizeY.s = "";
			fileSelected = 0;
			break;
		case 1:
		case 2:
			nameEnter =true;
			if(fileSelected!=i)
				textScanner.s = "";
			if(i==2 && editor.plateau!=null)
				textScanner.s = editor.plateau.name;
			fileSelected = i;
			break;
		case 3: 
			//exit
			this.editor.game.menuMapChoice.initialize();
			this.editor.game.inEditor = false;
			this.editor.game.setMenu(editor.game.menuIntro);
			break;
		}
	}
	public void callItemBis(int i){
		switch(i){
		case 0:
			// switch cam
			editor.optionCam = !editor.optionCam;
			break;
		case 1: 
			// switch grid
			editor.optionGridOn = !editor.optionGridOn;
			break;
		case 2:
			// switch collision
			editor.optionCollisionOn = !editor.optionCollisionOn;
			break;
		case 3:
			editor.plateau.zoomPlus();
			break;
		case 4: 
			//exit
			editor.plateau.zoomMoins();
			break;
		}
	}

	public void action(int i){
		fileSelected = -1;
		switch(i){
		case 0 : // New File
			int maxX = 0, maxY = 0;
			try{
				maxX = Integer.parseInt(scannerSizeX.s);
				maxY = Integer.parseInt(scannerSizeY.s);
			} catch (Exception e){
			}
			if(maxX != 0 && maxY!=0){
				this.editor.plateau = new EditorPlateau(this.editor, maxX, maxY);
			}
			this.textScanner.s = "";
			break;
		case 1: // Open File
			this.editor.plateau = new EditorPlateau(this.editor, 1, 1);
			this.editor.plateau.openFrom(textScanner.s);
			textScanner.s= "";
			break;
		case 2: // Save File
			if(this.editor.plateau.headquartersBlue!=null && this.editor.plateau.headquartersRed!=null){
				this.editor.plateau.saveTo(textScanner.s);
				textScanner.s = "";
			}
			break;
		default:
		}
	}
	

	public void update(InputObject im, Input in){
		// items de gauche
		if(im.yMouse>=0 && im.yMouse<=sizeY){
			if(im.xMouse<(sizeY+spaceY)*(5)){
				for(int i=0; i<4; i++){
					if(im.xMouse>=(sizeY+spaceY)*i && im.xMouse<(sizeY+spaceY)*(i+1)){
						if(this.mouseOver!=i){
							this.editor.game.sounds.menuMouseOverItem.play(1f,editor.game.options.soundVolume);
						}
						this.mouseOver = i;
						if(im.pressedLeftClick){
							this.editor.game.sounds.menuItemSelected.play(1f,editor.game.options.soundVolume);
							this.callItem(i);
						}
					}
				}
			} else {
				this.mouseOver = -1;
			}
		} else {
			this.mouseOver = -1;
		}
		// item de droite
		if(im.yMouse>=0 && im.yMouse<=sizeY){
			if(im.xMouse>editor.objectBar.startX-(sizeY+spaceY)*(5)){
				for(int i=0; i<5; i++){
					if(im.xMouse>=editor.objectBar.startX-(sizeY+spaceY)*(5)+(sizeY+spaceY)*i && im.xMouse<editor.objectBar.startX-(sizeY+spaceY)*(5)+(sizeY+spaceY)*(i+1)){
						if(this.mouseOverBis!=i){
							this.editor.game.sounds.menuMouseOverItem.play(1f,editor.game.options.soundVolume);
						}
						this.mouseOverBis = i;
						if(im.pressedLeftClick){
							this.editor.game.sounds.menuItemSelected.play(1f,editor.game.options.soundVolume);
							if(editor.plateau!=null)
								this.callItemBis(i);
						}
					}
				}
			} else {
				this.mouseOverBis = -1;
			}
		} else {
			this.mouseOverBis = -1;
		}
		if(nameEnter){
			if(fileSelected==0){
				if(im.pressedLeftClick && im.yMouse>sizeY && im.yMouse<2*sizeY && im.xMouse>sizeX/40f && im.xMouse<3*sizeX/40)
					currentSize = 0;
				if(im.pressedLeftClick && im.yMouse>sizeY && im.yMouse<2*sizeY && im.xMouse>4*sizeX/40f && im.xMouse<6*sizeX/40)
					currentSize = 1;				
				if(currentSize==0){
					this.scannerSizeX.isSelected = true;
					this.scannerSizeY.isSelected = false;
				}else{
					this.scannerSizeX.isSelected = false;
					this.scannerSizeY.isSelected = true;
				}
				this.scannerSizeY.update(in, im);
				this.scannerSizeX.update(in, im);
				if(im.isPressedTAB)
					currentSize = (currentSize+1)%2;
			} else {
				this.textScanner.isSelected = true;
				this.textScanner.update(in, im);
			}
			if(im.isPressedESC){
				if(fileSelected==0 && currentSize == 1){
					currentSize-=1;
					return;
				}
				this.nameEnter = false;
				this.textScanner.s = "";
				this.scannerSizeX.s = "";
				this.scannerSizeY.s = "";
			}
			if(im.isPressedENTER){
				if(fileSelected==0 && currentSize == 0){
					currentSize+=1;
					return;
				}
				this.nameEnter = false;
				this.action(this.fileSelected);
			}
		}
	}

	public void draw(Graphics gc){
		gc.setColor(Color.white);
		gc.fillRect(x-2f, y-2f, sizeX+4f, sizeY+4f);
		gc.setColor(Color.black);
		gc.fillRect(x, y, sizeX, sizeY);
		gc.drawImage(this.iconNewFile, 1, 1);
		gc.drawImage(this.iconOpenFile, sizeY+spaceY+1, 1);
		gc.drawImage(this.iconSaveFile, 2*(sizeY+spaceY)+1, 1);
		gc.drawImage(this.iconExit, 3*(sizeY+spaceY)+1, 1);
		gc.drawImage(this.iconZoomMoins, this.editor.objectBar.startX-sizeY, 1);
		gc.drawImage(this.iconZoomPlus, this.editor.objectBar.startX-2*sizeY-spaceY, 1);
		if(editor.optionCollisionOn)
			gc.drawImage(this.iconCollisionOn, this.editor.objectBar.startX-3*sizeY-2*spaceY, 1);
		else
			gc.drawImage(this.iconCollisionOff, this.editor.objectBar.startX-3*sizeY-2*spaceY, 1);			
		if(editor.optionGridOn)
			gc.drawImage(this.iconGridOn, this.editor.objectBar.startX-4*sizeY-3*spaceY, 1);
		else
			gc.drawImage(this.iconGridOff, this.editor.objectBar.startX-4*sizeY-3*spaceY, 1);		
		if(editor.optionCam)
			gc.drawImage(this.iconCamOn, this.editor.objectBar.startX-5*sizeY-4*spaceY, 1);
		else
			gc.drawImage(this.iconCamOff, this.editor.objectBar.startX-5*sizeY-4*spaceY, 1);
		if(mouseOver!=-1){
			gc.setColor(Color.white);
			gc.drawRect(mouseOver*(sizeY+spaceY), 0, sizeY, sizeY);
			String s = "";
			switch(mouseOver){
			case 0: s = "Nouveau fichier"; break;
			case 1: s = "Ouvrir fichier"; break;
			case 2: s = "Sauver fichier"; break;
			case 3: s= "Quitter"; break;
			default: 
			}
			gc.drawString(s, (sizeY+spaceY)*(4.2f), sizeY/2f-editor.game.font.getHeight("Ry")/2f);
		}
		if(mouseOverBis!=-1){
			gc.setColor(Color.white);
			gc.drawRect(mouseOverBis*(sizeY+spaceY)+this.editor.objectBar.startX-5*sizeY-4*spaceY, 0, sizeY, sizeY);
			String s = "";
			switch(mouseOverBis){
			case 0: 
				if(editor.optionCam)
					s = "Masquer les caméras";
				else
					s = "Montrer les caméras"; 
				break;
			case 1: 
				if(editor.optionGridOn)
					s = "Désactiver Grille";
				else
					s = "Activer Grille"; 
				break;
			case 2:
				if(editor.optionCollisionOn)
					s = "Désactiver Collision";
				else
					s = "Activer Collision"; 
				break;
			case 3: s = "Zoom plus"; break;
			case 4: s= "Zoom moins"; break;
			default: 
			}
			gc.drawString(s, editor.objectBar.startX-(sizeY+spaceY)*(5.2f)-editor.game.font.getWidth(s), sizeY/2f-editor.game.font.getHeight("Ry")/2f);
		}
		gc.setColor(Color.white);
		gc.drawString("RTS Ultra Mythe Editor", (editor.objectBar.startX)/2-editor.game.font.getWidth("RTS Ultra Mythe Editor")/2f,sizeY/2f-editor.game.font.getHeight("Ry")/2f);
		if(nameEnter){
			if(fileSelected==0){
				gc.setColor(Color.white);
				gc.drawString("X :", 2f, sizeY*3f/2f-editor.game.font.getHeight("X")/2f);
				gc.drawString("Y :", 3*sizeX/40f+2f, sizeY*3f/2f-editor.game.font.getHeight("X")/2f);
				this.scannerSizeX.draw(gc,0);
				this.scannerSizeY.draw(gc,0);
			} else {
				this.textScanner.draw(gc,0);
			}
		}
	}


}
