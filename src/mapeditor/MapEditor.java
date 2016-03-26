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
	
	public boolean optionGridOn = false;
	public boolean optionCollisionOn = true;
	public boolean optionCam = false;
	public boolean paintingWater = true;
	
	public EditorObject draggedObject = null;
	public float decX, decY;
	public float tempX, tempY;
	
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
		if(this.game.musics.get("themeMenu").playing()){
			this.game.musics.get("themeMenu").stop();
		}
		if(!this.game.musics.get("themeMapEditor").playing()){
			this.game.musics.get("themeMapEditor").loop(1f,this.game.options.musicVolume);
		}
		if(im.xMouse>Math.min(this.objectBar.startX,game.resX-10f)){
			this.objectBar.startX = Math.max(4*game.resX/5, this.objectBar.startX-35f);
		} else {
			this.objectBar.startX = Math.min(game.resX, this.objectBar.startX+35f);
		}
		this.editingBar.update(im,i);
		this.objectBar.update(im);
		if(this.plateau!=null){
			this.plateau.update(im,i);
		}
	}
	
}
