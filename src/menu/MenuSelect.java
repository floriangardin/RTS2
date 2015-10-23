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
import model.Sounds;


public class MenuSelect extends Menu{

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


	public MenuSelect(Game game){

		this.game = game;
		this.items = new Vector<Menu_Item>();
		//this.itemsSelected = new Vector<Menu_Item>();
		float startY = 100f+0.1f*this.game.resX;
		float stepY = 0.15f*this.game.resY;
		float ratioReso = this.game.resX/2400f;
		try {
			this.plus = new Image("pics/menu/plus.png").getScaledCopy(ratioReso);
			this.plusSelected = new Image("pics/menu/plusselected.png").getScaledCopy(ratioReso);
			this.minus= new Image("pics/menu/minus.png").getScaledCopy(ratioReso);
			this.minusSelected= new Image("pics/menu/minusselected.png").getScaledCopy(ratioReso);
			this.back = new Image("pics/menu/back.png").getScaledCopy(ratioReso);
			this.backSelected = new Image("pics/menu/backselected.png").getScaledCopy(ratioReso);
			this.music = new Image("pics/menu/musics.png").getScaledCopy(ratioReso);
			this.sound = new Image("pics/menu/sounds.png").getScaledCopy(ratioReso);
			this.options = new Image("pics/menu/options.png").getScaledCopy(ratioReso);
			float startX = this.game.resX/2-this.options.getWidth()/2;
			this.items.addElement(new Menu_Item(startX,startY,this.options,this.options,this.game));
			this.items.get(0).selectionable = false;
			this.items.addElement(new Menu_Item(this.game.resX/4f,startY+1*stepY,this.music,this.music,this.game));
			this.items.get(0).selectionable = false;
			this.items.addElement(new Menu_Item(this.game.resX/4f,startY+2*stepY,this.sound,this.sound,this.game));
			this.items.get(0).selectionable = false;
			this.items.addElement(new Menu_Item(2*this.game.resX/4f,startY+1*stepY,this.minus,this.minusSelected,this.game));
			this.items.addElement(new Menu_Item(2*this.game.resX/4f,startY+2*stepY,this.minus,this.minusSelected,this.game));
			this.items.addElement(new Menu_Item(2.5f*this.game.resX/4f,startY+1*stepY,this.plus,this.plusSelected,this.game));
			this.items.addElement(new Menu_Item(2.5f*this.game.resX/4f,startY+2*stepY,this.plus,this.plusSelected,this.game));
			this.items.addElement(new Menu_Item(startX,startY+3*stepY,this.back,this.backSelected,this.game));

		} catch (SlickException e1) {
			e1.printStackTrace();
		}
		//		}
		try{
			this.sounds = game.sounds;
			this.title = new Image("pics/menu/goldtitle.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}


	public void callItem(int i){
		switch(i){
		case 3: this.game.options.musicVolume-=0.1f;break;
		case 4: this.game.options.soundVolume-=0.02f;break;
		case 5: this.game.options.musicVolume+=0.1f;break;
		case 6: this.game.options.soundVolume+=0.02f;break;
		case 7: 
			this.game.setMenu(game.menuIntro);
			this.updateOptions();
			break;
		default:		
		}
		this.game.options.musicVolume = Math.min(Math.max(this.game.options.musicVolume, 0f), 1f);
		this.game.options.soundVolume = Math.min(Math.max(this.game.options.soundVolume, 0f), 0.2f);
	}

	public void draw(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, this.game.resX, this.game.resY);
		for(Menu_Item item: this.items){
			item.draw(g);
		}
		g.drawImage(this.title, this.game.resX/2f-this.title.getWidth()/2, 50f+0.05f*this.game.resY-this.title.getHeight()/2);
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
	}
	public void updateOptions(){
		try {
			FileWriter fw = new FileWriter ("././options.txt");
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
			fichierSortie.println ("musics: " + game.options.musicVolume);
			fichierSortie.println ("sounds: " + game.options.soundVolume); 
			fichierSortie.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}	
	}


}
