package plateau;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import bonus.Bonus;
import data.Attributs;
import data.AttributsChange;
import events.EventAttackDamage;
import events.EventHandler;
import main.Main;
import pathfinding.Case;
import ressources.Map;
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
	public Objet(Plateau plateau){
		this.id = plateau.id++;
		this.team = plateau.teams.get(0);
	}
	// Bonus, �quipements, potions et autres stuff
	public Vector<AttributsChange> attributsChanges = new Vector<AttributsChange>();


	// Spells ( what should appear in the bottom bar
	private Vector<ObjetsList> spells = new Vector<ObjetsList>();
	protected Vector<Float> spellsState = new Vector<Float>();
	
	private int roundLastAttack = -100;
	private static final float timerMaxValueAttacked = 100f;
	
	// visibility boolean 
	
	public boolean visibleByCamera;

	public int animation = 0;
	public float vx;
	public float vy;
	protected int target = NO_TARGET;
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
	
	public void updateAttributsChange(Plateau plateau){
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
//			if(this.inDash<=0f && this.getTarget(plateau)!=null && (this.getTarget(plateau) instanceof Checkpoint)){
//				this.mode = Character.AGGRESSIVE;
//			}
		}
	}
	

	public void setTarget(Objet t, Plateau plateau){
		Objet target = this.getTarget(plateau);
		if(target !=null && target instanceof Checkpoint){
			target.destroy();
		}
		if(t!=null){
			this.target = t.id;
		}
		if(t==null){
			this.target = NO_TARGET;
			if(this instanceof Character){
				((Character)this).distanceToTarget = -1f;
			}
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
	public void setXY(float x, float y, Plateau plateau){
		float oldx = this.x  ;
		float oldy = this.y ;
		// handling old cases
		Case c = plateau.mapGrid.getCase(this.idCase);
		if(this instanceof Character){
			if(c!=null && c.characters.contains((Character)this)){
				c.characters.remove((Character)this);
			}
		} else if(this instanceof NaturalObjet){
			if(c!=null && c.naturesObjet.contains((NaturalObjet)this)){
				c.naturesObjet.remove((NaturalObjet)this);
			}
		} else if(this instanceof Building){
			plateau.mapGrid.removeBuilding((Building)this);
		}
		
		// changing position
		if(this instanceof Bullet){
			this.x = x;
			this.y = y;
		} else if(this instanceof Building){
			float sizeX = plateau.teams.get(0).data.getAttribut(this.name, Attributs.sizeX);
			float sizeY = plateau.teams.get(0).data.getAttribut(this.name, Attributs.sizeY);
			((Building)this).i = plateau.mapGrid.getCase(x-sizeX/2+Map.stepGrid/2, y-sizeY/2+Map.stepGrid/2).i;
			((Building)this).j = plateau.mapGrid.getCase(x-sizeX/2+Map.stepGrid/2, y-sizeY/2+Map.stepGrid/2).j;
			this.x = (((Building)this).i*Map.stepGrid+this.getAttribut(Attributs.sizeX)/2);
			this.y = (((Building)this).j*Map.stepGrid+this.getAttribut(Attributs.sizeY)/2);
		} else {
			this.x = Math.min(plateau.maxX-1f, Math.max(1f, x));
			this.y = Math.min(plateau.maxY-1f, Math.max(1f, y));
		}
		
		//handling boxes
		this.collisionBox.setCenterX(this.x);
		this.collisionBox.setCenterY(this.y);
		
		// handling new cases
		c = plateau.mapGrid.getCase(this.x, this.y);
		if(c!=null){
			this.idCase = c.id;
		} else {
			this.idCase = -1;
		}
		if(this instanceof Character){
			//FIXME: on vire cette histoire de sight box ?
			((Character)this).sightBox.setCenterX(this.getX());
			((Character)this).sightBox.setCenterY(this.getY()-this.getAttribut(Attributs.size)/2f);
			this.selectionBox.setCenterX(this.x);
			this.selectionBox.setCenterY(this.y-2f*this.getAttribut(Attributs.size));
			plateau.mapGrid.getCase(this.idCase).characters.add((Character)this);
		} else if(this instanceof NaturalObjet){
			plateau.mapGrid.getCase(this.idCase).naturesObjet.add((NaturalObjet)this);
		}else if(this instanceof Building){
			plateau.mapGrid.addBuilding((Building)this);
		}

	}

	public boolean isAlive(){
		return this.lifePoints>0f;
	}

	public void setLifePoints(float lifepoints, Plateau plateau){
		if(lifepoints<this.lifePoints && this instanceof Character){
			this.roundLastAttack = plateau.getRound();
			EventHandler.addEvent(new EventAttackDamage(this, (int)(this.lifePoints-lifepoints), plateau), plateau);
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
//				System.out.println("valeur modii�e "+attribut+" "+a);
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
			System.out.println("vous essayez d'acc�der � un spell inexistant");
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
	
	public String toString(){
		return this.name.toString();
	}
	
	public String hash(){
		return ""+x+""+y+""+lifePoints+""+name;
	}

	public int roundSinceLastAttack(int currentRound){
		return currentRound - this.roundLastAttack;
	}
	public int getTarget(){
		return this.target;
	}
	public int getId(){
		return id;
	}
	public ObjetsList getName(){
		return this.name;
	}
	public HashMap<String, Object> toJson(){
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("x", this.getX());
		res.put("y", this.getY());
		res.put("id", this.getId());
		res.put("team", this.getTeam().id);
		res.put("sizeY", this.collisionBox.getHeight());
		res.put("sizeX", this.collisionBox.getWidth());
		res.put("lifepoints", this.lifePoints);
		res.put("maxLifepoints", this.getAttribut(Attributs.maxLifepoints));
		res.put("damage", this.getAttribut(Attributs.damage));
		res.put("armor", this.getAttribut(Attributs.armor));
		res.put("speed", this.getAttribut(Attributs.maxVelocity));
		res.put("target", this.getTarget());
		res.put("name", this.getName().toString());
		res.put("sight", this.getAttribut(Attributs.sight));
		// Pour chaque objet json
		return res;
	}

}

