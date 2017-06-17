package plateau;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import bonus.Bonus;
import bullets.Bullet;
import data.Attributs;
import data.AttributsChange;
import events.EventNames;
import main.Main;
import spells.Etats;
import spells.Spell;
import utils.ObjetsList;

public abstract class Objet implements java.io.Serializable {

	// Animation : mode,orientation,increment
	public int id;
	public int mode;
	public int orientation=2;
	public int increment;
	
	public boolean canMove=true;
	public float x;
	public float y;
	public int idCase;
	public Shape collisionBox;
	public Rectangle selectionBox;
	public Color color;
	public float lifePoints;
	public ObjetsList name;
	public float visibleHeight = 100;
	public static int NO_TARGET = -1;
	// draw
	public boolean toDrawOnGround = false;
	//State
	public Vector<Etats> etats = new Vector<Etats>();
	
	
	public Team team;

	// Bonus, équipements, potions et autres stuff
	public Vector<AttributsChange> attributsChanges = new Vector<AttributsChange>();


	// Spells ( what should appear in the bottom bar
	private Vector<ObjetsList> spells = new Vector<ObjetsList>();
	protected Vector<Float> spellsState = new Vector<Float>();

	// visibility boolean 
	public boolean visibleByCurrentTeam;
	public boolean visibleByCamera;

	public int animation = 0;
	public float vx;
	public float vy;
	private int target = NO_TARGET;
//	public int checkpointTarget = NO_TARGET;

	public boolean mouseOver = false;
	
	// spells attributs
	public float inDash = 0f;

	public void action(Plateau plateau){}
	
	public void move(Plateau plateau){}
	
	public boolean canAttack(Plateau plateau){
		boolean b= (getTarget(plateau)!=null && getTarget(plateau).getTeam()!=this.getTeam());
		return   ( b || (!b && this.getAttribut(Attributs.damage)<0)) ;
	}
	
	public void updateAttributsChange(){
		Vector<AttributsChange> toDelete = new Vector<AttributsChange>();
		for(AttributsChange ac : this.attributsChanges){
			ac.remainingTime-=1f*Main.increment;
			if(ac.remainingTime<=0 && !ac.endless){
				toDelete.add(ac);
			}
		}
		for(AttributsChange ac : toDelete){
			this.attributsChanges.remove(ac);
		}
		// handling end of dash
		if(this.inDash>0f){
			this.inDash-=1f*Main.increment;
//			if(this.inDash<=0f && this.getTarget()!=null && (this.getTarget() instanceof Checkpoint)){
//				this.mode = Character.AGGRESSIVE;
//			}
		}
	}
	

	public void setTarget(Objet t){
		if(t!=null){
			this.target = t.id;
			
		}
		if(t==null){
			this.target = NO_TARGET;
		}
	}

	public void drawIsSelected(Graphics g) {

	}
	
	public Vector<Integer> computeWay(Plateau plateau){
		if(this.getTarget(plateau)==null){
			return new Vector<Integer>();
		}
		if(this.getTarget(plateau) instanceof Building && !(this.getTarget(plateau) instanceof Bonus)){
			Building b = (Building)this.getTarget(plateau);
			return plateau.mapGrid.pathfinding(x, y, (Rectangle)(b.collisionBox));
		} else {
			float xEnd = this.getTarget(plateau).x, yEnd = this.getTarget(plateau).y;
			return plateau.mapGrid.pathfinding(this.getX(), this.getY(), xEnd, yEnd);
		}
	}

	public String toStringActionObjet(){
		String s = "";

		s+="maxLifePoints:"+getAttribut(Attributs.maxLifepoints)+";";


		return s;
	}


	
	public Team getTeam(){
		return this.team;
	}
	

	protected void destroy(){
		this.lifePoints = -10;

		this.x = -10f;
		this.y = -10f;
	}
	
	public Graphics draw(Graphics g){
		return g;
	}
	
	public void drawBasicImage(Graphics g){}
	
	protected void collision(Objet o){}
	
	public abstract void collision(Character c, Plateau plateau);
	
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	protected void setXY(float x, float y, Plateau plateau){

		if(this instanceof Bullet){
			this.x = x;
			this.y = y;
		} else {

			float xt = Math.min(plateau.maxX-1f, Math.max(1f, x));
			float yt = Math.min(plateau.maxY-1f, Math.max(1f, y));

			this.x = xt;
			this.y = yt;
		}
		this.collisionBox.setCenterX(x);
		this.collisionBox.setCenterY(y);
		try{
			this.idCase = plateau.mapGrid.getCase(x, y).id;
		} catch(Exception e){
			this.idCase = -1;
		}

	}

	public boolean isAlive(){
		return this.lifePoints>0f;
	}

