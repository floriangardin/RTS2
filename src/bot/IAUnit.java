package bot;

import java.util.Vector;

import data.Attributs;
import plateau.Building;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

public class IAUnit {

	
	protected Objet objet;
	private IA ia;
	protected Plateau plateau;
	
	public IAUnit(Objet o, IA ia, Plateau plateau){
		this.objet = o;
		this.ia = ia;
		this.plateau = plateau;

	}

	public ObjetsList getName(){
		return objet.name;
	}
	public Class<? extends Objet> getType(){
		return objet.getClass();
	}
	
	public float getX(){
		return objet.getX();
	}
	public float getY(){
		return objet.getY();
	}
	public int getId(){
		return objet.id;
	}
	public float getLifepoints(){
		return objet.lifePoints;
	}
	public int getGameTeam(){
		if(this.objet.getTeam() != null){
			return this.objet.getTeam().id;
		}
		return 0;
		
	}
	Objet getObjet(){
		return this.objet;
	}
	public float getAttribut(Attributs a){
		return objet.getAttribut(a);
	}
	public Vector<String> getAttributList(Attributs a){
		return objet.getAttributList(a);
	}
	public String getAttributString(Attributs a){
		return objet.getAttributString(a);
	}
	
	public boolean clickIn(float x , float y){
		return objet.collisionBox.contains(x, y);
	}
	
	public IA getIA(){
		return ia;
	}
	public Vector<ObjetsList> getProductionList(){
		
		if(objet instanceof Building){
			return ((Building) objet).getProductionList(plateau);
		}
		return new Vector<ObjetsList>();
	}
	public Vector<ObjetsList> getResearchList(){
		
		if(objet instanceof Building){
			return ((Building) objet).getTechnologyList(plateau);
		}
		return new Vector<ObjetsList>();
	}
	
	public static float distance(IAUnit u1,IAUnit u2){
		return (float) Math.sqrt((u1.getX()-u2.getX())*(u1.getX()-u2.getX())+(u1.getY()-u2.getY())*(u1.getY()-u2.getY()));
	}
	
	
	public Vector<ObjetsList> getSpells(){
		if(getObjet() instanceof Character){
			return getObjet().getSpellsName();
		}
		return new Vector<ObjetsList>();
	}
	
	
	public IAUnit getNearestNeutral(ObjetsList o){
		Vector<IAUnit> enemies = getIA().getNature();
		float minDist = -1;
		IAUnit best =null;
		for(IAUnit enemy : enemies){
			float dist = IAUnit.distance(enemy, this);
			if(enemy.getName()==o && (minDist==-1 || dist<minDist)){
				minDist = dist;
				best = enemy;
			}
		}
		return best;
	}
	public IAUnit getNearestNeutralorEnnemy(ObjetsList o){
		Vector<IAUnit> enemies = getIA().getNature();
		enemies.addAll(getIA().getEnemies());
		float minDist = -1;
		IAUnit best =null;
		for(IAUnit enemy : enemies){
			float dist = IAUnit.distance(enemy, this);
			if(enemy.getName()==o && (minDist==-1 || dist<minDist)){
				minDist = dist;
				best = enemy;
			}
		}
		return best;
	}
	
	public IAUnit getNearestAlly(ObjetsList o){
		Vector<IAAllyObject> enemies = getIA().getUnits();
		float minDist = -1;
		IAUnit best =null;
		for(IAAllyObject enemy : enemies){
			float dist = IAUnit.distance(enemy, this);
			if(enemy.getName()==o && (minDist==-1 || dist<minDist)){
				minDist = dist;
				best = enemy;
			}
		}
		return best;
	}
	
	public IAUnit getNearestEnemy(ObjetsList o){
		Vector<IAUnit> enemies = getIA().getEnemies();
		float minDist = -1;
		IAUnit best =null;
		for(IAUnit enemy : enemies){
			float dist = IAUnit.distance(enemy, this);
			if(enemy.getName()==o && (minDist==-1 || dist<minDist)){
				minDist = dist;
				best = enemy;
			}
		}
		return best;
	}
	public IAUnit getNearestEnemyCharacter(){
		Vector<IAUnit> enemies = getIA().getEnemies();
		float minDist = -1;
		IAUnit best =null;
		for(IAUnit enemy : enemies){
			float dist = IAUnit.distance(enemy, this);
			if(ObjetsList.getUnits().contains(enemy.getName()) && (minDist==-1 || dist<minDist)){
				minDist = dist;
				best = enemy;
			}
		}
		return best;
	}
	public boolean isNotUnit(){
		return this.objet==null || (!(this.objet instanceof Building) && !(this.objet instanceof Character));
	}
	
	public IAUnit getNearest(ObjetsList o ){
		Vector<IAUnit> enemies = new Vector<IAUnit>();
		enemies.addAll(getIA().getUnits());
		enemies.addAll(getIA().getEnemies());
		enemies.addAll(getIA().getNature());
		float minDist = -1;
		IAUnit best =null;
		for(IAUnit enemy : enemies){
			float dist = IAUnit.distance(enemy, this);
			if(enemy.getName()==o && (minDist==-1 || dist<minDist)){
				minDist = dist;
				best = enemy;
			}
		}
		return best;	
	}
	public boolean isNull(){
		return this.objet==null;
	}
	public IAUnit getTarget(){
		return new IAUnit(this.objet.getTarget(plateau),this.ia, plateau);
	}
	public boolean hasTarget(){
		return objet.getTarget(plateau)!=null;
	}
	
	public boolean equals(Object o){
		if(o instanceof IAUnit){
			return this.getId()==((IAUnit)o).getId();
		}
		return false;
	}
	
	// All things that you can produce (tech,units,spells ...)
	public Vector<ObjetsList> getAllProductions(){
		Vector<ObjetsList> result = new Vector<ObjetsList>();
		result.addAll(getProductionList());
		result.addAll(getResearchList());
		result.addAll(getSpells());
		return result;
	}
	
	
	
	
}
