package menu;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import model.Game;
import model.Map;
import model.Sounds;

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
	public Sounds sounds;
	public Image options;
	public Image nickname;

	public Image volume;
	public Image curseur;
	
	public String nicknameString;


	public MenuOptions(Game game){
		try {
			this.backGround = new Image("pics/fondMenu.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.game = game;
		this.items = new Vector<Menu_Item>();
		//this.itemsSelected = new Vector<Menu_Item>();
		float startY = 0.50f*this.game.resY;
		float stepY = 0.12f*this.game.resY;
		float ratioReso = this.game.resX/2800f;
		try {
			this.plus = new Image("pics/menu/plus.png").getScaledCopy(ratioReso);
			this.plusSelected = new Image("pics/menu/plusselected.png").getScaledCopy(ratioReso);
			this.minus= new Image("pics/menu/minus.png").getScaledCopy(ratioReso);
			this.minusSelected= new Image("pics/menu/minusselected.png").getScaledCopy(ratioReso);
			this.back = new Image("pics/menu/back.png").getScaledCopy(ratioReso);
			this.backSelected = new Image("pics/menu/backselected.png").getScaledCopy(ratioReso);
			this.music = new Image("pics/menu/musics.png").getScaledCopy(ratioReso);
			this.sound = new Image("pics/menu/sounds.png").getScaledCopy(ratioReso);
			this.nickname = new Image("pics/menu/nickname.png").getScaledCopy(ratioReso);
			this.volume = new Image("pics/menu/volume.png").getScaledCopy(ratioReso);
			this.curseur = new Image("pics/menu/curseur.png").getScaledCopy(ratioReso);
			this.title = new Image("pics/menu/title01.png").getScaledCopy(0.35f*this.game.resY/650);
			float startX = this.game.resX/2-this.back.getWidth()/2;
			this.items.addElement(new Menu_Item(this.game.resX/4f,startY+0*stepY,this.music,this.music,this.game));
			this.items.get(0).selectionable = false;
			this.items.addElement(new Menu_Item(this.game.resX/4f,startY+1*stepY,this.sound,this.sound,this.game));
			this.items.get(1).selectionable = false;
			this.items.addElement(new Menu_Item(startX,startY+3*stepY,this.back,this.backSelected,this.game));
			this.items.addElement(new Menu_Item(this.game.resX/4f,startY+2*stepY,this.nickname,this.nickname,this.game));
			this.items.get(3).selectionable = false;
			this.items.addElement(new Menu_TextScanner(game.options.nickname,2*this.game.resX/4f,startY+2.3f*stepY,0.8f*this.game.resX/4f,0.4f*stepY));
			this.items.addElement(new Menu_Curseur(2*this.game.resX/4f,startY+0*stepY,"msuique",this.volume,this.curseur,this.game,0.5f));
			this.items.addElement(new Menu_Curseur(2*this.game.resX/4f,startY+1*stepY,"volume",this.volume,this.curseur,this.game,0.5f));
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
		//		}
		this.sounds = game.sounds;
	}

	
	public void callItem(int i){
		((Menu_TextScanner) this.items.get(4)).isSelected = false;
		switch(i){
		case 2: 
			this.game.setMenu(game.menuIntro);
			this.updateOptions();
			break;
		case 4:
			((Menu_TextScanner) this.items.get(4)).isSelected = true;
		default:		
		}
		this.game.options.musicVolume = Math.min(Math.max(this.game.options.musicVolume, 0f), 1f);
		this.game.options.soundVolume = Math.min(Math.max(this.game.options.soundVolume, 0f), 0.2f);
	}

	public void draw(Graphics g){
		g.drawImage(this.backGround, 0,0,this.game.resX,this.game.resY,0,0,this.backGround.getWidth(),this.backGround.getHeight()-60f,new Color(10,10,10,1f));
		for(Menu_Item item: this.items){
			item.draw(g);
		}
		g.drawImage(this.title, this.game.resX/2f-this.title.getWidth()/2, 10f);
	}

	public void update(Input i){
		if(i!=null){
			if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				callItems(i);
				this.game.sounds.menuItemSelected.play(1f,game.options.soundVolume);
			}
			for(Menu_Item item: this.items){
				item.update(i);
			}			
		}
		this.game.options.musicVolume = ((Menu_Curseur)this.items.get(5)).value;
		this.game.options.soundVolume = ((Menu_Curseur)this.items.get(6)).value/5f;
		this.game.menuIntro.music.setVolume(game.options.musicVolume);
	}
	public void updateOptions(){
		try {
			FileWriter fw = new FileWriter ("././options.txt");
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
			fichierSortie.println ("musics: " + game.options.musicVolume);
			fichierSortie.println ("sounds: " + game.options.soundVolume); 
			this.game.options.nickname = ((Menu_TextScanner)this.items.get(8)).s;
			fichierSortie.println ("nickname: " + game.options.nickname); 
			fichierSortie.close();
			Map.initializePlateau(game, 1f, 1f);
		}
		catch (Exception e){
			System.out.println(e.toString());
		}	
	}
}
