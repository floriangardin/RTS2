package control;


import java.util.Vector;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import control.KeyMapper.KeyEnum;
import model.Objet;

public class InputModel implements java.io.Serializable{

	/**
	 * Handle selection rectangle ( it is part of inputs at the end ...)
	 */
	
	public int team;
	
	public Vector<KeyEnum> down;
	public Vector<KeyEnum> pressed;
	public float x;
	public float y;
	
	public Rectangle selectionRectangle ;
	public Vector<Objet> selection;
	private float anchorX;
	private float anchorY;
	
	private int idObjetMouse;

	
	
	public InputModel(int team){
		down = new Vector<KeyEnum>();
		pressed = new Vector<KeyEnum>();
		
		this.team = team;
		
		selection = new Vector<Objet>();
		selectionRectangle = new Rectangle(-1000f,-1000f,0.1f,0.1f);
	}

	/*
	 * On a pas besoin de le réinstancier à chaque fois ...
	 */
	public void update(Input input, KeyMapper km){
		down.clear();
		pressed.clear();
		x = input.getMouseX();
		y = input.getMouseY();
		// IL y a surement plus simple et moins coûteux
		for(Integer i : km.mapping.keySet()){
			if(input.isKeyPressed(i)){
				this.pressed.addElement(km.mapping.get(i));
			}
			if(input.isKeyDown(i)){
				this.down.addElement(km.mapping.get(i));
			}
		}
		// Mouse
		for(int i = 0; i<3; i++){
			if(input.isMousePressed(i)){
				pressed.addElement(km.mapping.get(i));
			}
			if(input.isMouseButtonDown(i)){
				down.addElement(km.mapping.get(i));
			}
		}
		updateSelectionRectangle();
	}
	
	private void updateSelectionRectangle() {
		if(!isDown(KeyEnum.LeftClick)){
			resetRectangle();
			return;
		}

		if (isPressed(KeyEnum.LeftClick)) {

			selectionRectangle.setBounds(x,y,x+0.1f,y+0.1f);
			anchorX = x;
			anchorY = y;
			
		}
		selectionRectangle.setBounds((float) Math.min(anchorX, x),
				(float) Math.min(anchorY, y), (float) Math.abs(x - anchorX) + 0.1f,
				(float) Math.abs(y - anchorY) + 0.1f);
	}
	
	public void resetRectangle(){
		selectionRectangle.setX(-1000f);
		selectionRectangle.setY(-1000f);
		selectionRectangle.setHeight(0.1f);
		selectionRectangle.setWidth(0.1f);
		anchorX = 0f;
		anchorY = 0f;
	}
	public boolean rectangleIsNone(){
		return selectionRectangle.getX()==-1000f;
	}
	public boolean isDown(KeyEnum key){
		return down.contains(key);
	}
	public boolean isPressed(KeyEnum key){
		return pressed.contains(key);
	}

	public int idObjetMouse() {

		return idObjetMouse;
	}
	
	
	



}
