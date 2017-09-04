package menuutils;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import model.Game;

public class Menu_Curseur extends Menu_Item{


	public Image curseur;
	public float value;
	public float decalage;
	public boolean isSelected;

	public Menu_Curseur(float x, float y, String name, Image im, Image curseur,float value){
		super(x,y,name,im,im);
		this.curseur = curseur;
		this.value = value;
		this.name = "image manquante";
		this.sizeX = this.image.getWidth();
		this.sizeY = this.image.getHeight();
		this.decalage = this.sizeX/11f;
	}

	public void draw(Graphics g){
		if(this.image!=null && this.curseur !=null){
			g.drawImage(this.image,x-this.image.getWidth()/2f, y-this.image.getHeight()/2f);
			g.drawImage(this.curseur, x+decalage+value*(this.sizeX-2*decalage)-image.getWidth()/2f-curseur.getWidth()/2f, y-this.image.getHeight()/2f);
		}
		else{
			g.drawString(this.name, x, y);
		}

	}


	public void update(InputObject im){
		if(this.isMouseOver(im) && im.isPressed(KeyEnum.LeftClick)){
			value = Math.min(1, Math.max(0,(im.xOnScreen-x-decalage+this.image.getWidth()/2f)/(this.sizeX-2*decalage)));
			isSelected = true;
		} else if (isSelected && im.isDown(KeyEnum.LeftClick)){
			value = Math.min(1, Math.max(0,(im.xOnScreen-x-decalage+this.image.getWidth()/2f)/(this.sizeX-2*decalage)));
		} else {
			isSelected = false;
		}
	}
}
