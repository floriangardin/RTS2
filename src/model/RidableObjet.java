package model;

public abstract class RidableObjet extends Objet {
	// A ridable objet doesn't do any action ....
	protected float velocity;

	public RidableObjet(float velocity) {
		super();
		this.velocity = velocity;
	}
}