	public void setLifePoints(float lifepoints){
		if(lifepoints<this.lifePoints && this instanceof Character){
			//Game.g.triggerEvent(EventNames.Blood, this);
		}
		if(lifepoints<this.getAttribut(Attributs.maxLifepoints))
			this.lifePoints= lifepoints;
		else{
			this.lifePoints = this.getAttribut(Attributs.maxLifepoints);
		}
	}
	
	
	

	public void parse(HashMap<String, String> hs) {

	}

	// Attributs
	public float getAttribut(Attributs attribut){
		float a = this.getTeam().data.getAttribut(this.name,attribut);
		for(AttributsChange ac : this.attributsChanges){
			if(ac.attribut==attribut){
				a = ac.apply(a);
//				System.out.println("valeur modiiée "+attribut+" "+a);
			}
		}
		return a;
	}
	public float getAttributAndRemoveUsageUnique(Attributs attribut){
		/**
		 * remove the corresponding attributes change if its with usage unique
		 */
		float a = this.getTeam().data.getAttribut(this.name,attribut);
		Vector<AttributsChange> toDelete = new Vector<AttributsChange>();
		for(AttributsChange ac : this.attributsChanges){
			if(ac.attribut==attribut){
				a = ac.apply(a);
				if(ac.usageUnique){
					toDelete.add(ac);
				}
			}
		}
		for(AttributsChange ac : toDelete){
			this.attributsChanges.remove(ac);
		}
		return a;
	}
	public String getAttributString(Attributs attribut){
		return this.getTeam().data.getAttributString(this.name,attribut);
	}
	public Vector<String> getAttributList(Attributs attribut){
		return this.getTeam().data.getAttributList(this.name,attribut);
	}

	// Spells
	public boolean canLaunch(int number){
		return this.spellsState.get(number) >= this.getSpell(number).getAttribut(Attributs.chargeTime);
	}
	public Spell getSpell(int i){

		
		if(this.spells.size()>i){
			if(this.getTeam().data.spells.containsKey(this.spells.get(i))){
				return this.getTeam().data.spells.get(this.spells.get(i));
			}
		} else {
			System.out.println("vous essayez d'accéder à un spell inexistant");
		}
		return null;
	}
	public Vector<Spell> getSpells() {
		Vector<Spell>spells = new Vector<Spell>();
		for(int i=0; i<this.spells.size(); i++){
			spells.addElement(getSpell(i));
		}
		return spells;
	}
	public Vector<ObjetsList> getSpellsName(){
		return this.spells;
	}
	public Spell getSpell(ObjetsList spell){

		int indexSpell = getSpellsName().indexOf(spell);
		if(indexSpell>=0){
			return getSpell(indexSpell);
		}
		return null;
	}
	
	public void launchSpell(Objet target,ObjetsList spell, Plateau plateau){
		if(this instanceof Character){
			this.getSpell(spell).launch(target, (Character)this, plateau);
			this.resetSpellState(spell);
		}
		
	}
	public float getSpellState(ObjetsList spell){
		int indexSpell = getSpellsName().indexOf(spell);
		if(indexSpell>=0){
			return this.spellsState.get(indexSpell);
		}
		return -1;
	}
	public void resetSpellState(ObjetsList spell){
		int indexSpell = getSpellsName().indexOf(spell);
		if(indexSpell>=0){
			this.spellsState.set(indexSpell,0f);
		}
		
	}
	public float getSpellState(int i){
		
		if(i>=0 && i<this.spellsState.size()){			
			return this.spellsState.get(i);
		}
		return -1;
	}
	public Vector<Float> getSpellsState(){
		return spellsState;
	}
	
	public void addSpell(ObjetsList s){
		this.spells.addElement(s);
	}
	
	// Autres
	public float getVisibleSize(){
		if(this.getTeam().data.datas.containsKey(this.name)){
			if(this.getTeam().data.datas.get(this.name).attributs.containsKey(Attributs.size)){
				return getAttribut(Attributs.size)+getAttribut(Attributs.sight);
			} else if(this.getTeam().data.datas.get(this.name).attributs.containsKey(Attributs.sizeX)){
				float sizeX=this.getTeam().data.getAttribut(this.name,Attributs.sizeX);
				float sizeY=this.getTeam().data.getAttribut(this.name,Attributs.sizeY);
				return (float) Math.sqrt(sizeX*sizeX+sizeY*sizeY)+getAttribut(Attributs.sight);
			}
		}
		return 1f;
	}
	public Objet getTarget(Plateau plateau) {
		return plateau.getById(target);
	}
	public void setSpells(Vector<ObjetsList> spells) {
		this.spells = spells;
	}
	


}

