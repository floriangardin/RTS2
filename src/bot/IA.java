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
	
	
	
	
	
	// High levels functions For IA Strategy
	
	
	public Vector<ObjetsList> getTechsRequired(ObjetsList o){
		return player.data.getAttributListAtt(o, Attributs.techsRequired);
	}
	
	/*
	 * Liste et/ou
	 */
	public Vector<Vector<ObjetsList>> getRequirements(ObjetsList o ){
		Vector<Vector<ObjetsList>> res = new Vector<Vector<ObjetsList>>();
		res.add(getProducers(o));
		for(ObjetsList ol : getTechsRequired(o)){
			Vector<ObjetsList> tech = new Vector<ObjetsList>();
			tech.add(ol);
			res.add(tech);
		}
		return res;
	}

	
	public Vector<Vector<ObjetsList>> getUnsatisfiedRequirements(ObjetsList o){
		
		Vector<Vector<ObjetsList>> req = getRequirements(o);
		Vector<Vector<ObjetsList>> res = new Vector<Vector<ObjetsList>>();
		for(Vector<ObjetsList> v : req){
			Vector<ObjetsList> requirement = new Vector<ObjetsList>();
			boolean toAdd = true;
			for(ObjetsList h : v){
				
				if(has(h)){
					
					toAdd = false;
					break;
				}
			}
			if(toAdd){				
				res.add(v);
			}
		}
		return res;
	}
	
	public Vector<ObjetsList> getTechsDiscovered(){
		return player.hq.techsDiscovered;
	}
	public GameTeam getPlayer(){
		return player;
	}
	
	public Vector<ObjetsList> getProducers(ObjetsList o){
		Vector<ObjetsList> res = new Vector<ObjetsList>();
		for(ObjetsList ol : ObjetsList.values()){
			if(getPlayer().data.getAttributListAtt(ol, Attributs.units).contains(o)){
				res.add(ol);
			}
			if(getPlayer().data.getAttributListAtt(ol, Attributs.technologies).contains(o)){
				res.add(ol);
			}
		}
		return res;
	}
	
	public Vector<IAAllyObject> getMyProducers(ObjetsList o){
		Vector<ObjetsList> res = getProducers(o);
		Vector<IAAllyObject> toReturn = new Vector<IAAllyObject>();
		for(IAAllyObject u : getUnits()){
			for(ObjetsList ol : res){
				if(u.getName()==ol ){
					toReturn.add(u);
				}
			}
		}
		
		return toReturn;
	}
	
	
	
	/*
	 * Look if we have the object or the tech
	 */
	public boolean has(ObjetsList o){
		for(IAUnit u : getUnits()){
			if(u.getName()==o){
				return true;
			}
		}
		
		//Now check if we have a technology of this name
		if(getTechsDiscovered().contains(o)){
			return true;
		}
		return false;
	}
	
	
	/*
	 * Check if we have at least one of the Object at our command
	 */
	public boolean hasOneOf(Vector<ObjetsList> o){
		for(IAUnit u : getUnits()){
			for(ObjetsList ol : o){
				if(u.getName()==ol){
					return true;
				}
			}

		}
		
		//Now check if we have a technology of this name
		if(getTechsDiscovered().contains(o)){
			return true;
		}
		return false;
	}
	
	/*
	 * Look if we have the requirements for producing the object or the tech
	 */
	public boolean hasRequirements(ObjetsList o){
		for(Vector<ObjetsList> ol : getRequirements(o)){
			if(!hasOneOf(ol)){
				return false;
			}
		}
		return true;
	}
	

	
}
