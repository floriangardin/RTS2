package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import model.Game;

public class Menu_Curseur extends Menu_Item{
	

	public Image curseur;
	public float value;
	
	public Menu_Curseur(float x, float y, String name, Image im, Image curseur, Game g,float value){
		super(x,y,name,im,im,g);
		this.curseur = curseur;
		this.value = value;
		this.name = "image manquante";
		
	}
	
	public void draw(Graphics g){
		if(this.image!=null && this.curseur !=null){
			g.drawImage(this.image,x, y);
			g.drawImage(this.curseur, x+value*this.sizeX, y);
		}
		else{
			g.drawString(this.name, x, y);
		}

	}


	public void update(Input i){
		if(this.selectionable){
			if(this.isClicked(i)){
				if(!mouseOver){
					this.game.sounds.menuMouseOverItem.play(1f,this.game.options.soundVolume);
					mouseOver = true;
				}
				this.toDraw = this.selectedImage;
			} else {
				if(mouseOver)
					mouseOver = false;
				this.toDraw = this.image;
			}
		}
	}
}
