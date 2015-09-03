package model;



public class Armor extends ActionObjet{

	protected float weight;
	protected float damageReductor;
	private Character owner;
	
	public void action(){
		if(this.owner!=null)
			this.setXY(this.owner.getX(), this.owner.getY());
	}
	
	public Character getOwner(){
		return owner;
	}
	public void setOwner(Character owner){
		this.owner = owner;
	}
	
// On s'en tamponne que l'armure aie un x ou un y en fait (hérite de objet)
	
}
