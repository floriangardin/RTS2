package bot;

import java.util.Vector;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import data.Attributs;
import plateau.Building;
import plateau.Character;
import plateau.Plateau;
import plateau.Team;
import utils.ObjetsList;
public abstract class IA{
	/*
	 * Tous les objectifs sont réalisés en même temps mais ne peut réalisé qu'un objectif à la fois
	 */
	public final static Vector<IA> ias = new Vector<IA>();
	public static boolean isInit = false;
	private Vector<IAAllyObject> units;
	private Vector<IAUnit> enemies;
	private Vector<IAUnit> nature;
	
	private Vector<IAAllyObject> selection;
	Plateau plateau;
	private int teamId;
	private Team player;
	
	InputObject im;
	public static Vector<InputObject> play(Plateau plateau, int roundToPlay){
		Vector<InputObject> ims = new Vector<InputObject>();
		for(IA ia : ias){
			ims.add(ia.action(plateau, roundToPlay));
		}
		return ims;
	}
	public static void init(Vector<IA> toAdd){
		for(IA ia : toAdd){
			ias.add(ia);
		}
		isInit = true;
	}
	
	public Vector<IAAllyObject> getSelection(){
		return selection;
	}
	public IA(int teamid)  {
		this.teamId= teamid;
		units = new Vector<IAAllyObject>();
		enemies = new Vector<IAUnit>();
		nature = new Vector<IAUnit>();
		selection = new Vector<IAAllyObject>();
		
		// Create ennemy static IA
		// Contain all the objects visible by the IA
	}
	public void sendMessage(String message){
		//Communications.sendMessage(new ChatMessage(this.player.id+"|"+message),this.player.id);
	}
	/*
	 * Renvoie les units , peut-être faire une copie du vecteur à terme par sécurité
	 */
	public Vector<IAAllyObject> getMyUnits(){
		/*
		 * Return objects of my team
		 */
		return units;
	}
	
	public Vector<IAAllyObject> getMyUnits(ObjetsList filter){
		/*
		 * Return objects of my team
		 */
		Vector<IAAllyObject> res = new Vector<IAAllyObject>();
		for(IAAllyObject unit: units ){
			if(unit.getName() == filter){
				res.add(unit);
			}
		}
		return res;
	}
	
	public Vector<IAUnit> getEnemies(){
		/*
		 * Return objects of enemy team
		 */
		return enemies;
	}
	
	public Vector<IAUnit> getEnnemies(ObjetsList filter){
		/*
		 * Return objects of my team
		 */
		Vector<IAUnit> res = new Vector<IAUnit>();
		for(IAUnit unit: this.enemies ){
			if(unit.getName() == filter){
				res.add(unit);
			}
		}
		return res;
	}
	public Vector<IAUnit> getNature(){
		/*
		 * Return object of nature
		 * 
		 */
		return nature;
	}
	
	public Vector<IAUnit> getNatureAndEnemies(){
		Vector<IAUnit> result = new Vector<IAUnit>();
		result.addAll(enemies);
		result.addAll(nature);
		return result;
	}
	/*
	 * Method always called when IA is awaken
	 */
	public InputObject initForRound(Plateau plateau, int roundToPlay){
		if(player==null){
			for(Team team : plateau.teams){
				if(team.id==teamId){
					this.player = team;
				}
			}
		}
		InputObject toReturn = new InputObject(player.id, roundToPlay);
		this.plateau = plateau;
		this.selection.clear();
		this.units.clear();
		this.nature.clear();
		this.enemies.clear();
		if(plateau.round<5){
			return toReturn;
		}
		//Update IA Objects
		for(Character c : plateau.characters){
			if(c.getTeam().id ==0 ||plateau.isVisibleByTeam(this.player.id, c)){
				if(c.getTeam().id==this.player.id){					
					this.units.addElement(new IAAllyObject(c,this));
				}else if( c.getTeam().id ==0){
					this.nature.addElement(new IAUnit(c,this, plateau));
				}else{
					this.enemies.addElement(new IAUnit(c, this, plateau));
				}
			}
		}
		for(Building b : plateau.buildings){
			if(b.getTeam().id==this.player.id){					
				this.units.addElement(new IAAllyObject(b,this));
			}else if(b.getTeam().id == 0 || !plateau.isVisibleByTeam(this.player.id, b)){
				this.nature.addElement(new IAUnit(b,this, plateau));
			}else{
				this.enemies.addElement(new IAUnit(b,this, plateau));
			}
		}
		return toReturn;
	}
	
