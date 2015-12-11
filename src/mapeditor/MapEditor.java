package mapeditor;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import model.Game;
import multiplaying.InputObject;

public class MapEditor {

	
	public Game game;

	public EditingBar editingBar;
	public ObjectBar objectBar;
	public EditorPlateau plateau;
	
	public MapEditor(Game g){
		this.game = g;
		this.editingBar = new EditingBar(this);
		this.objectBar = new ObjectBar(this);
		this.plateau = null;
	}
	
	public void draw(Graphics gc){
		gc.setColor(Color.gray);
		gc.fillRect(0, 0, game.resX, game.resY);
		if(this.plateau!=null){
			this.plateau.draw(gc);
		}
		this.editingBar.draw(gc);
		this.objectBar.draw(gc);
	}
	
	public void update(InputObject im, Input i){
		this.editingBar.update(im,i);
		this.objectBar.update(im);
		if(this.plateau!=null){
			this.plateau.update(im,i);
		}
	}
	
}
