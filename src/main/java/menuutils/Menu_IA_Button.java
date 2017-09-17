package menuutils;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import ressources.GraphicElements;

public class Menu_IA_Button extends Menu_Player {
	public boolean createNewIA = false;
	public Menu_IA_Button(int id, int team, String name) {
		super(id, team, name);
		// TODO Auto-generated constructor stub
	}
	
	
	public void draw(Graphics g, int id) {
		y = startYPlayers+1f*(id+1)/6f*sizeYPlayers-GraphicElements.font_main.getHeight("Pg")/2f;
		
		if(mouseOver){
			g.setColor(Color.gray);
		}else{			
			g.setColor(Color.white);
		}
		String s = "+ Ajouter une IA";
		g.drawString(s, x, y);
		g.setColor(Color.white);	
//		g.drawImage(Images.get("spell"+p.getGameTeam().civ.uniqueSpell.name).getScaledCopy((int)sizeYcolor, (int)sizeYcolor), startXready - sizeYcolor-18 , startYcolor);
	}
	
	
	public void update(InputObject im){
		float xMouse = im.xOnScreen;
		float yMouse = im.yOnScreen;
		
		//Testing the click
		if(xMouse>x && yMouse>y && xMouse<x+sizeX && yMouse<y+sizeYcolor){
			mouseOver = true;
		} else {
			mouseOver= false;
		}
		if(mouseOver && im.isPressed(KeyEnum.LeftClick)){
			// CREATE NEW MENU PLAYER
			createNewIA = true;
		}else{
			createNewIA = false;
		}
	}
	

}
