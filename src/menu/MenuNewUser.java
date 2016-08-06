package menu;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import model.Game;
import ressources.Map;

public class MenuNewUser extends Menu {



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


	public MenuNewUser(Game game){
		super(game);
		this.volume = Game.g.images.get("menuVolume").getScaledCopy(game.ratioResolution);
		this.curseur = Game.g.images.get("menuCurseur").getScaledCopy(game.ratioResolution);
		this.game = game;
		this.items = new Vector<Menu_Item>();
		//this.itemsSelected = new Vector<Menu_Item>();
		float startY = 0.40f*this.game.resY;
		float stepY = 0.3f*this.game.resY;
		float startX = this.game.resX/2;
//		this.items.addElement(new Menu_Item(this.game.resX/3f,startY+0*stepY,"Musique",this.game,false));
//		this.items.addElement(new Menu_Item(this.game.resX/3f,startY+1*stepY,"Son",this.game,false));
		this.items.addElement(new Menu_Item(this.game.resX/2f,startY+0*stepY,"Veuillez entrer votre pseudo",this.game,false));
		this.textscanner = new Menu_TextScanner(game.options.nickname,1*this.game.resX/2f,startY+1f*stepY,this.game.font.getWidth("Gilles de Bouard "),this.game.font.getHeight("R")*2f+2f, this.game);
//		this.items.addElement(new Menu_Curseur(2*this.game.resX/3f,startY+0*stepY,"Musique",this.volume,this.curseur,this.game,this.game.options.musicVolume));
//		this.items.addElement(new Menu_Curseur(2*this.game.resX/3f,startY+1*stepY,"Volume",this.volume,this.curseur,this.game,this.game.options.soundVolume*5));
		this.items.addElement(new Menu_Item(startX,startY+2*stepY,"Accepter",this.game,true));
		//		}
	}


	public void callItem(int i){
		switch(i){
		case 2: 
			this.game.setMenu(game.menuIntro);
			this.updateOptions();
			break;
		default:		
		}
		this.game.options.musicVolume = Math.min(Math.max(this.game.options.musicVolume, 0f), 1f);
		this.game.options.soundVolume = Math.min(Math.max(this.game.options.soundVolume, 0f), 0.2f);
	}

	public void update(InputObject im){
		this.updateItems(im);
		this.textscanner.update(this.game.app.getInput(),im);
		if(im.isPressed(KeyEnum.LeftClick)){
			textscanner.isSelected = textscanner.isMouseOver(im);
		} 
		this.game.options.musicVolume = 1f;
		this.game.options.soundVolume = 1f/5f;
		this.game.musics.get("themeMenu").setVolume(game.options.musicVolume);
	}

	public void updateOptions(){
		try {
			FileWriter fw = new FileWriter ("././options.opts");
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
			fichierSortie.println ("musics:-" + game.options.musicVolume);
			fichierSortie.println ("sounds:-" + game.options.soundVolume); 
			this.game.options.nickname = ((Menu_TextScanner)this.textscanner).s;
			fichierSortie.println ("nickname:-" + game.options.nickname); 
			fichierSortie.close();
			Map.initializePlateau(game, 1f, 1f);
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