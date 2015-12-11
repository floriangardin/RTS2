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

	// Icones
	public Image iconNewFile;
	public Image iconOpenFile;
	public Image iconSaveFile;
	public Image iconExit;

	public int fileSelected = -1;
	public boolean nameEnter;
	public String name;
	public Menu_TextScanner textScanner;
	
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
		try {
			this.iconExit = new Image("pics/icons/iconExit.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconNewFile = new Image("pics/icons/iconNewFile.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconOpenFile = new Image("pics/icons/iconOpenFile.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
			this.iconSaveFile = new Image("pics/icons/iconSaveFile.png").getScaledCopy((int)sizeY-2, (int)sizeY-2);
		} catch (SlickException e) {

		}
	}

	public void callItem(int i){
		switch(i){
		case 0: 
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
			this.editor.game.inEditor = false;
			this.editor.game.setMenu(editor.game.menuIntro);
			break;
		}
	}

	public void action(int i){
		fileSelected = -1;
		switch(i){
		case 0 : // New File
			this.editor.plateau = new EditorPlateau(this.editor, 1, 1);
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
		if(nameEnter){
			this.textScanner.update(in, im);
			if(im.isPressedESC){
				this.nameEnter = false;
				this.textScanner.s = "";
			}
			if(im.isPressedENTER){
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
		gc.setColor(Color.white);
		gc.drawString("RTS Ultra Mythe Editor", editor.game.resX/2-editor.game.font.getWidth("RTS Ultra Mythe Editor")/2f,sizeY/2f-editor.game.font.getHeight("Ry")/2f);
		if(nameEnter){
			this.textScanner.draw(gc,0);
		}
	}


}
