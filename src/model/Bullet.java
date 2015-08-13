package model;


import java.util.Vector;

import multiplaying.OutputModel.OutputBullet;

public abstract class Bullet extends ActionObjet {
	protected float damage;
	float areaEffect;
	Character owner;
	int id;
	private Vector<Character> getCharactersInAreaEffect(){
		return null;
	}
		
	public void change(OutputBullet ocb){
		this.x = ocb.x;
		this.y = ocb.y;
	}

}
