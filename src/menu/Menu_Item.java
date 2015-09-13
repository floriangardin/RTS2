package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import multiplaying.InputModel;

public class Menu_Item {

	public float sizeX;
	public float sizeY;
	public float x;
	public float y;

	public String name;

	public boolean isMouseOnIt = false;
	public float animation = 0f;
	public Color color = Color.black;

	public boolean colorAnimation = true;

	public Menu_Item(float x, float y, float sizeX, float sizeY, String name) {
		super();
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.x = x;
		this.y = y;
		this.name = name;
	}

	public boolean isClicked(Input i){
		float xMouse = i.getAbsoluteMouseX();
		float yMouse = i.getAbsoluteMouseY();
		return (x<xMouse && xMouse<x+sizeX && y<yMouse && yMouse<y+sizeY);
	}
	public boolean isClicked(InputModel im){
		float xMouse = im.xMouse;
		float yMouse = im.yMouse;
		return (x<xMouse && xMouse<x+sizeX && y<yMouse && yMouse<y+sizeY);
	}


	public void draw(Graphics g){
		
		g.setColor(Color.black);
		g.fillRect(x, y, sizeX, sizeY);
		g.setColor(this.color);
		g.fillRect(x+10f, y+10f, sizeX-20f, sizeY-20f);
		g.setColor(Color.white);
		g.drawString(name, x+sizeX/3f, y+sizeY/3f);
	}

	public void printDebug(){
		System.out.println(this.name+" "+this.x+" "+this.y+" " +this.sizeX+" " +this.sizeY);
	}

	public void update(Input i){
		if(this.colorAnimation){
			if(this.isClicked(i)){
				this.animation+=1f;
				if(this.animation>60f)
					this.animation = 0f;
				float c = Math.min((150f*animation/60f)/255f, (150f-150f*animation/60f)/255f);
				this.color = new Color(c,c,c);
			} else {
				this.color = Color.black;
			}
		}
	}

}