	public InputObject action(Plateau plateau, int roundToPlay){
		im = initForRound(plateau, roundToPlay); // Input object to return at the end of turn
		// Call abstract method to overrides
		try {
			this.selection = this.select();
			this.selectUnits(this.selection);
			if(this.selection.size()>0){				
				this.update(this.getSelection());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//alert("Error in IA ... "+e.toString());
			e.printStackTrace();
		}
		return im;
	}
	
	public float getAttributs(ObjetsList o, Attributs a){
		return this.player.data.getAttribut(o, a);
	}
	public void selectUnits(Vector<IAAllyObject> units){
		im.selection = new Vector<Integer>();
		for(IAAllyObject unit : units){
			im.selection.add(unit.getId());
		}
	}
	public void rightClick(float x, float y){
		im.rightClick(x, y);
	}
	public void rightClick(IAUnit unit){
		im.rightClick(unit.getX(), unit.getY());
	}
	public void stopMove(){
		im.pressed.add(KeyEnum.StopperMouvement);
	}
	public  void attack(float x , float y){
		im.attack(x, y);
	}; // attack move
	
	public  void produceUnit(ObjetsList production){
		// Check price of unit first
		if(this.selection.get(0).objet instanceof Building){
			if(this.selection.get(0).getProductionList().contains(production)){
				int index = this.selection.get(0).getProductionList().indexOf(production);
				switch(index){
				case 0:
					im.pressed.add(KeyEnum.Prod0);
					break;
				case 1:
					im.pressed.add(KeyEnum.Prod1);
					break;
				case 2:
					im.pressed.add(KeyEnum.Prod2);
					break;
				case 3:
					im.pressed.add(KeyEnum.Prod3);
					break;
				default:
					break;
				}
			}
		}else{
			//Communications.sendMessage(new ChatMessage("1|Warning : Tried to produce not in a building ..."));
		}
	}; // Produce unit
	public  void produceTechnology(ObjetsList tech){
		if(this.selection.get(0).objet instanceof Building){
			int index = this.selection.get(0).getResearchList().indexOf(tech);
			switch(index){
			case 0:
				im.pressed.add(KeyEnum.Tech0);
				break;
			case 1:
				im.pressed.add(KeyEnum.Tech1);
				break;
			case 2:
				im.pressed.add(KeyEnum.Tech2);
				break;
			case 3:
				im.pressed.add(KeyEnum.Tech3);
				break;
			default:
				break;
			}
		}else{
			//Communications.sendMessage(new ChatMessage("1|Warning : Tried to research not in a building ..."));
		}
	}; // Produce research
	
	public void produce(ObjetsList o, InputObject im){
		produceUnit(o);
		produceTechnology(o);
	}
	
	public void launchSpell(float x , float y,ObjetsList spell){
		
		if(this.selection.get(0).objet instanceof Character ){
			Character c = (Character) this.selection.get(0).objet;
			if(c.getSpellsName().contains(spell)){
				int index = this.selection.get(0).getSpells().indexOf(spell);
				im.x = x;
				im.y = y;
				switch(index){
				case 0:
					im.pressed.add(KeyEnum.Prod0);
					break;
				case 1:
					im.pressed.add(KeyEnum.Prod1);
					break;
				case 2:
					im.pressed.add(KeyEnum.Prod2);
					break;
				case 3:
					im.pressed.add(KeyEnum.Prod3);
					break;
				default:
					break;
				}
				
			}
			
		}
	}
	
	public void launchSpell(IAUnit target,ObjetsList spell){
		launchSpell(target.getX(), target.getY(), spell);
	}
	
	public abstract void update(Vector<IAAllyObject> selection) throws Exception;
	public abstract Vector<IAAllyObject> select() throws Exception;
	
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

	
	
//	protected void print(String s){
//		Communications.sendMessage(new ChatMessage("0|"+s));
//	}
//	protected void alert(String s){
//		Communications.sendMessage(new ChatMessage("2|"+s));
//	}
	

	protected int getFood(){
		return player.food;
	}
	

	protected int getPop(){
		return player.getPop(plateau);
	}
	protected int getMaxPop(){
		return player.getMaxPop(plateau);
	}
	
	protected boolean canProduce(ObjetsList o){
		
		return this.getFood()>this.getAttribut(o, Attributs.foodCost)
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
		return ((Building) plateau.getById(player.hq)).techsDiscovered;
	}
	public Team getPlayer(){
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
		for(IAAllyObject u : getMyUnits()){
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
		for(IAUnit u : getMyUnits()){
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
		for(IAUnit u : getMyUnits()){
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
