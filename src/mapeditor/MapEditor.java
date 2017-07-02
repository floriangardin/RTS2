package mapeditor;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import control.InputObject;
import display.Camera;
import ressources.Musics;

public class MapEditor {

	

	public EditingBar editingBar;
	public ObjectBar objectBar;
	public EditorPlateau plateau;
	
	public boolean optionGridOn = false;
	public boolean optionCollisionOn = true;
	public boolean optionCam = false;
	public boolean paintingWater = false;
	
	public EditorObject draggedObject = null;
	public float decX, decY;
	public float tempX, tempY;
	
	public Camera camera;
	
	public MapEditor(Camera camera){
		this.editingBar = new EditingBar(this);
		this.objectBar = new ObjectBar(this);
		this.plateau = null;
		this.camera = camera;
	}
	
	public void draw(Graphics gc){
		gc.setColor(Color.gray);
		gc.fillRect(0, 0, camera.resX, camera.resY);
		if(this.plateau!=null){
			this.plateau.draw(gc);
		}
		this.editingBar.draw(gc);
		this.objectBar.draw(gc);
	}
	
	public void update(InputObject im, Input i){
		if(Musics.get("themeMenu").playing()){
			Musics.get("themeMenu").stop();
		}
		if(!Musics.get("themeMapEditor").playing()){
			Musics.playMusic("themeMapEditor");
		}
		if(im.x>Math.min(this.objectBar.startX,camera.resX-10f)){
			this.objectBar.startX = Math.max(4*camera.resX/5, this.objectBar.startX-35f);
		} else {
			this.objectBar.startX = Math.min(camera.resX, this.objectBar.startX+35f);
		}
		this.editingBar.update(im,i);
		this.objectBar.update(im);
		if(this.plateau!=null){
			this.plateau.update(im,i);
		}
	}
	
}
