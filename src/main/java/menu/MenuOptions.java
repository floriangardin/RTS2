package menu;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import menuutils.Menu_Curseur;
import menuutils.Menu_Item;
import menuutils.Menu_TextScanner;
import model.Game;
import model.Options;
import ressources.GraphicElements;
import ressources.Images;
import ressources.Musics;
import system.MenuSystem.MenuNames;

public class MenuOptions extends Menu {



	Image title;
	public Image plus;
	public Image plusSelected;
	public Image minus;
	public Image minusSelected;

	public Image back;
	public Image backSelected;
	public Image music;
	public Image sound;
	public Image options;
	public Image nickname;

	public Image volume;
	public Image curseur;

	public String nicknameString;

	public Menu_TextScanner textscanner;


	public MenuOptions(){
		super();
		this.volume = Images.get("menuVolume").getScaledCopy(Game.ratioResolution);
		this.curseur = Images.get("menuCurseur").getScaledCopy(Game.ratioResolution);
		this.items = new Vector<Menu_Item>();
		//this.itemsSelected = new Vector<Menu_Item>();
		float startY = 0.50f*Game.resY;
		float stepY = 0.12f*Game.resY;
		float startX = Game.resX/2;
		this.items.addElement(new Menu_Item(Game.resX/3f,startY+0*stepY,"Musique",false));
		this.items.lastElement().font_current = GraphicElements.font_mid;
		this.items.addElement(new Menu_Item(Game.resX/3f,startY+1*stepY,"Son",false));
		this.items.lastElement().font_current = GraphicElements.font_mid;
		this.items.addElement(new Menu_Item(Game.resX/3f,startY+2*stepY,"Pseudo",false));
		this.items.lastElement().font_current = GraphicElements.font_mid;
		this.textscanner = new Menu_TextScanner(Options.nickname,2*Game.resX/3f,startY+2f*stepY,GraphicElements.font_main.getWidth("Gilles de Bouard "),GraphicElements.font_main.getHeight("R")*2f+2f);
		this.items.addElement(new Menu_Curseur(2*Game.resX/3f,startY+0*stepY,"Musique",this.volume,this.curseur,Options.musicVolume));
		this.items.addElement(new Menu_Curseur(2*Game.resX/3f,startY+1*stepY,"Volume",this.volume,this.curseur,Options.soundVolume*2));
		this.items.addElement(new Menu_Item(startX,startY+3*stepY,"Retour",true));
		//		}
	}


	public void callItem(int i){
		switch(i){
		case 5: 
			Game.menuSystem.setMenu(MenuNames.MenuIntro);
			this.updateOptions();
			break;
		default:		
		}
		Options.musicVolume = StrictMath.min(StrictMath.max(Options.musicVolume, 0f), 1f);
		Options.soundVolume = StrictMath.min(StrictMath.max(Options.soundVolume, 0f), 0.5f);
	}

	public void update(InputObject im){
		this.updateItems(im);
		this.textscanner.update(Game.app.getInput(),im);
		if(im.isPressed(KeyEnum.LeftClick)){
			textscanner.isSelected = textscanner.isMouseOver(im);
		} 
		Options.musicVolume = ((Menu_Curseur)this.items.get(3)).value;
		Options.soundVolume = ((Menu_Curseur)this.items.get(4)).value/2f;
		if(Musics.getPlayingMusic()!=null){
			Musics.getPlayingMusic().setVolume(Options.musicVolume);
		}
	}

	public void updateOptions(){
		try {
			FileWriter fw = new FileWriter ("././options.opts");
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
			fichierSortie.println ("musics:-" + Options.musicVolume);
			fichierSortie.println ("sounds:-" + Options.soundVolume); 
			Options.nickname = ((Menu_TextScanner)this.textscanner).s;
			fichierSortie.println ("nickname:-" + Options.nickname); 
			fichierSortie.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}	
	}

	public void draw(Graphics g){
		this.drawItems(g);
		this.textscanner.draw(g);
	}
}
