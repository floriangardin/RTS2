package bot;

import java.util.Vector;

import data.Attributs;
import model.Building;
import model.Objet;
import utils.ObjetsList;

public class IAUnit {

	
	protected Objet objet;
	private IA ia;
	
	
	public IAUnit(Objet o, IA ia){
		this.objet = o;
		this.ia = ia;

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
	public int getGameTeam(){
		return this.objet.getGameTeam().id;
	}
	protected Objet getObjet(){
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
			return ((Building) objet).getProductionList();
		}
		return new Vector<ObjetsList>();
	}
	public Vector<ObjetsList> getResearchList(){
		
		if(objet instanceof Building){
			return ((Building) objet).getTechnologyList();
		}
		return new Vector<ObjetsList>();
	}
	
	public static float distance(IAUnit u1,IAUnit u2){
		return (float) Math.sqrt((u1.getX()-u2.getX())*(u1.getX()-u2.getX())+(u1.getY()-u2.getY())*(u1.getY()-u2.getY()));
	}
	
	
	
	public IAUnit getNearestNeutral(ObjetsList o){
		Vector<IAEnemyObject> enemies = getIA().getNature();
		float minDist = -1;
		IAUnit best =null;
		for(IAEnemyObject enemy : enemies){
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
		Vector<IAEnemyObject> enemies = getIA().getEnemies();
		float minDist = -1;
		IAUnit best =null;
		for(IAEnemyObject enemy : enemies){
			float dist = IAUnit.distance(enemy, this);
			if(enemy.getName()==o && (minDist==-1 || dist<minDist)){
				minDist = dist;
				best = enemy;
			}
		}
		return best;
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
}
