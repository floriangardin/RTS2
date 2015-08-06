package model;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class MenuIntro extends Menu {

	Vector<Objet> trees = new Vector<Objet>();
	Image title;

	public MenuIntro(Game game){
		this.game = game;
		this.items = this.createHorizontalCentered(2, this.game.resX, this.game.resY);
		this.items.get(0).name = "Nouvelle Partie";
		this.items.get(1).name = "Quitter";
		float x1,x2,y1,y2;
		float sizeX = this.game.resX;
		float sizeY = this.game.resY;
		for(int i=0; i<4; i++){
			x1 = sizeX/18f+(float)Math.random()*2f*sizeX/9f;
			x2 = 13f*sizeX/18f+(float)Math.random()*2f*sizeX/9f;
			y1 = sizeY/5f+(float)Math.random()*3f*sizeY/5f;
			y2 = sizeY/5f+(float)Math.random()*3f*sizeY/5f;
			this.trees.add(new Tree(x1,y1,(int)(Math.random()*4f+1f)));
			this.trees.add(new Tree(x2,y2,(int)(Math.random()*4f+1f)));
		}
		Utils.triY(trees);
		try{
			title = new Image("pics/Title.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void callItems(Input i){
		for(int j=0; j<items.size(); j++){
			if(items.get(j).isClicked(i))
				callItem(j);
		}
	}
	public void callItem(int i){
		switch(i){
		case 0:
			this.game.newGame();
			this.game.quitMenu();
			break;
		case 1: 
			this.game.app.exit();
			break;
		default:		
		}
	}

	public void draw(Graphics g){

		for(int i=0;i<3;i++){
			for(int j=0;j<2;j++){
				g.drawImage(this.game.background, i*512, j*512);
			}
		}
		for(Objet o: this.trees)
			o.draw(g);
		for(int i=0; i<this.items.size(); i++){
			this.items.get(i).draw(g);
		}
		g.drawImage(this.title, this.game.resX/2f-200f, 20f);
		g.setColor(Color.black);
		String copyright = "           Copyright 2015           \n- GdB Production / Welcome's Games -";
		g.drawString(copyright, 6.6f*this.game.resX/18f, 4f*this.game.resY/5f);
		//            abcdefghijklmn  abcdefghijklmnopqrstuvwxyabcdefghijk
	}

	public void update(Input i){
		if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON))
			callItems(i);

	}
}
