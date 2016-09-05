package bot;

import java.util.Vector;

import data.Attributs;
import model.Building;
import model.Character;
import model.Game;
import model.GameTeam;
import model.Player;
import multiplaying.ChatMessage;
import utils.ObjetsList;
public abstract class IA {
	
	/*
	 * Tous les objectifs sont réalisés en même temps mais ne peut réalisé qu'un objectif à la fois
	 */
	
	private Vector<IAAllyObject> units;
	private Vector<IAUnit> enemies;
	private Vector<IAUnit> nature;
	
	private GameTeam player;
		
	public IA(Player p) {
		this.player = p.getGameTeam();
		units = new Vector<IAAllyObject>();
		enemies = new Vector<IAUnit>();
		nature = new Vector<IAUnit>();
		// Create ennemy static IA
		// Contain all the objects visible by the IA
	}
	
	/*
	 * Renvoie les units , peut-être faire une copie du vecteur à terme par sécurité
	 */
	public Vector<IAAllyObject> getUnits(){
		/*
		 * Return objects of my team
		 */
		return units;
	}
	
	public Vector<IAUnit> getEnemies(){
		/*
		 * Return objects of enemy team
		 */
		return enemies;
	}
	public Vector<IAUnit> getNature(){
		/*
		 * Return object of nature
		 * 
		 */
		return nature;
	}
	
	/*
	 * Method always called when IA is awaken
	 */
	public void action(){
		this.units.clear();
		this.nature.clear();
		this.enemies.clear();
		
		//Update IA Objects
		for(Character c : Game.g.plateau.characters){
			if(c.getGameTeam().id ==0 ||Game.g.plateau.isVisibleByTeam(this.player.id, c)){
				if(c.getGameTeam().id==this.player.id){					
					this.units.addElement(new IAAllyObject(c,this));
				}else if( c.getGameTeam().id ==0){
					this.nature.addElement(new IAUnit(c,this));
				}else{
					this.enemies.addElement(new IAUnit(c, this));
				}
			}
		}
		for(Building b : Game.g.plateau.buildings){
			if(b.getGameTeam().id==this.player.id){					
				this.units.addElement(new IAAllyObject(b,this));
			}else if(b.getGameTeam().id == 0 || !Game.g.plateau.isVisibleByTeam(this.player.id, b)){
				this.nature.addElement(new IAUnit(b,this));
			}else{
				this.enemies.addElement(new IAUnit(b,this));
			}
		}
		
		// Call abstract method to overrides
		try {
			this.update();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			alert("Error in IA ... "+e.toString());
			
		}
	}
	

	
	
	public abstract void update() throws Exception;
	
	
	/*Find an unit on a mouse click
	 * 
	 */
	public IAUnit findUnit(float x,  float y){
		Vector<IAUnit> units = new Vector<IAUnit>();
		units.addAll(this.units);
		units.addAll(nature);
		units.addAll(enemies);
		for(IAUnit u : units){
			
			if(u.clickIn(x, y)){
				return u;
			}
		}
		return null;
	}

	
	
	protected void print(String s){
		Game.g.sendMessage(new ChatMessage("0|"+s));
	}
	protected void alert(String s){
		Game.g.sendMessage(new ChatMessage("2|"+s));
	}
	
	protected int getGold(){
		return player.gold;
	}
	protected int getFood(){
		return player.food;
	}
	
	protected int getSpecial(){
		return player.special;
	}
	protected int getPop(){
		return player.pop;
	}
	protected int getMaxPop(){
		return player.maxPop;
	}
	
	protected boolean canProduce(ObjetsList o){
		
		return this.getFood()>this.getAttribut(o, Attributs.foodCost)
				&& this.getGold()>this.getAttribut(o, Attributs.goldCost)
				&& this.getSpecial()>this.getAttribut(o, Attributs.faithCost)
				&& (this.getMaxPop()-this.getPop())>=1; //FIXME : Not generic 
	}
	
	protected float getAttribut(ObjetsList o , Attributs a){
		return player.data.getAttribut(o, a);
	}
	
	protected String getAttributString(ObjetsList o , Attributs a){
		return player.data.getAttributString(o, a);
	}
	protected Vector<String> getAttributList(ObjetsList o , Attributs a){
		return player.data.getAttributList(o, a);
	}
	

	
}
